/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagepreview.activity;

import android.app.Activity;
import android.os.Bundle;

import com.netease.hearttouch.htimagepicker.core.imagepick.event.EventNotifier;
import com.netease.hearttouch.htimagepicker.core.imagepick.event.EventUpdateSelectedPhotosModel;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 2/21/16.
 */
public abstract class HTBaseImagePreviewActivity extends Activity {
    static final String IMAGE_LIST_KEY = "HTBaseImagePreviewActivity_ImageListKey";
    protected ArrayList<Image> mImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processExtras();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        List<Image> images = getSelectedImages();
        if (images != null) {
            EventUpdateSelectedPhotosModel event = new EventUpdateSelectedPhotosModel();
            event.setSelectedImages(images);
            EventNotifier.notifyUpdatePickMarksListener(event);
        }
    }

    /**
     * 在多图预览的时候，支持取消部分图片的选中
     * @return 选中的图片列表，可以返回 null，表示全选
     */
    public abstract List<Image> getSelectedImages();

    protected List<String> getImagePaths() {
        List<String> imagePaths = new ArrayList<String>();
        int size = mImages.size();
        for (int i = 0; i < size; ++i) {
            imagePaths.add(mImages.get(i).getAbsolutePath());
        }
        return imagePaths;
    }

    private void processExtras() {
        mImages = getIntent().getExtras().getParcelableArrayList(IMAGE_LIST_KEY);
        if (mImages == null) {
            mImages = new ArrayList<>();
        }
    }
}
