/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.htimagepicker;

import android.app.Application;

import com.netease.hearttouch.htimagepicker.core.HTImagePicker;

/**
 * Created by zyl06 on 3/23/16.
 */
public class HTApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        HTDefaultUIConfig config = new HTDefaultUIConfig(PickImageActivity.class,
//                SingleItemImagePreviewActivity.class,
//                MultiItemImagesPreviewActivity.class,
//                ImageCropActivity.class, null, true);
        HTImagePicker.INSTANCE.init(this, null);
    }
}
