/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagepick.event;

import com.netease.hearttouch.htimagepicker.core.imagescan.Image;

import java.util.List;

/**
 * Created by zyl06 on 6/25/15.
 */
public class EventUpdateSelectedPhotosModel {
    private List<Image> mSelectedImages;

    public List<Image> getSelectedImages() {
        return mSelectedImages;
    }

    public void setSelectedImages(List<Image> selectedImages) {
        this.mSelectedImages = selectedImages;
    }
}
