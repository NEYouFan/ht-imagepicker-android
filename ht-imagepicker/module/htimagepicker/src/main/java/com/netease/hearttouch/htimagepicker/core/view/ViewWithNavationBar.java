/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;

/**
 * Created by zyl06 on 2/22/16.
 */
public class ViewWithNavationBar extends FrameLayout {
    protected Context mContext;
    protected FrameLayout mNavigationBarContainer;
    protected NavigationBar mNavigationBar;
    protected FrameLayout mContentView;

    public ViewWithNavationBar(Context context) {
        this(context, null);
    }

    public ViewWithNavationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewWithNavationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mNavigationBarContainer = new FrameLayout(this.getContext());
        mNavigationBar = new NavigationBar(this.getContext());
        mNavigationBar.setLeftBackImage(R.drawable.ic_back_arrow);
        mNavigationBarContainer.addView(mNavigationBar);

        mContentView = new FrameLayout(this.getContext());
        FrameLayout.LayoutParams lpContent = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(mContentView, lpContent);

        FrameLayout.LayoutParams lpNavigationBar = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, ContextUtil.INSTANCE.dip2px(45));
        this.addView(mNavigationBarContainer, lpNavigationBar);
    }

    public FrameLayout getContentView() {
        return mContentView;
    }

    public FrameLayout getNavigationBarContainer() {
        return mNavigationBarContainer;
    }

    public NavigationBar getNavigationBar() {
        return mNavigationBar;
    }
}
