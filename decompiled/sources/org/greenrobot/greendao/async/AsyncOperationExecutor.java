package org.greenrobot.greendao.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

/* JADX INFO: loaded from: classes2.dex */
class AsyncOperationExecutor implements Runnable, Handler.Callback {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private int countOperationsCompleted;
    private int countOperationsEnqueued;
    private volatile boolean executorRunning;
    private Handler handlerMainThread;
    private int lastSequenceNumber;
    private volatile AsyncOperationListener listener;
    private volatile AsyncOperationListener listenerMainThread;
    private final BlockingQueue<AsyncOperation> queue = new LinkedBlockingQueue();
    private volatile int maxOperationCountToMerge = 50;
    private volatile int waitForMergeMillis = 50;

    AsyncOperationExecutor() {
    }

    public void enqueue(AsyncOperation asyncOperation) {
        synchronized (this) {
            int i = this.lastSequenceNumber + 1;
            this.lastSequenceNumber = i;
            asyncOperation.sequenceNumber = i;
            this.queue.add(asyncOperation);
            this.countOperationsEnqueued++;
            if (!this.executorRunning) {
                this.executorRunning = true;
                executorService.execute(this);
            }
        }
    }

    public int getMaxOperationCountToMerge() {
        return this.maxOperationCountToMerge;
    }

    public void setMaxOperationCountToMerge(int i) {
        this.maxOperationCountToMerge = i;
    }

    public int getWaitForMergeMillis() {
        return this.waitForMergeMillis;
    }

    public void setWaitForMergeMillis(int i) {
        this.waitForMergeMillis = i;
    }

    public AsyncOperationListener getListener() {
        return this.listener;
    }

    public void setListener(AsyncOperationListener asyncOperationListener) {
        this.listener = asyncOperationListener;
    }

    public AsyncOperationListener getListenerMainThread() {
        return this.listenerMainThread;
    }

    public void setListenerMainThread(AsyncOperationListener asyncOperationListener) {
        this.listenerMainThread = asyncOperationListener;
    }

    public synchronized boolean isCompleted() {
        return this.countOperationsEnqueued == this.countOperationsCompleted;
    }

    public synchronized void waitForCompletion() {
        while (!isCompleted()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for all operations to complete", e);
            }
        }
    }

    public synchronized boolean waitForCompletion(int i) {
        if (!isCompleted()) {
            try {
                wait(i);
            } catch (InterruptedException e) {
                throw new DaoException("Interrupted while waiting for all operations to complete", e);
            }
        }
        return isCompleted();
    }

    @Override // java.lang.Runnable
    public void run() {
        AsyncOperation asyncOperationPoll;
        AsyncOperation asyncOperationPoll2;
        while (true) {
            try {
                asyncOperationPoll = this.queue.poll(1L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                DaoLog.w(Thread.currentThread().getName() + " was interruppted", e);
                return;
            } finally {
                this.executorRunning = false;
            }
            if (asyncOperationPoll == null) {
                synchronized (this) {
                    asyncOperationPoll = this.queue.poll();
                    if (asyncOperationPoll == null) {
                        return;
                    }
                    DaoLog.w(Thread.currentThread().getName() + " was interruppted", e);
                    return;
                }
            }
            if (asyncOperationPoll.isMergeTx() && (asyncOperationPoll2 = this.queue.poll(this.waitForMergeMillis, TimeUnit.MILLISECONDS)) != null) {
                if (asyncOperationPoll.isMergeableWith(asyncOperationPoll2)) {
                    mergeTxAndExecute(asyncOperationPoll, asyncOperationPoll2);
                } else {
                    executeOperationAndPostCompleted(asyncOperationPoll);
                    executeOperationAndPostCompleted(asyncOperationPoll2);
                }
            } else {
                executeOperationAndPostCompleted(asyncOperationPoll);
            }
        }
    }

    private void mergeTxAndExecute(AsyncOperation asyncOperation, AsyncOperation asyncOperation2) {
        boolean z;
        ArrayList<AsyncOperation> arrayList = new ArrayList();
        arrayList.add(asyncOperation);
        arrayList.add(asyncOperation2);
        Database database = asyncOperation.getDatabase();
        database.beginTransaction();
        boolean z2 = false;
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                AsyncOperation asyncOperation3 = (AsyncOperation) arrayList.get(i);
                executeOperation(asyncOperation3);
                if (asyncOperation3.isFailed()) {
                    break;
                }
                z = true;
                if (i == arrayList.size() - 1) {
                    AsyncOperation asyncOperationPeek = this.queue.peek();
                    if (i < this.maxOperationCountToMerge && asyncOperation3.isMergeableWith(asyncOperationPeek)) {
                        AsyncOperation asyncOperationRemove = this.queue.remove();
                        if (asyncOperationRemove != asyncOperationPeek) {
                            throw new DaoException("Internal error: peeked op did not match removed op");
                        }
                        arrayList.add(asyncOperationRemove);
                    } else {
                        database.setTransactionSuccessful();
                        break;
                    }
                }
            } catch (Throwable th) {
                try {
                    database.endTransaction();
                } catch (RuntimeException e) {
                    DaoLog.i("Async transaction could not be ended, success so far was: false", e);
                }
                throw th;
            }
        }
        z = false;
        try {
            database.endTransaction();
            z2 = z;
        } catch (RuntimeException e2) {
            DaoLog.i("Async transaction could not be ended, success so far was: " + z, e2);
        }
        if (z2) {
            int size = arrayList.size();
            for (AsyncOperation asyncOperation4 : arrayList) {
                asyncOperation4.mergedOperationsCount = size;
                handleOperationCompleted(asyncOperation4);
            }
            return;
        }
        DaoLog.i("Reverted merged transaction because one of the operations failed. Executing operations one by one instead...");
        for (AsyncOperation asyncOperation5 : arrayList) {
            asyncOperation5.reset();
            executeOperationAndPostCompleted(asyncOperation5);
        }
    }

    private void handleOperationCompleted(AsyncOperation asyncOperation) {
        asyncOperation.setCompleted();
        AsyncOperationListener asyncOperationListener = this.listener;
        if (asyncOperationListener != null) {
            asyncOperationListener.onAsyncOperationCompleted(asyncOperation);
        }
        if (this.listenerMainThread != null) {
            if (this.handlerMainThread == null) {
                this.handlerMainThread = new Handler(Looper.getMainLooper(), this);
            }
            this.handlerMainThread.sendMessage(this.handlerMainThread.obtainMessage(1, asyncOperation));
        }
        synchronized (this) {
            int i = this.countOperationsCompleted + 1;
            this.countOperationsCompleted = i;
            if (i == this.countOperationsEnqueued) {
                notifyAll();
            }
        }
    }

    private void executeOperationAndPostCompleted(AsyncOperation asyncOperation) {
        executeOperation(asyncOperation);
        handleOperationCompleted(asyncOperation);
    }

    private void executeOperation(AsyncOperation asyncOperation) {
        asyncOperation.timeStarted = System.currentTimeMillis();
        try {
            switch (AnonymousClass1.$SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[asyncOperation.type.ordinal()]) {
                case 1:
                    asyncOperation.dao.delete(asyncOperation.parameter);
                    break;
                case 2:
                    asyncOperation.dao.deleteInTx((Iterable<Object>) asyncOperation.parameter);
                    break;
                case 3:
                    asyncOperation.dao.deleteInTx((Object[]) asyncOperation.parameter);
                    break;
                case 4:
                    asyncOperation.dao.insert(asyncOperation.parameter);
                    break;
                case 5:
                    asyncOperation.dao.insertInTx((Iterable<Object>) asyncOperation.parameter);
                    break;
                case 6:
                    asyncOperation.dao.insertInTx((Object[]) asyncOperation.parameter);
                    break;
                case 7:
                    asyncOperation.dao.insertOrReplace(asyncOperation.parameter);
                    break;
                case 8:
                    asyncOperation.dao.insertOrReplaceInTx((Iterable<Object>) asyncOperation.parameter);
                    break;
                case 9:
                    asyncOperation.dao.insertOrReplaceInTx((Object[]) asyncOperation.parameter);
                    break;
                case 10:
                    asyncOperation.dao.update(asyncOperation.parameter);
                    break;
                case 11:
                    asyncOperation.dao.updateInTx((Iterable<Object>) asyncOperation.parameter);
                    break;
                case 12:
                    asyncOperation.dao.updateInTx((Object[]) asyncOperation.parameter);
                    break;
                case 13:
                    executeTransactionRunnable(asyncOperation);
                    break;
                case 14:
                    executeTransactionCallable(asyncOperation);
                    break;
                case 15:
                    asyncOperation.result = ((Query) asyncOperation.parameter).forCurrentThread().list();
                    break;
                case 16:
                    asyncOperation.result = ((Query) asyncOperation.parameter).forCurrentThread().unique();
                    break;
                case 17:
                    asyncOperation.dao.deleteByKey(asyncOperation.parameter);
                    break;
                case 18:
                    asyncOperation.dao.deleteAll();
                    break;
                case 19:
                    asyncOperation.result = asyncOperation.dao.load(asyncOperation.parameter);
                    break;
                case 20:
                    asyncOperation.result = asyncOperation.dao.loadAll();
                    break;
                case 21:
                    asyncOperation.result = Long.valueOf(asyncOperation.dao.count());
                    break;
                case 22:
                    asyncOperation.dao.refresh(asyncOperation.parameter);
                    break;
                default:
                    throw new DaoException("Unsupported operation: " + asyncOperation.type);
            }
        } catch (Throwable th) {
            asyncOperation.throwable = th;
        }
        asyncOperation.timeCompleted = System.currentTimeMillis();
    }

    /* JADX INFO: renamed from: org.greenrobot.greendao.async.AsyncOperationExecutor$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType;

        static {
            int[] iArr = new int[AsyncOperation.OperationType.values().length];
            $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType = iArr;
            try {
                iArr[AsyncOperation.OperationType.Delete.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.DeleteInTxIterable.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.DeleteInTxArray.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.Insert.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.InsertInTxIterable.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.InsertInTxArray.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.InsertOrReplace.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.InsertOrReplaceInTxIterable.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.InsertOrReplaceInTxArray.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.Update.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.UpdateInTxIterable.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.UpdateInTxArray.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.TransactionRunnable.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.TransactionCallable.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.QueryList.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.QueryUnique.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.DeleteByKey.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.DeleteAll.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.Load.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.LoadAll.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.Count.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$org$greenrobot$greendao$async$AsyncOperation$OperationType[AsyncOperation.OperationType.Refresh.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
        }
    }

    private void executeTransactionRunnable(AsyncOperation asyncOperation) {
        Database database = asyncOperation.getDatabase();
        database.beginTransaction();
        try {
            ((Runnable) asyncOperation.parameter).run();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private void executeTransactionCallable(AsyncOperation asyncOperation) throws Exception {
        Database database = asyncOperation.getDatabase();
        database.beginTransaction();
        try {
            asyncOperation.result = ((Callable) asyncOperation.parameter).call();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        AsyncOperationListener asyncOperationListener = this.listenerMainThread;
        if (asyncOperationListener == null) {
            return false;
        }
        asyncOperationListener.onAsyncOperationCompleted((AsyncOperation) message.obj);
        return false;
    }
}
