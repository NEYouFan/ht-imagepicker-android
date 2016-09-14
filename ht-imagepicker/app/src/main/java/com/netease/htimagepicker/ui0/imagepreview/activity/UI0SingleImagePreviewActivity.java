/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.htimagepicker.ui0.imagepreview.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.netease.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.imagepreview.activity.HTBaseImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.core.view.photoview.HTPhotoView;

import java.util.List;

/**
 * Created by zyl06 on 2/22/16.
 */
public class UI0SingleImagePreviewActivity extends HTBaseImagePreviewActivity
        implements View.OnClickListener {
    private static final int IMG_WIDTH = ContextUtil.INSTANCE.getScreenWidthPixel();

    private View mRoot;

    private HTPhotoView mImageView;

    private ImageButton mBtnDelete;

    private int mWidth = 0;
    private int mHeight = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = LayoutInflater.from(this).inflate(R.layout.ui0_activity_single_item_image_preview, null);
        setContentView(mRoot);
        initContentView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preview_image_view:
                finish();
                break;
            case R.id.preview_delete:
                if (mImages != null)
                    mImages.clear();
                finish();
                break;
        }
    }

    private void initContentView() {
        mImageView = (HTPhotoView) findViewById(R.id.preview_image_view);
        mBtnDelete = (ImageButton) findViewById(R.id.preview_delete);

        mImageView.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);

        String imagePath = getImagePath();
        if (imagePath != null) {
            showPreviewImage(imagePath);
        }
    }

    private String getImagePath() {
        if (mImages != null && !mImages.isEmpty()) {
            return mImages.get(0).getAbsolutePath();
        }
        return null;
    }

    private void showPreviewImage(@NonNull final String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return;
        }

        ImageUtil.setImagePath(mImageView, imagePath, mWidth, mHeight);
    }

    @Override
    public List<Image> getSelectedImages() {
        return null;
    }
}
