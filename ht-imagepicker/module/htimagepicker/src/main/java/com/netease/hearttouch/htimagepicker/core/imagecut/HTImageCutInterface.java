/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagecut;

import com.netease.hearttouch.htimagepicker.core.view.HTImageCutView;

/**
 * Created by zyl06 on 2/23/16.
 */
public interface HTImageCutInterface {
    /**
     * 子类调用，触发取消裁剪并结束activity
     */
    void confirmCutFinish();
    /**
     * 子类调用，触发确认裁剪并结束activity
     */
    void cancelCutFinish();

    /**
     * 获取页面中的 ImageCutView 控件
     * @return xml中定义或者java中新建的图片裁剪控件
     */
    HTImageCutView getImageCutView();
}
