/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.htimagepicker.ui0.imagepick.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageInfoUtil;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.core.util.ViewHolder;

import java.util.List;

/**
 * Created by zyl06 on 6/23/15.
 */
public class ImageFolderListAdapter extends BaseAdapter {
    private List<ImageFolder> mImageFolders;
    private Context mContext;

    public ImageFolderListAdapter(Context context) {
        mImageFolders = ImageInfoUtil.getImageFolders();
        mContext = context;
    }

    @Override
    public int getCount() {
        int count = 0;
        //if (mThumbnailsMap != null) {
        //    count += 1;
        //}
        if (mImageFolders != null) {
            count += mImageFolders.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mImageFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.htimagepicker_item_pick_image_folder_list, null);
        }

        ImageFolder imageFolder = mImageFolders.get(position);

        ImageView albumThumbnail = ViewHolder.get(convertView, R.id.imagefolder_first_thumbnail);
        albumThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Image image = imageFolder.getLastestModifiedPhoto();
        String defaultUrl = imageFolder.getUriPath();
        if (image != null) {
            defaultUrl = image.getUriPath();
        }
        String thumbUrl = ImageInfoUtil.getThumbnailPathWithId(image.getId(), defaultUrl);
        if (thumbUrl != null) {
            Uri uri = Uri.parse(thumbUrl);
            ImageUtil.setImagePath(albumThumbnail, uri.getPath(), 80, 80, true);
        }

        TextView albumName = ViewHolder.get(convertView, R.id.image_folder_name);
        albumName.setText(imageFolder.getName());

        TextView folderImageCount = ViewHolder.get(convertView, R.id.folder_images_count);
        String countUnit = ContextUtil.INSTANCE.getString(R.string.pick_image_count_unit);
        if (imageFolder.getImages() != null) {
            folderImageCount.setText(imageFolder.getImages().size() + countUnit);
        } else {
            folderImageCount.setText("0 " + countUnit);
        }

        return convertView;
    }
}
