/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.defaultui.imagepick.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageInfoUtil;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.core.util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/23/15.
 */
public class ImageGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Image> mImages = new ArrayList<>();
    private List<Image> mSelectedPhotos;
    private LayoutInflater mInflater;
    private static final int IMAGE_WIDTH = (int) ContextUtil.INSTANCE.getScreenWidthDp() / 3;
    private static final int IMAGE_HEIGHT = IMAGE_WIDTH;

    public ImageGridAdapter(Context context, ImageFolder imageFolder,
                            @Nullable List<Image> selectedImages) {
        mContext = context;

        initAllImages(imageFolder);
        mSelectedPhotos = selectedImages != null ? selectedImages : new ArrayList<Image>();
        mInflater = LayoutInflater.from(mContext);
    }

    public void changeImageFolder(ImageFolder imageFolder, @Nullable List<Image> selectedImages) {
        initAllImages(imageFolder);

        mSelectedPhotos = selectedImages != null ? selectedImages : new ArrayList<Image>();
        notifyDataSetChanged();
    }

    private void initAllImages(ImageFolder imageFolder) {
        if (imageFolder != null && imageFolder.getImages() != null) {
            mImages = imageFolder.getImages();
        }
    }

    @Override
    public int getCount() {
        // 0 是拍照
        return mImages.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        // 0 是拍照
        return mImages.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (position == 0)
                convertView = mInflater.inflate(R.layout.htimagepicker_item_pick_image_grid_view_camera, null);
            else {
                convertView = mInflater.inflate(R.layout.htimagepicker_item_pick_image_grid_image_view, null);
                TextView orderView = ViewHolder.get(convertView, R.id.order_number);
            }
            int hMargin = (int) ContextUtil.INSTANCE.getDimen(R.dimen.ne_pick_image_horizontal_margin);
            int hSpace = (int) ContextUtil.INSTANCE.getDimen(R.dimen.ne_pick_image_grid_internal_space);
            int height = (ContextUtil.INSTANCE.getScreenWidthPixel() - 2 * hMargin - 2 * hSpace) / 3;
            GridView.LayoutParams lp = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, height);
            convertView.setLayoutParams(lp);
        }

        if (position > 0) {
            Image image = mImages.get(position - 1);

            ImageView photoThumbnail = ViewHolder.get(convertView, R.id.pick_image_photo);
            String thumbUrl = ImageInfoUtil.getThumbnailPathWithId(image.getId(), image.getUriPath());

            String oldThumbUrl = (String) photoThumbnail.getTag();
            if (oldThumbUrl == null || !oldThumbUrl.equals(thumbUrl)) {
                photoThumbnail.setTag(thumbUrl);
                Uri uri = Uri.parse(thumbUrl);
                ImageUtil.setImagePath(photoThumbnail, uri.getPath(), IMAGE_WIDTH, IMAGE_HEIGHT, true);
            }

            ImageView pickMarkRect = ViewHolder.get(convertView, R.id.pick_image_mark_rect);
            ImageView pickMarkCorner = ViewHolder.get(convertView, R.id.pick_image_mark_corner);
            int pickOrder = mSelectedPhotos.indexOf(image);
            if (pickOrder >= 0) {
                pickMarkRect.setVisibility(View.VISIBLE);
                pickMarkCorner.setVisibility(View.VISIBLE);
            } else {
                pickMarkRect.setVisibility(View.GONE);
                pickMarkCorner.setVisibility(View.GONE);
            }

            TextView orderView = ViewHolder.get(convertView, R.id.order_number);
            String text = pickOrder >= 0 ? "" + (pickOrder + 1) : "";
            orderView.setText(text);
        }

        return convertView;
    }
}
