package com.netease.hearttouch.htimagepicker.core.uiconfig;

import android.support.annotation.Nullable;

/**
 * Created by zyl06 on 3/21/16.
 */
public class HTDefaultUIConfig extends BaseUIConfig {
    public HTDefaultUIConfig() {
        super();
    }

    public HTDefaultUIConfig(@Nullable Class imagePickActivityClazz,
                             @Nullable Class singleImagePreviewActivityClazz,
                             @Nullable Class multiImagePreviewActivityClazz,
                             @Nullable Class imageCutActivityClazz,
                             @Nullable Class cameraFragmentClazz,
                             boolean isUseSystemCamera) {
        super(imagePickActivityClazz, singleImagePreviewActivityClazz, multiImagePreviewActivityClazz,
                imageCutActivityClazz, cameraFragmentClazz, isUseSystemCamera);
    }

}
