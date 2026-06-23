package com.icwork.shiningglass.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.security.MessageDigest;

/* JADX INFO: loaded from: classes.dex */
public class RotateTransformation extends BitmapTransformation {
    private float rotateRotationAngle = 0.0f;

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }

    @Override // com.bumptech.glide.load.resource.bitmap.BitmapTransformation
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i2) {
        Matrix matrix = new Matrix();
        matrix.postRotate(this.rotateRotationAngle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
