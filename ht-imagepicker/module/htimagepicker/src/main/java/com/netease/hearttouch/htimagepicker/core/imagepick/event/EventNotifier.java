/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagepick.event;

import java.lang.ref.WeakReference;

/**
 * Created by zyl06 on 2/19/16.
 */
public class EventNotifier {
    private static WeakReference<InternalUpdateSelectedPhotosListener> sUpdateSelectedPhotosListenerRef;

    public static void registerUpdatePickMarksListener(InternalUpdateSelectedPhotosListener listener) {
        if (listener != null) {
            sUpdateSelectedPhotosListenerRef = new WeakReference<InternalUpdateSelectedPhotosListener>(listener);
        }
    }

    public static void unregisterUpdatePickMarksListener() {
        sUpdateSelectedPhotosListenerRef = null;
    }

    public static void notifyUpdatePickMarksListener(EventUpdateSelectedPhotosModel model) {
        if (sUpdateSelectedPhotosListenerRef != null && sUpdateSelectedPhotosListenerRef.get() != null) {
            sUpdateSelectedPhotosListenerRef.get().onInternalUpdateSelectedPhotos(model);
        }
    }
}
