package com.netease.hearttouch.htimagepicker.core.view.base;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.netease.hearttouch.htimagepicker.R;

/**
 * Created by zyl06 on 2/19/16.
 */
public class BaseActivity extends Activity {
    protected FrameLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.htimagepicker_activity_blank);
        initContentView();
    }

    private void initContentView() {
        mContentView = (FrameLayout) findViewById(R.id.content_view);
    }

}