/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.util.image;

import android.graphics.Bitmap;
import android.text.TextUtils;

/**
 * Created by zyl06 on 3/1/16.
 */
public class BitmapCache implements IBitmapCache {
    private static final String TAG = "HT_BitmapCache";
    private static BitmapMemoryCache memoryCache;
    private static BitmapDiskCache diskCache;

    private static BitmapCache sDefault = null;

    public static BitmapCache getDefault() {
        if (sDefault == null || BitmapCache.diskCache == null || BitmapCache.memoryCache == null) {
            synchronized (BitmapCache.class) {
                if (sDefault == null || BitmapCache.diskCache == null || BitmapCache.memoryCache == null) {
                    sDefault = new BitmapCache();
                }
            }
        }
        return sDefault;
    }

    private BitmapCache() {
        if (memoryCache == null) {
            synchronized (BitmapCache.class) {
                if (memoryCache == null) {
                    memoryCache = new BitmapMemoryCache();
                }
            }
        }
        if (diskCache == null || diskCache.isClosed()) {
            synchronized (BitmapCache.class) {
                if (diskCache == null || diskCache.isClosed()) {
                    diskCache = new BitmapDiskCache("image_cache");
                }
            }
        }
    }

    @Override
    public Bitmap get(String key) {
        if (!TextUtils.isEmpty(key)) {
            Bitmap bm = getFromMemory(key);
            if (bm == null) {
                bm = getFromDisk(key);
                putToMemory(key, bm);
            }
            return bm;
        }
        return null;
    }

    @Override
    public Bitmap put(String key, Bitmap value) {
        Bitmap bm = putToMemory(key, value);
        putToDisk(key, value);
        return bm;
    }

    public synchronized Bitmap getFromMemory(String key) {
        return (memoryCache != null) ? memoryCache.get(key) : null;
    }

    public synchronized Bitmap putToMemory(String key, Bitmap value) {
        if (!TextUtils.isEmpty(key) && value != null && !value.isRecycled()) {
            if (memoryCache != null) {
                return memoryCache.put(key, value);
            }
        }
        return null;
    }

    public synchronized Bitmap getFromDisk(String key) {
        return (diskCache != null && !diskCache.isClosed()) ?
                diskCache.get(key) :
                null;
    }

    public synchronized Bitmap putToDisk(String key, Bitmap value) {
        if (!TextUtils.isEmpty(key) && value != null && !value.isRecycled()) {
            if (diskCache != null && !diskCache.isClosed()) {
                return diskCache.put(key, value);
            }
        }
        return null;
    }

    @Override
    public synchronized void close() {
        memoryCache.close();
        if (!diskCache.isClosed())
            diskCache.close();
        memoryCache = null;
        diskCache = null;
    }

    @Override
    public void clear() {
        clear(true, true);
    }

    public void clear(boolean clearMemory, boolean clearDisk) {
        if (clearMemory) memoryCache.clear();
        if (clearDisk && !diskCache.isClosed()) diskCache.clear();
    }
}