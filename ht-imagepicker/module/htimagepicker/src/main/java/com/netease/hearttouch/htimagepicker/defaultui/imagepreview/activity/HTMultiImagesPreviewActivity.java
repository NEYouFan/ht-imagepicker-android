package com.netease.hearttouch.htimagepicker.defaultui.imagepreview.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.imagepreview.activity.HTBaseImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.defaultui.imagepreview.adapter.ImagePreviewPagerAdapter;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.view.NavigationBar;
import com.netease.hearttouch.htimagepicker.core.view.photoview.PhotoViewPager;
import com.netease.hearttouch.htimagepicker.core.view.ViewWithNavationBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zyl06 on 2/22/16.
 */
public class HTMultiImagesPreviewActivity extends HTBaseImagePreviewActivity {
    private FrameLayout mNavigationBarContainer;
    private FrameLayout mContentView;
    private NavigationBar mNavigationBar;

    private Button mFinishBtn;
    private CheckBox mPickBox;
    private PhotoViewPager mImagePager;

    private Integer mPagePosition = 0;
    private boolean mIsPickBoxChangedByHand = true;
    private Set<Integer> mCancelMarks = new HashSet<>();

    private List<Image> mSelectedImages = new ArrayList<Image>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mImages != null && !mImages.isEmpty()) {
            mSelectedImages = new ArrayList<Image>(mImages.size());
            mSelectedImages.addAll(mImages);
        }

        ViewWithNavationBar view = new ViewWithNavationBar(this);
        setContentView(view);
        mNavigationBarContainer = view.getNavigationBarContainer();
        mNavigationBar = view.getNavigationBar();
        mContentView = view.getContentView();

        initNavigationBar();
        initContentView();
    }

    private void initNavigationBar() {
        mNavigationBarContainer.setBackgroundColor(ContextUtil.INSTANCE.getColor(R.color.transparent));
        mNavigationBar.setLeftBackImage(R.drawable.ic_back_arrow_white);
        mNavigationBar.setBackgroundColor(ContextUtil.INSTANCE.getColor(R.color.bg_transparent_grey));
        mNavigationBar.setBackButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTMultiImagesPreviewActivity.this.finish();
            }
        });

        View rightView = LayoutInflater.from(this).inflate(R.layout.htimagepicker_view_image_picker, null);
        mNavigationBar.setRightView(rightView);
        mPickBox = (CheckBox) rightView.findViewById(R.id.pick_mark);
        mPickBox.setChecked(true);
        mPickBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mIsPickBoxChangedByHand) {
                    if (!isChecked)
                        mCancelMarks.add(mPagePosition);
                    else
                        mCancelMarks.remove(mPagePosition);
                    updateFinishButton();
                }
            }
        });
    }

    private void initContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.htimagepicker_activity_image_fullscreen, null, false);
        mContentView.addView(view);
        mContentView.setBackgroundResource(R.color.black);

        mFinishBtn = (Button) view.findViewById(R.id.btn_finish);
        mFinishBtn.setVisibility(View.VISIBLE);
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTMultiImagesPreviewActivity.this.finish();
            }
        });
        updateFinishButton();

        mImagePager = (PhotoViewPager) view.findViewById(R.id.image_fullscreen_pager);
        mImagePager.setBackgroundResource(R.color.black);
        ImagePreviewPagerAdapter adapter = new ImagePreviewPagerAdapter(this, getImagePaths());
        mImagePager.setAdapter(adapter);
        mImagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPagePosition = position;
                updatePickMarkBox();
                updateTitle(position);
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
        updateTitle(0);
    }

    @Override
    public List<Image> getSelectedImages() {
        //
        if (mCancelMarks.size() == 0) {
            return null;
        }

        ArrayList<Image> selectedPhotos = new ArrayList<>();
        final int size = mImages.size();
        for (int i = 0; i < size; ++i) {
            if (!mCancelMarks.contains(i)) {
                selectedPhotos.add(mImages.get(i));
            }
        }
        return selectedPhotos;
    }

    private void updatePickMarkBox() {
        boolean isCanceled = mCancelMarks.contains(mPagePosition);
        mIsPickBoxChangedByHand = false;
        mPickBox.setChecked(!isCanceled);
        mIsPickBoxChangedByHand = true;
    }

    private void updateFinishButton() {
        int selectCount = mImages.size() - mCancelMarks.size();
        mFinishBtn.setText(ContextUtil.INSTANCE.stringFormat(R.string.pick_image_finish_with_number, selectCount));
    }

    private void updateTitle(int position) {
        String title = String.format("%d / %d", position + 1, mImages.size());
        mNavigationBar.setTitleColor(Color.WHITE);
        mNavigationBar.setTitle(title);
    }

}
