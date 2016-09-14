/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagecut;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.core.HTImagePicker;
import com.netease.hearttouch.htimagepicker.core.imagepick.activity.Extras;
import com.netease.hearttouch.htimagepicker.core.imagepreview.listener.IIntentProcess;

/**
 * Created by zyl06 on 2/23/16.
 */
public class ImageCutHelper {
    public static void startForFile(@NonNull Activity activity, String srcFile,
                                    float cutRatio, String path, int requestCode,
                                    @Nullable IIntentProcess intentProcess) {
        startForFile(activity, srcFile, cutRatio, path, "", requestCode, intentProcess);
    }

    public static void startForFile(@NonNull Activity activity, String srcFile, float cutRatio,
                                    String path, String from, int requestCode,
                                    @Nullable IIntentProcess intentProcess) {
        Class imageCutActivityClazz = HTImagePicker.INSTANCE.getUIConfig().getImageCutActivityClazz();

        Intent intent = new Intent(activity, imageCutActivityClazz);

        intent.putExtra(Extras.EXTRA_SRC_FILE, srcFile);
        intent.putExtra(Extras.EXTRA_CUT_RATIO, cutRatio);
        intent.putExtra(Extras.EXTRA_FILE_PATH, path);
        intent.putExtra(Extras.EXTRA_FROM, from);

        if (intentProcess != null) {
            intentProcess.onProcessIntent(intent);
        }

        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForData(Activity activity,
                                    String srcFile, float cutRatio, int requestCode,
                                    @Nullable IIntentProcess intentProcess) {
        startForData(activity, srcFile, cutRatio, "", requestCode, intentProcess);
    }

    public static void startForData(Activity activity, String srcFile,
                                    float cutRatio, String from, int requestCode,
                                    @Nullable IIntentProcess intentProcess) {
        Class imageCutActivityClazz = HTImagePicker.INSTANCE.getUIConfig().getImageCutActivityClazz();
        Intent intent = new Intent(activity, imageCutActivityClazz);
        intent.putExtra(Extras.EXTRA_SRC_FILE, srcFile);
        intent.putExtra(Extras.EXTRA_CUT_RATIO, cutRatio);
        intent.putExtra(Extras.EXTRA_RETURN_DATA, true);
        intent.putExtra(Extras.EXTRA_FROM, from);

        if (intentProcess != null) {
            intentProcess.onProcessIntent(intent);
        }

        activity.startActivityForResult(intent, requestCode);
    }
}
