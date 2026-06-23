package org.greenrobot.eventbus.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.greenrobot.eventbus.Logger;

/* JADX INFO: loaded from: classes2.dex */
public class ExceptionToResourceMapping {
    public final Map<Class<? extends Throwable>, Integer> throwableToMsgIdMap = new HashMap();

    public Integer mapThrowable(Throwable th) {
        int i = 20;
        Throwable cause = th;
        do {
            Integer numMapThrowableFlat = mapThrowableFlat(cause);
            if (numMapThrowableFlat != null) {
                return numMapThrowableFlat;
            }
            cause = cause.getCause();
            i--;
            if (i <= 0 || cause == th) {
                break;
            }
        } while (cause != null);
        Logger.Default.get().log(Level.FINE, "No specific message resource ID found for " + th);
        return null;
    }

    protected Integer mapThrowableFlat(Throwable th) {
        Class<?> cls = th.getClass();
        Integer value = this.throwableToMsgIdMap.get(cls);
        if (value == null) {
            Class<? extends Throwable> cls2 = null;
            for (Map.Entry<Class<? extends Throwable>, Integer> entry : this.throwableToMsgIdMap.entrySet()) {
                Class<? extends Throwable> key = entry.getKey();
                if (key.isAssignableFrom(cls) && (cls2 == null || cls2.isAssignableFrom(key))) {
                    value = entry.getValue();
                    cls2 = key;
                }
            }
        }
        return value;
    }

    public ExceptionToResourceMapping addMapping(Class<? extends Throwable> cls, int i) {
        this.throwableToMsgIdMap.put(cls, Integer.valueOf(i));
        return this;
    }
}
