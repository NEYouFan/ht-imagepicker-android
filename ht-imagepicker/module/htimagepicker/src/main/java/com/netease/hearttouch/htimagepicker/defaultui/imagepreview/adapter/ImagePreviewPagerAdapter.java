/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.defaultui.imagepreview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.core.view.photoview.ViewPagerAdapter;
import com.netease.hearttouch.htimagepicker.core.view.photoview.HTPhotoView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zyl06 on 6/25/15.
 */
public class ImagePreviewPagerAdapter extends ViewPagerAdapter {
    private static final int IMG_WIDTH = ContextUtil.INSTANCE.getScreenWidthPixel();
    private static final int IMG_HEIGHT = ContextUtil.INSTANCE.getScreenHeightPixel();


    private Context mContext;
    private LinkedList<String> pathList;

    public ImagePreviewPagerAdapter(Context context, List<String> imagePaths) {
        mContext = context;
        pathList = new LinkedList();
        for (String path : imagePaths) {
            pathList.add(path);
        }
    }

    @Override
    protected View getView(View convertView, int position) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.htimagepicker_item_img_fullscreen, null);

            viewHolder = new ViewHolder();
            viewHolder.photoView = (HTPhotoView) convertView.findViewById(R.id.img_fullscreen);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String path = pathList.get(position);
        viewHolder.photoView.setVisibility(View.VISIBLE);
        ImageUtil.setImagePath(viewHolder.photoView, path, IMG_WIDTH, IMG_HEIGHT);
        viewHolder.photoView.resetToInit(false);

        return convertView;
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

//    @Override
//    public void onClick(View v) {
//        back();
//    }

//    @Override
//    public void onViewTap(View view, float x, float y) {
//        back();
//    }
//
//    private void back() {
//        Activity activity = (Activity) mContext;
//        activity.finish();
//        activity.overridePendingTransition(0,
//                R.anim.anim_img_fullscreen_out);
//    }

    private static class ViewHolder {
        HTPhotoView photoView;
    }
}
