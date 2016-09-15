/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.htimagepicker.ui0.imagecut.activity;

import android.os.Bundle;

import com.netease.hearttouch.htimagepicker.core.imagecut.HTBaseImageCutActivity;
import com.netease.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.view.HTImageCutView;
import com.netease.hearttouch.htimagepicker.core.view.NavigationBar;

import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by zyl06 on 6/25/15.
 */
public class UI0ImageCutActivity extends HTBaseImageCutActivity {

    /**
     * 导航栏
     */
    protected FrameLayout navigationBarContainer;
    protected NavigationBar navigationBar;
    protected View navigationBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui0_activity_cut_image);
        initNavigationBar();
    }

    private void initNavigationBar() {
        navigationBarContainer = (FrameLayout) findViewById(R.id.navigation_bar_container);
        navigationBar = new NavigationBar(this);
        navigationBarContainer.addView(navigationBar);

        navigationBar.setLeftBackImage(R.mipmap.ic_back_red);
        // 设置背景色
        navigationBackground.setBackgroundColor(getResources().getColor(R.color.ne_black));
        // title颜色
        navigationBar.setTitleColor(R.color.ne_white);
        // 设置右文字
        navigationBar.setRightText(getResources().getString(R.string.save));
        // 设置title文字
        navigationBar.setTitle(R.string.custom_cut_image_title);

        navigationBarContainer.findViewById(R.id.nav_right_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UI0ImageCutActivity.this.confirmCutFinish();
            }
        });
        navigationBarContainer.findViewById(R.id.nav_left_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UI0ImageCutActivity.this.cancelCutFinish();
            }
        });
    }

    @Override
    public HTImageCutView getImageCutView() {
        return (HTImageCutView) findViewById(R.id.cuttable_image_view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
