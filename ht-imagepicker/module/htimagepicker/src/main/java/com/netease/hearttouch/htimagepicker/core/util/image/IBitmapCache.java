package com.netease.hearttouch.htimagepicker.core.util.image;

import android.graphics.Bitmap;

/**
 * Created by zyl06 on 3/1/16.
 */
public interface IBitmapCache {
    Bitmap get(String key);
    Bitmap put(String key, Bitmap value);
    void close();
    void clear();
}
