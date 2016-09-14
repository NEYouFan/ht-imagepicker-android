/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.view;

/**
 * Created by zyl06 on 2/18/16.
 */
public interface ImageGestureListener {
    void onImageGestureSingleTapConfirmed();
    void onImageGestureLongPress();
    void onImageGestureFlingDown();
}