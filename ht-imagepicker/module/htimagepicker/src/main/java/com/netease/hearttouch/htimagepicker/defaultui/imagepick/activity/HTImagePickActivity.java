/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.defaultui.imagepick.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.netease.hearttouch.htimagepicker.core.HTImageFrom;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.imagepick.activity.HTBaseImagePickActivity;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageInfoUtil;
import com.netease.hearttouch.htimagepicker.core.imagescan.Thumbnail;
import com.netease.hearttouch.htimagepicker.core.view.NavigationBar;
import com.netease.hearttouch.htimagepicker.defaultui.imagepick.adapter.ImageFolderListAdapter;
import com.netease.hearttouch.htimagepicker.defaultui.imagepick.adapter.ImageGridAdapter;
import com.netease.hearttouch.htimagepicker.core.imagepick.event.EventNotifier;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.view.PopupwindowMenu;

import java.util.List;

/**
 * Created by zyl06 on 6/22/15.
 */
public class HTImagePickActivity
        extends HTBaseImagePickActivity
        implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener {

    private static final String TAG = "HTImagePickActivity";

    protected FrameLayout mNavigationBarContainer;
    protected NavigationBar mNavigationBar;
    protected FrameLayout mContentView;
    protected boolean mIsFromScreenTop = true;

    private PopupwindowMenu mPopupwindowMenu;

    private GridView mImageGridView;
    private ImageGridAdapter mImageGridAdapter;
    private TextView mPreviewBtn;
    private Button mCompleteBtn;
    private TextView mTitleView;

    private ImageFolder mAllImageFolder;

    private boolean mIsConfirmUse = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.htimagepicker_activity_with_navigation);

        initNavigationBar();
        initContentView();
        setStartFromScreenTop(false);

        EventNotifier.registerUpdatePickMarksListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventNotifier.unregisterUpdatePickMarksListener();
    }

    @Override
    public boolean isConfirmUse() {
        return mIsConfirmUse;
    }

    @Override
    public void onUpdateSelectedImages(List<Image> images) {
        mImageGridAdapter.notifyDataSetChanged();
        updateFinishButton();
    }

    @Override
    public void onUpdateAllImageFolders(List<Thumbnail> thumbnails, List<ImageFolder> imageFolders) {
        if (imageFolders != null && imageFolders.size() > 0) {
            mAllImageFolder = imageFolders.get(0);

            mImageGridAdapter = new ImageGridAdapter(this, mCurrImageFolder, mSelectedImages);
            mImageGridView.setAdapter(mImageGridAdapter);
            mTitleView.setText(mCurrImageFolder.getName());
        }
        updateFinishButton();
    }

    @Override
    public List<Image> getSelectedImages() {
        return mSelectedImages;
    }

    @Override
    public void onCancelFromCamera() {
        mIsConfirmUse = false;
        finish();
    }

    @Override
    public void onPickedFromCamera(Image image) {
        if (mSelectedImages.size() < getSelectLimit()) {
            mSelectedImages.add(image);//0是拍照的index
            mImageGridAdapter.notifyDataSetChanged();
        }

        if (getImageFrom() == HTImageFrom.FROM_CAMERA) {
            mIsConfirmUse = true;
            finish();
        }
    }

    @Override
    public void onCompleteFromCut(HTImageFrom from) {
        // 结束图片选取
        mIsConfirmUse = true;
    }

    @Override
    public void onCancelFromCut(HTImageFrom from) {
        mIsConfirmUse = false;
        if (from == HTImageFrom.FROM_LOCAL) {
            mSelectedImages.clear();
            mImageGridAdapter.notifyDataSetChanged();
        }
    }

    private void initNavigationBar() {
        mNavigationBarContainer = (FrameLayout) findViewById(R.id.navigation_bar_container);
        mNavigationBar = new NavigationBar(this);
        mNavigationBarContainer.addView(mNavigationBar);

        LayoutInflater inflater = LayoutInflater.from(this);
        View titleView = inflater.inflate(R.layout.htimagepicker_view_title_image, null, false);
        mTitleView = (TextView) titleView.findViewById(R.id.title_text);
        mTitleView.setText(mTitle);

        ImageView imageView = (ImageView) titleView.findViewById(R.id.title_image);
        imageView.setImageResource(R.drawable.pick_image_title_show);
        titleView.setOnClickListener(this);

        mNavigationBar.setLeftBackImage(R.drawable.ic_back_arrow);
        mNavigationBar.setTitleView(titleView);
        mNavigationBar.setBackButtonClick(this);
    }

    private void initContentView() {
        mContentView = (FrameLayout) findViewById(R.id.content_view);

        final LayoutInflater inflater = LayoutInflater.from(this);
        View contentView = inflater.inflate(R.layout.htimagepicker_view_pick_local_image, null, false);
        mContentView.addView(contentView);

        mImageGridView = (GridView) contentView.findViewById(R.id.pick_image_grid_view);
        mImageGridView.setOnItemLongClickListener(this);
        mImageGridView.setOnItemClickListener(this);

        mPreviewBtn = (TextView) contentView.findViewById(R.id.preview_button);
        mPreviewBtn.setOnClickListener(this);

        mCompleteBtn = (Button) contentView.findViewById(R.id.complete_button);
        mCompleteBtn.setOnClickListener(this);
        updateFinishButton();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            startTakePhoto();
            return;
        }

        int index = position - 1;
        Image image = mCurrImageFolder.getImages().get(index);

        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
        } else if (mSelectedImages.size() >= getSelectLimit()) {
            ContextUtil.INSTANCE.makeToast(R.string.ne_pick_image_max_pick_warning_with_number,
                    getSelectLimit());
        } else {
            mSelectedImages.add(image);
        }

        mImageGridAdapter.notifyDataSetChanged();
        updateFinishButton();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_pick_image) {
            final ImageView imageView = (ImageView) v.findViewById(R.id.title_image);
            imageView.setImageResource(R.drawable.pick_image_title_hide);
            if (mPopupwindowMenu == null) {
                int lpHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                final List<ImageFolder> imageFolders = ImageInfoUtil.getImageFolders();
                if (imageFolders != null && imageFolders.size() > 5) {
                    lpHeight = 5 * (int) ContextUtil.INSTANCE.getDimen(R.dimen.ne_pick_image_folder_list_item_height);
                }
                mPopupwindowMenu = new PopupwindowMenu(HTImagePickActivity.this,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        lpHeight,
                        Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                ImageFolderListAdapter adapter = new ImageFolderListAdapter(HTImagePickActivity.this);
                mPopupwindowMenu.setCustomAdapter(adapter);
                mPopupwindowMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            if (mImageGridAdapter != null) {
                                if (imageFolders != null && imageFolders.size() > 0) {
                                    mCurrImageFolder = imageFolders.get(position);
                                    mSelectedImages.clear();
                                    mImageGridAdapter.changeImageFolder(mCurrImageFolder, mSelectedImages);
                                    mTitleView.setText(mCurrImageFolder.getName());
                                    updateFinishButton();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        } finally {
                            mPopupwindowMenu.dismiss();
                        }
                    }
                });
                mPopupwindowMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        imageView.setImageResource(R.drawable.pick_image_title_show);
                    }
                });
            }

            mPopupwindowMenu.showAsDropDown(mNavigationBar, 0, 0);
        } else if (v.getId() == R.id.nav_left_container) {
            mIsConfirmUse = false;
            finish();
        } else if (v.getId() == R.id.preview_button) {
            startPreviewMultiImages(mSelectedImages, null);
        } else if (v.getId() == R.id.complete_button) {
            if (mSelectedImages.size() > 0) {
                mIsConfirmUse = true;
                completeSelection(mSelectedImages, isCut());
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int imagePosition = position - 1; // 第一个是拍照
        if (imagePosition < 0) return true;
        Image image = mCurrImageFolder.getImages().get(imagePosition);
        startPreviewSingleImage(image, null);
        return true;
    }

    private void updateFinishButton() {
        if (mSelectedImages.size() == 0) {
            mCompleteBtn.setBackgroundResource(R.drawable.ne_shape_round_30dp_green_light);
            mCompleteBtn.setText(ContextUtil.INSTANCE.stringFormat(R.string.ne_pick_image_finish_with_number, 0));
        } else {
            mCompleteBtn.setBackgroundResource(R.drawable.ne_shape_round_30dp_green_normal);
            mCompleteBtn.setText(ContextUtil.INSTANCE.stringFormat(R.string.ne_pick_image_finish_with_number, mSelectedImages.size()));
        }
    }

    private void completeSelection(List<Image> images, boolean needCut) {
        if (images.size() > 0 && needCut) {
            Image image = images.get(0);
            // 从本地图片选择进入裁剪
            startCutImage(image.getAbsolutePath(), HTImageFrom.FROM_LOCAL.toString(), null);
        } else {
//            Intent intent = mTarget.getIntent();
//            if (intent == null) intent = new Intent();
//
//            ArrayList<String> selectedImagePaths = getSelectedImagePaths(imageFolder, pickIndices);
//
//            intent.putExtra(Extras.EXTRA_IMAGE_FOLDER, imageFolder);
//            intent.putStringArrayListExtra(Extras.EXTRA_PICK_IMAGE_PATHS, selectedImagePaths);
//            intent.putIntegerArrayListExtra(Extras.EXTRA_PICK_INDICES, pickIndices);
//
//            mTarget.setResult(Activity.RESULT_OK, intent);

            finish();
        }
    }

    protected void setStartFromScreenTop(boolean isFromTop) {
        if (isFromTop != mIsFromScreenTop) {
            if (isFromTop) {
                mContentView.setPadding(0, 0, 0, 0);
            } else {
                float actionBarHeight = getResources().getDimension(R.dimen.ne_action_bar_height);
                if (mNavigationBarContainer.getHeight() > 0) {
                    actionBarHeight = mNavigationBarContainer.getHeight();
                }
                mContentView.setPadding(0, (int) actionBarHeight, 0, 0);
            }
            mIsFromScreenTop = isFromTop;
        }
    }
}
