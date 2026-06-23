package com.icwork.shiningglass.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.alibaba.fastjson2.internal.asm.Opcodes;
import com.icwork.shiningglass.ui.utils.LogUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.UByte;
import org.apache.http.HttpStatus;

/* JADX INFO: loaded from: classes.dex */
public class LedTextView extends ViewGroup {
    private static final String TAG = "LedView";
    Handler handler;
    private int heightCount;
    int heightSize;
    private LinkedList<Integer> leftMoveList;
    private int mColorBgType;
    LinkedList<Integer> mColorList;
    LinkedList<Integer> mColorListRight;
    private boolean mTextBgEnable;
    private boolean mTextColorEnable;
    private int mTextColorType;
    int moveMax;
    int moveYMax;
    MyTimerTask2 myTimerTask2;
    MyTimerTask4 myTimerTask4;
    private int offset;
    private int orientation;
    private int pointAllLength;
    private int pointLength;
    private int pointMargin;
    private int pointYAllLength;
    private LinkedList<Integer> rightMoveList;
    private int selectedColor3;
    private int[] textColor1;
    private int[] textColor2;
    private int[] textColor3;
    private int[] textColor4;
    private int[] textColorBg1;
    private int[] textColorBg2;
    private int[] textColorBg3;
    private byte[] textData;
    Timer timer;
    private int unSelectedColor;
    private int widthCount;
    int widthSize;
    private int xMore;
    private int yMore;

    public LedTextView(Context context) {
        super(context);
        this.widthCount = 5;
        this.heightCount = 10;
        this.pointMargin = 1;
        this.selectedColor3 = Color.rgb(82, 227, 255);
        this.unSelectedColor = 0;
        this.leftMoveList = new LinkedList<>();
        this.rightMoveList = new LinkedList<>();
        this.mColorList = new LinkedList<>();
        this.mColorListRight = new LinkedList<>();
        this.textColor1 = new int[]{Color.rgb(255, 0, 0), Color.rgb(255, 42, 0), Color.rgb(255, 85, 0), Color.rgb(255, 127, 0), Color.rgb(255, Opcodes.TABLESWITCH, 0), Color.rgb(255, 212, 0), Color.rgb(255, 255, 0), Color.rgb(213, 255, 0), Color.rgb(Opcodes.TABLESWITCH, 255, 0), Color.rgb(128, 255, 0), Color.rgb(85, 255, 0), Color.rgb(42, 255, 0), Color.rgb(0, 255, 0), Color.rgb(0, 255, 43), Color.rgb(0, 255, 85), Color.rgb(0, 255, 128), Color.rgb(0, 255, Opcodes.TABLESWITCH), Color.rgb(0, 255, 213), Color.rgb(0, 255, 255), Color.rgb(0, 213, 255), Color.rgb(0, Opcodes.TABLESWITCH, 255), Color.rgb(0, 128, 255), Color.rgb(0, 85, 255), Color.rgb(0, 43, 255), Color.rgb(0, 0, 255), Color.rgb(42, 0, 255), Color.rgb(85, 0, 255), Color.rgb(128, 0, 255), Color.rgb(Opcodes.TABLESWITCH, 0, 255), Color.rgb(213, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 0, 212), Color.rgb(255, 0, Opcodes.TABLESWITCH), Color.rgb(255, 0, 127), Color.rgb(255, 0, 85), Color.rgb(255, 0, 42)};
        this.textColor2 = new int[]{Color.rgb(255, 0, 0), Color.rgb(255, 127, 0), Color.rgb(255, 255, 0), Color.rgb(128, 255, 0), Color.rgb(0, 255, 0), Color.rgb(0, 255, 128), Color.rgb(0, 255, 255), Color.rgb(0, 128, 255), Color.rgb(0, 0, 255), Color.rgb(128, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 0, 127)};
        this.textColor3 = new int[]{Color.rgb(0, 255, 255), Color.rgb(0, 255, 255), Color.rgb(0, 234, 255), Color.rgb(0, 234, 255), Color.rgb(3, 213, 255), Color.rgb(3, 213, 255), Color.rgb(0, Opcodes.ATHROW, 255), Color.rgb(0, Opcodes.ATHROW, 255), Color.rgb(3, Opcodes.LOOKUPSWITCH, 255), Color.rgb(3, Opcodes.LOOKUPSWITCH, 255), Color.rgb(3, Opcodes.FCMPG, 255), Color.rgb(3, Opcodes.FCMPG, 255), Color.rgb(3, Opcodes.LOR, 255), Color.rgb(3, Opcodes.LOR, 255), Color.rgb(3, 108, 255), Color.rgb(3, 108, 255), Color.rgb(3, 87, 255), Color.rgb(3, 87, 255), Color.rgb(3, 66, 255), Color.rgb(3, 66, 255), Color.rgb(3, 45, 255), Color.rgb(3, 45, 255), Color.rgb(3, 24, 255), Color.rgb(3, 24, 255), Color.rgb(3, 3, 255), Color.rgb(3, 3, 255), Color.rgb(24, 3, 255), Color.rgb(24, 3, 255), Color.rgb(45, 3, 255), Color.rgb(45, 3, 255), Color.rgb(66, 3, 255), Color.rgb(66, 3, 255), Color.rgb(87, 3, 255), Color.rgb(87, 3, 255), Color.rgb(108, 3, 255), Color.rgb(108, 3, 255)};
        this.textColor4 = new int[]{Color.rgb(255, 247, 0), Color.rgb(255, 247, 0), Color.rgb(251, 255, 0), Color.rgb(251, 255, 0), Color.rgb(238, 255, 3), Color.rgb(238, 255, 3), Color.rgb(221, 255, 3), Color.rgb(221, 255, 3), Color.rgb(HttpStatus.SC_RESET_CONTENT, 255, 3), Color.rgb(HttpStatus.SC_RESET_CONTENT, 255, 3), Color.rgb(Opcodes.CHECKCAST, 255, 3), Color.rgb(Opcodes.CHECKCAST, 255, 3), Color.rgb(Opcodes.PUTSTATIC, 255, 3), Color.rgb(Opcodes.PUTSTATIC, 255, 3), Color.rgb(Opcodes.GOTO, 255, 3), Color.rgb(Opcodes.GOTO, 255, 3), Color.rgb(Opcodes.IFNE, 255, 3), Color.rgb(Opcodes.IFNE, 255, 3), Color.rgb(141, 255, 3), Color.rgb(141, 255, 3), Color.rgb(Opcodes.LOR, 255, 3), Color.rgb(Opcodes.LOR, 255, 3), Color.rgb(108, 255, 3), Color.rgb(108, 255, 3), Color.rgb(95, 255, 3), Color.rgb(95, 255, 3), Color.rgb(83, 255, 3), Color.rgb(83, 255, 3), Color.rgb(70, 255, 3), Color.rgb(70, 255, 3), Color.rgb(53, 255, 3), Color.rgb(53, 255, 3), Color.rgb(24, 255, 3), Color.rgb(24, 255, 3), Color.rgb(3, 255, 24), Color.rgb(3, 255, 24)};
        this.textColorBg1 = new int[]{Color.rgb(77, 0, 0), Color.rgb(77, 14, 1), Color.rgb(77, 26, 1), Color.rgb(77, 39, 1), Color.rgb(77, 52, 1), Color.rgb(77, 64, 1), Color.rgb(76, 77, 1), Color.rgb(64, 77, 1), Color.rgb(51, 77, 1), Color.rgb(39, 77, 1), Color.rgb(26, 77, 1), Color.rgb(13, 77, 1), Color.rgb(1, 77, 1), Color.rgb(1, 77, 13), Color.rgb(1, 77, 26), Color.rgb(1, 77, 39), Color.rgb(1, 77, 51), Color.rgb(1, 77, 64), Color.rgb(1, 77, 77), Color.rgb(1, 64, 77), Color.rgb(1, 51, 77), Color.rgb(1, 39, 77), Color.rgb(1, 26, 77), Color.rgb(1, 13, 77), Color.rgb(1, 1, 77), Color.rgb(13, 1, 77), Color.rgb(39, 1, 77), Color.rgb(51, 1, 77), Color.rgb(64, 1, 77), Color.rgb(76, 1, 77), Color.rgb(77, 1, 64), Color.rgb(77, 1, 51), Color.rgb(77, 1, 39), Color.rgb(77, 1, 26), Color.rgb(77, 1, 13), Color.rgb(77, 1, 1)};
        this.textColorBg2 = new int[]{Color.rgb(77, 0, 0), Color.rgb(77, 38, 0), Color.rgb(77, 77, 0), Color.rgb(39, 77, 0), Color.rgb(0, 77, 0), Color.rgb(0, 77, 39), Color.rgb(0, 77, 77), Color.rgb(0, 39, 77), Color.rgb(0, 0, 77), Color.rgb(39, 0, 77), Color.rgb(77, 0, 77), Color.rgb(77, 0, 38)};
        this.textColorBg3 = new int[]{Color.rgb(0, 77, 77), Color.rgb(0, 77, 77), Color.rgb(1, 64, 77), Color.rgb(1, 64, 77), Color.rgb(1, 51, 77), Color.rgb(1, 51, 77), Color.rgb(1, 39, 77), Color.rgb(1, 39, 77), Color.rgb(1, 26, 77), Color.rgb(1, 26, 77), Color.rgb(1, 13, 77), Color.rgb(1, 13, 77), Color.rgb(13, 1, 77), Color.rgb(13, 1, 77), Color.rgb(26, 1, 77), Color.rgb(26, 1, 77), Color.rgb(39, 1, 77), Color.rgb(39, 1, 77), Color.rgb(51, 1, 77), Color.rgb(51, 1, 77), Color.rgb(64, 1, 77), Color.rgb(64, 1, 77), Color.rgb(76, 1, 77), Color.rgb(76, 1, 77), Color.rgb(77, 1, 64), Color.rgb(77, 1, 64), Color.rgb(77, 1, 51), Color.rgb(77, 1, 51), Color.rgb(77, 1, 39), Color.rgb(77, 1, 39), Color.rgb(77, 1, 26), Color.rgb(77, 1, 26), Color.rgb(77, 1, 13), Color.rgb(77, 1, 13), Color.rgb(77, 1, 1), Color.rgb(77, 1, 1)};
        this.handler = new Handler() { // from class: com.icwork.shiningglass.ui.widget.LedTextView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 2) {
                    LedTextView.this.textLeftMove();
                }
                if (message.what == 4) {
                    LedTextView.this.textRightMove();
                }
                super.handleMessage(message);
            }
        };
    }

    public LedTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.widthCount = 5;
        this.heightCount = 10;
        this.pointMargin = 1;
        this.selectedColor3 = Color.rgb(82, 227, 255);
        this.unSelectedColor = 0;
        this.leftMoveList = new LinkedList<>();
        this.rightMoveList = new LinkedList<>();
        this.mColorList = new LinkedList<>();
        this.mColorListRight = new LinkedList<>();
        this.textColor1 = new int[]{Color.rgb(255, 0, 0), Color.rgb(255, 42, 0), Color.rgb(255, 85, 0), Color.rgb(255, 127, 0), Color.rgb(255, Opcodes.TABLESWITCH, 0), Color.rgb(255, 212, 0), Color.rgb(255, 255, 0), Color.rgb(213, 255, 0), Color.rgb(Opcodes.TABLESWITCH, 255, 0), Color.rgb(128, 255, 0), Color.rgb(85, 255, 0), Color.rgb(42, 255, 0), Color.rgb(0, 255, 0), Color.rgb(0, 255, 43), Color.rgb(0, 255, 85), Color.rgb(0, 255, 128), Color.rgb(0, 255, Opcodes.TABLESWITCH), Color.rgb(0, 255, 213), Color.rgb(0, 255, 255), Color.rgb(0, 213, 255), Color.rgb(0, Opcodes.TABLESWITCH, 255), Color.rgb(0, 128, 255), Color.rgb(0, 85, 255), Color.rgb(0, 43, 255), Color.rgb(0, 0, 255), Color.rgb(42, 0, 255), Color.rgb(85, 0, 255), Color.rgb(128, 0, 255), Color.rgb(Opcodes.TABLESWITCH, 0, 255), Color.rgb(213, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 0, 212), Color.rgb(255, 0, Opcodes.TABLESWITCH), Color.rgb(255, 0, 127), Color.rgb(255, 0, 85), Color.rgb(255, 0, 42)};
        this.textColor2 = new int[]{Color.rgb(255, 0, 0), Color.rgb(255, 127, 0), Color.rgb(255, 255, 0), Color.rgb(128, 255, 0), Color.rgb(0, 255, 0), Color.rgb(0, 255, 128), Color.rgb(0, 255, 255), Color.rgb(0, 128, 255), Color.rgb(0, 0, 255), Color.rgb(128, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 0, 127)};
        this.textColor3 = new int[]{Color.rgb(0, 255, 255), Color.rgb(0, 255, 255), Color.rgb(0, 234, 255), Color.rgb(0, 234, 255), Color.rgb(3, 213, 255), Color.rgb(3, 213, 255), Color.rgb(0, Opcodes.ATHROW, 255), Color.rgb(0, Opcodes.ATHROW, 255), Color.rgb(3, Opcodes.LOOKUPSWITCH, 255), Color.rgb(3, Opcodes.LOOKUPSWITCH, 255), Color.rgb(3, Opcodes.FCMPG, 255), Color.rgb(3, Opcodes.FCMPG, 255), Color.rgb(3, Opcodes.LOR, 255), Color.rgb(3, Opcodes.LOR, 255), Color.rgb(3, 108, 255), Color.rgb(3, 108, 255), Color.rgb(3, 87, 255), Color.rgb(3, 87, 255), Color.rgb(3, 66, 255), Color.rgb(3, 66, 255), Color.rgb(3, 45, 255), Color.rgb(3, 45, 255), Color.rgb(3, 24, 255), Color.rgb(3, 24, 255), Color.rgb(3, 3, 255), Color.rgb(3, 3, 255), Color.rgb(24, 3, 255), Color.rgb(24, 3, 255), Color.rgb(45, 3, 255), Color.rgb(45, 3, 255), Color.rgb(66, 3, 255), Color.rgb(66, 3, 255), Color.rgb(87, 3, 255), Color.rgb(87, 3, 255), Color.rgb(108, 3, 255), Color.rgb(108, 3, 255)};
        this.textColor4 = new int[]{Color.rgb(255, 247, 0), Color.rgb(255, 247, 0), Color.rgb(251, 255, 0), Color.rgb(251, 255, 0), Color.rgb(238, 255, 3), Color.rgb(238, 255, 3), Color.rgb(221, 255, 3), Color.rgb(221, 255, 3), Color.rgb(HttpStatus.SC_RESET_CONTENT, 255, 3), Color.rgb(HttpStatus.SC_RESET_CONTENT, 255, 3), Color.rgb(Opcodes.CHECKCAST, 255, 3), Color.rgb(Opcodes.CHECKCAST, 255, 3), Color.rgb(Opcodes.PUTSTATIC, 255, 3), Color.rgb(Opcodes.PUTSTATIC, 255, 3), Color.rgb(Opcodes.GOTO, 255, 3), Color.rgb(Opcodes.GOTO, 255, 3), Color.rgb(Opcodes.IFNE, 255, 3), Color.rgb(Opcodes.IFNE, 255, 3), Color.rgb(141, 255, 3), Color.rgb(141, 255, 3), Color.rgb(Opcodes.LOR, 255, 3), Color.rgb(Opcodes.LOR, 255, 3), Color.rgb(108, 255, 3), Color.rgb(108, 255, 3), Color.rgb(95, 255, 3), Color.rgb(95, 255, 3), Color.rgb(83, 255, 3), Color.rgb(83, 255, 3), Color.rgb(70, 255, 3), Color.rgb(70, 255, 3), Color.rgb(53, 255, 3), Color.rgb(53, 255, 3), Color.rgb(24, 255, 3), Color.rgb(24, 255, 3), Color.rgb(3, 255, 24), Color.rgb(3, 255, 24)};
        this.textColorBg1 = new int[]{Color.rgb(77, 0, 0), Color.rgb(77, 14, 1), Color.rgb(77, 26, 1), Color.rgb(77, 39, 1), Color.rgb(77, 52, 1), Color.rgb(77, 64, 1), Color.rgb(76, 77, 1), Color.rgb(64, 77, 1), Color.rgb(51, 77, 1), Color.rgb(39, 77, 1), Color.rgb(26, 77, 1), Color.rgb(13, 77, 1), Color.rgb(1, 77, 1), Color.rgb(1, 77, 13), Color.rgb(1, 77, 26), Color.rgb(1, 77, 39), Color.rgb(1, 77, 51), Color.rgb(1, 77, 64), Color.rgb(1, 77, 77), Color.rgb(1, 64, 77), Color.rgb(1, 51, 77), Color.rgb(1, 39, 77), Color.rgb(1, 26, 77), Color.rgb(1, 13, 77), Color.rgb(1, 1, 77), Color.rgb(13, 1, 77), Color.rgb(39, 1, 77), Color.rgb(51, 1, 77), Color.rgb(64, 1, 77), Color.rgb(76, 1, 77), Color.rgb(77, 1, 64), Color.rgb(77, 1, 51), Color.rgb(77, 1, 39), Color.rgb(77, 1, 26), Color.rgb(77, 1, 13), Color.rgb(77, 1, 1)};
        this.textColorBg2 = new int[]{Color.rgb(77, 0, 0), Color.rgb(77, 38, 0), Color.rgb(77, 77, 0), Color.rgb(39, 77, 0), Color.rgb(0, 77, 0), Color.rgb(0, 77, 39), Color.rgb(0, 77, 77), Color.rgb(0, 39, 77), Color.rgb(0, 0, 77), Color.rgb(39, 0, 77), Color.rgb(77, 0, 77), Color.rgb(77, 0, 38)};
        this.textColorBg3 = new int[]{Color.rgb(0, 77, 77), Color.rgb(0, 77, 77), Color.rgb(1, 64, 77), Color.rgb(1, 64, 77), Color.rgb(1, 51, 77), Color.rgb(1, 51, 77), Color.rgb(1, 39, 77), Color.rgb(1, 39, 77), Color.rgb(1, 26, 77), Color.rgb(1, 26, 77), Color.rgb(1, 13, 77), Color.rgb(1, 13, 77), Color.rgb(13, 1, 77), Color.rgb(13, 1, 77), Color.rgb(26, 1, 77), Color.rgb(26, 1, 77), Color.rgb(39, 1, 77), Color.rgb(39, 1, 77), Color.rgb(51, 1, 77), Color.rgb(51, 1, 77), Color.rgb(64, 1, 77), Color.rgb(64, 1, 77), Color.rgb(76, 1, 77), Color.rgb(76, 1, 77), Color.rgb(77, 1, 64), Color.rgb(77, 1, 64), Color.rgb(77, 1, 51), Color.rgb(77, 1, 51), Color.rgb(77, 1, 39), Color.rgb(77, 1, 39), Color.rgb(77, 1, 26), Color.rgb(77, 1, 26), Color.rgb(77, 1, 13), Color.rgb(77, 1, 13), Color.rgb(77, 1, 1), Color.rgb(77, 1, 1)};
        this.handler = new Handler() { // from class: com.icwork.shiningglass.ui.widget.LedTextView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 2) {
                    LedTextView.this.textLeftMove();
                }
                if (message.what == 4) {
                    LedTextView.this.textRightMove();
                }
                super.handleMessage(message);
            }
        };
    }

    public LedTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.widthCount = 5;
        this.heightCount = 10;
        this.pointMargin = 1;
        this.selectedColor3 = Color.rgb(82, 227, 255);
        this.unSelectedColor = 0;
        this.leftMoveList = new LinkedList<>();
        this.rightMoveList = new LinkedList<>();
        this.mColorList = new LinkedList<>();
        this.mColorListRight = new LinkedList<>();
        this.textColor1 = new int[]{Color.rgb(255, 0, 0), Color.rgb(255, 42, 0), Color.rgb(255, 85, 0), Color.rgb(255, 127, 0), Color.rgb(255, Opcodes.TABLESWITCH, 0), Color.rgb(255, 212, 0), Color.rgb(255, 255, 0), Color.rgb(213, 255, 0), Color.rgb(Opcodes.TABLESWITCH, 255, 0), Color.rgb(128, 255, 0), Color.rgb(85, 255, 0), Color.rgb(42, 255, 0), Color.rgb(0, 255, 0), Color.rgb(0, 255, 43), Color.rgb(0, 255, 85), Color.rgb(0, 255, 128), Color.rgb(0, 255, Opcodes.TABLESWITCH), Color.rgb(0, 255, 213), Color.rgb(0, 255, 255), Color.rgb(0, 213, 255), Color.rgb(0, Opcodes.TABLESWITCH, 255), Color.rgb(0, 128, 255), Color.rgb(0, 85, 255), Color.rgb(0, 43, 255), Color.rgb(0, 0, 255), Color.rgb(42, 0, 255), Color.rgb(85, 0, 255), Color.rgb(128, 0, 255), Color.rgb(Opcodes.TABLESWITCH, 0, 255), Color.rgb(213, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 0, 212), Color.rgb(255, 0, Opcodes.TABLESWITCH), Color.rgb(255, 0, 127), Color.rgb(255, 0, 85), Color.rgb(255, 0, 42)};
        this.textColor2 = new int[]{Color.rgb(255, 0, 0), Color.rgb(255, 127, 0), Color.rgb(255, 255, 0), Color.rgb(128, 255, 0), Color.rgb(0, 255, 0), Color.rgb(0, 255, 128), Color.rgb(0, 255, 255), Color.rgb(0, 128, 255), Color.rgb(0, 0, 255), Color.rgb(128, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 0, 127)};
        this.textColor3 = new int[]{Color.rgb(0, 255, 255), Color.rgb(0, 255, 255), Color.rgb(0, 234, 255), Color.rgb(0, 234, 255), Color.rgb(3, 213, 255), Color.rgb(3, 213, 255), Color.rgb(0, Opcodes.ATHROW, 255), Color.rgb(0, Opcodes.ATHROW, 255), Color.rgb(3, Opcodes.LOOKUPSWITCH, 255), Color.rgb(3, Opcodes.LOOKUPSWITCH, 255), Color.rgb(3, Opcodes.FCMPG, 255), Color.rgb(3, Opcodes.FCMPG, 255), Color.rgb(3, Opcodes.LOR, 255), Color.rgb(3, Opcodes.LOR, 255), Color.rgb(3, 108, 255), Color.rgb(3, 108, 255), Color.rgb(3, 87, 255), Color.rgb(3, 87, 255), Color.rgb(3, 66, 255), Color.rgb(3, 66, 255), Color.rgb(3, 45, 255), Color.rgb(3, 45, 255), Color.rgb(3, 24, 255), Color.rgb(3, 24, 255), Color.rgb(3, 3, 255), Color.rgb(3, 3, 255), Color.rgb(24, 3, 255), Color.rgb(24, 3, 255), Color.rgb(45, 3, 255), Color.rgb(45, 3, 255), Color.rgb(66, 3, 255), Color.rgb(66, 3, 255), Color.rgb(87, 3, 255), Color.rgb(87, 3, 255), Color.rgb(108, 3, 255), Color.rgb(108, 3, 255)};
        this.textColor4 = new int[]{Color.rgb(255, 247, 0), Color.rgb(255, 247, 0), Color.rgb(251, 255, 0), Color.rgb(251, 255, 0), Color.rgb(238, 255, 3), Color.rgb(238, 255, 3), Color.rgb(221, 255, 3), Color.rgb(221, 255, 3), Color.rgb(HttpStatus.SC_RESET_CONTENT, 255, 3), Color.rgb(HttpStatus.SC_RESET_CONTENT, 255, 3), Color.rgb(Opcodes.CHECKCAST, 255, 3), Color.rgb(Opcodes.CHECKCAST, 255, 3), Color.rgb(Opcodes.PUTSTATIC, 255, 3), Color.rgb(Opcodes.PUTSTATIC, 255, 3), Color.rgb(Opcodes.GOTO, 255, 3), Color.rgb(Opcodes.GOTO, 255, 3), Color.rgb(Opcodes.IFNE, 255, 3), Color.rgb(Opcodes.IFNE, 255, 3), Color.rgb(141, 255, 3), Color.rgb(141, 255, 3), Color.rgb(Opcodes.LOR, 255, 3), Color.rgb(Opcodes.LOR, 255, 3), Color.rgb(108, 255, 3), Color.rgb(108, 255, 3), Color.rgb(95, 255, 3), Color.rgb(95, 255, 3), Color.rgb(83, 255, 3), Color.rgb(83, 255, 3), Color.rgb(70, 255, 3), Color.rgb(70, 255, 3), Color.rgb(53, 255, 3), Color.rgb(53, 255, 3), Color.rgb(24, 255, 3), Color.rgb(24, 255, 3), Color.rgb(3, 255, 24), Color.rgb(3, 255, 24)};
        this.textColorBg1 = new int[]{Color.rgb(77, 0, 0), Color.rgb(77, 14, 1), Color.rgb(77, 26, 1), Color.rgb(77, 39, 1), Color.rgb(77, 52, 1), Color.rgb(77, 64, 1), Color.rgb(76, 77, 1), Color.rgb(64, 77, 1), Color.rgb(51, 77, 1), Color.rgb(39, 77, 1), Color.rgb(26, 77, 1), Color.rgb(13, 77, 1), Color.rgb(1, 77, 1), Color.rgb(1, 77, 13), Color.rgb(1, 77, 26), Color.rgb(1, 77, 39), Color.rgb(1, 77, 51), Color.rgb(1, 77, 64), Color.rgb(1, 77, 77), Color.rgb(1, 64, 77), Color.rgb(1, 51, 77), Color.rgb(1, 39, 77), Color.rgb(1, 26, 77), Color.rgb(1, 13, 77), Color.rgb(1, 1, 77), Color.rgb(13, 1, 77), Color.rgb(39, 1, 77), Color.rgb(51, 1, 77), Color.rgb(64, 1, 77), Color.rgb(76, 1, 77), Color.rgb(77, 1, 64), Color.rgb(77, 1, 51), Color.rgb(77, 1, 39), Color.rgb(77, 1, 26), Color.rgb(77, 1, 13), Color.rgb(77, 1, 1)};
        this.textColorBg2 = new int[]{Color.rgb(77, 0, 0), Color.rgb(77, 38, 0), Color.rgb(77, 77, 0), Color.rgb(39, 77, 0), Color.rgb(0, 77, 0), Color.rgb(0, 77, 39), Color.rgb(0, 77, 77), Color.rgb(0, 39, 77), Color.rgb(0, 0, 77), Color.rgb(39, 0, 77), Color.rgb(77, 0, 77), Color.rgb(77, 0, 38)};
        this.textColorBg3 = new int[]{Color.rgb(0, 77, 77), Color.rgb(0, 77, 77), Color.rgb(1, 64, 77), Color.rgb(1, 64, 77), Color.rgb(1, 51, 77), Color.rgb(1, 51, 77), Color.rgb(1, 39, 77), Color.rgb(1, 39, 77), Color.rgb(1, 26, 77), Color.rgb(1, 26, 77), Color.rgb(1, 13, 77), Color.rgb(1, 13, 77), Color.rgb(13, 1, 77), Color.rgb(13, 1, 77), Color.rgb(26, 1, 77), Color.rgb(26, 1, 77), Color.rgb(39, 1, 77), Color.rgb(39, 1, 77), Color.rgb(51, 1, 77), Color.rgb(51, 1, 77), Color.rgb(64, 1, 77), Color.rgb(64, 1, 77), Color.rgb(76, 1, 77), Color.rgb(76, 1, 77), Color.rgb(77, 1, 64), Color.rgb(77, 1, 64), Color.rgb(77, 1, 51), Color.rgb(77, 1, 51), Color.rgb(77, 1, 39), Color.rgb(77, 1, 39), Color.rgb(77, 1, 26), Color.rgb(77, 1, 26), Color.rgb(77, 1, 13), Color.rgb(77, 1, 13), Color.rgb(77, 1, 1), Color.rgb(77, 1, 1)};
        this.handler = new Handler() { // from class: com.icwork.shiningglass.ui.widget.LedTextView.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 2) {
                    LedTextView.this.textLeftMove();
                }
                if (message.what == 4) {
                    LedTextView.this.textRightMove();
                }
                super.handleMessage(message);
            }
        };
    }

    public void init(int i, int i2) {
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
        int size = View.MeasureSpec.getSize(i2);
        this.heightSize = size;
        int i3 = this.widthSize;
        int i4 = this.widthCount;
        int i5 = i3 % i4;
        this.xMore = i5;
        int i6 = this.heightCount;
        this.yMore = size % i6;
        int i7 = i3 / i4;
        this.pointAllLength = i7;
        int i8 = size / i6;
        this.pointYAllLength = i8;
        int i9 = this.pointMargin;
        this.pointLength = i7 - (i9 * 2);
        int i10 = i5 / 2;
        this.offset = i10;
        this.moveMax = (i7 * i6) - size;
        this.moveYMax = (i8 * i4) - i3;
        int i11 = (i7 * i6) - (i9 + i10);
        this.heightSize = i11;
        setMeasuredDimension(i3, i11);
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

    public void setData(byte[] bArr) {
        if (bArr == null) {
            return;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LedItemView1 ledItemView1 = (LedItemView1) getChildAt(i);
            if (bArr[i] == 1) {
                ledItemView1.setChecked(true);
                ledItemView1.setPaint(this.selectedColor3);
            } else {
                ledItemView1.setChecked(false);
                ledItemView1.setPaint(this.unSelectedColor);
            }
            ledItemView1.postInvalidate();
        }
    }

    public void setData(List<Integer> list) {
        if (list == null) {
            return;
        }
        getChildCount();
        for (int i = 0; i < list.size(); i++) {
            LedItemView1 ledItemView1 = (LedItemView1) getChildAt(i);
            LogUtil.d("====data:" + list.get(i));
            if (list.get(i).intValue() == 1) {
                ledItemView1.setChecked(true);
                ledItemView1.setPaint(this.selectedColor3);
            } else {
                ledItemView1.setChecked(false);
                ledItemView1.setPaint(this.unSelectedColor);
            }
            ledItemView1.postInvalidate();
        }
    }

    public void setTextData(byte[] bArr, int[] iArr, boolean z, boolean z2, int i, int i2) {
        LogUtil.d("textColorEnable:" + z + " textBgEnable:" + z2 + "\u3000bgColorType：" + i2);
        if (bArr == null || iArr == null) {
            return;
        }
        if (i2 > 0) {
            this.unSelectedColor = 0;
        }
        this.mColorList.clear();
        for (int i3 : iArr) {
            this.mColorList.add(Integer.valueOf(i3));
        }
        this.mTextColorType = i;
        this.mColorBgType = i2;
        this.textData = bArr;
        clearSelected();
        int length = bArr.length / 2;
        int[] iArr2 = new int[length];
        if (length < 36) {
            iArr2 = new int[36];
        }
        if (this.mColorList.size() < 36) {
            for (int size = this.mColorList.size(); size < 36; size++) {
                this.mColorList.add(0);
            }
        }
        for (int i4 = 0; i4 < length; i4++) {
            int i5 = i4 * 2;
            iArr2[i4] = ((bArr[i5] & UByte.MAX_VALUE) * 64) + ((bArr[i5 + 1] & UByte.MAX_VALUE) >> 2);
        }
        for (int i6 = 0; i6 < 36; i6++) {
            if (i6 < iArr2.length) {
                int iIntValue = this.mColorList.get(i6).intValue();
                for (int i7 = 0; i7 < 12; i7++) {
                    int i8 = (iArr2[i6] >> (i7 + 2)) & 1;
                    LedItemView1 ledItemView1 = getLedItemView1(i6, i7);
                    if (ledItemView1 == null) {
                        return;
                    }
                    if (i8 == 1) {
                        ledItemView1.setChecked(true);
                        if (z) {
                            setTextColorType(ledItemView1, i6, i7, this.mTextColorType);
                        } else {
                            ledItemView1.setPaint(iIntValue);
                        }
                    } else {
                        ledItemView1.setChecked(false);
                        if (z2) {
                            setTextColorBg(ledItemView1, i6, i7, this.mColorBgType);
                        } else {
                            ledItemView1.setPaint(0);
                        }
                    }
                    ledItemView1.postInvalidate();
                }
            }
        }
    }

    public void setHistoryData(byte[] bArr, int[] iArr) {
        if (bArr == null || iArr == null) {
            return;
        }
        this.textData = bArr;
        clearSelected();
        int length = bArr.length / 2;
        int[] iArr2 = new int[length];
        for (int i = 0; i < bArr.length / 2; i++) {
            int i2 = i * 2;
            iArr2[i] = ((bArr[i2] & UByte.MAX_VALUE) * 64) + ((bArr[i2 + 1] & UByte.MAX_VALUE) >> 2);
        }
        int i3 = length < 96 ? length : 96;
        for (int i4 = 0; i4 < i3; i4++) {
            if (i4 < length) {
                int i5 = iArr[i4];
                for (int i6 = 0; i6 < 12; i6++) {
                    int i7 = (iArr2[i4] >> (i6 + 2)) & 1;
                    LedItemView1 ledItemView1 = getLedItemView1(i4, i6);
                    if (ledItemView1 == null) {
                        return;
                    }
                    if (i7 == 1) {
                        ledItemView1.setChecked(true);
                        ledItemView1.setPaint(i5);
                    } else {
                        ledItemView1.setChecked(false);
                        ledItemView1.setPaint(0);
                    }
                    ledItemView1.postInvalidate();
                }
            }
        }
    }

    public void setTextMarquee(byte[] bArr, int[] iArr, boolean z, boolean z2, int i, int i2) {
        if (bArr == null || iArr == null) {
            return;
        }
        LogUtil.d("textColorEnable:" + z + " textBgEnable:" + z2 + "\u3000bgColorType：" + i2);
        if (i2 > 0) {
            this.unSelectedColor = 0;
        }
        this.textData = bArr;
        this.mTextColorType = i;
        this.mColorBgType = i2;
        this.mTextColorEnable = z;
        this.mTextBgEnable = z2;
        this.mColorList.clear();
        for (int i3 : iArr) {
            this.mColorList.add(Integer.valueOf(i3));
        }
        if (bArr != null) {
            clearSelected();
            this.leftMoveList.clear();
            cancelTimerTask4();
            Timer timer = this.timer;
            if (timer != null) {
                timer.cancel();
                this.timer.purge();
                this.timer = null;
            }
        }
        int i4 = 0;
        while (true) {
            byte[] bArr2 = this.textData;
            if (i4 >= bArr2.length / 2) {
                break;
            }
            int i5 = i4 * 2;
            this.leftMoveList.add(Integer.valueOf(((bArr2[i5] & UByte.MAX_VALUE) * 64) + ((bArr2[i5 + 1] & UByte.MAX_VALUE) >> 2)));
            i4++;
        }
        for (int i6 = 0; i6 < 36; i6++) {
            this.leftMoveList.addFirst(0);
            this.mColorList.addFirst(0);
        }
        this.timer = new Timer();
        MyTimerTask2 myTimerTask2 = new MyTimerTask2();
        this.myTimerTask2 = myTimerTask2;
        this.timer.schedule(myTimerTask2, 100L, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void textLeftMove() {
        if (this.leftMoveList == null) {
            return;
        }
        for (int i = 0; i < 36; i++) {
            if (i < this.leftMoveList.size()) {
                this.mColorList.size();
                int iIntValue = this.mColorList.get(i).intValue();
                for (int i2 = 0; i2 < 12; i2++) {
                    int iIntValue2 = (this.leftMoveList.get(i).intValue() >> (i2 + 2)) & 1;
                    LedItemView1 ledItemView1 = getLedItemView1(i, i2);
                    if (iIntValue2 == 1) {
                        ledItemView1.setChecked(true);
                        if (this.mTextColorEnable) {
                            setTextColorType(ledItemView1, i, i2, this.mTextColorType);
                        } else {
                            ledItemView1.setPaint(iIntValue);
                        }
                    } else {
                        ledItemView1.setChecked(false);
                        if (this.mTextBgEnable) {
                            setTextColorBg(ledItemView1, i, i2, this.mColorBgType);
                        } else {
                            ledItemView1.setPaint(0);
                        }
                    }
                    ledItemView1.postInvalidate();
                }
            } else {
                LogUtil.d("====j");
            }
        }
        if (this.leftMoveList.size() > 0) {
            int iIntValue3 = this.leftMoveList.get(0).intValue();
            this.leftMoveList.remove(0);
            this.leftMoveList.add(Integer.valueOf(iIntValue3));
        }
        if (this.mColorList.size() > 0) {
            int iIntValue4 = this.mColorList.get(0).intValue();
            this.mColorList.remove(0);
            this.mColorList.add(Integer.valueOf(iIntValue4));
        }
    }

    public void setTextRight(byte[] bArr, int[] iArr, boolean z, boolean z2, int i, int i2) {
        LinkedList<Integer> linkedList;
        if (bArr == null || (linkedList = this.mColorListRight) == null) {
            return;
        }
        if (i2 > 0) {
            this.unSelectedColor = 0;
        }
        this.mTextColorEnable = z;
        this.mTextBgEnable = z2;
        this.mTextColorType = i;
        this.mColorBgType = i2;
        this.textData = bArr;
        linkedList.clear();
        for (int length = iArr.length - 1; length >= 0; length--) {
            this.mColorListRight.addFirst(Integer.valueOf(iArr[length]));
        }
        this.textData = bArr;
        if (bArr != null) {
            clearSelected();
            this.rightMoveList.clear();
            cancelTimerTask2();
            Timer timer = this.timer;
            if (timer != null) {
                timer.cancel();
                this.timer.purge();
                this.timer = null;
            }
        }
        int i3 = 0;
        while (true) {
            byte[] bArr2 = this.textData;
            if (i3 >= bArr2.length / 2) {
                break;
            }
            int i4 = i3 * 2;
            this.rightMoveList.add(Integer.valueOf(((bArr2[i4] & UByte.MAX_VALUE) * 64) + ((bArr2[i4 + 1] & UByte.MAX_VALUE) >> 2)));
            i3++;
        }
        for (int i5 = 0; i5 < 36; i5++) {
            this.rightMoveList.addFirst(0);
            this.mColorListRight.addFirst(0);
        }
        this.timer = new Timer();
        MyTimerTask4 myTimerTask4 = new MyTimerTask4();
        this.myTimerTask4 = myTimerTask4;
        this.timer.schedule(myTimerTask4, 100L, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void textRightMove() {
        if (this.rightMoveList == null) {
            return;
        }
        for (int i = 35; i >= 0; i--) {
            if (i < this.rightMoveList.size()) {
                int iIntValue = this.mColorListRight.get(i).intValue();
                for (int i2 = 0; i2 < 12; i2++) {
                    int iIntValue2 = (this.rightMoveList.get(i).intValue() >> (i2 + 2)) & 1;
                    LedItemView1 ledItemView1 = getLedItemView1(i, i2);
                    if (iIntValue2 == 1) {
                        ledItemView1.setChecked(true);
                        if (this.mTextColorEnable) {
                            setTextColorType(ledItemView1, i, i2, this.mTextColorType);
                        } else {
                            ledItemView1.setPaint(iIntValue);
                        }
                    } else {
                        ledItemView1.setChecked(false);
                        if (this.mTextBgEnable) {
                            setTextColorBg(ledItemView1, i, i2, this.mColorBgType);
                        } else {
                            ledItemView1.setPaint(0);
                        }
                    }
                    ledItemView1.postInvalidate();
                }
            } else {
                LogUtil.d("====j");
            }
        }
        if (this.rightMoveList.size() > 0) {
            int size = this.rightMoveList.size() - 1;
            int iIntValue3 = this.rightMoveList.get(size).intValue();
            this.rightMoveList.remove(size);
            this.rightMoveList.addFirst(Integer.valueOf(iIntValue3));
        }
        if (this.mColorListRight.size() > 0) {
            int size2 = this.mColorListRight.size() - 1;
            int iIntValue4 = this.mColorListRight.get(size2).intValue();
            this.mColorListRight.remove(size2);
            this.mColorListRight.addFirst(Integer.valueOf(iIntValue4));
        }
    }

    private LedItemView1 getLedItemView1(int i, int i2) {
        return (LedItemView1) getChildAt((i * 12) + (12 - (i2 + 1)));
    }

    public void setPointMargin(int i) {
        this.pointMargin = i;
    }

    public void setSelectedColor(int i) {
        this.selectedColor3 = i;
    }

    public void setBgColor(int i) {
        this.unSelectedColor = i;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i) {
        this.orientation = i;
    }

    class MyTimerTask2 extends TimerTask {
        MyTimerTask2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Message message = new Message();
            message.what = 2;
            LedTextView.this.handler.sendMessage(message);
        }
    }

    class MyTimerTask4 extends TimerTask {
        MyTimerTask4() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Message message = new Message();
            message.what = 4;
            LedTextView.this.handler.sendMessage(message);
        }
    }

    public void cancelTimerTask() {
        MyTimerTask2 myTimerTask2 = this.myTimerTask2;
        if (myTimerTask2 != null) {
            myTimerTask2.cancel();
            this.handler.removeCallbacks(this.myTimerTask2);
            this.myTimerTask2 = null;
        }
        MyTimerTask4 myTimerTask4 = this.myTimerTask4;
        if (myTimerTask4 != null) {
            myTimerTask4.cancel();
            this.handler.removeCallbacks(this.myTimerTask4);
            this.myTimerTask4 = null;
        }
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer.purge();
            this.timer = null;
        }
    }

    public void cancelTimerTask4() {
        MyTimerTask4 myTimerTask4 = this.myTimerTask4;
        if (myTimerTask4 != null) {
            myTimerTask4.cancel();
            this.handler.removeCallbacks(this.myTimerTask4);
            this.myTimerTask4 = null;
        }
    }

    public void cancelTimerTask2() {
        MyTimerTask2 myTimerTask2 = this.myTimerTask2;
        if (myTimerTask2 != null) {
            myTimerTask2.cancel();
            this.handler.removeCallbacks(this.myTimerTask2);
            this.myTimerTask2 = null;
        }
    }

    private void setTextColorType(LedItemView1 ledItemView1, int i, int i2, int i3) {
        if (i3 == 1) {
            ledItemView1.setPaint(this.textColor1[i]);
            return;
        }
        if (i3 == 2) {
            ledItemView1.setPaint(this.textColor2[i2]);
            return;
        }
        if (i3 == 3) {
            ledItemView1.setPaint(this.textColor3[i]);
        } else if (i3 == 4) {
            ledItemView1.setPaint(this.textColor4[i]);
        } else {
            ledItemView1.setPaint(this.selectedColor3);
        }
    }

    private void setTextColorBg(LedItemView1 ledItemView1, int i, int i2, int i3) {
        if (i3 == 1) {
            ledItemView1.setPaint(this.textColorBg1[i]);
            return;
        }
        if (i3 == 2) {
            ledItemView1.setPaint(this.textColorBg2[i2]);
        } else if (i3 == 3) {
            ledItemView1.setPaint(this.textColorBg3[i]);
        } else {
            ledItemView1.setPaint(this.unSelectedColor);
        }
    }
}
