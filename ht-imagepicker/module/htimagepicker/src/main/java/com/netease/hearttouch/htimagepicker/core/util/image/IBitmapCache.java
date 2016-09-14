/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

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
