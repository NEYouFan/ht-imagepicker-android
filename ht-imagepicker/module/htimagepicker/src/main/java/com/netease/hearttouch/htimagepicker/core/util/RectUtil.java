package com.netease.hearttouch.htimagepicker.core.util;

import android.graphics.Rect;

/**
 * Created by zyl06 on 3/25/16.
 */
public class RectUtil {
    // 顺时针设置顶点坐标
    public static float[] newRectCorners(Rect r) {
        return new float[]{
                r.left, r.top,
                r.right, r.top,
                r.right, r.bottom,
                r.left, r.bottom
        };
    }

    // 顺时针设置顶点坐标
    public static float[] newRectCorners(int l, int t, int r, int b) {
        return new float[]{
                l, t,
                r, t,
                r, b,
                l, b
        };
    }

    public static Rect newRect(int centerX, int centerY, int width, int height) {
        int l = centerX - width / 2;
        int t = centerY - height / 2;
        int r = centerX + width / 2;
        int b = centerY + height / 2;
        return new Rect(l, t, r, b);
    }

    // 顺时针设置顶点坐标
    public static float[] newRectCenter(int l, int t, int r, int b) {
        return new float[]{(l + r) / 2, (t + b) / 2};
    }

    public static float getRectLeft(float[] corners) {
        return corners[0];
    }

    public static float getRectTop(float[] corners) {
        return corners[1];
    }

    public static float getRectRight(float[] corners) {
        return corners[4];
    }

    public static float getRectBottom(float[] corners) {
        return corners[5];
    }

    public static float getRectWidth(float[] corners) {
        return corners[2] - corners[0];
    }

    public static float getRectHeight(float[] corners) {
        return corners[5] - corners[1];
    }

    public static float[] getRectCenter(Rect rect) {
        float x = (rect.left + rect.right) / 2;
        float y = (rect.top + rect.bottom) / 2;
        return new float[]{x, y};
    }

    public static int getIntRectLeft(float[] corners) {
        return (int) corners[0];
    }

    public static int getIntRectTop(float[] corners) {
        return (int) corners[1];
    }

    public static int getIntRectRight(float[] corners) {
        return (int) corners[4];
    }

    public static int getIntRectBottom(float[] corners) {
        return (int) corners[5];
    }

    public static boolean equals(float[] corners0, float[] corners1) {
        return getRectLeft(corners0) == getRectLeft(corners1) &&
                getRectRight(corners0) == getRectRight(corners1) &&
                getRectTop(corners0) == getRectTop(corners1) &&
                getRectBottom(corners0) == getRectBottom(corners1);
    }
}
