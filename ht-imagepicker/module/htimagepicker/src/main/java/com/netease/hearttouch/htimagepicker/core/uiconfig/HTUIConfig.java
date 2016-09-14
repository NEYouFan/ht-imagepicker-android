/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.uiconfig;

import android.text.TextUtils;

/**
 * Created by zyl06 on 3/21/16.
 */
public class HTUIConfig {
    private HTDefaultUIConfig mDefaultUIConfig;
    private HTRuntimeUIConfig mRuntimeUIConfig;

    public HTUIConfig(HTDefaultUIConfig defaultUIConfig, HTRuntimeUIConfig runtimeUIConfig) {
        mDefaultUIConfig = defaultUIConfig;
        mRuntimeUIConfig = runtimeUIConfig;
    }

    public void setRuntimeUIConfig(HTRuntimeUIConfig runtimeUIConfig) {
        mRuntimeUIConfig = runtimeUIConfig;
    }

    /**
     * @return 拍照的照片保存的文件名，包含路径
     */
    public String getPhotoFileSavePath() {
        if (mRuntimeUIConfig != null && !TextUtils.isEmpty(mRuntimeUIConfig.getPhotoFileSavePath())) {
            return mRuntimeUIConfig.getPhotoFileSavePath();
        }
        return mDefaultUIConfig.getPhotoFileSavePath();
    }

    public Class getImagePickActivityClass() {
        return (mRuntimeUIConfig != null) ?
                mRuntimeUIConfig.getImagePickActivityClass() :
                mDefaultUIConfig.getImagePickActivityClass();
    }

    public Class getSingleImagePreviewActivity() {
        return (mRuntimeUIConfig != null) ?
                mRuntimeUIConfig.getSingleImagePreviewActivity() :
                mDefaultUIConfig.getSingleImagePreviewActivity();
    }

    public Class getMultiImagePreviewActivityClazz() {
        return mRuntimeUIConfig != null ?
                mRuntimeUIConfig.getMultiImagePreviewActivityClazz() :
                mDefaultUIConfig.getMultiImagePreviewActivityClazz();
    }

    public Class getImageCutActivityClazz() {
        return mRuntimeUIConfig != null ?
                mRuntimeUIConfig.getImageCutActivityClazz() :
                mDefaultUIConfig.getImageCutActivityClazz();
    }

    public Class getCameraFragmentClazz() {
        return mRuntimeUIConfig != null ?
                mRuntimeUIConfig.getCameraFragmentClazz() :
                mDefaultUIConfig.getCameraFragmentClazz();
    }

    public boolean isUseSystemCamera() {
        return mRuntimeUIConfig != null ?
                mRuntimeUIConfig.isUseSystemCamera() :
                mDefaultUIConfig.isUseSystemCamera();
    }
}
