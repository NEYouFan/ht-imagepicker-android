/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zyl06 on 3/15/16.
 */
public interface HTCameraFragmentInterface {
    /**
     * 创建自定义拍照界面的布局
     * @param cameraView 已经创建的一个 cameraView 实例，需要放入自定义的布局当中，用于拍照
     * @param inflater
     * @param container 当前布局的父容器
     * @param savedInstanceState 保存状态
     * @return 创建的布局视图
     */
    View onCreateCameraLayout(View cameraView, LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState);

    /**
     * 拍照接口 takePicture 能否可用的回调
     * @param isEnabled
     */
    void onTakePicureEnabledChanged(boolean isEnabled);

    /**
     * 拍完照，预览照片状态，confirmUsePicture 能否可用的回调
     * @param isEnabled
     */
    void onConfirmPictureEnabledChanged(boolean isEnabled);

    /**
     * 拍完照，预览照片状态，cancelUsePicture 能否可用的回调
     * @param isEnabled
     */
    void onCancelPicureEnabledChanged(boolean isEnabled);
}
