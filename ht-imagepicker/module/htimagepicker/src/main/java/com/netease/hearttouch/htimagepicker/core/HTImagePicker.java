package com.netease.hearttouch.htimagepicker.core;

import android.content.Context;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.core.imagepick.activity.ImagePickHelper;
import com.netease.hearttouch.htimagepicker.core.uiconfig.HTDefaultUIConfig;
import com.netease.hearttouch.htimagepicker.core.uiconfig.HTRuntimeUIConfig;
import com.netease.hearttouch.htimagepicker.core.uiconfig.HTUIConfig;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;

/**
 * Created by zyl06 on 2/18/16.
 */
public enum HTImagePicker {

    INSTANCE;

    // 包级作用域
    HTUIConfig mConfig;

    private HTImagePicker() {
    }

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, @Nullable HTDefaultUIConfig defaultUIConfig) {
        ContextUtil.INSTANCE.init(context);

        mConfig = (defaultUIConfig != null) ?
                new HTUIConfig(defaultUIConfig, null) :
                new HTUIConfig(new HTDefaultUIConfig(), null);
    }

    public synchronized HTUIConfig getUIConfig() {
        return mConfig;
    }

    public void start(Context context, HTPickParamsConfig paramConfig, HTPickFinishedListener listener) {
        start(context, paramConfig, null, listener);
    }

    public void start(Context context, HTPickParamsConfig paramConfig,
                      HTRuntimeUIConfig runtimeUIConfig, HTPickFinishedListener listener) {
        ImagePickHelper.start(context, paramConfig, runtimeUIConfig, listener);
    }
}
