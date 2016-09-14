package com.netease.hearttouch.htimagepicker.core.view.base;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.view.NavigationBar;

/**
 * Created by zyl06 on 2/19/16.
 */
public class BaseActionBarActivity extends BaseActivity {
    protected FrameLayout mNavigationBarContainer;
    protected NavigationBar mNavigationBar;
    protected FrameLayout mContentView;
    protected boolean mIsFromScreenTop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.htimagepicker_activity_with_navigation);
        initNavigationBar();
        initContentView();
    }

    private void initNavigationBar() {
        mNavigationBarContainer = (FrameLayout) findViewById(R.id.navigation_bar_container);
        mNavigationBar = new NavigationBar(this);

        mNavigationBarContainer.addView(mNavigationBar);
    }

    private void initContentView() {
        mContentView = (FrameLayout) findViewById(R.id.content_view);
    }

    protected void setStartFromScreenTop(boolean isFromTop) {
        if (isFromTop != mIsFromScreenTop) {
            if (isFromTop) {
                mContentView.setPadding(0, 0, 0, 0);
            } else {
                float actionBarHeight = getResources().getDimension(R.dimen.action_bar_height);
                if (mNavigationBarContainer.getHeight() > 0) {
                    actionBarHeight = mNavigationBarContainer.getHeight();
                }
                mContentView.setPadding(0, (int) actionBarHeight, 0, 0);
            }
            mIsFromScreenTop = isFromTop;
        }
    }
}
