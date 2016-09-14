/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.util.matrix;

import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.view.animation.DecelerateInterpolator;

import com.netease.hearttouch.htimagepicker.core.util.RectUtil;

/**
 * Created by zyl06 on 3/24/16.
 */
public class MatrixUtil {
    private static float[] values = new float[9];

    public static synchronized float getScaleX(Matrix matrix) {
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    public static synchronized float getScaleY(Matrix matrix) {
        matrix.getValues(values);
        return values[Matrix.MSCALE_Y];
    }

    public static synchronized void getScaleXY(Matrix matrix, float[] xy) {
        if (xy == null) return;
        matrix.getValues(values);
        xy[0] = values[Matrix.MSCALE_X];
        xy[1] = values[Matrix.MSCALE_Y];
    }

    public static synchronized float getTransX(Matrix matrix) {
        matrix.getValues(values);
        return values[Matrix.MTRANS_X];
    }

    public static synchronized float getTransY(Matrix matrix) {
        matrix.getValues(values);
        return values[Matrix.MTRANS_Y];
    }

    public static synchronized void getTransXY(Matrix matrix, float[] xy) {
        if (xy == null) return;
        matrix.getValues(values);
        xy[0] = values[Matrix.MTRANS_X];
        xy[1] = values[Matrix.MTRANS_Y];
    }

    public static void makeMatrixChange(final Matrix m, final float x, final float y,
                                        final OnMatrixChangedListener listener, boolean withAnim) {
        makeMatrixChange(m, x, y, 1, 1, listener, withAnim);
    }

    public static void makeMatrixChange(final Matrix m, final float offsetX, final float offsetY,
                                        final float scaleX, final float scaleY,
                                        final OnMatrixChangedListener listener, boolean withAnim) {
        final Matrix origMatrix = new Matrix(m);
        if (withAnim) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    m.set(origMatrix);
                    m.postScale(1 + fraction * (scaleX - 1), 1 + fraction * (scaleY - 1));
                    m.postTranslate(fraction * offsetX, fraction * offsetY);
                    if (listener != null) {
                        listener.onMatrixChanged(m);
                    }
                }
            });
            animator.setDuration(100);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else {
            m.postScale(scaleX, scaleY);
            m.postTranslate(offsetX, offsetY);
            if (listener != null) {
                listener.onMatrixChanged(m);
            }
        }
    }

    // corners的坐标都是从左上角开始顺时针的
    // corners之间只有缩放和平移。没有旋转和扭转。
    public static void makeMatrixChange(final Matrix m, final float[] srcCorners, final float[] desCorners,
                                        final OnMatrixChangedListener listener, boolean isAnim) {
        final float scaleW = RectUtil.getRectWidth(desCorners) / RectUtil.getRectWidth(srcCorners);
        final float scaleH = RectUtil.getRectHeight(desCorners) / RectUtil.getRectHeight(srcCorners);
        Matrix scaleM = new Matrix();
        scaleM.setScale(scaleW, scaleH);

        float[] scaleDesCorners = new float[8];
        scaleM.mapPoints(scaleDesCorners, srcCorners);

        final float x = RectUtil.getRectLeft(desCorners) - RectUtil.getRectLeft(scaleDesCorners);
        final float y = RectUtil.getRectTop(desCorners) - RectUtil.getRectTop(scaleDesCorners);

        final Matrix srcMatrix = new Matrix(m);

        if (isAnim) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    m.set(srcMatrix);
                    m.postScale(fraction * (scaleW - 1) + 1, fraction * (scaleH - 1) + 1);
                    m.postTranslate(fraction * x, fraction * y);
                    if (listener != null) {
                        listener.onMatrixChanged(m);
                    }
                }
            });
            animator.setDuration(100);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else {
            m.set(srcMatrix);
            m.postScale(scaleW, scaleH);
            m.postTranslate(x, y);
            if (listener != null) {
                listener.onMatrixChanged(m);
            }
        }
    }
}
