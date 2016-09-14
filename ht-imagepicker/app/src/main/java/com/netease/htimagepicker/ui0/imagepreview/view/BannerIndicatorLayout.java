package com.netease.htimagepicker.ui0.imagepreview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.netease.htimagepicker.R;

/**
 * Created by zyl06 on 4/1/16.
 */
public class BannerIndicatorLayout extends LinearLayout {
    /**
     * 选中的背景(颜色和尺寸)
     */
    private int selcetBackground;
    /**
     * 未选中的背景(颜色和尺寸)
     */
    private int unselcetBackground;
    /**
     * 指示器的间距
     */
    private int space;

    Context mContext;
    ImageView[] mIndicators;


    public BannerIndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BannerIndicatorLayout);

        try {
            selcetBackground = typedArray.getResourceId(R.styleable.BannerIndicatorLayout_select_background, R.color.transparent);
            unselcetBackground = typedArray.getResourceId(R.styleable.BannerIndicatorLayout_unselect_background, R.color.transparent);
            space = typedArray.getDimensionPixelSize(R.styleable.BannerIndicatorLayout_indicator_space, 10);
        } finally {
            typedArray.recycle();
        }
    }

    public void initIndicators(int num, int position) {
        if (num <= 1) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        position = position % num;
        if (mIndicators == null || mIndicators.length != num) {
            removeAllViews();
            mIndicators = new ImageView[num];

            for (int i = 0; i < num; i++) {
                ImageView indicator = new ImageView(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(space / 2, 0, space / 2, 0);
                lp.gravity = Gravity.CENTER_VERTICAL;
                addView(indicator, lp);
                mIndicators[i] = indicator;
            }
        }

        changeIndicator(position);

        invalidate();
    }

    public void changeIndicator(int position) {
        for (int i = 0; i < mIndicators.length; i++) {
            mIndicators[i].setImageResource(i == position ? selcetBackground : unselcetBackground);
        }
    }
}
