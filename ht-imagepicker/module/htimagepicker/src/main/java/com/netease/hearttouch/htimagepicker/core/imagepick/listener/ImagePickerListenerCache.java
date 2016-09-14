/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagepick.listener;

import android.content.Context;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.core.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by zyl06 on 2/20/16.
 */
public class ImagePickerListenerCache {
    public static final int INVALID_IMAGE_PICK_KEY = 0;
    private static WeakHashMap<Context, HTPickFinishedListener> sPickerFinishedListeners =
            new WeakHashMap<Context, HTPickFinishedListener>();

    public static int insertImagePickerListener(Context context, HTPickFinishedListener listener) {
        if (context != null && listener != null) {
            sPickerFinishedListeners.put(context, listener);
            return context.hashCode();
        }
        return INVALID_IMAGE_PICK_KEY;
    }

    public static void callImagePickerFinishListener(int key, @Nullable ImageFolder imageFolder,
                                                     List<Image> images) {
        if (key == INVALID_IMAGE_PICK_KEY)
            return;

        Context contextKey = getContextKey(key);

        if (contextKey != null) {
            HTPickFinishedListener listener = sPickerFinishedListeners.get(contextKey);
            if (listener != null) {
                listener.onImagePickFinished(imageFolder, images);
            }
            sPickerFinishedListeners.remove(contextKey);
        }
    }

    public static void callImagePickerCancelListener(int key) {
        if (key == INVALID_IMAGE_PICK_KEY)
            return;

        Context contextKey = getContextKey(key);

        if (contextKey != null) {
            HTPickFinishedListener listener = sPickerFinishedListeners.get(contextKey);
            if (listener != null) {
                listener.onImagePickCanceled();
            }
            sPickerFinishedListeners.remove(contextKey);
        }
    }

    private static Context getContextKey(int key) {
        Context contextKey = null;
        for (WeakHashMap.Entry<Context, HTPickFinishedListener> entry : sPickerFinishedListeners.entrySet()) {
            Context context = entry.getKey();
            if (context != null && context.hashCode() == key) {
                contextKey = context;
                break;
            }
        }
        return contextKey;
    }
}
