/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.defaultui.imagecut.activity;

import android.os.Bundle;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.imagecut.HTBaseImageCutActivity;
import com.netease.hearttouch.htimagepicker.core.view.HTImageCutView;
import com.netease.hearttouch.htimagepicker.core.HTImageFrom;

import android.view.View;
import android.widget.TextView;

/**
 * Created by zyl06 on 6/25/15.
 */
public class HTImageCutActivity extends HTBaseImageCutActivity {

    // 显示的裁剪图片控件
    private HTImageCutView mCutImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.htimagepicker_activity_cut_image);
        mCutImageView = (HTImageCutView) findViewById(R.id.cuttable_image_view);
        initButtons();
    }

    @Override
    protected void onDestroy() {
//        mCutImageView.clear();
        super.onDestroy();
    }

    @Override
    public HTImageCutView getImageCutView() {
        return mCutImageView;
    }

    private void initButtons() {
        TextView reselectBtn = (TextView) findViewById(R.id.btn_reselect);
        if (HTImageFrom.FROM_CAMERA.toString().equals(mFrom)) {
            reselectBtn.setText(R.string.ne_pick_image_re_take_photo);
        } else {
            reselectBtn.setText(R.string.ne_pick_image_reselect);
        }
        reselectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTImageCutActivity.this.cancelCutFinish();
            }
        });

        findViewById(R.id.btn_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTImageCutActivity.this.confirmCutFinish();
            }
        });
    }
}
