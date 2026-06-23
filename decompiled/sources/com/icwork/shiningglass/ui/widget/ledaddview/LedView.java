package com.icwork.shiningglass.ui.widget.ledaddview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import com.alibaba.fastjson2.JSONB;
import com.icwork.shiningglass.ui.utils.DensityUtil;
import com.icwork.shiningglass.ui.utils.LogUtil;
import com.icwork.shiningglass.ui.widget.LedItemView1;
import java.util.List;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;

/* JADX INFO: loaded from: classes.dex */
public class LedView extends ViewGroup {
    public static final int MODE_ERASER = 2;
    public static final int MODE_NO = 0;
    public static final int MODE_PAINT = 1;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 0;
    private static final String TAG = "LedView";
    private int heightCount;
    int heightSize;
    private boolean isDispatchTouch;
    private boolean isOpen;
    private boolean isValidToggle;
    private int lastX;
    private int lastY;
    private LedListener ledListener;
    private int mLastX;
    private int mode;
    int moveMax;
    private int offset;
    private int orientation;
    private int pointAllLength;
    private int pointLength;
    private int pointMargin;
    private RealTimeDataListener realTimeDataListener;
    private int selectedColor;
    private int unSelectedColor;
    private int widthCount;
    int widthSize;
    private int xMore;

    public interface LedListener {
        void onItemSelect(int i, int i2, int i3, boolean z);
    }

    public interface RealTimeDataListener {
        void onRealTimeData(int i, byte[] bArr);
    }

    public LedView(Context context) {
        super(context);
        this.widthCount = 128;
        this.heightCount = 16;
        this.pointMargin = 0;
        this.unSelectedColor = 0;
        this.selectedColor = Color.rgb(18, 255, 1);
        this.isDispatchTouch = true;
    }

    public LedView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.widthCount = 128;
        this.heightCount = 16;
        this.pointMargin = 0;
        this.unSelectedColor = 0;
        this.selectedColor = Color.rgb(18, 255, 1);
        this.isDispatchTouch = true;
        ViewConfiguration.get(context);
    }

    public LedView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.widthCount = 128;
        this.heightCount = 16;
        this.pointMargin = 0;
        this.unSelectedColor = 0;
        this.selectedColor = Color.rgb(18, 255, 1);
        this.isDispatchTouch = true;
    }

    public void init(int i, int i2) {
        LogUtil.d("widthCount:" + i + " heightCount:" + i2);
        this.widthCount = i;
        this.heightCount = i2;
        for (int i3 = 0; i3 < i * i2; i3++) {
            LedItemView1 ledItemView1 = new LedItemView1(getContext());
            ledItemView1.setViewNumber(i3);
            ledItemView1.setColumnNumber(i3 / i2);
            ledItemView1.setRowNumber(i3 % i2);
            ledItemView1.setPaint(this.unSelectedColor);
            ledItemView1.postInvalidate();
            addView(ledItemView1);
        }
    }

    public void init(int i, int i2, float f) {
        this.widthCount = i;
        this.heightCount = i2;
        for (int i3 = 0; i3 < i * i2; i3++) {
            LedItemView1 ledItemView1 = new LedItemView1(getContext());
            ledItemView1.setViewNumber(i3);
            ledItemView1.setColumnNumber(i3 / i2);
            ledItemView1.setRowNumber(i3 % i2);
            ledItemView1.setPaint(this.unSelectedColor, f);
            ledItemView1.postInvalidate();
            addView(ledItemView1);
        }
    }

    public void removeAllChildView() {
        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                removeViewAt(0);
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        measureChildren(i, i2);
        this.widthSize = View.MeasureSpec.getSize(i);
        int iDp2px = (int) DensityUtil.dp2px(getContext(), 90.0f);
        int i3 = this.heightCount;
        int i4 = iDp2px % i3;
        this.xMore = i4;
        int i5 = iDp2px / i3;
        this.pointAllLength = i5;
        this.pointLength = i5 - (this.pointMargin * 2);
        this.offset = i4 / 2;
        this.moveMax = (i5 * this.widthCount) - this.widthSize;
        LogUtil.d("moveMax:" + this.moveMax + " offset:" + this.offset + " pointLength:" + this.pointLength + " pointAllLength:" + this.pointAllLength + "widthSize:" + this.widthSize);
        int i6 = this.pointAllLength * this.widthCount;
        LogUtil.d("当前的宽度：pointAllLength:" + this.pointAllLength + "   width:" + i6 + "  xMore:" + (this.xMore * this.widthCount));
        setMeasuredDimension(i6, iDp2px);
    }

    public int getPointAllLength() {
        return this.pointAllLength;
    }

    public int getPointLength() {
        return this.pointLength;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            int i6 = this.heightCount;
            int i7 = this.pointAllLength;
            int i8 = this.pointMargin;
            int i9 = ((i5 / i6) * i7) + i8 + this.offset;
            int i10 = ((i5 % i6) * i7) + i8;
            int i11 = this.pointLength;
            childAt.layout(i9, i10, i9 + i11, i11 + i10);
        }
    }

    public void clearSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            LedItemView1 ledItemView1 = (LedItemView1) getChildAt(i);
            ledItemView1.setChecked(false);
            ledItemView1.setPaint(this.unSelectedColor);
            ledItemView1.postInvalidate();
        }
    }

    public void setItemSelected(int i, boolean z) {
        if (i < 0 || i >= getChildCount()) {
            return;
        }
        LedItemView1 ledItemView1 = (LedItemView1) getChildAt(i);
        if (z) {
            ledItemView1.setChecked(true);
            ledItemView1.setPaint(this.selectedColor);
            ledItemView1.postInvalidate();
        } else {
            ledItemView1.setChecked(false);
            ledItemView1.setPaint(this.unSelectedColor);
            ledItemView1.postInvalidate();
        }
    }

    public byte[] getData(int i) {
        int childCount = getChildCount();
        LogUtil.d("childCount:" + childCount);
        byte[] bArr = new byte[childCount];
        int i2 = this.orientation;
        int i3 = 0;
        if (i2 == 0) {
            if (i == 0) {
                while (i3 < childCount) {
                    if (((LedItemView1) getChildAt(i3)).isChecked()) {
                        bArr[i3] = 1;
                    }
                    i3++;
                }
            } else if (i == 1) {
                LogUtil.d("横屏数据childCount:" + childCount);
                while (i3 < childCount) {
                    if (((LedItemView1) getChildAt(i3)).isChecked()) {
                        int i4 = this.heightCount;
                        int i5 = this.widthCount;
                        bArr[((i3 % i4) * i5) + ((i5 - (i3 / i4)) - 1)] = 1;
                    }
                    i3++;
                }
            }
        } else if (i2 == 1) {
            if (i == 0) {
                while (i3 < childCount) {
                    if (((LedItemView1) getChildAt(i3)).isChecked()) {
                        int i6 = this.heightCount;
                        bArr[(((i6 - (i3 % i6)) - 1) * this.widthCount) + (i3 / i6)] = 1;
                    }
                    i3++;
                }
            } else if (i == 1) {
                while (i3 < childCount) {
                    if (((LedItemView1) getChildAt(i3)).isChecked()) {
                        bArr[i3] = 1;
                    }
                    i3++;
                }
            }
        }
        return bArr;
    }

    public void setData(byte[] bArr) {
        int childCount;
        if (bArr != null && bArr.length == (childCount = getChildCount())) {
            for (int i = 0; i < childCount; i++) {
                LedItemView1 ledItemView1 = (LedItemView1) getChildAt(i);
                if (bArr[i] == 1) {
                    ledItemView1.setChecked(true);
                    ledItemView1.setPaint(this.selectedColor);
                } else {
                    ledItemView1.setChecked(false);
                    ledItemView1.setPaint(this.unSelectedColor);
                }
                ledItemView1.postInvalidate();
            }
        }
    }

    public void setData(byte[] bArr, float f) {
        int childCount;
        if (bArr != null && bArr.length == (childCount = getChildCount())) {
            for (int i = 0; i < childCount; i++) {
                LedItemView1 ledItemView1 = (LedItemView1) getChildAt(i);
                if (bArr[i] == 1) {
                    ledItemView1.setChecked(true);
                    ledItemView1.setPaint(this.selectedColor, f);
                } else {
                    ledItemView1.setChecked(false);
                    ledItemView1.setPaint(this.unSelectedColor, f);
                }
                ledItemView1.postInvalidate();
            }
        }
    }

    public int getWidthCount() {
        return this.widthCount;
    }

    public int getHeightCount() {
        return this.heightCount;
    }

    public void setPointMargin(int i) {
        this.pointMargin = i;
    }

    public int getMoveMax() {
        return this.moveMax;
    }

    public void setUnSelectedColor(int i) {
        this.unSelectedColor = i;
    }

    public void setSelectedColor(int i) {
        this.selectedColor = i;
    }

    public void setMode(int i) {
        this.mode = i;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i) {
        this.orientation = i;
    }

    public LedListener getLedListener() {
        return this.ledListener;
    }

    public void setLedListener(LedListener ledListener) {
        this.ledListener = ledListener;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void get1248RealTime(boolean r9, int r10, com.icwork.shiningglass.ui.widget.ledaddview.LedView.RealTimeDataListener r11) {
        /*
            r8 = this;
            r0 = 16
            byte[] r1 = new byte[r0]
            r2 = 0
            r3 = 3
            r1[r2] = r3
            r4 = 1
            if (r9 == 0) goto L1a
            r9 = 48
            if (r9 > r10) goto L16
            int r9 = r10 + (-48)
            byte r9 = (byte) r9
            r1[r4] = r9
            r9 = r4
            goto L1e
        L16:
            byte r9 = (byte) r10
            r1[r4] = r9
            goto L1d
        L1a:
            byte r9 = (byte) r10
            r1[r4] = r9
        L1d:
            r9 = r2
        L1e:
            int r5 = r8.orientation
            byte[] r5 = r8.getData(r5)
        L24:
            int r6 = r5.length
            if (r2 >= r6) goto L96
            int r6 = r8.heightCount
            int r7 = r2 / r6
            int r6 = r2 % r6
            if (r6 != r10) goto L93
            r6 = r5[r2]
            if (r6 != r4) goto L93
            r6 = 2
            switch(r7) {
                case 0: goto L8d;
                case 1: goto L85;
                case 2: goto L7d;
                case 3: goto L75;
                case 4: goto L6e;
                case 5: goto L67;
                case 6: goto L5f;
                case 7: goto L57;
                case 8: goto L50;
                case 9: goto L48;
                case 10: goto L40;
                case 11: goto L38;
                default: goto L37;
            }
        L37:
            goto L93
        L38:
            r7 = r1[r6]
            r7 = r7 | 128(0x80, float:1.794E-43)
            byte r7 = (byte) r7
            r1[r6] = r7
            goto L93
        L40:
            r7 = r1[r6]
            r7 = r7 | 64
            byte r7 = (byte) r7
            r1[r6] = r7
            goto L93
        L48:
            r7 = r1[r6]
            r7 = r7 | 32
            byte r7 = (byte) r7
            r1[r6] = r7
            goto L93
        L50:
            r7 = r1[r6]
            r7 = r7 | r0
            byte r7 = (byte) r7
            r1[r6] = r7
            goto L93
        L57:
            r7 = r1[r6]
            r7 = r7 | 8
            byte r7 = (byte) r7
            r1[r6] = r7
            goto L93
        L5f:
            r7 = r1[r6]
            r7 = r7 | 4
            byte r7 = (byte) r7
            r1[r6] = r7
            goto L93
        L67:
            r7 = r1[r6]
            r7 = r7 | r6
            byte r7 = (byte) r7
            r1[r6] = r7
            goto L93
        L6e:
            r7 = r1[r6]
            r7 = r7 | r4
            byte r7 = (byte) r7
            r1[r6] = r7
            goto L93
        L75:
            r6 = r1[r3]
            r6 = r6 | 128(0x80, float:1.794E-43)
            byte r6 = (byte) r6
            r1[r3] = r6
            goto L93
        L7d:
            r6 = r1[r3]
            r6 = r6 | 64
            byte r6 = (byte) r6
            r1[r3] = r6
            goto L93
        L85:
            r6 = r1[r3]
            r6 = r6 | 32
            byte r6 = (byte) r6
            r1[r3] = r6
            goto L93
        L8d:
            r6 = r1[r3]
            r6 = r6 | r0
            byte r6 = (byte) r6
            r1[r3] = r6
        L93:
            int r2 = r2 + 1
            goto L24
        L96:
            if (r11 == 0) goto L9b
            r11.onRealTimeData(r9, r1)
        L9b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.icwork.shiningglass.ui.widget.ledaddview.LedView.get1248RealTime(boolean, int, com.icwork.shiningglass.ui.widget.ledaddview.LedView$RealTimeDataListener):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void get536RealTime(boolean r10, int r11, com.icwork.shiningglass.ui.widget.ledaddview.LedView.RealTimeDataListener r12) {
        /*
            r9 = this;
            r0 = 16
            byte[] r1 = new byte[r0]
            r2 = 0
            r3 = 3
            r1[r2] = r3
            r4 = 1
            if (r10 == 0) goto L1a
            r10 = 36
            if (r10 > r11) goto L16
            int r10 = r11 + (-36)
            byte r10 = (byte) r10
            r1[r4] = r10
            r10 = r4
            goto L1e
        L16:
            byte r10 = (byte) r11
            r1[r4] = r10
            goto L1d
        L1a:
            byte r10 = (byte) r11
            r1[r4] = r10
        L1d:
            r10 = r2
        L1e:
            byte[] r5 = r9.getData(r2)
        L22:
            int r6 = r5.length
            if (r2 >= r6) goto L64
            int r6 = r9.heightCount
            int r7 = r2 / r6
            int r6 = r2 % r6
            if (r7 != r11) goto L61
            r7 = r5[r2]
            if (r7 != r4) goto L61
            r7 = 2
            if (r6 == 0) goto L5b
            if (r6 == r4) goto L53
            r8 = 4
            if (r6 == r7) goto L4c
            if (r6 == r3) goto L45
            if (r6 == r8) goto L3e
            goto L61
        L3e:
            r6 = r1[r7]
            r6 = r6 | r4
            byte r6 = (byte) r6
            r1[r7] = r6
            goto L61
        L45:
            r6 = r1[r7]
            r6 = r6 | r7
            byte r6 = (byte) r6
            r1[r7] = r6
            goto L61
        L4c:
            r6 = r1[r7]
            r6 = r6 | r8
            byte r6 = (byte) r6
            r1[r7] = r6
            goto L61
        L53:
            r6 = r1[r7]
            r6 = r6 | 8
            byte r6 = (byte) r6
            r1[r7] = r6
            goto L61
        L5b:
            r6 = r1[r7]
            r6 = r6 | r0
            byte r6 = (byte) r6
            r1[r7] = r6
        L61:
            int r2 = r2 + 1
            goto L22
        L64:
            if (r12 == 0) goto L69
            r12.onRealTimeData(r10, r1)
        L69:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.icwork.shiningglass.ui.widget.ledaddview.LedView.get536RealTime(boolean, int, com.icwork.shiningglass.ui.widget.ledaddview.LedView$RealTimeDataListener):void");
    }

    public byte[] getRealTime(int i) {
        byte[] bArr = new byte[16];
        bArr[0] = 3;
        bArr[1] = (byte) i;
        byte[] data = getData(this.orientation);
        for (int i2 = 0; i2 < data.length; i2++) {
            int i3 = this.heightCount;
            int i4 = i2 / i3;
            if (i2 % i3 == i && data[i2] == 1) {
                switch (i4) {
                    case 0:
                        bArr[3] = (byte) (bArr[3] | 4);
                        break;
                    case 1:
                        bArr[3] = (byte) (bArr[3] | 8);
                        break;
                    case 2:
                        bArr[3] = (byte) (bArr[3] | JSONB.Constants.BC_INT32_NUM_16);
                        break;
                    case 3:
                        bArr[3] = (byte) (bArr[3] | 32);
                        break;
                    case 4:
                        bArr[3] = (byte) (bArr[3] | JSONB.Constants.BC_INT32_SHORT_MIN);
                        break;
                    case 5:
                        bArr[3] = (byte) (bArr[3] | ByteCompanionObject.MIN_VALUE);
                        break;
                    case 6:
                        bArr[2] = (byte) (bArr[2] | 1);
                        break;
                    case 7:
                        bArr[2] = (byte) (bArr[2] | 2);
                        break;
                    case 8:
                        bArr[2] = (byte) (bArr[2] | 4);
                        break;
                    case 9:
                        bArr[2] = (byte) (bArr[2] | 8);
                        break;
                    case 10:
                        bArr[2] = (byte) (bArr[2] | JSONB.Constants.BC_INT32_NUM_16);
                        break;
                    case 11:
                        bArr[2] = (byte) (bArr[2] | 32);
                        break;
                    case 12:
                        bArr[2] = (byte) (bArr[2] | JSONB.Constants.BC_INT32_SHORT_MIN);
                        break;
                    case 13:
                        bArr[2] = (byte) (bArr[2] | ByteCompanionObject.MIN_VALUE);
                        break;
                }
            }
        }
        return bArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:59:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void get1664RealTime(boolean r10, int r11, com.icwork.shiningglass.ui.widget.ledaddview.LedView.RealTimeDataListener r12) {
        /*
            Method dump skipped, instruction units count: 222
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.icwork.shiningglass.ui.widget.ledaddview.LedView.get1664RealTime(boolean, int, com.icwork.shiningglass.ui.widget.ledaddview.LedView$RealTimeDataListener):void");
    }

    public void setDispatchTouch(boolean z) {
        this.isDispatchTouch = z;
    }

    public RealTimeDataListener getRealTimeDataListener() {
        return this.realTimeDataListener;
    }

    public void setRealTimeDataListener(RealTimeDataListener realTimeDataListener) {
        this.realTimeDataListener = realTimeDataListener;
    }

    public void getRealTime(int i, boolean z, int i2, int i3, RealTimeDataListener realTimeDataListener) {
        if (i == 1248) {
            get1248RealTime(z, i2, realTimeDataListener);
            return;
        }
        if (i == 1664) {
            get1664RealTime(z, i3, realTimeDataListener);
        } else if (i == 536) {
            get536RealTime(z, i3, realTimeDataListener);
        } else {
            get1664RealTime(z, i3, realTimeDataListener);
        }
    }

    public void setTextData(byte[] bArr) {
        LogUtil.d("设置文本数据：" + bArr.length);
        int length = bArr.length / 2;
        int[] iArr = new int[length];
        for (int i = 0; i < bArr.length / 2; i++) {
            int i2 = i * 2;
            iArr[i] = ((bArr[i2] & UByte.MAX_VALUE) * 64) + ((bArr[i2 + 1] & UByte.MAX_VALUE) >> 2);
        }
        for (int i3 = 0; i3 < this.widthCount; i3++) {
            if (i3 < length) {
                for (int i4 = 0; i4 < 12; i4++) {
                    int i5 = (iArr[i3] >> (i4 + 2)) & 1;
                    LedItemView1 ledItemView1 = getLedItemView1(i3, i4);
                    if (ledItemView1 == null) {
                        return;
                    }
                    if (i5 == 1) {
                        ledItemView1.setChecked(true);
                        ledItemView1.setPaint(this.selectedColor);
                    } else {
                        ledItemView1.setChecked(false);
                        ledItemView1.setPaint(this.unSelectedColor);
                    }
                    ledItemView1.postInvalidate();
                }
            }
        }
    }

    public void setTextDataByList(List<Byte> list) {
        clearSelected();
        int size = list.size() / 2;
        int[] iArr = new int[size];
        for (int i = 0; i < list.size() / 2; i++) {
            int i2 = i * 2;
            iArr[i] = ((list.get(i2).byteValue() & UByte.MAX_VALUE) * 64) + ((list.get(i2 + 1).byteValue() & UByte.MAX_VALUE) >> 2);
        }
        for (int i3 = 0; i3 < this.widthCount; i3++) {
            if (i3 < size) {
                for (int i4 = 0; i4 < 12; i4++) {
                    int i5 = (iArr[i3] >> (i4 + 2)) & 1;
                    LedItemView1 ledItemView1 = getLedItemView1(i3, i4);
                    if (ledItemView1 == null) {
                        return;
                    }
                    if (i5 == 1) {
                        ledItemView1.setChecked(true);
                        ledItemView1.setPaint(this.selectedColor);
                    } else {
                        ledItemView1.setChecked(false);
                        ledItemView1.setPaint(this.unSelectedColor);
                    }
                    ledItemView1.postInvalidate();
                }
            }
        }
    }

    private LedItemView1 getLedItemView1(int i, int i2) {
        return (LedItemView1) getChildAt((i * 12) + (12 - (i2 + 1)));
    }
}
