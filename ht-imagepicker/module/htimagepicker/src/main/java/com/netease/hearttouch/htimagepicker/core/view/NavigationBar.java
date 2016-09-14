package com.netease.hearttouch.htimagepicker.core.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.hearttouch.htimagepicker.R;

/**
 * Created by zyl06 on 2/19/16.
 */
public class NavigationBar extends RelativeLayout {
    private FrameLayout mLeftViewContainer;
    private FrameLayout mRightViewContainer;
    private TextView mTitleTextView;

    private ImageView mLeftBackView;
    private TextView mLeftTextView;

    private FrameLayout mCenterViewContainer;

    private TextView mRightTextView;
    private ImageView mRightImageView;

    private View mSepLine;

    public NavigationBar(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View root= inflater.inflate(R.layout.htimagepicker_view_navigation_bar, null);
        mTitleTextView = (TextView)root.findViewById(R.id.nav_title);

        mRightViewContainer = (FrameLayout)root.findViewById(R.id.nav_right_container);
        mRightTextView = (TextView)root.findViewById(R.id.nav_right_text);
        mRightImageView = (ImageView)root.findViewById(R.id.nav_right_img);

        mLeftViewContainer = (FrameLayout)root.findViewById(R.id.nav_left_container);
        mCenterViewContainer = (FrameLayout)root.findViewById(R.id.nav_titleView_container);
        mLeftBackView = (ImageView)root.findViewById(R.id.nav_left_img);
        mLeftTextView = (TextView)root.findViewById(R.id.nav_left_text);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

        mSepLine = root.findViewById(R.id.nav_sep_line);

        this.addView(root, lp);
    }

    public void setShowBackButton(boolean bShow){
        mLeftBackView.setVisibility(bShow ? View.VISIBLE : View.GONE);
    }

    public void setLeftBackImage(int imageId) {
        mLeftBackView.setImageResource(imageId);
    }
    public void setLeftBackImage(Drawable drawable) {
        if(mLeftBackView.getParent() == null){
            mLeftViewContainer.removeAllViews();
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mLeftViewContainer.addView(mLeftBackView, lp);
        }
        mLeftTextView.setVisibility(View.GONE);
        mLeftBackView.setImageDrawable(drawable);
    }

    public void setBackButtonClick(View.OnClickListener onclick) {
        mLeftViewContainer.setOnClickListener(onclick);
    }

    public void setRightButtonClick(View.OnClickListener onclick) {
        mRightViewContainer.setOnClickListener(onclick);
    }

    public void setTitleClick(View.OnClickListener onclick) {
        mTitleTextView.setOnClickListener(onclick);
    }

    public void setLeftView(View view){
        if(mLeftViewContainer.getChildCount() > 0){
            mLeftViewContainer.removeAllViews();
        }
        if(view != null){
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mLeftViewContainer.addView(view, lp);
        }
    }

    public void setLeftImageView(int drawableId) {
        if(mLeftBackView.getParent() == null){
            mLeftViewContainer.removeAllViews();
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mLeftViewContainer.addView(mLeftBackView, lp);
        }
        mLeftTextView.setVisibility(View.GONE);
        mLeftBackView.setImageResource(drawableId);
    }

    public void setLeftTextString(String str) {
        if(mLeftTextView.getParent() == null){
            mLeftViewContainer.removeAllViews();
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mLeftViewContainer.addView(mLeftTextView, lp);
        }
        mLeftBackView.setVisibility(View.GONE);
        mLeftTextView.setText(str);
    }

    public void setLeftTextString(int stringId) {
        String str = getResources().getString(stringId);
        setLeftTextString(str);
    }

    public void setRightView(View view){
        if(mRightViewContainer.getChildCount()>0){
            mRightViewContainer.removeAllViews();
        }
        if(view!=null){
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mRightViewContainer.addView(view,lp);
        }
    }

    public void setRightImageResource(int drawableId) {
        if (mRightImageView.getParent() == null) {
            mRightViewContainer.removeAllViews();
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mRightViewContainer.addView(mRightImageView, lp);
        }
        mRightTextView.setVisibility(View.GONE);
        mRightImageView.setImageResource(drawableId);
    }

    public void setRightText(int stringId) {
        String str = getResources().getString(stringId);
        setRightText(str);
    }

    public void setRightText(String str) {
        if (mRightTextView.getParent() == null) {
            mRightViewContainer.removeAllViews();
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            mRightViewContainer.addView(mRightTextView, lp);
        }
        mRightImageView.setVisibility(View.GONE);
        mRightTextView.setText(str);
    }

    public void setTitleView(View view){
        mTitleTextView.setVisibility(View.INVISIBLE);
        mCenterViewContainer.setVisibility(View.VISIBLE);
        mCenterViewContainer.removeAllViews();
        if(view!=null){
            FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            mCenterViewContainer.addView(view, lp);
        }
    }

    public void setTitle(int stringId) {
        String title = getResources().getString(stringId);
        setTitle(title);
    }

    public void setTitle(String title){
        mTitleTextView.setVisibility(View.VISIBLE);
        mCenterViewContainer.setVisibility(View.INVISIBLE);

        mTitleTextView.setText(title);
    }

    public void setTitleColor(int color){
        mTitleTextView.setTextColor(color);
    }

    public void setTitleTextStyle(int typefaceStyle) {
        mTitleTextView.setTypeface(null, typefaceStyle);
    }

    public void setBgColor(int color){
        setBackgroundColor(color);
    }

    public void setSepLineVisiable(boolean isVisiable) {
        if (isVisiable) {
            mSepLine.setVisibility(VISIBLE);
        }
        else {
            mSepLine.setVisibility(INVISIBLE);
        }
    }
}
