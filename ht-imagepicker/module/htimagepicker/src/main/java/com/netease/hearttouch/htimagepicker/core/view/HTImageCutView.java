package com.netease.hearttouch.htimagepicker.core.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.util.RectUtil;
import com.netease.hearttouch.htimagepicker.core.util.image.BitmapDecoder;
import com.netease.hearttouch.htimagepicker.core.util.matrix.MatrixUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by zyl06 on 3/22/16.
 */
public class HTImageCutView extends HTTransformImageView {
    private Rect mOutputRect;
    private float mCutWHRatio;
    private static float mMarginRatio = 0.8f;
    private Paint mShadowPaint;
    private Paint mLinePaint;
    private int mSampleSize = 1;

    private Rect mMinRect;

    public HTImageCutView(Context context) {
        this(context, null);
    }

    public HTImageCutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTImageCutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSelf();
    }

    private void initSelf() {
        mOutputRect = new Rect();
        mShadowPaint = new Paint();
        mShadowPaint.setARGB(153, 0, 0, 0);

        mLinePaint = new Paint();
        mLinePaint.setARGB(204, 124, 124, 124);
    }

    public void setMinRect(Rect minRect) {
        if (minRect != null) {
            this.mMinRect = minRect;
        }
    }

    public void setCutWidthHeightRatio(float cutRatio) {
        this.mCutWHRatio = cutRatio;
        updateOutputRect(getLeft(), getTop(), getRight(), getBottom());
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            updateOutputRect(left + getPaddingLeft(), top + getPaddingTop(),
                    right - getPaddingRight(), bottom - getPaddingBottom());
        }
    }

    @Override
    protected void onImageLaidOut() {
        super.onImageLaidOut();
        initImageCurrentCoords();
    }

    private void initMinRect() {
        int l = getPaddingLeft();
        int t = getPaddingTop();
        int r = getWidth() - getPaddingRight();
        int b = getHeight() - getPaddingBottom();

        if (mMinRect == null) {
            mMinRect = new Rect(l, t, r, b);
        }
        if (mMinRect.left < l)
            mMinRect.left = l;
        if (mMinRect.top < t)
            mMinRect.top = t;
        if (mMinRect.right > r)
            mMinRect.right = r;
        if (mMinRect.bottom > b)
            mMinRect.bottom = b;

        // make center
        float[] center = new float[]{l + (r - l) / 2, t + (b - t) / 2};
        float[] minRectCenter = new float[]{mMinRect.left + mMinRect.width() / 2, mMinRect.top + mMinRect.height() / 2};
        float offsetX = center[0] - minRectCenter[0];
        float offsetY = center[1] - minRectCenter[1];
        mMinRect.left += offsetX;
        mMinRect.right += offsetX;
        mMinRect.top += offsetY;
        mMinRect.bottom += offsetY;
    }

    private void initMinScale() {
        float scaleX = mMinRect.width() / RectUtil.getRectWidth(mOrigCorners);
        float scaleY = mMinRect.height() / RectUtil.getRectHeight(mOrigCorners);
        setMinScale(Math.max(scaleX, scaleY));
    }

    private void initImageCurrentCoords() {
        if (mBitmapDrawable == null)
            return;

        initMinRect();
        initMinScale();

        int bitmapWidth = mBitmapDrawable.getIntrinsicWidth();
        int bitmapHeight = mBitmapDrawable.getIntrinsicHeight();
        float bmWHRatio = bitmapWidth / (float) bitmapHeight;

        Rect currRect = mMinRect;
        if (bitmapWidth > mMinRect.width() && bitmapHeight > mMinRect.height()) {
            currRect = new Rect(getPaddingLeft(), getPaddingTop(),
                    getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());

            float minCenterX = mMinRect.left + mMinRect.width() / 2;
            float minCenterY = mMinRect.top + mMinRect.height() / 2;

            float innerWidth = RectUtil.getRectWidth(mInnerCorners);
            float innerHeight = RectUtil.getRectHeight(mInnerCorners);

            float innerRatio = innerWidth / innerHeight;
            if (bmWHRatio > innerRatio) {
                float tmpHeight = innerWidth / bmWHRatio;
                if (tmpHeight > mMinRect.height()) {
                    currRect = RectUtil.newRect((int) minCenterX, (int) minCenterY, (int) innerWidth, (int) tmpHeight);
                }
            } else {
                float tmpWidth = innerHeight * bmWHRatio;
                if (tmpWidth > mMinRect.width()) {
                    currRect = RectUtil.newRect((int) minCenterX, (int) minCenterY, (int) tmpWidth, (int) innerHeight);
                }
            }
        }

        mCurrCenter[0] = currRect.left + currRect.width() / 2;
        mCurrCenter[1] = currRect.top + currRect.height() / 2;

        float currWidth, currHeight;
        float whRatio = 1.0f * currRect.width() / currRect.height();
        if (whRatio > bmWHRatio) {
            currWidth = currRect.width();
            currHeight = currWidth / bmWHRatio;
        } else {
            currHeight = currRect.height();
            currWidth = currHeight * bmWHRatio;
        }

        int left = (int) (mCurrCenter[0] - currWidth / 2);
        int top = (int) (mCurrCenter[1] - currHeight / 2);
        int right = (int) (mCurrCenter[0] + currWidth / 2);
        int bottom = (int) (mCurrCenter[1] + currHeight / 2);
        initCurrentRectCorners(left, top, right, bottom);
    }

    @Override
    protected float[] getAdjustScaleXY() {
        return null;
    }

    @Override
    protected float[] getAdjustOffsetXY() {
        if (!checkCurrentRectCorners(mAdjustXY)) {
            return mAdjustXY;
        }
        return null;
    }

    private float[] mAdjustXY = new float[2];
    private boolean checkCurrentRectCorners(float[] offset) {
        boolean result = true;

        float l = RectUtil.getRectLeft(mCurrCorners);
        float t = RectUtil.getRectTop(mCurrCorners);
        float r = RectUtil.getRectRight(mCurrCorners);
        float b = RectUtil.getRectBottom(mCurrCorners);

        offset[0] = offset[1] = 0;
        if (l > mMinRect.left) {
            result = false;
            offset[0] = mMinRect.left - l;
        }
        if (r < mMinRect.right) {
            result = false;
            offset[0] = mMinRect.right - r;
        }
        if (t > mMinRect.top) {
            result = false;
            offset[1] = mMinRect.top - t;
        }
        if (b < mMinRect.bottom) {
            result = false;
            offset[1] = mMinRect.bottom - b;
        }

        return result;
    }

    private void updateOutputRect(int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        if (width == 0 || height == 0) {
            return;
        }

        float ratio = width / height;

        int desWidth = width;
        int desHeight = height;
        if (mCutWHRatio > ratio) {
            desWidth = (int) (width * mMarginRatio);
            desHeight = (int) (desWidth / mCutWHRatio);
        } else {
            desHeight = (int) (height * mMarginRatio);
            desWidth = (int) (desHeight * mCutWHRatio);
        }

        int desLeft = (width - desWidth) / 2;
        int desTop = (height - desHeight) / 2;

        mOutputRect.left = desLeft;
        mOutputRect.top = desTop;
        mOutputRect.right = desLeft + desWidth;
        mOutputRect.bottom = desTop + desHeight;

        setMinRect(mOutputRect);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw line
        canvas.drawLine(mOutputRect.left, mOutputRect.top, mOutputRect.right, mOutputRect.top, mLinePaint);
        canvas.drawLine(mOutputRect.right, mOutputRect.top, mOutputRect.right, mOutputRect.bottom, mLinePaint);
        canvas.drawLine(mOutputRect.right, mOutputRect.bottom, mOutputRect.left, mOutputRect.bottom, mLinePaint);
        canvas.drawLine(mOutputRect.left, mOutputRect.bottom, mOutputRect.left, mOutputRect.top, mLinePaint);

        // draw rect
        canvas.drawRect(0, 0, getRight(), mOutputRect.top, mShadowPaint);
        canvas.drawRect(0, mOutputRect.top, mOutputRect.left, mOutputRect.bottom, mShadowPaint);
        canvas.drawRect(0, mOutputRect.bottom, getRight(), getBottom(), mShadowPaint);
        canvas.drawRect(mOutputRect.right, mOutputRect.top, getRight(), mOutputRect.bottom, mShadowPaint);
    }

    public void setBitmapFilePath(String filePath) {
        mSampleSize = (int) (ContextUtil.INSTANCE.getScreenWidthPixel() * 0.8f);
        Bitmap src = BitmapDecoder.decodeFile(filePath,
                mSampleSize,
                mSampleSize,
                true);
        setImageBitmap(src);
    }

    public Bitmap getCuttedBitmap() {
        Bitmap bitmap = getImageBitmap();
        if (bitmap == null || bitmap.isRecycled())
            return null;

        float[] currCorners = getCurrentCorners();

        // 检查并矫正输出rect
        int l = mOutputRect.left - (int) RectUtil.getRectLeft(currCorners);
        int t = mOutputRect.top - (int) RectUtil.getRectTop(currCorners);
        int r = l + mOutputRect.width();
        int b = t + mOutputRect.height();
        if (b > RectUtil.getRectHeight(currCorners))
            b = (int) RectUtil.getRectHeight(currCorners);

        if (l == 0 && t == 0 && r == RectUtil.getRectWidth(currCorners) &&
                b == RectUtil.getRectHeight(currCorners)) {
            return bitmap;
        }

        float[] scaleXY = new float[2];
        MatrixUtil.getScaleXY(getImageMatrix(), scaleXY);
        if (scaleXY[0] <= 0 || scaleXY[1] <= 0)
            return null;

        l /= scaleXY[0];
        t /= scaleXY[1];
        r /= scaleXY[0];
        b /= scaleXY[1];

        float[] origCorners = getOrigCorners();

        if (l < 0) l = 0;
        if (t < 0) t = 0;
        if (r > RectUtil.getRectWidth(origCorners))
            r = (int) RectUtil.getRectWidth(origCorners);
        if (b > RectUtil.getRectHeight(origCorners))
            b = (int) RectUtil.getRectHeight(origCorners);
        return Bitmap.createBitmap(bitmap, l, t, r - l, b - t);
    }

    public byte[] getCuttedBitmapData() {
        Bitmap cutted = getCuttedBitmap();
        if (cutted == null) {
            return null;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        cutted.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        if (cutted != getImageBitmap()) {
            cutted.recycle();
        }
        byte[] data = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
