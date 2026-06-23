package com.icwork.shiningglass.model.data;

import com.alibaba.fastjson2.JSONB;
import com.icwork.shiningglass.ui.utils.ByteUtils;
import com.icwork.shiningglass.ui.utils.LogUtil;
import csh.tiro.cc.aes;
import java.util.Random;

/* JADX INFO: loaded from: classes.dex */
public class Agreement {
    public static byte[] getEnterDiyCommand() {
        return new byte[]{6, 83, JSONB.Constants.BC_STR_ASCII_FIX_4, 86, 69, 87, 1, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getEnterDiyCommand1() {
        return new byte[]{6, 83, JSONB.Constants.BC_STR_ASCII_FIX_4, 86, 69, 87, 3, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getExitDiyCommand() {
        LogUtil.d("退出diy时没有数据");
        return new byte[]{6, 83, JSONB.Constants.BC_STR_ASCII_FIX_4, 86, 69, 87, 0, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getExitDiySaveCommand() {
        LogUtil.d("退出diy时有数据");
        return new byte[]{6, 83, JSONB.Constants.BC_STR_ASCII_FIX_4, 86, 69, 87, 2, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getExitRhyCommand() {
        return new byte[]{4, 83, 79, 85, 84, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getCallPhone(int i, int i2) {
        LogUtil.e("status:" + i + " time:" + i2);
        return new byte[]{6, 67, 65, 76, 76, (byte) i, (byte) i2, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getContentCommand(int i) {
        return new byte[]{5, JSONB.Constants.BC_STR_ASCII_FIX_4, 79, JSONB.Constants.BC_INT32_SHORT_ZERO, 69, (byte) i, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getTextColor(byte b, byte b2, byte b3, boolean z) {
        return new byte[]{6, 70, 67, z ? (byte) 1 : (byte) 0, b, b2, b3, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getPlayerDiyCommand() {
        return new byte[]{4, 80, 76, 65, 89, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getTextBgColor(byte b, byte b2, byte b3, boolean z) {
        byte[] bArr = {6, 66, 67, z ? (byte) 1 : (byte) 0, b, b2, b3, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
        LogUtil.d("发送的文本背景颜色：" + ByteUtils.binaryToHexString(bArr));
        return bArr;
    }

    public static byte[] getDefaultMode(int i, boolean z) {
        byte[] bArr = {3, JSONB.Constants.BC_STR_ASCII_FIX_4, z ? (byte) 1 : (byte) 0, (byte) i, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
        LogUtil.d("得到预设颜色模式命令:" + ByteUtils.binaryToHexString(bArr));
        return bArr;
    }

    public static byte[] getSpeed(int i) {
        byte b = (byte) i;
        LogUtil.e("speed:" + ((int) b));
        return new byte[]{6, 83, 80, 69, 69, JSONB.Constants.BC_INT32_SHORT_ZERO, b, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getLight(int i) {
        byte b = (byte) i;
        LogUtil.e("light:" + ((int) b));
        return new byte[]{6, 76, 73, JSONB.Constants.BC_INT32_SHORT_MAX, JSONB.Constants.BC_INT32, 84, b, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getAnimLoopCommand() {
        LogUtil.e("得到发送动画循环命令:");
        return new byte[]{4, 76, 79, 79, 65, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getAnimCommand(int i) {
        byte b = (byte) i;
        LogUtil.e("发送动画命令:" + ((int) b));
        return new byte[]{5, 65, JSONB.Constants.BC_STR_ASCII_FIX_5, 73, JSONB.Constants.BC_STR_ASCII_FIX_4, b, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] getImageCommand(int i) {
        byte b = (byte) i;
        LogUtil.e("得到发送图片命令:" + ((int) b));
        return new byte[]{5, 73, JSONB.Constants.BC_STR_ASCII_FIX_4, 65, JSONB.Constants.BC_INT32_SHORT_MAX, b, getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom(), getRandom()};
    }

    public static byte[] int2Bytes(int i) {
        return new byte[]{(byte) (i / 256), (byte) (i % 256)};
    }

    public static byte[] getEncryptData(byte[] bArr) {
        aes.cipher(bArr, bArr);
        return bArr;
    }

    public static byte[] getDecodeData(byte[] bArr) {
        aes.invCipher(bArr, bArr);
        return bArr;
    }

    public static byte getRandom() {
        return (byte) (new Random().nextInt(256) & 255);
    }
}
