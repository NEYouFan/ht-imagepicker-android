/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.view.NavigationBar;

/**
 * Created by zyl06 on 2/19/16.
 */
public class BaseActionBarFragment extends Fragment {
    protected FrameLayout mRootView;
    protected ViewGroup mNavigationBarContainer;
    protected NavigationBar mNavigationBar;
    protected FrameLayout mContentView;
    protected Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = (FrameLayout)inflater.inflate(R.layout.htimagepicker_activity_with_navigation, null);
        mContext = getActivity();

        initNavigationBar();
        initContentView();

        return mRootView;
    }

    private void initNavigationBar(){
        mNavigationBarContainer = (FrameLayout)mRootView.findViewById(R.id.navigation_bar_container);
        mNavigationBar = new NavigationBar(mContext);
        mNavigationBarContainer.addView(mNavigationBar);
    }

    private void initContentView(){
        mContentView = (FrameLayout)mRootView.findViewById(R.id.content_view);
    }
}
