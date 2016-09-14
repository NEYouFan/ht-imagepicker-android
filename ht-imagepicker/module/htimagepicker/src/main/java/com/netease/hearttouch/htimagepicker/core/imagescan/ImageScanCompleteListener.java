/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagescan;

import java.util.List;

/**
 * Created by zyl06 on 3/28/16.
 */
public interface ImageScanCompleteListener {
    void onScanComplete(List<Thumbnail> thumbnails, List<ImageFolder> imageFolders);
}
