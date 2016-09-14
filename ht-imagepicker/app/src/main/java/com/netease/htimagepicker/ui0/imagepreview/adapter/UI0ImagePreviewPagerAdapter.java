package com.netease.htimagepicker.ui0.imagepreview.adapter;

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
public class UI0ImagePreviewPagerAdapter extends ViewPagerAdapter {
    static final int IMG_WIDTH = ContextUtil.INSTANCE.getContext().getResources().getDisplayMetrics().widthPixels;

    private Context mContext;
    private LinkedList<String> pathList;

    public UI0ImagePreviewPagerAdapter(Context context, List<String> imagePaths) {
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
        ImageUtil.setImagePath(viewHolder.photoView, path, IMG_WIDTH, IMG_WIDTH);
        viewHolder.photoView.resetToInit(false);

        return convertView;
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    private static class ViewHolder {
        HTPhotoView photoView;
    }
}
