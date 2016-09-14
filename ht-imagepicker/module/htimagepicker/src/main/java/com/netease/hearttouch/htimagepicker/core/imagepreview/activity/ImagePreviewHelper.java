/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagepreview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.core.HTImagePicker;
import com.netease.hearttouch.htimagepicker.core.imagepreview.listener.IIntentProcess;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 2/23/16.
 */
public class ImagePreviewHelper {
    public static void startMultiImagePreview(@NonNull Activity activity,
                                               final List<Image> images,
                                               @Nullable IIntentProcess intentProcess) {
        Class imagePreviewActivityClass = HTImagePicker.INSTANCE.getUIConfig().getMultiImagePreviewActivityClazz();
        start(activity, imagePreviewActivityClass, images, intentProcess);
    }

    public static void startSingleImagePreview(@NonNull Activity activity,
                                               Image image,
                                               @Nullable IIntentProcess intentProcess) {
        Class imagePreviewActivityClass = HTImagePicker.INSTANCE.getUIConfig().getSingleImagePreviewActivity();
        ArrayList<Image> images = new ArrayList<>();
        images.add(image);
        start(activity, imagePreviewActivityClass, images, intentProcess);
    }

    private static void start(@NonNull Activity activity, @NonNull Class imagePreviousActivity,
                              final List<Image> images,
                              @Nullable IIntentProcess intentProcess) {
        Intent intent = new Intent(activity, imagePreviousActivity);
        intent.putParcelableArrayListExtra(HTBaseImagePreviewActivity.IMAGE_LIST_KEY, new ArrayList<Parcelable>(images));
        if (intentProcess != null) {
            intentProcess.onProcessIntent(intent);
        }

        activity.startActivity(intent);
    }
}
