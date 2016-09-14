package com.netease.hearttouch.htimagepicker.core.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.netease.hearttouch.htimagepicker.core.util.matrix.MatrixUtil;
import com.netease.hearttouch.htimagepicker.core.util.matrix.OnMatrixChangedListener;
import com.netease.hearttouch.htimagepicker.core.util.RectUtil;

/**
 * Created by zyl06 on 3/24/16.
 */
public class HTTransformImageView extends ImageView {
    private static final String TAG = "HTTransformImageView";
    private static int _4_POINT_COORS = 8;
    private static int _1_POINT_COORS = 2;

    protected float[] mOrigCorners = new float[_4_POINT_COORS];
    protected float[] mOrigCenter = new float[_1_POINT_COORS];

    protected float[] mInitCorners = new float[_4_POINT_COORS];
    protected float[] mCurrCorners = new float[_4_POINT_COORS];
    protected float[] mCurrCenter = new float[_1_POINT_COORS];

    protected float[] mInnerCorners = new float[_4_POINT_COORS];
    protected float[] mInnerCenter = new float[_1_POINT_COORS];

    // 最小缩放比例， 如果为null，则不做限制
    private Float mMinScale = null;

    private GestureDetector mGestureDetector = null;
    private ScaleGestureDetector mScaleGestureDetector = null;

    // 手势触发缩放之后，触发的缩放、平移信息

    /**
     * 滑动的最小距离
     */
    protected float mTouchSlop = 0.f;
    private float mTouchScale = 1.0f;
    private float[] mTouchTranslate = new float[2];
    private boolean mNeedUpdateFromTouch = false;
    private OnMatrixChangedListener mMatrixChangeListener;
    private boolean mIsAnimInTouchupAdjust = true;

    protected HTBitmapDrawable mBitmapDrawable;

    public HTTransformImageView(Context context) {
        this(context, null);
    }

    public HTTransformImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTTransformImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setScaleType(ScaleType.MATRIX);
        mGestureDetector = new GestureDetector(context, new GestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureListener());

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setMatrixChangeListener(OnMatrixChangedListener listener) {
        this.mMatrixChangeListener = listener;
    }

    public float[] getCurrentCorners() {
        return mCurrCorners.clone();
    }

    public float[] getOrigCorners() {
        return mOrigCorners.clone();
    }

    protected void callMatrixChangeListener(Matrix m) {
        if (mMatrixChangeListener != null) {
            mMatrixChangeListener.onMatrixChanged(m);
        }
        m.mapPoints(mCurrCorners, mOrigCorners);
        m.mapPoints(mCurrCenter, mOrigCenter);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm != null && !bm.isRecycled()) {
            mBitmapDrawable = new HTBitmapDrawable(bm);
            super.setImageDrawable(mBitmapDrawable);
            if (getWidth() > 0 && getHeight() > 0) {
                onImageLaidOut();
            }
        }
    }

    @Override
    public void setImageDrawable(Drawable d) {
        throw new UnsupportedOperationException("Please use setImageBitmap(Bitmap bm)!");
//        super.setImageDrawable(d);
    }

    public Bitmap getImageBitmap() {
        return mBitmapDrawable != null ? mBitmapDrawable.getBitmap() : null;
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed && left != right && top != bottom) {
            int innerLeft = getPaddingLeft();
            int innerTop = getPaddingTop();
            int innerRight = getWidth() - getPaddingRight();
            int innerBottom = getHeight() - getPaddingBottom();

            mInnerCorners = RectUtil.newRectCorners(innerLeft, innerTop, innerRight, innerBottom);
            mInnerCenter = RectUtil.newRectCenter(innerLeft, innerTop, innerRight, innerBottom);

            onImageLaidOut();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            resetTouchMatrix();
        }

        boolean gestureResult = mGestureDetector.onTouchEvent(e);
        boolean scaleResult = mScaleGestureDetector.onTouchEvent(e);

        if (mNeedUpdateFromTouch) {
            updateMatrixFromTouch();
        }

        if (e.getAction() == MotionEvent.ACTION_UP ||
                e.getAction() == MotionEvent.ACTION_CANCEL) {
            tryAdjustMatrixTouchUp();
        }

        return gestureResult || scaleResult;
    }

    public void tryAdjustMatrixTouchUp() {
        float[] offsetXY = getAdjustOffsetXY();
        float[] scaleXY = getAdjustScaleXY();
        if (offsetXY == null && scaleXY == null)
            return;

        if (offsetXY == null) {
            offsetXY = new float[]{0.0f, 0.0f};
        }
        if (scaleXY == null) {
            scaleXY = new float[]{1.0f, 1.0f};
        }

        MatrixUtil.makeMatrixChange(getImageMatrix(), offsetXY[0], offsetXY[1],
                scaleXY[0], scaleXY[1],
                new OnMatrixChangedListener() {
                    @Override
                    public void onMatrixChanged(Matrix m) {
                        setImageMatrix(m);

                        callMatrixChangeListener(m);
                        postInvalidate();
                    }
                }, mIsAnimInTouchupAdjust);
    }

    protected float[] getAdjustScaleXY() {
        return null;
    }

    protected float[] getAdjustOffsetXY() {
        return null;
    }

    protected void updateMatrixFromTouch() {
        Matrix matrix = getImageMatrix();

        matrix.reset();
        matrix.postTranslate(-mOrigCenter[0], -mOrigCenter[1]);

        if (mMinScale != null && mTouchScale < mMinScale) {
            mTouchScale = mMinScale;
        }

        matrix.postScale(mTouchScale, mTouchScale);
        matrix.postTranslate(mTouchTranslate[0], mTouchTranslate[1]);
        setImageMatrix(matrix);

        callMatrixChangeListener(matrix);
        postInvalidate();
    }

    public Float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(Float minScale) {
        mMinScale = minScale;
    }

    private void resetTouchMatrix() {
        Matrix m = getImageMatrix();
        mTouchScale = MatrixUtil.getScaleX(m); // 是等比例缩放，所以x和y方向是一样的
        mTouchTranslate[0] = mCurrCenter[0];
        mTouchTranslate[1] = mCurrCenter[1];
        mNeedUpdateFromTouch = false;
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
    }

    public void resetToInit(boolean withAnim) {
        if (RectUtil.getIntRectLeft(mInitCorners) == RectUtil.getIntRectRight(mInitCorners) ||
                RectUtil.getIntRectTop(mInitCorners) == RectUtil.getIntRectBottom(mInitCorners)) {
            Log.i(TAG, "resetToInit failed as mInitCorners is uninitialized");
            return;
        }
        if (RectUtil.equals(mCurrCorners, mInitCorners)) {
            Log.i(TAG, "resetToInit is already init statement");
            return;
        }

        MatrixUtil.makeMatrixChange(getImageMatrix(), mCurrCorners, mInitCorners, new OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(Matrix m) {
                setImageMatrix(m);
                callMatrixChangeListener(m);
                postInvalidate();
            }
        }, withAnim);
    }

    // 顺时针设置顶点坐标
    protected void initCurrentRectCorners(int l, int t, int r, int b) {
        mCurrCorners = RectUtil.newRectCorners(l, t, r, b);
        mInitCorners = RectUtil.newRectCorners(l, t, r, b);
        initCurrentMatrixFromCorners();
    }

    private void initCurrentMatrixFromCorners() {
        Matrix m = getImageMatrix();
        m.reset();
        m.postTranslate(-mOrigCenter[0], -mOrigCenter[1]);

        float scaleX = RectUtil.getRectWidth(mCurrCorners) / RectUtil.getRectWidth(mOrigCorners);
        float scaleY = RectUtil.getRectHeight(mCurrCorners) / RectUtil.getRectHeight(mOrigCorners);
        m.postScale(scaleX, scaleY);

        m.postTranslate(mCurrCenter[0], mCurrCenter[1]);
        setImageMatrix(m);

        postInvalidate();
    }

    protected void translate(Matrix m, float x, float y) {
        if (m == null) {
            m = getImageMatrix();
        }
        m.postTranslate(x, y);
        setImageMatrix(m);

        callMatrixChangeListener(m);
        postInvalidate();
    }

    protected void onImageLaidOut() {
        if (mBitmapDrawable == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }

        int w = mBitmapDrawable.getIntrinsicWidth();
        if (w < 0) w = 0;
        int h = mBitmapDrawable.getIntrinsicHeight();
        if (h < 0) h = 0;

        initInitialRectCorners(0, 0, w, h);
        initInitialCenter(0, 0, w, h);
    }

    // 顺时针设置顶点坐标
    private void initInitialRectCorners(int l, int t, int r, int b) {
        mOrigCorners = RectUtil.newRectCorners(l, t, r, b);
        getImageMatrix().mapPoints(mCurrCorners, mOrigCorners);
        mInitCorners = mCurrCorners.clone();
    }

    private void initInitialCenter(int l, int t, int r, int b) {
        mOrigCenter[0] = (r - l) / 2 + l;
        mOrigCenter[1] = (b - t) / 2 + t;

        getImageMatrix().mapPoints(mCurrCenter, mOrigCenter);
    }

    private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float mBeginScale = 1;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float factor = detector.getScaleFactor();
            mTouchScale = mBeginScale * factor;
            mNeedUpdateFromTouch = true;
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Matrix matrix = getImageMatrix();
            mBeginScale = MatrixUtil.getScaleX(matrix);
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            mTouchTranslate[0] -= distanceX;
            mTouchTranslate[1] -= distanceY;
            mNeedUpdateFromTouch = true;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            resetToInit(true);
            return false;
        }
    }
}
