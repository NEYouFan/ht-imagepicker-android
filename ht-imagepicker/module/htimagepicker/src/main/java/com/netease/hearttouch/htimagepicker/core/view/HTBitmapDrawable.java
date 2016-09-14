package com.netease.hearttouch.htimagepicker.core.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by zyl06 on 3/25/16.
 */
public class HTBitmapDrawable extends Drawable {
    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF mRectF;
    private float mRoundRadius = 0;

    public HTBitmapDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    public void setRoundRadius(float radius) {
        this.mRoundRadius = radius;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        mRectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
