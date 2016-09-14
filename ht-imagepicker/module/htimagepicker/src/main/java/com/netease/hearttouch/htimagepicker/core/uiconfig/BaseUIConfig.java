package com.netease.hearttouch.htimagepicker.core.uiconfig;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.netease.hearttouch.htimagepicker.core.camera.HTBaseCameraFragment;
import com.netease.hearttouch.htimagepicker.core.constants.C;
import com.netease.hearttouch.htimagepicker.core.imagecut.HTBaseImageCutActivity;
import com.netease.hearttouch.htimagepicker.core.imagepick.activity.HTBaseImagePickActivity;
import com.netease.hearttouch.htimagepicker.core.imagepreview.activity.HTBaseImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.core.util.storage.StorageUtil;
import com.netease.hearttouch.htimagepicker.defaultui.camera.HTCameraFragment;
import com.netease.hearttouch.htimagepicker.defaultui.imagecut.activity.HTImageCutActivity;
import com.netease.hearttouch.htimagepicker.defaultui.imagepick.activity.HTImagePickActivity;
import com.netease.hearttouch.htimagepicker.defaultui.imagepreview.activity.HTMultiImagesPreviewActivity;
import com.netease.hearttouch.htimagepicker.defaultui.imagepreview.activity.HTSingleImagePreviewActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zyl06 on 3/21/16.
 */
public class BaseUIConfig {
    private static final String TAG = "HT_BaseUIConfig";

    Class mImagePickActivityClazz = HTImagePickActivity.class;
    Class mSingleImagePreviewActivityClazz = HTSingleImagePreviewActivity.class;
    Class mMultiImagePreviewActivityClazz = HTMultiImagesPreviewActivity.class;
    Class mImageCutActivityClazz = HTImageCutActivity.class;
    Class mCameraFragmentClazz = HTCameraFragment.class;
    boolean mIsUseSystemCamera = true; // 如果为true，则 mCameraFragmentClazz 参数的设置无效

    public BaseUIConfig() {
        this(null, null, null, null, null, true);
    }

    public BaseUIConfig(@Nullable Class imagePickActivityClazz,
                        @Nullable Class singleImagePreviewActivityClazz,
                        @Nullable Class multiImagePreviewActivityClazz,
                        @Nullable Class imageCutActivityClazz,
                        @Nullable Class cameraFragmentClazz,
                        boolean isUseSystemCamera) {
        if (checkClassExtendsBase(imagePickActivityClazz, HTBaseImagePickActivity.class)) {
            mImagePickActivityClazz = imagePickActivityClazz;
        } else if (imagePickActivityClazz != null) {
            Log.e(TAG, "pickImageActivity class is not extends of HTBaseImagePickActivity");
        }

        if (checkClassExtendsBase(singleImagePreviewActivityClazz, HTBaseImagePreviewActivity.class)) {
            mSingleImagePreviewActivityClazz = singleImagePreviewActivityClazz;
        } else if (singleImagePreviewActivityClazz != null) {
            Log.e(TAG, "singleImagePreviewActivity class is not extends of HTBaseImagePreviewActivity");
        }

        if (checkClassExtendsBase(multiImagePreviewActivityClazz, HTBaseImagePreviewActivity.class)) {
            mMultiImagePreviewActivityClazz = multiImagePreviewActivityClazz;
        } else if (multiImagePreviewActivityClazz != null) {
            Log.e(TAG, "multiImagePreviewActivity class is not extends of HTBaseImagePreviewActivity");
        }

        if (checkClassExtendsBase(imageCutActivityClazz, HTBaseImageCutActivity.class)) {
            mImageCutActivityClazz = imageCutActivityClazz;
        } else if (imageCutActivityClazz != null) {
            Log.e(TAG, "imageCutActivityClazz class is not extends of HTBaseImageCutActivity");
        }

        if (checkClassExtendsBase(cameraFragmentClazz, HTBaseCameraFragment.class)) {
            mCameraFragmentClazz = cameraFragmentClazz;
        } else if (cameraFragmentClazz != null) {
            Log.e(TAG, "cameraFragmentClazz class is not extends of HTBaseCameraFragment");
        }

        mIsUseSystemCamera = isUseSystemCamera;
    }

    /**
     * @return 拍照的照片保存的文件名，包含路径
     */
    public String getPhotoFileSavePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStamp + "_" + C.FileSuffix.JPG;

        return StorageUtil.getWriteSystemCameraPath(fileName, true);
    }

    public Class getImagePickActivityClass() {
        return mImagePickActivityClazz;
    }

    public Class getSingleImagePreviewActivity() {
        return mSingleImagePreviewActivityClazz;
    }

    public Class getMultiImagePreviewActivityClazz() {
        return mMultiImagePreviewActivityClazz;
    }

    public Class getImageCutActivityClazz() {
        return mImageCutActivityClazz;
    }

    public Class getCameraFragmentClazz() {
        return mCameraFragmentClazz;
    }

    public boolean isUseSystemCamera() {
        return mIsUseSystemCamera || mCameraFragmentClazz == null;
    }

    private boolean checkClassExtendsBase(Class clz, @NonNull Class base) {
        Class superClz = clz;
        while (superClz != null && superClz != base) {
            superClz = superClz.getSuperclass();
        }

        return superClz == base;
    }
}
