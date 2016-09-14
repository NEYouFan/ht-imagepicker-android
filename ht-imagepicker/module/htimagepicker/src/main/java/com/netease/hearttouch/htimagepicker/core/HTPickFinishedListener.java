/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core;

import android.support.annotation.Nullable;


import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;

import java.util.List;

/**
 * Created by zyl06 on 2/20/16.
 */
public interface HTPickFinishedListener {
    // 返回相册的信息和该相册中照片文件的列表
    void onImagePickFinished(@Nullable ImageFolder imageFolder, List<Image> images);
    // 图片选择取消
    void onImagePickCanceled();
}
