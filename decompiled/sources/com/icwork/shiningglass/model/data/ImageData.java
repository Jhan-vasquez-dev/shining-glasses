package com.icwork.shiningglass.model.data;

import androidx.core.view.InputDeviceCompat;
import androidx.core.view.PointerIconCompat;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import com.cdbwsoft.library.ble.BleDevice;
import com.cdbwsoft.library.bluetooth.BtDevice;
import com.cdbwsoft.library.bluetooth.BtManager;
import com.cdbwsoft.library.net.ErrorStatus;
import org.apache.http.HttpStatus;

/* JADX INFO: loaded from: classes.dex */
public class ImageData {
    public static int[] getImage1() {
        return new int[]{0, 16320, PointerIconCompat.TYPE_COPY, 53244, 15600, 4080, 4080, 15612, 53235, PointerIconCompat.TYPE_TEXT, 16320, 0, 0, 16320, PointerIconCompat.TYPE_COPY, 53244, 15600, 4080, 4080, 15612, 53235, PointerIconCompat.TYPE_TEXT, 16320, 0};
    }

    public static int[] getImage2() {
        return new int[]{0, 4112, 29812, 130557, 32756, 8144, 32756, 130557, 29812, 4112, 0, 0, 0, 0, 4112, 29812, 65021, 32756, 8144, 32756, 130557, 29812, 4112, 0};
    }

    public static int[] getImage3() {
        return new int[]{0, 4092, 15408, 64764, 65520, 16380, 65520, 64572, 15600, 4092, 0, 0, 0, 0, 4092, 15408, 64764, 65520, 16380, 65520, 64572, 15600, 4092, 0};
    }

    public static int[] getImage4() {
        return new int[]{0, 16368, 49164, 212163, 196851, 196851, 196851, 212163, 49164, 16368, 0, 0, 0, 0, 16368, 49164, 212163, 196851, 196851, 196851, 212163, 49164, 16368, 0};
    }

    public static int[] getImage5() {
        return new int[]{0, 16368, 49164, 196611, 196611, 212739, 197379, 197379, 49164, 16368, 0, 0, 0, 0, 16368, 49164, 196611, 196611, 212739, 197379, 197379, 49164, 16368, 0};
    }

    public static int[] getImage6() {
        return new int[]{5376, 27200, 114576, 114580, 28644, 7161, 28644, 114580, 114576, 27200, 5376, 0, 0, 5376, 27200, 114576, 114580, 28644, 7161, 28644, 114580, 114576, 27200, 5376};
    }

    public static int[] getImage7() {
        return new int[]{0, 64514, 13096, 16298, 13098, 64554, 10, 2, 340, 0, 0, 0, 0, 0, 0, 64514, 13096, 16298, 13098, 64554, 10, 2, 340, 0};
    }

    public static int[] getImage8() {
        return new int[]{0, 15360, 15363, 65535, PointerIconCompat.TYPE_TEXT, PointerIconCompat.TYPE_TEXT, PointerIconCompat.TYPE_TEXT, PointerIconCompat.TYPE_COPY, 1023, 3072, 0, 0, 0, 0, 15360, 15363, 65535, PointerIconCompat.TYPE_TEXT, PointerIconCompat.TYPE_TEXT, PointerIconCompat.TYPE_TEXT, PointerIconCompat.TYPE_COPY, 1023, 3072, 0};
    }

    public static int[] getImage9() {
        return new int[]{0, 3072, 13296, 49164, 52227, 49155, 15363, BtManager.MSG_READ_DATA, 972, 240, 0, 0, 0, 0, 3072, 13296, 49164, 52227, 49155, 15363, BtManager.MSG_READ_DATA, 972, 240, 0};
    }

    public static int[] getImage10() {
        return new int[]{0, 0, 0, 14352, 49668, 260757, 49668, 12432, 0, 0, 0, 0, 0, 0, 0, 0, 14352, 49668, 260757, 49668, 12432, 0, 0, 0};
    }

    public static int[] getImage11() {
        return new int[]{4080, 12348, 62271, 258111, 262143, 262143, 258111, 62271, 12348, 4080, 0, 0, 0, 0, 4080, 12348, 62271, 258111, 262143, 262143, 258111, 62271, 12348, 4080};
    }

    public static int[] getImage12() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 130034, 130034, 130034, 130034, 130034, 0, 130034, 130034, 130034, 0, 0, 130034, 390637, 326129, 3949055, 130034, 130034, 130034, 130034, 130034, 130034, 0, 390637, 130034, 260335, 260335, 260335, 3702267, 3702267, 130034, 65526, 130034, 0, 0, 326129, 130034, 0, 0, 3702267, 3702267, 3702267, 130034, 65526, 130034, 0, 130034, 390637, 260335, 260335, 260335, 76924, 3702267, 3702267, 130034, 65526, 130034, 65526, 130034, 390637, 260335, 260335, 260335, 3702267, 3702267, 3702267, 130034, 65526, 130034, 130034, 130034, 390637, 260335, 260335, 260335, 3702267, 3702267, 3702267, 130034, 65526, 130034, 130034, 130034, 390637, 260335, 260335, 260335, 76924, 3702267, 3702267, 130034, 65526, 130034, 65526, 0, 326129, 130034, 0, 0, 3702267, 3702267, 3702267, 130034, 65526, 130034, 0, 0, 390637, 130034, 260335, 260335, 260335, 3702267, 3702267, 130034, 65526, 130034, 0, 0, 0, 130034, 326129, 390637, 3949055, 130034, 130034, 130034, 130034, 130034, 130034, 0, 0, 0, 130034, 130034, 130034, 130034, 130034, 0, 130034, 130034, 130034, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 130034, 130034, 130034, 130034, 130034, 0, 130034, 130034, 130034, 0, 0, 130034, 390637, 326129, 3949055, 130034, 130034, 130034, 130034, 130034, 130034, 0, 390637, 130034, 260335, 260335, 260335, 3702267, 3702267, 130034, 65526, 130034, 0, 0, 326129, 130034, 0, 0, 3702267, 3702267, 3702267, 130034, 65526, 130034, 0, 130034, 390637, 260335, 260335, 260335, 76924, 3702267, 3702267, 130034, 65526, 130034, 65526, 130034, 390637, 260335, 260335, 260335, 3702267, 3702267, 3702267, 130034, 65526, 130034, 130034, 130034, 390637, 260335, 260335, 260335, 3702267, 3702267, 3702267, 130034, 65526, 130034, 130034, 130034, 390637, 260335, 260335, 260335, 76924, 3702267, 3702267, 130034, 65526, 130034, 65526, 0, 326129, 130034, 0, 0, 3702267, 3702267, 3702267, 130034, 65526, 130034, 0, 0, 390637, 130034, 260335, 260335, 260335, 3702267, 3702267, 130034, 65526, 130034, 0, 0, 0, 130034, 326129, 390637, 3949055, 130034, 130034, 130034, 130034, 130034, 130034, 0, 0, 0, 130034, 130034, 130034, 130034, 130034, 0, 130034, 130034, 130034, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getImage13() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 0, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3585535, 3585535, 3585535, 0, 3519227, 3585535, 3519227, 16514301, 16514301, 16514301, 11646205, 16514301, 16514301, 16514301, 0, 0, 3519227, 3519227, 3519227, 16514301, 0, 0, 16514301, 16514301, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 16514301, 16514301, 16514301, 16514301, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 16514301, 16514301, 16514301, 3686141, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 16514301, 16514301, 16514301, 3686141, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 16514301, 16514301, 16514301, 16514301, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 0, 0, 16514301, 16514301, 16514301, 16514301, 3620347, 3620347, 3519227, 3585535, 3519227, 16514301, 16514301, 16514301, 11646205, 16514301, 16514301, 16514301, 0, 0, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3585535, 3585535, 3585535, 0, 0, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 0, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3585535, 3585535, 3585535, 0, 3519227, 3585535, 3519227, 16514301, 16514301, 16514301, 11646205, 16514301, 16514301, 16514301, 0, 0, 3519227, 3519227, 3519227, 16514301, 0, 0, 16514301, 16514301, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 16514301, 16514301, 16514301, 16514301, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 16514301, 16514301, 16514301, 3686141, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 16514301, 16514301, 16514301, 3686141, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 16514301, 16514301, 16514301, 16514301, 16514301, 16514301, 3620347, 3620347, 3519227, 3519227, 3519227, 16514301, 0, 0, 16514301, 16514301, 16514301, 16514301, 3620347, 3620347, 3519227, 3585535, 3519227, 16514301, 16514301, 16514301, 11646205, 16514301, 16514301, 16514301, 0, 0, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3585535, 3585535, 3585535, 0, 0, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 3519227, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getImage14() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3028475, 3028475, 3094264, 3094264, 3094264, 0, 0, 0, 0, 3094264, 0, 3028475, 3028475, 11316987, 3094264, 3094264, 3094264, 3094264, 0, 0, 0, 3094264, 3028475, 3028475, 0, 3028475, 11251192, 11251192, 11251192, 3094264, 3094264, 3094264, 3094264, 3094264, 3028475, 3028475, 3028475, 3028475, 11251192, 0, 11251192, 3094264, 3094264, 0, 0, 0, 3028475, 3028475, 3028475, 3028475, 11251192, 0, 11251192, 3094264, 3094264, 0, 0, 0, 3094264, 3094264, 3160060, 3094264, 11382777, 0, 11382777, 3160053, 3160053, 3160053, 3160053, 3160053, 3094264, 3160060, 3160060, 3160060, 11382777, 0, 11382777, 3160053, 3160053, 3160053, 3160053, 3160053, 3028475, 3028475, 3028475, 3028475, 11251192, 0, 11251192, 3094264, 3094264, 0, 0, 0, 3028475, 3028475, 3028475, 3028475, 11251192, 0, 11251192, 3094264, 3094264, 0, 0, 0, 3028475, 3028475, 0, 3028475, 11251192, 11251192, 11251192, 3094264, 3094264, 3094264, 3094264, 3094264, 0, 3028475, 3028475, 11316987, 3094264, 3094264, 3094264, 3094264, 0, 0, 0, 3094264, 0, 0, 3028475, 3028475, 3094264, 3094264, 3094264, 0, 0, 0, 0, 3094264, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3028475, 3028475, 3094264, 3094264, 3094264, 0, 0, 0, 0, 3094264, 1, 3028475, 3028475, 11316987, 3094264, 3094264, 3094264, 3094264, 0, 0, 0, 3094264, 3028475, 3028475, 0, 3028475, 11251192, 11251192, 11251192, 3094264, 3094264, 3094264, 3094264, 3094264, 3028475, 3028475, 3028475, 3028475, 11251192, 0, 11251192, 3094264, 3094264, 0, 0, 0, 3028475, 3028475, 3028475, 3028475, 11251192, 0, 11251192, 3094264, 3094264, 0, 0, 0, 3094264, 3094264, 3160060, 3094264, 11382777, 0, 11382777, 3160053, 3160053, 3160053, 3160053, 3160053, 3094264, 3160060, 3160060, 3160060, 11382777, 0, 11382777, 3160053, 3160053, 3160053, 3160053, 3160053, 3028475, 3028475, 3028475, 3028475, 11251192, 0, 11251192, 3094264, 3094264, 0, 0, 0, 3028475, 3028475, 3028475, 3028475, 11251192, 0, 11251192, 3094264, 3094264, 0, 0, 0, 3028475, 3028475, 0, 3028475, 11251192, 11251192, 11251192, 3094264, 3094264, 3094264, 3094264, 3094264, 0, 3028475, 3028475, 11316987, 3094264, 3094264, 3094264, 3094264, 0, 0, 0, 3094264, 0, 0, 3028475, 3028475, 3094264, 3094264, 3094264, 0, 0, 0, 0, 3094264, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getImage000() {
        return new int[]{PointerIconCompat.TYPE_GRAB, 2046, 3591, BtManager.MSG_READ_DATA, BtManager.MSG_READ_DATA, 3591, 1950, 924, 0, PointerIconCompat.TYPE_GRAB, 2046, 3591, BtManager.MSG_READ_DATA, BtManager.MSG_READ_DATA, 3591, 2046, PointerIconCompat.TYPE_GRAB, 0, PointerIconCompat.TYPE_GRAB, 2046, 3591, BtManager.MSG_READ_DATA, BtManager.MSG_READ_DATA, 3591, 2046, PointerIconCompat.TYPE_GRAB, 0, 4095, 4095, 3, 3, 3, 0, 4095, 4095, 0, 3072, 3072, 4095, 4095, 3072, 3072, 0, 4095, 4095, 3171, 3171, BtManager.MSG_READ_DATA};
    }

    public static int[] getImage001() {
        return new int[]{3107, 2065, 2080, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 8, 1028, 520, 1024, 224, 280, 516, 1330, 1194, 2341, 2085, 2085, 2085, 2085, 2341, 1194, 1330, 516, 280, 224, 1024, 520, 1028, 8, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 2496, 2081, 3091, 3623};
    }

    public static int[] getImage002() {
        return new int[]{4095, 4089, 4086, 4078, 4078, 4078, 3086, 3049, 2035, 2043, 1923, 1795, 1539, BtDevice.STATE_CONNECTED, BtManager.MSG_READ_DATA, 3587, 4095, 4095, 4093, 4089, 4083, 4071, 4079, 4079, 4079, 4079, 4071, 4083, 4089, 4093, 4095, 4095, 3587, BtManager.MSG_READ_DATA, BtDevice.STATE_CONNECTED, 1539, 1795, 1923, 2043, 2035, 3049, 3086, 4078, 4078, 4078, 4086, 4089, 4095};
    }

    public static int[] getImage003() {
        return new int[]{4095, 4095, 4095, 4095, 4095, 2111, 2079, 2063, 2151, 2263, 2471, 2903, 2727, 2391, 2727, 2375, 2695, BleDevice.STATE_WRITING_DESCRIPTOR, 2575, 2075, 2109, 2174, 3198, 3198, 3198, 3198, 2174, 2109, 2075, 2063, 2151, 2263, 2471, 2903, 2727, 2391, 2727, 2375, 2695, BleDevice.STATE_WRITING_DESCRIPTOR, 2575, 2079, 2111, 4095, 4095, 4095, 4095, 4095};
    }

    public static int[] getImage004() {
        return new int[]{4095, 4095, 4095, 4095, 3327, 2303, 511, 963, 769, 1024, 1024, 2104, 2160, 2144, 2112, 3265, 3073, 3587, 4095, 4095, 4095, 4035, 3969, 3969, 3969, 4017, 4035, 4095, 4095, 4095, 3587, 3073, 2241, 2112, 2144, 3184, 3128, 1024, 1024, 769, 963, 511, 2303, 3327, 4095, 4095, 4095, 4095};
    }

    public static int[] getImage005() {
        return new int[]{4095, 4095, 4095, 4095, 3839, 3839, 3839, 3839, 3839, 3839, 3839, 3615, 3615, 3599, 3599, 3615, 3615, 3711, 4095, 4095, 4092, 4089, 4083, 4087, 4087, 4087, 4087, 4091, 4095, 4095, 3839, 3839, 3839, 3839, 3839, 3839, 3839, 3615, 3615, 3599, 3599, 3615, 3615, 3711, 4095, 4095, 4095, 4095};
    }

    public static int[] getImage006() {
        return new int[]{4095, 4095, 4095, 3327, 2303, 255, 511, 1023, 2047, 3587, 3201, 3553, 2528, 3008, 3008, 2444, 2072, BleDevice.MSG_CONNECT, 3073, 3591, 4095, 4092, 4093, 4093, 4093, 4093, 4095, 4095, 3591, 3073, BleDevice.MSG_CONNECT, 2072, 2444, 3008, 3008, 2528, 3553, 3201, 3587, 2047, 1023, 511, 255, 2303, 3327, 4095, 4095, 4095};
    }

    public static int[] getImage007() {
        return new int[]{4095, 4095, 4031, 3231, 3103, 3647, 3199, 3199, 3199, 3199, 3615, 3871, 4095, 4095, 4031, 3999, 3983, 4007, 4023, 4019, 4027, 4027, 4027, 4027, 4027, 4027, 4027, 4027, 4019, 4023, 4007, 3983, 3999, 4031, 4095, 4095, 3871, 3615, 3711, 3199, 3199, 3199, 3647, 3615, 3231, 3903, 4095, 4095};
    }

    public static int[] getImage008() {
        return new int[]{4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4031, 4063, 4063, 3983, 3975, 3859, 3931, 3677, 3293, 3548, 3550, 3550, 3548, 3277, 3821, 3821, 3277, 3548, 3550, 3550, 3550, 3292, 3677, 3929, 3867, 3987, 3975, 4047, 4063, 4031, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095};
    }

    public static int[] getImage009() {
        return new int[]{4095, 4095, 4095, 4095, BtManager.MSG_READ_DATA, 3069, 3045, 3045, 2973, 2973, 2685, 2685, 3069, BtManager.MSG_READ_DATA, 4095, 4095, 4095, 4095, 4095, BtManager.MSG_READ_DATA, 3069, 3045, 3045, 2973, 2973, 2685, 2685, 3069, BtManager.MSG_READ_DATA, 4095, 4095, 4095, 4095, 4095, BtManager.MSG_READ_DATA, 3069, 3045, 3045, 2973, 2973, 2685, 2685, 3069, BtManager.MSG_READ_DATA, 4095, 4095, 4095, 4095};
    }

    public static int[] getImage0010() {
        return new int[]{4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4065, 4044, 4062, 3614, 3806, 3294, 3550, 2526, 2270, 734, 1758, 1758, 1246, 734, 862, 3870, 4062, 4046, 4078, 4065, 4083, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095, 4095};
    }

    public static int[] getImage0011() {
        return new int[]{2730, 1365, 2730, 1365, 2730, 1365, 2730, 1365, 2730, 1365, 2730, 1365, 2730, 1525, 3066, 2045, 3838, 2175, 2587, 3081, 2057, 3095, 3844, 3585, 3079, BleDevice.MSG_SET_NOTIFICATION, BtManager.MSG_WRITE_DATA, 2574, 2335, 3711, 3838, 2045, 3066, 1525, 2730, 1365, 2730, 1365, 2730, 1365, 2730, 1365, 2730, 1365, 2730, 1365, 2730, 1365};
    }

    public static int[] getImage0012() {
        return new int[]{0, 0, 0, 0, 0, 252, 1022, 1539, 2300, 1022, 1539, 2300, 1022, 2047, 4095, Opcodes.I2D, 3633, 2556, 2942, 1790, 1918, 1922, 1978, 1954, 1978, 1954, 1978, 1922, 1918, 1790, 2942, 2556, 3619, Opcodes.D2L, 4095, 2047, 1022, 2300, 1539, 1022, 2300, 1539, 1022, 252, 0, 0, 0, 0};
    }

    public static int[] getImage0013() {
        return new int[]{0, 256, HttpStatus.SC_BAD_REQUEST, 920, 984, 3070, 4094, 4095, 3587, 3587, 3833, 2301, 1537, 1948, 2014, 1822, 1806, 2030, 2030, 1806, 1916, 1793, 2175, 4095, 4095, 2175, 1793, 1916, 1806, 2030, 2030, 1806, 1822, 2014, 1948, 1537, 2301, 3833, 3587, 3587, 4095, 4094, 3070, 984, 920, HttpStatus.SC_BAD_REQUEST, 256, 0};
    }

    public static int[] getImage0014() {
        return new int[]{3583, 3079, 4067, 3939, 3827, 3583, 3079, 4067, 3939, 3827, 3583, 3079, 4067, 3555, 1779, 2942, 3581, 4095, 2046, 3971, 4091, 3083, 2730, 2720, 2730, 3083, 4091, 3971, 2046, 4095, 3581, 2942, 1779, 3555, 4067, 3079, 3583, 3827, 3939, 4067, 3079, 3583, 3827, 3939, 4067, 3079, 3583, 3839};
    }

    public static int[] getanim_1_1() {
        return new int[]{478, ErrorStatus.IO_ERROR, 252, 240, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_GRAB, 2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 128, 64, 128, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB, HttpStatus.SC_GATEWAY_TIMEOUT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, ErrorStatus.IO_ERROR, 478, 911};
    }

    public static int[] getanim_1_2() {
        return new int[]{252, 240, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_GRAB, 2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 128, 448, 224, 112, 224, 448, 128, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB, HttpStatus.SC_GATEWAY_TIMEOUT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, ErrorStatus.IO_ERROR};
    }

    public static int[] getanim_1_3() {
        return new int[]{HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_GRAB, 2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB, HttpStatus.SC_GATEWAY_TIMEOUT, HttpStatus.SC_GATEWAY_TIMEOUT};
    }

    public static int[] getanim_1_4() {
        return new int[]{2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 0, 0, 480, PointerIconCompat.TYPE_TEXT, 2040, 2044, 1022, 511, 1022, 2044, 2040, PointerIconCompat.TYPE_TEXT, 480, 0, 0, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB};
    }

    public static int[] getanim_1_5() {
        return new int[]{HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_GRAB, 2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB, HttpStatus.SC_GATEWAY_TIMEOUT, HttpStatus.SC_GATEWAY_TIMEOUT};
    }

    public static int[] getanim_1_6() {
        return new int[]{478, ErrorStatus.IO_ERROR, 252, 240, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_GRAB, 2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 128, 64, 128, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB, HttpStatus.SC_GATEWAY_TIMEOUT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, ErrorStatus.IO_ERROR, 478, 911};
    }

    public static int[] getanim_1_7() {
        return new int[]{252, 240, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_GRAB, 2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 128, 448, 224, 112, 224, 448, 128, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB, HttpStatus.SC_GATEWAY_TIMEOUT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, ErrorStatus.IO_ERROR};
    }

    public static int[] getanim_1_8() {
        return new int[]{HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_GRAB, 2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB, HttpStatus.SC_GATEWAY_TIMEOUT, HttpStatus.SC_GATEWAY_TIMEOUT};
    }

    public static int[] getanim_1_9() {
        return new int[]{2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 0, 0, 480, PointerIconCompat.TYPE_TEXT, 2040, 2044, 1022, 511, 1022, 2044, 2040, PointerIconCompat.TYPE_TEXT, 480, 0, 0, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB};
    }

    public static int[] getanim_1_10() {
        return new int[]{HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_GRAB, 2044, 2046, 3846, 3587, 3073, 2112, 2190, 2179, 2179, 1090, 514, 260, Opcodes.L2I, 112, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 112, Opcodes.L2I, 260, 514, 1089, 2179, 2179, 2189, 2113, 3073, 3587, 3846, 2046, 2044, PointerIconCompat.TYPE_GRAB, HttpStatus.SC_GATEWAY_TIMEOUT, HttpStatus.SC_GATEWAY_TIMEOUT};
    }

    public static int[] getanim_2_1() {
        return new int[]{0, 0, 0, 0, 128, 448, 128, 0, 0, 0, 32, 1057, 546, 260, 0, 0, 3587, 0, 0, 260, 546, 1057, 32, 0, 0, 0, 0, 0, 0, 512, 0, 2, 7, 2, 0, 0, 64, 336, 0, 792, 0, 336, 64, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_2_2() {
        return new int[]{0, 0, 0, 128, 128, 864, 128, 128, 0, 0, 32, 1057, 514, 0, 0, 0, 3073, 0, 0, 0, 514, 1057, 32, 0, 0, 0, 0, 0, 512, 1280, 514, 2, 13, 2, 2, 64, 584, 272, 0, 1548, 0, 272, 584, 64, 0, 0, 0, 0};
    }

    public static int[] getanim_2_3() {
        return new int[]{0, 128, 1168, 672, 0, 3640, 0, Opcodes.IF_ICMPNE, 658, 1159, 34, InputDeviceCompat.SOURCE_GAMEPAD, 0, 0, 0, 0, 2048, 0, 0, 0, 0, InputDeviceCompat.SOURCE_GAMEPAD, 32, 0, 32, 112, 32, 512, 0, 2178, 10, 512, 24, 64, 2122, 1094, 520, 0, 0, 3079, 0, 0, 520, 1092, 2114, 64, 0, 0};
    }

    public static int[] getanim_2_4() {
        return new int[]{128, 2184, 1168, 0, 0, 3100, 2, 2, 144, 1176, 2176, 2, 2, 64, 224, 64, 0, 0, 0, 0, 0, 0, 32, Opcodes.JSR, 0, 396, 0, Opcodes.JSR, 32, 0, 0, 0, 0, 64, 2114, 1028, 0, 0, 0, BleDevice.MSG_DISCOVER_SERVICES, 0, 0, 0, 4, 2114, 64, 0, 0};
    }

    public static int[] getanim_2_5() {
        return new int[]{Opcodes.IINC, 2056, 0, 0, 0, 6, 8, 0, 0, 40, 2180, 128, 72, 66, 432, 64, 64, 0, 0, 0, 0, 32, 292, Opcodes.L2I, 0, 774, 0, Opcodes.L2I, 292, 32, 512, 1792, 512, 64, BleDevice.MSG_DISCONNECT, 0, 0, 0, 0, 1, 0, 0, 0, 0, BleDevice.MSG_DISCONNECT, 320, 896, 256};
    }

    public static int[] getanim_2_6() {
        return new int[]{4, 0, 0, 0, 1024, BtDevice.STATE_CONNECTING, 1024, 0, 0, 0, 0, Opcodes.CHECKCAST, 336, 64, 952, 64, 336, 64, 0, 0, 32, 546, 260, 0, 0, 1539, 0, 0, 260, 546, 544, 3456, 512, 512, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 256, 256, 1728, 256};
    }

    public static int[] getanim_2_7() {
        return new int[]{0, 0, 0, 1024, 1024, 2816, 1024, 1024, 0, 0, 64, 584, 336, 0, 1820, 0, 336, 584, 64, 0, 32, 514, 0, 0, 0, InputDeviceCompat.SOURCE_GAMEPAD, 0, 512, 2624, 642, 32, 2272, 0, 640, 2624, 512, 0, 32, 112, 32, 0, 0, 0, 256, 1344, 0, 3168, 0};
    }

    public static int[] getanim_2_8() {
        return new int[]{0, 0, 1024, 1280, 0, 384, 0, 1280, 1024, 64, 1092, 584, 0, 0, 3598, 0, 0, 584, 1092, 96, InputDeviceCompat.SOURCE_GAMEPAD, 0, 0, 0, 0, 2048, 512, 544, 576, 0, InputDeviceCompat.SOURCE_KEYBOARD, 112, 0, 0, 576, 544, 544, 32, 216, 32, 32, 0, 256, 2336, 1088, 0, 2096, 0};
    }

    public static int[] getanim_2_9() {
        return new int[]{0, 1024, 1152, 256, 0, Opcodes.CHECKCAST, 0, 256, 1152, 1088, 1092, 0, 0, 0, BtManager.MSG_STOP_SCAN, 0, 0, 0, 1092, 64, 0, 0, 0, 0, 0, 0, 512, 544, 64, 0, 0, 48, 0, 0, 64, 544, 680, 0, 396, 0, Opcodes.JSR, 288, 272, 2080, 0, 0, 24, 0};
    }

    public static int[] getanim_2_10() {
        return new int[]{1056, 1088, 0, 0, 0, 112, 0, 0, 0, 1088, 1060, 1024, 0, 0, BleDevice.MSG_DISCONNECT, 0, 0, 0, 1028, 64, 0, 0, 0, 0, 0, 512, 528, 32, 0, 0, 0, 24, 0, 0, 32, 292, 664, 512, 774, 0, 392, 292, 48, 0, 0, 0, 12, 0};
    }

    public static int[] getanim_3_1() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 64, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 64, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_2() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 448, 224, 112, 224, 448, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 448, 224, 112, 224, 448, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_3() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_4() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 480, PointerIconCompat.TYPE_TEXT, 2040, 2044, 1022, 511, 1022, 2044, 2040, PointerIconCompat.TYPE_TEXT, 480, 0, 0, 0, 0, 0, 0, 480, PointerIconCompat.TYPE_TEXT, 2040, 2044, 1022, 511, 1022, 2044, 2040, PointerIconCompat.TYPE_TEXT, 480, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_5() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_6() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 448, 224, 112, 224, 448, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 448, 224, 112, 224, 448, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_7() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_8() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 480, PointerIconCompat.TYPE_TEXT, 2040, 2044, 1022, 511, 1022, 2044, 2040, PointerIconCompat.TYPE_TEXT, 480, 0, 0, 0, 0, 0, 0, 480, PointerIconCompat.TYPE_TEXT, 2040, 2044, 1022, 511, 1022, 2044, 2040, PointerIconCompat.TYPE_TEXT, 480, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_9() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, PointerIconCompat.TYPE_TEXT, HttpStatus.SC_GATEWAY_TIMEOUT, 252, HttpStatus.SC_GATEWAY_TIMEOUT, PointerIconCompat.TYPE_TEXT, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_3_10() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 448, 224, 112, 224, 448, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 128, 448, 224, 112, 224, 448, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_4_1() {
        return new int[]{12, 18, 18, 18, 34, 34, 28, 65, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_4_2() {
        return new int[]{12, 18, 18, 18, 34, 34, 28, 65, 20, 0, 0, 0, 768, 1152, 1152, 1088, 1088, 1088, 896, 2080, 640, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_4_3() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 768, 1152, 1152, 1088, 1088, 1088, 896, 2080, 640, 0, 0, 12, 18, 18, 18, 34, 34, 28, 65, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_4_4() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 18, 18, 18, 34, 34, 28, 65, 20, 0, 0, 768, 1152, 1152, 1088, 1088, 1088, 896, 2080, 640, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_4_5() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 768, 1152, 1152, 1088, 1088, 1088, 896, 2080, 640, 0, 0, 12, 18, 18};
    }

    public static int[] getanim_4_6() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 18, 18};
    }

    public static int[] getanim_4_7() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_5_1() {
        return new int[]{0, 0, 0, 0, 0, 0, 4094, 1092, 584, 496, 64, 64, 64, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_2() {
        return new int[]{0, 0, 0, 0, 0, 2044, 2114, 1092, 584, 496, 64, 64, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_3() {
        return new int[]{0, 0, 0, 0, PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 1092, 2114, 1092, 584, 496, 64, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_4() {
        return new int[]{0, 0, 0, 224, 856, 1092, 2114, 1092, 584, 496, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_5() {
        return new int[]{0, 0, 224, 336, 584, 1092, 2114, 1092, 584, 496, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_6() {
        return new int[]{0, 0, 0, 0, 0, 2044, 2274, 1092, 584, 496, 64, 64, 64, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_7() {
        return new int[]{0, 0, 0, 0, 0, 2044, 2114, 1252, 584, 496, 64, 64, 64, 64, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_8() {
        return new int[]{0, 0, 0, 0, 0, 2044, 2210, 1092, 744, 496, 64, 64, 64, 64, 64, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_9() {
        return new int[]{0, 0, 0, 0, 0, 2044, BleDevice.MSG_DISCONNECT, 1028, 744, 496, 224, 64, 64, 64, 64, 64, 64, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_5_10() {
        return new int[]{0, 0, 0, 0, 0, 2044, BleDevice.MSG_DISCONNECT, 1028, 584, 432, Opcodes.IF_ICMPNE, 64, 224, 64, 64, 64, 64, 64, 64, 64, 336, 224, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 544, 528, 264, Opcodes.IINC, 264, 528, 544, 448, 0, 0};
    }

    public static int[] getanim_6_1() {
        return new int[]{3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315};
    }

    public static int[] getanim_6_2() {
        return new int[]{2457, 780, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 3219, 3219, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 780, 2457, 2457, 780, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 3219, 3219, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 780, 2457};
    }

    public static int[] getanim_6_3() {
        return new int[]{780, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 3219, 2313, 2313, 3219, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 780, 780, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 3219, 2313, 2313, 3219, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 780};
    }

    public static int[] getanim_6_4() {
        return new int[]{1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 1638, 3315, 2457, 780, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638, 780, 2457, 3315, 1638};
    }

    public static int[] getanim_7_1() {
        return new int[]{0, 0, 0, 0, 0, 0, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_2() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_3() {
        return new int[]{0, 0, 0, 0, 0, 0, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_4() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_5() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_6() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_7() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_8() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_9() {
        return new int[]{0, 0, 0, 0, 0, 0, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_7_10() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, PointerIconCompat.TYPE_GRAB, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_8_1() {
        return new int[]{585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170};
    }

    public static int[] getanim_8_2() {
        return new int[]{2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585};
    }

    public static int[] getanim_8_3() {
        return new int[]{1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 1170, 585, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340, 585, 1170, 2340};
    }

    public static int[] getanim_9_1() {
        return new int[]{240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145};
    }

    public static int[] getanim_9_2() {
        return new int[]{HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240};
    }

    public static int[] getanim_9_3() {
        return new int[]{780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT};
    }

    public static int[] getanim_9_4() {
        return new int[]{1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780};
    }

    public static int[] getanim_9_5() {
        return new int[]{BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542};
    }

    public static int[] getanim_9_6() {
        return new int[]{2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA, 2145, 240, HttpStatus.SC_REQUEST_TIMEOUT, 780, 1542, BtManager.MSG_READ_DATA};
    }

    public static int[] getanim_10_1() {
        return new int[]{0, 0, 0, 0, 0, 448, 992, 2032, 2040, 2044, 1022, 511, 1022, 2044, 2040, 2032, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, 2032, 2040, 2044, 1022, 511, 1022, 2044, 2040, 2032, 992, 448, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_2() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_3() {
        return new int[]{0, 0, 0, 0, 0, 448, 992, 2032, 2040, 2044, 1022, 511, 1022, 2044, 2040, 2032, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, 2032, 2040, 2044, 1022, 511, 1022, 2044, 2040, 2032, 992, 448, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_4() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_5() {
        return new int[]{0, 0, 0, 0, 0, 448, 992, 2032, 2040, 2044, 1022, 511, 1022, 2044, 2040, 2032, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, 2032, 2040, 2044, 1022, 511, 1022, 2044, 2040, 2032, 992, 448, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_6() {
        return new int[]{0, 0, 0, 0, 0, 448, 992, 2032, 2040, 2042, PointerIconCompat.TYPE_ZOOM_OUT, ErrorStatus.UNSUPPORTED_ENCODING, 1022, 2044, 2028, 2024, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, 2032, 2040, 2042, PointerIconCompat.TYPE_ZOOM_OUT, ErrorStatus.UNSUPPORTED_ENCODING, 1022, 2044, 2028, 2024, 992, 448, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_7() {
        return new int[]{0, 0, 0, 0, 0, 448, 992, 2024, 2028, 2029, PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 494, PointerIconCompat.TYPE_GRABBING, 2044, 2022, 1972, 992, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 448, 992, 2024, 2028, 2029, PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 494, PointerIconCompat.TYPE_GRABBING, 2044, 2022, 1972, 992, 448, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_8() {
        return new int[]{0, 0, 0, 0, 0, 352, 864, 1908, 2022, 1910, 956, 487, PointerIconCompat.TYPE_CELL, 2030, 1907, 1882, 880, 448, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 352, 864, 1908, 2022, 1910, 956, 487, PointerIconCompat.TYPE_CELL, 2030, 1907, 1882, 880, 448, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_9() {
        return new int[]{0, 0, 0, 0, 0, Opcodes.ARETURN, 816, 1722, 1523, 1467, 862, 371, 887, 1527, 1465, 1837, 440, 224, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Opcodes.ARETURN, 816, 1722, 1523, 1467, 862, 371, 887, 1527, 1465, 1837, 440, 224, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_10_10() {
        return new int[]{0, 0, 0, 0, 0, 88, HttpStatus.SC_REQUEST_TIMEOUT, 861, 1273, 733, 431, Opcodes.INVOKEINTERFACE, 443, 763, 732, 1686, 220, 112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 88, HttpStatus.SC_REQUEST_TIMEOUT, 861, 1273, 733, 431, Opcodes.INVOKEINTERFACE, 443, 763, 732, 1686, 220, 112, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_11_1() {
        return new int[]{1548, 520, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_11_2() {
        return new int[]{4030, 4030, 1980, 952, 64, 224, 496, 224, 64, 0, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_11_3() {
        return new int[]{4094, 4094, 4030, 3870, 1612, 744, 496, 224, 64, 0, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_11_4() {
        return new int[]{2044, 4094, 4094, 4094, 4030, 4030, 1980, 952, 2, 772, 272, 0, 64, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_11_5() {
        return new int[]{0, PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 2044, 4094, 4094, 4094, 4030, 3870, 1548, 520, 0, 48, 2, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_11_6() {
        return new int[]{0, 0, 0, PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 2044, 4094, 4094, 4094, 4030, 4030, 1980, 952, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_11_7() {
        return new int[]{0, 0, 0, 0, 0, PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 2044, 4094, 4094, 4094, 4030, 3870, 1548, 520, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_11_8() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 2044, 4094, 4094, 4094, 4030, 4030, 1980, 952, 4, 768, HttpStatus.SC_NOT_MODIFIED, 32, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_11_9() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 2044, 4094, 4094, 4094, 4030, 3870, 1548, 520, 0, 48, 2, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0, 0, 0, 64, 224, 496, 224, 64, 0, 0};
    }

    public static int[] getanim_12_1() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 56, 124};
    }

    public static int[] getanim_12_2() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3072, 3584, 3584, 3584, 3072, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_12_3() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 224, 496, 496, 496, 224, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_12_4() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 7, 7, 7, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_12_5() {
        return new int[]{0, 0, 0, 112, 248, 248, 248, 112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_12_6() {
        return new int[]{3584, 3072, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_12_7() {
        return new int[]{0, 0, 14, 31, 31, 31, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_12_8() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 224, 496, 496, 496, 224, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_12_9() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3072, 3584, 3584, 3584, 3072, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static int[] getanim_12_10() {
        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 248, 248, 248, 112, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
