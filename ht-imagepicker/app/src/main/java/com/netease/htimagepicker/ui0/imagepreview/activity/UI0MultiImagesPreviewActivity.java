/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.htimagepicker.ui0.imagepreview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.netease.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.imagepreview.activity.HTBaseImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.view.photoview.PhotoViewPager;
import com.netease.htimagepicker.ui0.imagepreview.adapter.UI0ImagePreviewPagerAdapter;
import com.netease.htimagepicker.ui0.imagepreview.view.BannerIndicatorLayout;

import java.util.List;

/**
 * Created by zyl06 on 2/22/16.
 */
public class UI0MultiImagesPreviewActivity extends HTBaseImagePreviewActivity {
    private PhotoViewPager mImagePager;

    private BannerIndicatorLayout mIndicatorLayout;

    private boolean mIsFirst = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui0_activity_multi_item_images_preview);
        initContentView();
        mIsFirst = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFirst) {
            mIsFirst = false;
        }
    }

    private void initContentView() {
        mImagePager = (PhotoViewPager) findViewById(R.id.image_fullscreen_pager);
        mIndicatorLayout = (BannerIndicatorLayout) findViewById(R.id.indicator_banner);

        mImagePager.setBackgroundResource(R.color.ne_black);
        UI0ImagePreviewPagerAdapter adapter = new UI0ImagePreviewPagerAdapter(this, getImagePaths());
        mImagePager.setAdapter(adapter);
        mImagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mIndicatorLayout.changeIndicator(position % mImages.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        break;
                    default:
                        break;
                }
            }
        });
        mImagePager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UI0MultiImagesPreviewActivity.this.finish();
            }
        });

        if (mImages.size() < 2) {
            mIndicatorLayout.setVisibility(View.GONE);
        } else {
            mIndicatorLayout.initIndicators(mImages.size(), 0);
            mImagePager.setCurrentItem(0);
        }
    }

    @Override
    public List<Image> getSelectedImages() {
        return null;
    }
}
