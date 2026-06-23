package me.leefeng.promptlibrary;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes.dex */
class PromptView extends ImageView {
    public static final int CUSTOMER_LOADING = 110;
    public static final int PROMPT_AD = 109;
    public static final int PROMPT_ALERT_WARN = 107;
    public static final int PROMPT_CUSTOM = 108;
    public static final int PROMPT_ERROR = 103;
    public static final int PROMPT_INFO = 105;
    public static final int PROMPT_LOADING = 102;
    public static final int PROMPT_NONE = 104;
    public static final int PROMPT_SUCCESS = 101;
    public static final int PROMPT_WARN = 106;
    private static final String TAG = "LOADVIEW";
    private Bitmap adBitmap;
    private ValueAnimator animator;
    private float bottomHeight;
    private Builder builder;
    float buttonH;
    float buttonW;
    private PromptButton[] buttons;
    private int canvasHeight;
    private int canvasWidth;
    private int currentType;
    private float density;
    private Drawable drawableClose;
    private int height;
    private boolean isSheet;
    private Matrix max;
    private Paint paint;
    private PromptDialog promptDialog;
    private RectF roundRect;
    private RectF roundTouchRect;
    private float sheetHeight;
    private Rect textRect;
    private int transX;
    private int transY;
    private int width;

    public PromptView(Context context) {
        super(context);
        this.buttons = new PromptButton[0];
    }

    public PromptView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.buttons = new PromptButton[0];
    }

    public PromptView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.buttons = new PromptButton[0];
    }

    public PromptView(Activity activity, Builder builder, PromptDialog promptDialog) {
        super(activity);
        this.buttons = new PromptButton[0];
        this.density = getResources().getDisplayMetrics().density;
        this.builder = builder;
        this.promptDialog = promptDialog;
    }

    private Bitmap createRoundConerImage(Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight()), 50.0f, 50.0f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return bitmapCreateBitmap;
    }

    /* JADX WARN: Removed duplicated region for block: B:128:0x09df A[PHI: r1
      0x09df: PHI (r1v9 android.graphics.Canvas) = (r1v8 android.graphics.Canvas), (r1v13 android.graphics.Canvas) binds: [B:103:0x07d9, B:113:0x08c5] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // android.widget.ImageView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onDraw(android.graphics.Canvas r35) {
        /*
            Method dump skipped, instruction units count: 2540
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: me.leefeng.promptlibrary.PromptView.onDraw(android.graphics.Canvas):void");
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setScaleType(ImageView.ScaleType.MATRIX);
        if (this.paint == null) {
            this.paint = new Paint();
        }
        initData();
    }

    private void initData() {
        if (this.textRect == null) {
            this.textRect = new Rect();
        }
        if (this.roundRect == null) {
            this.roundTouchRect = new RectF();
        }
        float f = this.density;
        this.buttonW = 120.0f * f;
        this.buttonH = f * 44.0f;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Bitmap bitmap = this.adBitmap;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.adBitmap = null;
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.animator = null;
        this.buttons = null;
        this.promptDialog.onDetach();
        this.currentType = 104;
    }

    private void start() {
        if (this.max == null || this.animator == null) {
            this.max = new Matrix();
            ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(0, 12);
            this.animator = valueAnimatorOfInt;
            valueAnimatorOfInt.setDuration(960L);
            this.animator.setInterpolator(new LinearInterpolator());
            this.animator.setRepeatCount(-1);
            this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: me.leefeng.promptlibrary.PromptView.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PromptView.this.max.setRotate(((Integer) valueAnimator.getAnimatedValue()).intValue() * 30, PromptView.this.width, PromptView.this.height);
                    PromptView promptView = PromptView.this;
                    promptView.setImageMatrix(promptView.max);
                }
            });
        }
        if (this.animator.isRunning()) {
            return;
        }
        this.animator.start();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int i = this.currentType;
        if (i == 107) {
            if (this.builder.cancleAble && motionEvent.getAction() == 1 && !this.roundTouchRect.contains(x, y)) {
                this.promptDialog.dismiss();
            }
            for (final PromptButton promptButton : this.buttons) {
                if (promptButton.getRect() != null && promptButton.getRect().contains(x, y)) {
                    if (motionEvent.getAction() == 0) {
                        promptButton.setFocus(true);
                        invalidate();
                    }
                    if (motionEvent.getAction() == 1) {
                        promptButton.setFocus(false);
                        invalidate();
                        if (promptButton.isDismissAfterClick()) {
                            this.promptDialog.dismiss();
                        }
                        if (promptButton.getListener() != null) {
                            if (promptButton.isDelyClick()) {
                                postDelayed(new Runnable() { // from class: me.leefeng.promptlibrary.PromptView.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        promptButton.getListener().onClick(promptButton);
                                    }
                                }, PromptDialog.viewAnimDuration + 100);
                            } else {
                                promptButton.getListener().onClick(promptButton);
                            }
                        }
                    }
                    return true;
                }
            }
            if (motionEvent.getAction() == 1) {
                for (PromptButton promptButton2 : this.buttons) {
                    promptButton2.setFocus(false);
                    invalidate();
                }
            }
        } else if (i == 109 && motionEvent.getAction() == 1) {
            Drawable drawable = this.drawableClose;
            if ((drawable != null && drawable.getBounds().contains(((int) motionEvent.getX()) - this.transX, ((int) motionEvent.getY()) - this.transY)) || this.builder.cancleAble) {
                this.promptDialog.dismiss();
            } else if (getDrawable() != null && getDrawable().getBounds().contains(((int) motionEvent.getX()) - this.transX, ((int) motionEvent.getY()) - this.transY)) {
                this.promptDialog.onAdClick();
                this.promptDialog.dismiss();
            }
        }
        return !this.builder.touchAble;
    }

    private void endAnimator() {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            return;
        }
        this.animator.end();
    }

    public void showLoading() {
        if (this.currentType == 107) {
            this.isSheet = this.buttons.length > 2;
        } else {
            this.isSheet = false;
        }
        setImageDrawable(getResources().getDrawable(this.builder.icon));
        this.width = getDrawable().getMinimumWidth() / 2;
        this.height = getDrawable().getMinimumHeight() / 2;
        start();
        this.currentType = 102;
    }

    Builder getBuilder() {
        return this.builder;
    }

    public void showSomthing(int i) {
        this.currentType = i;
        if (i == 107) {
            this.isSheet = this.buttons.length > 2;
        } else {
            this.isSheet = false;
        }
        endAnimator();
        setImageDrawable(getResources().getDrawable(this.builder.icon));
        this.width = getDrawable().getMinimumWidth() / 2;
        int minimumHeight = getDrawable().getMinimumHeight() / 2;
        this.height = minimumHeight;
        Matrix matrix = this.max;
        if (matrix != null) {
            matrix.setRotate(0.0f, this.width, minimumHeight);
            setImageMatrix(this.max);
        }
        if (this.isSheet) {
            this.sheetHeight = ((this.builder.sheetCellPad * 1.5f) + (this.builder.sheetCellHeight * this.buttons.length)) * this.density;
            Log.i(TAG, "showSomthing: " + this.sheetHeight);
            startBottomToTopAnim();
        }
        invalidate();
    }

    private void startBottomToTopAnim() {
        ValueAnimator valueAnimatorOfFloat = ObjectAnimator.ofFloat(0.0f, 1.0f);
        valueAnimatorOfFloat.setDuration(300L);
        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: me.leefeng.promptlibrary.PromptView.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float f = (Float) valueAnimator.getAnimatedValue();
                PromptView promptView = PromptView.this;
                promptView.bottomHeight = promptView.sheetHeight * f.floatValue();
                Log.i(PromptView.TAG, "onAnimationUpdate: " + PromptView.this.bottomHeight);
                PromptView.this.invalidate();
            }
        });
        valueAnimatorOfFloat.start();
    }

    void showSomthingAlert(PromptButton... promptButtonArr) {
        this.buttons = promptButtonArr;
        showSomthing(107);
    }

    public void setBuilder(Builder builder) {
        if (this.builder != builder) {
            this.builder = builder;
        }
    }

    public int getCurrentType() {
        return this.currentType;
    }

    public void setText(String str) {
        this.builder.text(str);
        invalidate();
    }

    public void dismiss() {
        if (this.isSheet) {
            ValueAnimator valueAnimatorOfFloat = ObjectAnimator.ofFloat(1.0f, 0.0f);
            valueAnimatorOfFloat.setDuration(300L);
            valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: me.leefeng.promptlibrary.PromptView.4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Float f = (Float) valueAnimator.getAnimatedValue();
                    PromptView promptView = PromptView.this;
                    promptView.bottomHeight = promptView.sheetHeight * f.floatValue();
                    PromptView.this.invalidate();
                }
            });
            valueAnimatorOfFloat.start();
        }
    }

    public void showAd() {
        this.currentType = 109;
        endAnimator();
    }

    public void showCustomLoading() {
        if (this.currentType == 107) {
            this.isSheet = this.buttons.length > 2;
        } else {
            this.isSheet = false;
        }
        setImageDrawable(getResources().getDrawable(this.builder.icon));
        this.width = getDrawable().getMinimumWidth() / 2;
        this.height = getDrawable().getMinimumHeight() / 2;
        ((AnimationDrawable) getDrawable()).start();
        this.currentType = 110;
    }

    public void stopCustomerLoading() {
        ((AnimationDrawable) getDrawable()).stop();
    }
}
