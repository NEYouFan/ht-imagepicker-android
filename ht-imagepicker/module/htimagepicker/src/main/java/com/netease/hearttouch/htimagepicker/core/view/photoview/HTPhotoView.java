/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.view.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.netease.hearttouch.htimagepicker.core.util.RectUtil;
import com.netease.hearttouch.htimagepicker.core.view.HTTransformImageView;

/**
 * Created by zyl06 on 3/30/16.
 */
public class HTPhotoView extends HTTransformImageView {
    private Rect mBoundRect;
    private float mBoundScale = 1.0f;
    private Boolean mIsGrandParentViewPager = null;

    public HTPhotoView(Context context) {
        this(context, null);
    }

    public HTPhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTPhotoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onImageLaidOut() {
        super.onImageLaidOut();
        initImageCurrentCoords();
    }

    public void setImageBitmap(Bitmap b) {
        Matrix m = getImageMatrix();
        m.reset();
        setImageMatrix(m);
        super.setImageBitmap(b);
    }

    @Override
    protected float[] getAdjustScaleXY() {
        if (!checkCurrentRectCorners(mAdjustOffsetXY, mAdjustScaleXY)) {
            return mAdjustScaleXY;
        }
        return null;
    }

    @Override
    protected float[] getAdjustOffsetXY() {
        if (!checkCurrentRectCorners(mAdjustOffsetXY, mAdjustScaleXY)) {
            return mAdjustOffsetXY;
        }
        return null;
    }

    private float[] mAdjustOffsetXY = new float[2];
    private float[] mAdjustScaleXY = new float[2];

    private boolean checkCurrentRectCorners(float[] offset, float[] scale) {
        boolean result = true;

        float cw = RectUtil.getRectWidth(mCurrCorners);
        float ch = RectUtil.getRectHeight(mCurrCorners);
        float bw = mBoundRect.width();
        float bh = mBoundRect.height();

        float minScale = Math.min(bw / cw, bh / ch);
        if (minScale <= 1) {
            minScale = 1;
        } else {
            result = false;
        }
        scale[0] = scale[1] = minScale;

        float cl = scale[0] * RectUtil.getRectLeft(mCurrCorners);
        float ct = scale[1] * RectUtil.getRectTop(mCurrCorners);
        float cr = scale[0] * RectUtil.getRectRight(mCurrCorners);
        float cb = scale[1] * RectUtil.getRectBottom(mCurrCorners);

        float ccx = (cl + cr) / 2.0f;
        float ccy = (ct + cb) / 2.0f;

        // 处理水平方向
        offset[0] = offset[1] = 0;
        if (cr - cl <= mBoundRect.width()) {
            offset[0] = mBoundRect.centerX() - ccx;
            if ((int) Math.abs(offset[0]) > 0) {
                result = false;
            }
        } else if (cr < mBoundRect.right) {
            result = false;
            offset[0] = mBoundRect.right - cr;
        } else if (cl > mBoundRect.left) {
            result = false;
            offset[0] = mBoundRect.left - cl;
        }

        // 处理垂直方向
        if (cb - ct <= mBoundRect.height()) {
            offset[1] = mBoundRect.centerY() - ccy;
            if ((int) Math.abs(offset[1]) > 0) {
                result = false;
            }
        } else if (ct > mBoundRect.top) {
            result = false;
            offset[1] = mBoundRect.top - ct;
        } else if (cb < mBoundRect.bottom) {
            result = false;
            offset[1] = mBoundRect.bottom - cb;
        }

        return result;
    }

    private void initImageCurrentCoords() {
        if (mBitmapDrawable == null || getWidth() == 0 || getHeight() == 0)
            return;

        initBoundRect();
        initBoundScale();

        int bitmapWidth = mBitmapDrawable.getIntrinsicWidth();
        int bitmapHeight = mBitmapDrawable.getIntrinsicHeight();
        float bmWHRatio = bitmapWidth / (float) bitmapHeight;

        float innerWidth = RectUtil.getRectWidth(mInnerCorners);
        float innerHeight = RectUtil.getRectHeight(mInnerCorners);
        float innerCenterX = RectUtil.getRectLeft(mInnerCorners) + innerWidth / 2;
        float innerCenterY = RectUtil.getRectTop(mInnerCorners) + innerHeight / 2;

        Rect currRect;
        float innerRatio = innerWidth / innerHeight;
        if (bmWHRatio > innerRatio) {
            float tmpHeight = innerWidth / bmWHRatio;
            currRect = RectUtil.newRect((int) innerCenterX, (int) innerCenterY, (int) innerWidth, (int) tmpHeight);
        } else {
            float tmpWidth = innerHeight * bmWHRatio;
            currRect = RectUtil.newRect((int) innerCenterX, (int) innerCenterY, (int) tmpWidth, (int) innerHeight);
        }

        mCurrCenter = RectUtil.getRectCenter(currRect);
        initCurrentRectCorners(currRect.left, currRect.top, currRect.right, currRect.bottom);
    }

    private void initBoundRect() {
        int l = getPaddingLeft();
        int t = getPaddingTop();
        int r = getWidth() - getPaddingRight();
        int b = getHeight() - getPaddingBottom();

        mBoundRect = new Rect(l, t, r, b);
    }

    private void initBoundScale() {
        float scaleX = mBoundRect.width() / RectUtil.getRectWidth(mOrigCorners);
        float scaleY = mBoundRect.height() / RectUtil.getRectHeight(mOrigCorners);
        mBoundScale = Math.min(scaleX, scaleY);
        setMinScale(mBoundScale * 0.8f);
    }

    private float[] mMotionDownLocation = new float[2];
    private float[] mOldMotionLocation = new float[2];
    private Boolean mIsDisallowGrandParentIntercept = null;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean result = false;
        if (mIsGrandParentViewPager == null) {
            mIsGrandParentViewPager = (getParent().getParent() instanceof PhotoViewPager);
        }
        if (mIsGrandParentViewPager) {
            result = onTouchEventInViewPager(e);
        } else {
            result = super.onTouchEvent(e);
        }

        return result;
    }

    private boolean onTouchEventInViewPager(MotionEvent e) {
        boolean result = false;

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mOldMotionLocation[0] = e.getX();
            mOldMotionLocation[1] = e.getY();
            mMotionDownLocation[0] = e.getX();
            mMotionDownLocation[1] = e.getY();

            mIsDisallowGrandParentIntercept = null;
            result = super.onTouchEvent(e);
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (mIsDisallowGrandParentIntercept == null) {
                float distanceX = e.getX() - mOldMotionLocation[0];
                float distanceY = e.getY() - mOldMotionLocation[1];
                mOldMotionLocation[0] = e.getX();
                mOldMotionLocation[1] = e.getY();

                // 这里的 + - 1 是为了能增量冗余
                boolean isOutOfBounds = (distanceX >= 0 && RectUtil.getRectLeft(mCurrCorners) + 1 >= mBoundRect.left) ||
                        (distanceX <= 0 && RectUtil.getRectRight(mCurrCorners) - 1 <= mBoundRect.right);
                if (e.getPointerCount() == 1 && isOutOfBounds) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    mIsDisallowGrandParentIntercept = false;
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mIsDisallowGrandParentIntercept = true;
                }
            }
            if (mIsDisallowGrandParentIntercept) {
                result = super.onTouchEvent(e);
            }
        } else {
            result = super.onTouchEvent(e);
        }

        return result;
    }
}
