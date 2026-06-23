package com.icwork.shiningglass.model.data;

import android.graphics.Color;
import com.alibaba.fastjson2.JSONB;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.ble.BleListener;
import com.cdbwsoft.library.ble.BleManager;
import com.icwork.shiningglass.base.App;
import com.icwork.shiningglass.ble.HeartBeatDevice;
import com.icwork.shiningglass.ble.HeartBeatDeviceFactory;
import com.icwork.shiningglass.model.bean.DiyData;
import com.icwork.shiningglass.ui.activity.ConnectActivity;
import com.icwork.shiningglass.ui.utils.ByteUtils;
import com.icwork.shiningglass.ui.utils.LogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.ByteCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class DiyAgreement {
    public static DiyAgreement instance;
    BleManager bleManager;
    Map<BleDevice, byte[]> dataMap = new HashMap();
    Map<BleDevice, TextData> dataMap1 = new HashMap();
    Map<BleDevice, DiyAgreementListener> listenerMap = new HashMap();
    BleListenerImpl bleListener = new BleListenerImpl();

    public interface DiyAgreementListener {
        void onFinishSend(BleDevice bleDevice);
    }

    public static DiyAgreement getInstance() {
        if (instance == null) {
            instance = new DiyAgreement();
        }
        return instance;
    }

    private DiyAgreement() {
        this.bleManager = ConnectActivity.getBleManager();
        if (this.bleManager == null) {
            this.bleManager = App.getInstance().getBleManager(HeartBeatDevice.class, this.bleListener, new HeartBeatDeviceFactory(App.getInstance()));
        }
        this.bleManager.registerBleListener(this.bleListener);
    }

    public void sendDiy(BleDevice bleDevice, DiyData diyData, DiyAgreementListener diyAgreementListener) {
        if (diyData != null) {
            byte[] diyBytes1236 = getDiyBytes1236(diyData.getData());
            ArrayList<Integer> colorArray = diyData.getColorArray();
            if (colorArray == null || colorArray == null) {
                return;
            }
            int length = diyBytes1236.length + (colorArray.size() * 3);
            byte[] bArr = new byte[length];
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < colorArray.size(); i++) {
                arrayList.add(Byte.valueOf((byte) Color.red(colorArray.get(i).intValue())));
                arrayList.add(Byte.valueOf((byte) Color.green(colorArray.get(i).intValue())));
                arrayList.add(Byte.valueOf((byte) Color.blue(colorArray.get(i).intValue())));
            }
            int size = arrayList.size();
            byte[] bArr2 = new byte[size];
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                bArr2[i2] = ((Byte) arrayList.get(i2)).byteValue();
            }
            System.arraycopy(diyBytes1236, 0, bArr, 0, diyBytes1236.length);
            System.arraycopy(bArr2, 0, bArr, diyBytes1236.length, size);
            this.dataMap.put(bleDevice, bArr);
            this.listenerMap.put(bleDevice, diyAgreementListener);
            sendSendDataCommand(bleDevice, length, diyBytes1236.length);
        }
    }

    private void sendSendDataCommand(BleDevice bleDevice, int i, int i2) {
        if (bleDevice == null) {
            return;
        }
        byte[] bArrInt2Bytes = Agreement.int2Bytes(i);
        byte[] bArrInt2Bytes2 = Agreement.int2Bytes(i2);
        byte[] encryptData = Agreement.getEncryptData(new byte[]{9, JSONB.Constants.BC_INT32_SHORT_ZERO, 65, 84, 83, bArrInt2Bytes[0], bArrInt2Bytes[1], bArrInt2Bytes2[0], bArrInt2Bytes2[1], 1, 0, 0, 0, 0, 0, 0});
        LogUtil.d("发送的数据11：" + ByteUtils.binaryToHexString(encryptData));
        bleDevice.writeCharacteristic(encryptData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean parseSendDataCommand(byte[] bArr) {
        return bArr != null && 7 <= bArr.length && bArr[1] == 68 && bArr[2] == 65 && bArr[3] == 84 && bArr[4] == 83 && bArr[5] == 79 && bArr[6] == 75;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isResultReOk(byte[] bArr) {
        return bArr != null && bArr.length >= 5 && bArr[1] == 82 && bArr[2] == 69 && bArr[3] == 79 && bArr[4] == 75;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<byte[]> getSendData(BleDevice bleDevice, byte[] bArr) {
        int length;
        ArrayList arrayList = new ArrayList();
        if (bleDevice == null) {
            return null;
        }
        arrayList.clear();
        LogUtil.d("MTU设置状态：" + bleDevice.isSetMtuStatus());
        int i = bleDevice.isSetMtuStatus() ? 98 : 18;
        LogUtil.d("数据总长度：" + bArr.length);
        if (bArr.length % i == 0) {
            length = bArr.length / i;
        } else {
            length = (bArr.length / i) + 1;
        }
        LogUtil.d("frameTotal：" + length);
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            if (i3 == length - 1) {
                int length2 = bArr.length - i2;
                byte[] bArr2 = new byte[i + 2];
                bArr2[0] = (byte) (length2 + 1);
                bArr2[1] = (byte) i3;
                System.arraycopy(bArr, i2, bArr2, 2, length2);
                LogUtil.d("最后的数据：" + length2 + " index:0");
                arrayList.add(bArr2);
                i2 = 0;
            } else {
                byte[] bArr3 = new byte[i + 2];
                bArr3[0] = (byte) (i + 1);
                bArr3[1] = (byte) i3;
                System.arraycopy(bArr, i2, bArr3, 2, i);
                i2 += i;
                arrayList.add(bArr3);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSendDataFinishCommand(BleDevice bleDevice) {
        if (bleDevice == null) {
            return;
        }
        bleDevice.writeCharacteristic(Agreement.getEncryptData(new byte[]{5, JSONB.Constants.BC_INT32_SHORT_ZERO, 65, 84, 67, 80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean parseDataCheck(byte[] bArr) {
        return bArr != null && 8 <= bArr.length && bArr[1] == 68 && bArr[2] == 65 && bArr[3] == 84 && bArr[4] == 67 && bArr[5] == 80 && bArr[6] == 79 && bArr[7] == 75;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:10:0x0018. Please report as an issue. */
    private byte[] getDiyBytes1236(byte[] bArr) {
        int i;
        int i2;
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = new byte[72];
        byte b = 0;
        byte b2 = 0;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            int i4 = i3 / 12;
            int i5 = i3 % 12;
            if (bArr[i3] == 1) {
                switch (i5) {
                    case 0:
                        i = b | ByteCompanionObject.MIN_VALUE;
                        b = (byte) i;
                        break;
                    case 1:
                        i = b | JSONB.Constants.BC_INT32_SHORT_MIN;
                        b = (byte) i;
                        break;
                    case 2:
                        i = b | 32;
                        b = (byte) i;
                        break;
                    case 3:
                        i = b | JSONB.Constants.BC_INT32_NUM_16;
                        b = (byte) i;
                        break;
                    case 4:
                        i = b | 8;
                        b = (byte) i;
                        break;
                    case 5:
                        i = b | 4;
                        b = (byte) i;
                        break;
                    case 6:
                        i = b | 2;
                        b = (byte) i;
                        break;
                    case 7:
                        i = b | 1;
                        b = (byte) i;
                        break;
                    case 8:
                        i2 = b2 | ByteCompanionObject.MIN_VALUE;
                        b2 = (byte) i2;
                        break;
                    case 9:
                        i2 = b2 | JSONB.Constants.BC_INT32_SHORT_MIN;
                        b2 = (byte) i2;
                        break;
                    case 10:
                        i2 = b2 | 32;
                        b2 = (byte) i2;
                        break;
                    case 11:
                        i2 = b2 | JSONB.Constants.BC_INT32_NUM_16;
                        b2 = (byte) i2;
                        break;
                }
            }
            if (i5 == 11) {
                int i6 = i4 * 2;
                bArr2[i6] = b;
                bArr2[i6 + 1] = b2;
                b = 0;
                b2 = 0;
            }
        }
        return bArr2;
    }

    class BleListenerImpl extends BleListener {
        BleListenerImpl() {
        }

        @Override // com.cdbwsoft.library.ble.BleListener
        public void onChanged(BleDevice bleDevice, byte[] bArr) {
            if (DiyAgreement.this.dataMap.containsKey(bleDevice) && DiyAgreement.this.listenerMap.containsKey(bleDevice)) {
                System.arraycopy(bArr, 0, new byte[bArr.length], 0, bArr.length);
                byte[] decodeData = Agreement.getDecodeData(bArr);
                StringBuffer stringBuffer = new StringBuffer();
                for (byte b : decodeData) {
                    stringBuffer.append((char) b);
                }
                LogUtil.d("MCU回复App:" + bleDevice.getBleName() + " " + stringBuffer.toString());
                if (DiyAgreement.this.parseSendDataCommand(decodeData)) {
                    TextData textData = new TextData(0, DiyAgreement.this.getSendData(bleDevice, DiyAgreement.this.dataMap.get(bleDevice)));
                    DiyAgreement.this.dataMap1.put(bleDevice, textData);
                    LogUtil.d("设备数：" + DiyAgreement.this.dataMap1.size());
                    DiyAgreement.this.sendTextData(bleDevice, textData.getDataList().get(textData.getCurIndex()));
                    return;
                }
                if (DiyAgreement.this.isResultReOk(decodeData)) {
                    TextData textData2 = DiyAgreement.this.dataMap1.get(bleDevice);
                    if (textData2 == null) {
                        return;
                    }
                    List<byte[]> dataList = textData2.getDataList();
                    int curIndex = textData2.getCurIndex();
                    if (curIndex < dataList.size() - 1) {
                        int i = curIndex + 1;
                        textData2.setCurIndex(i);
                        DiyAgreement.this.sendTextData(bleDevice, dataList.get(i));
                        return;
                    } else {
                        if (curIndex == dataList.size() - 1) {
                            LogUtil.d("数据发送完成：" + curIndex);
                            DiyAgreement.this.sendSendDataFinishCommand(bleDevice);
                            return;
                        }
                        return;
                    }
                }
                if (DiyAgreement.this.parseDataCheck(decodeData)) {
                    LogUtil.d("返回DATCPOK");
                    DiyAgreementListener diyAgreementListener = DiyAgreement.this.listenerMap.get(bleDevice);
                    if (diyAgreementListener != null) {
                        diyAgreementListener.onFinishSend(bleDevice);
                    }
                    DiyAgreement.this.dataMap.remove(bleDevice);
                    DiyAgreement.this.dataMap1.remove(bleDevice);
                    DiyAgreement.this.listenerMap.remove(bleDevice);
                    return;
                }
                if (DiyAgreement.this.parseErrorDataCheck(decodeData)) {
                    LogUtil.d("返回ERROR=");
                    DiyAgreementListener diyAgreementListener2 = DiyAgreement.this.listenerMap.get(bleDevice);
                    if (diyAgreementListener2 != null) {
                        diyAgreementListener2.onFinishSend(bleDevice);
                    }
                    DiyAgreement.this.dataMap.remove(bleDevice);
                    DiyAgreement.this.dataMap1.remove(bleDevice);
                    DiyAgreement.this.listenerMap.remove(bleDevice);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean parseErrorDataCheck(byte[] bArr) {
        return bArr != null && 8 <= bArr.length && ((char) bArr[1]) == 'E' && ((char) bArr[2]) == 'R' && ((char) bArr[3]) == 'R' && ((char) bArr[4]) == 'O' && ((char) bArr[5]) == 'R';
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTextData(BleDevice bleDevice, byte[] bArr) {
        bleDevice.writeCharacteristicBy2(bArr);
        LogUtil.d("数据发送:" + bleDevice.getBleName() + " frameData:" + ByteUtils.binaryToHexString(bArr));
    }

    public void clear() {
        BleManager bleManager = this.bleManager;
        if (bleManager != null) {
            bleManager.unRegisterBleListener(this.bleListener);
            Map<BleDevice, DiyAgreementListener> map = this.listenerMap;
            if (map != null) {
                map.clear();
            }
            Map<BleDevice, byte[]> map2 = this.dataMap;
            if (map2 != null) {
                map2.clear();
            }
            instance = null;
        }
    }
}
