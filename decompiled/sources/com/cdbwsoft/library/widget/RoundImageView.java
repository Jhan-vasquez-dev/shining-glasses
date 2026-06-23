package com.cdbwsoft.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.cdbwsoft.library.R;

/* JADX INFO: loaded from: classes.dex */
public class RoundImageView extends ImageView {
    private static final float BORDER_DEFAULT = 1.0f;
    private static final int DEFAULT_BORDER_COLOR = -3355444;
    private static final float RECT_DEFAULT = 10.0f;
    private int mBorderColor;
    private final Paint mBorderPaint;
    private int mBorderWidth;
    private final Paint mMaskPaint;
    private float mRadius;
    private final RectF mRoundRect;
    private final Paint mZonePaint;

    public RoundImageView(Context context) {
        super(context);
        this.mBorderPaint = new Paint();
        this.mMaskPaint = new Paint();
        this.mRoundRect = new RectF();
        this.mZonePaint = new Paint();
        init(context, null);
    }

    public RoundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBorderPaint = new Paint();
        this.mMaskPaint = new Paint();
        this.mRoundRect = new RectF();
        this.mZonePaint = new Paint();
        init(context, attributeSet);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        canvas.saveLayer(this.mRoundRect, this.mZonePaint, 31);
        RectF rectF = this.mRoundRect;
        float f = this.mRadius;
        canvas.drawRoundRect(rectF, f, f, this.mZonePaint);
        canvas.saveLayer(this.mRoundRect, this.mMaskPaint, 31);
        super.draw(canvas);
        RectF rectF2 = this.mRoundRect;
        float f2 = this.mRadius;
        canvas.drawRoundRect(rectF2, f2, f2, this.mBorderPaint);
        canvas.restore();
    }

    private void init(Context context, AttributeSet attributeSet) {
        float f = getResources().getDisplayMetrics().density;
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RoundImageView);
            this.mBorderWidth = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.RoundImageView_border_width, (int) (1.0f * f));
            this.mBorderColor = typedArrayObtainStyledAttributes.getColor(R.styleable.RoundImageView_border_color, DEFAULT_BORDER_COLOR);
            this.mRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.RoundImageView_round_radius, (int) (f * RECT_DEFAULT));
            typedArrayObtainStyledAttributes.recycle();
        } else {
            this.mBorderWidth = (int) (1.0f * f);
            this.mBorderColor = DEFAULT_BORDER_COLOR;
            this.mRadius = f * RECT_DEFAULT;
        }
        this.mMaskPaint.setAntiAlias(true);
        this.mMaskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setAntiAlias(true);
        this.mBorderPaint.setColor(this.mBorderColor);
        this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
        this.mZonePaint.setAntiAlias(true);
        this.mZonePaint.setColor(-1);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mRoundRect.set(0.0f, 0.0f, getWidth(), getHeight());
    }

    public void setRectRadius(float f) {
        this.mRadius = f;
        invalidate();
    }
}
