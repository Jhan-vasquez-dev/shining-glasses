package com.icwork.shiningglass.model.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.alibaba.fastjson2.JSONB;
import com.icwork.shiningglass.ui.utils.FontUtil;
import com.icwork.shiningglass.ui.utils.LogUtil;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.LinkedList;
import kotlin.jvm.internal.ByteCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class Text1456 {
    private static final String TAG = "Text1456";
    private static int textColor = Color.parseColor("#FFFFFF");

    public static byte[] getStringBytes(String str) throws UnsupportedEncodingException {
        LinkedList linkedList = new LinkedList();
        int length = 0;
        for (char c : str.toCharArray()) {
            byte[] charDataByFont = getCharDataByFont(c);
            if (charDataByFont == null) {
                charDataByFont = getCharDataByBitmap(c);
            }
            linkedList.add(charDataByFont);
            length += charDataByFont.length;
        }
        byte[] bArr = new byte[length];
        int length2 = 0;
        for (int i = 0; i < linkedList.size(); i++) {
            byte[] bArr2 = (byte[]) linkedList.get(i);
            System.arraycopy(bArr2, 0, bArr, length2, bArr2.length);
            length2 += bArr2.length;
        }
        LogUtil.d("dataBs 长度:" + length);
        return bArr;
    }

    private static byte[] getCharDataByBitmap(char c) {
        return getCharData(getCharBitmapPointCheckData(getCharBitmap(c)));
    }

    private static Bitmap getCharBitmap(char c) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(12, 12, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint();
        paint.setTextSize(12);
        paint.setTypeface(FontUtil.getTypeface1248());
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(textColor);
        canvas.drawText(String.valueOf(c), 5 + 1.0f, ((float) ((((double) 12) * 5.3d) / 6.0d)) - 1.0f, paint);
        return bitmapCreateBitmap;
    }

    private static byte[][] getCharBitmapPointCheckData(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[][] bArr = (byte[][]) Array.newInstance((Class<?>) Byte.TYPE, width, height);
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                if (bitmap.getPixel(i, i2) < 0) {
                    bArr[i][i2] = 1;
                }
            }
        }
        return bArr;
    }

    public static boolean isWhitish(int i) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        return ((double) fArr[2]) > 0.7d;
    }

    private static byte[] getCharData(byte[][] bArr) {
        int i;
        int i2;
        byte[] bArr2 = new byte[24];
        for (int i3 = 0; i3 < bArr.length; i3++) {
            byte[] bArr3 = bArr[i3];
            byte b = 0;
            byte b2 = 0;
            for (int i4 = 0; i4 < bArr3.length; i4++) {
                if (bArr3[i4] == 1) {
                    switch (i4) {
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
            }
            int i5 = i3 * 2;
            bArr2[i5] = b;
            bArr2[i5 + 1] = b2;
        }
        return bArr2;
    }

    private static byte[] getCharDataByFont(char c) {
        if (c == 'A') {
            return get_A();
        }
        if (c == 'B') {
            return get_B();
        }
        if (c == 'C') {
            return get_C();
        }
        if (c == 'D') {
            return get_D();
        }
        if (c == 'E') {
            return get_E();
        }
        if (c == 'F') {
            return get_F();
        }
        if (c == 'G') {
            return get_G();
        }
        if (c == 'H') {
            return get_H();
        }
        if (c == 'I') {
            return get_I();
        }
        if (c == 'J') {
            return get_J();
        }
        if (c == 'K') {
            return get_K();
        }
        if (c == 'L') {
            return get_L();
        }
        if (c == 'M') {
            return get_M();
        }
        if (c == 'N') {
            return get_N();
        }
        if (c == 'O') {
            return get_O();
        }
        if (c == 'P') {
            return get_P();
        }
        if (c == 'Q') {
            return get_Q();
        }
        if (c == 'R') {
            return get_R();
        }
        if (c == 'S') {
            return get_S();
        }
        if (c == 'T') {
            return get_T();
        }
        if (c == 'U') {
            return get_U();
        }
        if (c == 'V') {
            return get_V();
        }
        if (c == 'W') {
            return get_W();
        }
        if (c == 'X') {
            return get_X();
        }
        if (c == 'Y') {
            return get_Y();
        }
        if (c == 'Z') {
            return get_Z();
        }
        if (c == '0') {
            return get_0();
        }
        if (c == '1') {
            return get_1();
        }
        if (c == '2') {
            return get_2();
        }
        if (c == '3') {
            return get_3();
        }
        if (c == '4') {
            return get_4();
        }
        if (c == '5') {
            return get_5();
        }
        if (c == '6') {
            return get_6();
        }
        if (c == '7') {
            return get_7();
        }
        if (c == '8') {
            return get_8();
        }
        if (c == '9') {
            return get_9();
        }
        if (c == 'a') {
            return get_a();
        }
        if (c == 'b') {
            return get_b();
        }
        if (c == 'c') {
            return get_c();
        }
        if (c == 'd') {
            return get_d();
        }
        if (c == 'e') {
            return get_e();
        }
        if (c == 'f') {
            return get_f();
        }
        if (c == 'g') {
            return get_g();
        }
        if (c == 'h') {
            return get_h();
        }
        if (c == 'i') {
            return get_i();
        }
        if (c == 'j') {
            return get_j();
        }
        if (c == 'k') {
            return get_k();
        }
        if (c == 'l') {
            return get_l();
        }
        if (c == 'm') {
            return get_m();
        }
        if (c == 'n') {
            return get_n();
        }
        if (c == 'o') {
            return get_o();
        }
        if (c == 'p') {
            return get_p();
        }
        if (c == 'q') {
            return get_q();
        }
        if (c == 'r') {
            return get_r();
        }
        if (c == 's') {
            return get_s();
        }
        if (c == 't') {
            return get_t();
        }
        if (c == 'u') {
            return get_u();
        }
        if (c == 'v') {
            return get_v();
        }
        if (c == 'w') {
            return get_w();
        }
        if (c == 'x') {
            return get_x();
        }
        if (c == 'y') {
            return get_y();
        }
        if (c == 'z') {
            return get_z();
        }
        if (c == '<') {
            return get_left();
        }
        if (c == '>') {
            return get_right();
        }
        if (c == ',') {
            return get_comma();
        }
        if (c == '.') {
            return get_period();
        }
        if (c == ';') {
            return get_fenhao();
        }
        if (c == ':') {
            return get_maohao();
        }
        if (c == '\'') {
            return get_suoxiehao();
        }
        if (c == '\"') {
            return get_shuangsuoxiehao();
        }
        if (c == '[') {
            return get_zuozhongkuohao();
        }
        if (c == ']') {
            return get_youzhongkuohao();
        }
        if (c == '{') {
            return get_zuodakuohao();
        }
        if (c == '}') {
            return get_youdakuohao();
        }
        if (c == '|') {
            return get_shuxian();
        }
        if (c == '\\') {
            return get_fanxiegang();
        }
        if (c == '/') {
            return get_xiegang();
        }
        if (c == '?') {
            return get_question();
        }
        if (c == '~') {
            return get_pozhehao();
        }
        if (c == '`') {
            return get_piedian();
        }
        if (c == '!') {
            return get_exclamation();
        }
        if (c == '@') {
            return get_xiaolaoshu();
        }
        if (c == '#') {
            return get_jinghao();
        }
        if (c == '$') {
            return get_meiyuanfuhao();
        }
        if (c == '%') {
            return get_baifenbi();
        }
        if (c == '^') {
            return get_yunsuanfuhao();
        }
        if (c == '&') {
            return get_yufuhao();
        }
        if (c == '*') {
            return get_xinghao();
        }
        if (c == '(') {
            return get_zuokuohao();
        }
        if (c == ')') {
            return get_youkuohao();
        }
        if (c == '-') {
            return get_dash();
        }
        if (c == '_') {
            return get_xiahuaxian();
        }
        if (c == '+') {
            return get_add();
        }
        if (c == '=') {
            return get_equal();
        }
        if (c == ' ') {
            return get_space();
        }
        return null;
    }

    private static byte[] get_A() {
        return new byte[]{3, JSONB.Constants.BC_INT64_SHORT_MIN, 29, 0, 97, 0, 29, 0, 3, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_B() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 59, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_C() {
        return new byte[]{63, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 49, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_D() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 32, ByteCompanionObject.MIN_VALUE, 31, 0, 0, 0};
    }

    private static byte[] get_E() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_F() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, 0, JSONB.Constants.BC_INT32_SHORT_ZERO, 0, JSONB.Constants.BC_INT32_SHORT_ZERO, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0, 0};
    }

    private static byte[] get_G() {
        return new byte[]{63, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 66, ByteCompanionObject.MIN_VALUE, 51, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_H() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, 4, 0, 4, 0, 4, 0, 127, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_I() {
        return new byte[]{0, 0, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0, 0, 0};
    }

    private static byte[] get_J() {
        return new byte[]{1, ByteCompanionObject.MIN_VALUE, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 127, ByteCompanionObject.MIN_VALUE, 0, 0, 0, 0};
    }

    private static byte[] get_K() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, 4, 0, 27, 0, 96, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0};
    }

    private static byte[] get_L() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_M() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, 28, 0, 3, JSONB.Constants.BC_INT64_SHORT_MIN, 28, 0, 127, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_N() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_BYTE_MIN, 0, 14, 0, 1, ByteCompanionObject.MIN_VALUE, 127, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_O() {
        return new byte[]{63, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 63, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_P() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, 0, JSONB.Constants.BC_INT32_SHORT_ZERO, 0, JSONB.Constants.BC_INT32_SHORT_ZERO, 0, JSONB.Constants.BC_INT32_BYTE_ZERO, 0, 0, 0};
    }

    private static byte[] get_Q() {
        return new byte[]{63, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 65, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, ByteCompanionObject.MIN_VALUE, 63, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_R() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, 0, JSONB.Constants.BC_INT32_SHORT_ZERO, 0, 70, 0, 57, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_S() {
        return new byte[]{49, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 66, JSONB.Constants.BC_INT32_SHORT_MIN, 49, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_T() {
        return new byte[]{JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 127, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0, 0};
    }

    private static byte[] get_U() {
        return new byte[]{127, ByteCompanionObject.MIN_VALUE, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 127, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_V() {
        return new byte[]{112, 0, 14, 0, 1, JSONB.Constants.BC_INT64_SHORT_MIN, 14, 0, 112, 0, 0, 0};
    }

    private static byte[] get_W() {
        return new byte[]{JSONB.Constants.BC_STR_UTF16LE, 0, 3, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_STR_UTF16LE, 0, 3, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_STR_UTF16LE, 0, 0, 0};
    }

    private static byte[] get_X() {
        return new byte[]{96, JSONB.Constants.BC_INT64_SHORT_MIN, 27, 0, 4, 0, 27, 0, 96, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_Y() {
        return new byte[]{96, 0, 24, 0, 7, JSONB.Constants.BC_INT64_SHORT_MIN, 24, 0, 96, 0, 0, 0};
    }

    private static byte[] get_Z() {
        return new byte[]{JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT64_SHORT_MIN, 67, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 88, JSONB.Constants.BC_INT32_SHORT_MIN, 96, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_a() {
        return new byte[]{5, ByteCompanionObject.MIN_VALUE, 10, JSONB.Constants.BC_INT32_SHORT_MIN, 10, JSONB.Constants.BC_INT32_SHORT_MIN, 7, ByteCompanionObject.MIN_VALUE, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_b() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 7, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_c() {
        return new byte[]{7, ByteCompanionObject.MIN_VALUE, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 4, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_d() {
        return new byte[]{7, ByteCompanionObject.MIN_VALUE, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 127, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_e() {
        return new byte[]{7, ByteCompanionObject.MIN_VALUE, 10, JSONB.Constants.BC_INT32_SHORT_MIN, 10, JSONB.Constants.BC_INT32_SHORT_MIN, 10, JSONB.Constants.BC_INT32_SHORT_MIN, 6, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_f() {
        return new byte[]{8, 0, 63, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0, 0, 0, 0};
    }

    private static byte[] get_g() {
        return new byte[]{5, JSONB.Constants.BC_INT32_SHORT_MIN, 10, -96, 10, -96, 4, -96, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_h() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, 8, 0, 8, 0, 8, 0, 7, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_i() {
        return new byte[]{0, 0, 0, 0, 111, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_j() {
        return new byte[]{0, 32, 0, 32, 111, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_k() {
        return new byte[]{127, JSONB.Constants.BC_INT64_SHORT_MIN, 1, 0, 3, 0, 4, ByteCompanionObject.MIN_VALUE, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_l() {
        return new byte[]{0, 0, 0, 0, 127, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_m() {
        return new byte[]{15, JSONB.Constants.BC_INT64_SHORT_MIN, 8, 0, 7, JSONB.Constants.BC_INT64_SHORT_MIN, 8, 0, 7, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_n() {
        return new byte[]{15, JSONB.Constants.BC_INT64_SHORT_MIN, 8, 0, 8, 0, 8, 0, 7, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_o() {
        return new byte[]{7, ByteCompanionObject.MIN_VALUE, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 7, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_p() {
        return new byte[]{15, -32, 8, ByteCompanionObject.MIN_VALUE, 8, ByteCompanionObject.MIN_VALUE, 8, ByteCompanionObject.MIN_VALUE, 7, 0, 0, 0};
    }

    private static byte[] get_q() {
        return new byte[]{7, 0, 8, ByteCompanionObject.MIN_VALUE, 8, ByteCompanionObject.MIN_VALUE, 8, ByteCompanionObject.MIN_VALUE, 15, -32, 0, 0};
    }

    private static byte[] get_r() {
        return new byte[]{0, 0, 15, JSONB.Constants.BC_INT64_SHORT_MIN, 4, 0, 8, 0, 8, 0, 0, 0};
    }

    private static byte[] get_s() {
        return new byte[]{4, ByteCompanionObject.MIN_VALUE, 10, JSONB.Constants.BC_INT32_SHORT_MIN, 10, JSONB.Constants.BC_INT32_SHORT_MIN, 9, JSONB.Constants.BC_INT32_SHORT_MIN, 4, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_t() {
        return new byte[]{8, 0, 127, ByteCompanionObject.MIN_VALUE, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0, 0, 0};
    }

    private static byte[] get_u() {
        return new byte[]{15, ByteCompanionObject.MIN_VALUE, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 15, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0};
    }

    private static byte[] get_v() {
        return new byte[]{12, 0, 3, 0, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 3, 0, 12, 0, 0, 0};
    }

    private static byte[] get_w() {
        return new byte[]{14, 0, 1, JSONB.Constants.BC_INT64_SHORT_MIN, 14, 0, 1, JSONB.Constants.BC_INT64_SHORT_MIN, 14, 0, 0, 0};
    }

    private static byte[] get_x() {
        return new byte[]{8, JSONB.Constants.BC_INT32_SHORT_MIN, 4, ByteCompanionObject.MIN_VALUE, 3, 0, 4, ByteCompanionObject.MIN_VALUE, 8, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_y() {
        return new byte[]{12, 32, 3, 32, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 3, 0, 12, 0, 0, 0};
    }

    private static byte[] get_z() {
        return new byte[]{8, JSONB.Constants.BC_INT32_SHORT_MIN, 8, JSONB.Constants.BC_INT64_SHORT_MIN, 9, JSONB.Constants.BC_INT32_SHORT_MIN, 10, JSONB.Constants.BC_INT32_SHORT_MIN, 12, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_0() {
        return new byte[]{63, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 63, ByteCompanionObject.MIN_VALUE, 0, 0, 0, 0};
    }

    private static byte[] get_1() {
        return new byte[]{0, 0, 32, 0, 127, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_2() {
        return new byte[]{JSONB.Constants.BC_INT32_BYTE_MIN, JSONB.Constants.BC_INT64_SHORT_MIN, 67, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_BYTE_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0, 0, 0};
    }

    private static byte[] get_3() {
        return new byte[]{49, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 59, ByteCompanionObject.MIN_VALUE, 0, 0, 0, 0};
    }

    private static byte[] get_4() {
        return new byte[]{3, 0, 13, 0, 49, 0, 127, JSONB.Constants.BC_INT64_SHORT_MIN, 1, 0, 0, 0};
    }

    private static byte[] get_5() {
        return new byte[]{JSONB.Constants.BC_STR_UTF16BE, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MAX, ByteCompanionObject.MIN_VALUE, 0, 0, 0, 0};
    }

    private static byte[] get_6() {
        return new byte[]{63, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 51, ByteCompanionObject.MIN_VALUE, 0, 0, 0, 0};
    }

    private static byte[] get_7() {
        return new byte[]{JSONB.Constants.BC_INT32_SHORT_MIN, 0, 65, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_STR_ASCII_FIX_5, 0, 112, 0, 0, 0, 0, 0};
    }

    private static byte[] get_8() {
        return new byte[]{59, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 59, ByteCompanionObject.MIN_VALUE, 0, 0, 0, 0};
    }

    private static byte[] get_9() {
        return new byte[]{57, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 63, ByteCompanionObject.MIN_VALUE, 0, 0, 0, 0};
    }

    private static byte[] get_pozhehao() {
        return new byte[]{JSONB.Constants.BC_INT32_SHORT_MIN, 0, ByteCompanionObject.MIN_VALUE, 0, ByteCompanionObject.MIN_VALUE, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, ByteCompanionObject.MIN_VALUE, 0, 0, 0};
    }

    private static byte[] get_piedian() {
        return new byte[]{0, 0, ByteCompanionObject.MIN_VALUE, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_exclamation() {
        return new byte[]{0, 0, 0, 0, -4, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_xiaolaoshu() {
        return new byte[]{63, ByteCompanionObject.MIN_VALUE, 82, JSONB.Constants.BC_INT32_SHORT_MIN, 94, JSONB.Constants.BC_INT32_SHORT_MIN, 65, JSONB.Constants.BC_INT32_SHORT_MIN, 62, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_jinghao() {
        return new byte[]{32, ByteCompanionObject.MIN_VALUE, -1, -32, 32, ByteCompanionObject.MIN_VALUE, -1, -32, 32, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_meiyuanfuhao() {
        return new byte[]{49, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32, JSONB.Constants.BC_INT32_SHORT_MIN, -1, -32, JSONB.Constants.BC_INT32_SHORT_ZERO, JSONB.Constants.BC_INT32_SHORT_MIN, 51, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_baifenbi() {
        return new byte[]{96, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_REFERENCE, 0, JSONB.Constants.BC_STR_ASCII_FIX_36, ByteCompanionObject.MIN_VALUE, 50, JSONB.Constants.BC_INT32_SHORT_MIN, -63, ByteCompanionObject.MIN_VALUE, 0, 0};
    }

    private static byte[] get_yunsuanfuhao() {
        return new byte[]{0, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, ByteCompanionObject.MIN_VALUE, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0, 0, 0, 0};
    }

    private static byte[] get_yufuhao() {
        return new byte[]{103, ByteCompanionObject.MIN_VALUE, -104, JSONB.Constants.BC_INT32_SHORT_MIN, 102, JSONB.Constants.BC_INT32_SHORT_MIN, 1, ByteCompanionObject.MIN_VALUE, 6, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_xinghao() {
        return new byte[]{27, 0, 4, 0, 63, ByteCompanionObject.MIN_VALUE, 4, 0, 27, 0, 0, 0};
    }

    private static byte[] get_zuokuohao() {
        return new byte[]{0, 0, 31, 0, 96, JSONB.Constants.BC_INT64_SHORT_MIN, ByteCompanionObject.MIN_VALUE, 32, 0, 0, 0, 0};
    }

    private static byte[] get_youkuohao() {
        return new byte[]{0, 0, ByteCompanionObject.MIN_VALUE, 32, 96, JSONB.Constants.BC_INT64_SHORT_MIN, 31, 0, 0, 0, 0, 0};
    }

    private static byte[] get_xiahuaxian() {
        return new byte[]{0, JSONB.Constants.BC_INT32_NUM_16, 0, JSONB.Constants.BC_INT32_NUM_16, 0, JSONB.Constants.BC_INT32_NUM_16, 0, JSONB.Constants.BC_INT32_NUM_16, 0, JSONB.Constants.BC_INT32_NUM_16, 0, JSONB.Constants.BC_INT32_NUM_16};
    }

    private static byte[] get_add() {
        return new byte[]{4, 0, 4, 0, 63, ByteCompanionObject.MIN_VALUE, 4, 0, 4, 0, 0, 0};
    }

    private static byte[] get_dash() {
        return new byte[]{4, 0, 4, 0, 4, 0, 4, 0, 0, 0, 0, 0};
    }

    private static byte[] get_equal() {
        return new byte[]{18, 0, 18, 0, 18, 0, 18, 0, 0, 0, 0, 0};
    }

    private static byte[] get_zuodakuohao() {
        return new byte[]{0, 0, 4, 0, -5, -32, ByteCompanionObject.MIN_VALUE, 32, 0, 0, 0, 0};
    }

    private static byte[] get_youdakuohao() {
        return new byte[]{0, 0, ByteCompanionObject.MIN_VALUE, 32, -5, -32, 4, 0, 0, 0, 0, 0};
    }

    private static byte[] get_zuozhongkuohao() {
        return new byte[]{0, 0, 0, 0, -1, -32, ByteCompanionObject.MIN_VALUE, 32, ByteCompanionObject.MIN_VALUE, 32, 0, 0};
    }

    private static byte[] get_youzhongkuohao() {
        return new byte[]{ByteCompanionObject.MIN_VALUE, 32, ByteCompanionObject.MIN_VALUE, 32, -1, -32, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_shuxian() {
        return new byte[]{0, 0, 0, 0, -1, JSONB.Constants.BC_INT32_NUM_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_fanxiegang() {
        return new byte[]{0, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 0, JSONB.Constants.BC_INT32_BYTE_MIN, 0, 14, 0, 1, ByteCompanionObject.MIN_VALUE, 0, 96};
    }

    private static byte[] get_maohao() {
        return new byte[]{0, 0, 24, JSONB.Constants.BC_INT64_SHORT_MIN, 24, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_fenhao() {
        return new byte[]{0, 0, 24, -96, 24, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_shuangsuoxiehao() {
        return new byte[]{32, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 32, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0};
    }

    private static byte[] get_suoxiehao() {
        return new byte[]{0, 0, -96, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_left() {
        return new byte[]{4, 0, 10, 0, 17, 0, 32, ByteCompanionObject.MIN_VALUE, JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 0};
    }

    private static byte[] get_right() {
        return new byte[]{JSONB.Constants.BC_INT32_SHORT_MIN, JSONB.Constants.BC_INT32_SHORT_MIN, 32, ByteCompanionObject.MIN_VALUE, 17, 0, 10, 0, 4, 0, 0, 0};
    }

    private static byte[] get_comma() {
        return new byte[]{0, 0, 0, -96, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_period() {
        return new byte[]{0, 0, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0, 0, 0, 0};
    }

    private static byte[] get_question() {
        return new byte[]{JSONB.Constants.BC_INT32_BYTE_MIN, 0, JSONB.Constants.BC_INT32_SHORT_MIN, 0, 70, JSONB.Constants.BC_INT64_SHORT_MIN, JSONB.Constants.BC_INT32_BYTE_ZERO, 0, 0, 0, 0, 0};
    }

    private static byte[] get_xiegang() {
        return new byte[]{0, 96, 1, ByteCompanionObject.MIN_VALUE, 14, 0, JSONB.Constants.BC_INT32_BYTE_MIN, 0, JSONB.Constants.BC_INT64_SHORT_MIN, 0, 0, 0};
    }

    private static byte[] get_space() {
        return new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
}
