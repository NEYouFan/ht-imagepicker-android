/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.view.photoview;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * view pager adapter，复用view
 *
 * @author viyu
 */
public abstract class ViewPagerAdapter extends PagerAdapter {
    private SparseArray<View> mPageViews = new SparseArray<View>();

    public ViewPagerAdapter() {
        super();
    }

    /**
     * 获取view方法，子类实现这个方法来获取渲染View
     *
     * @param convertView 如果是null则没有可复用的View，如果非null则是可复用的View
     * @param position
     * @return
     */
    protected abstract View getView(View convertView, int position);

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 获取复用的view给子类去用，没有可复用的view时为null
        View view = getView(pullViewFromPool(), position);
//        resetPhotoViews();

        // 记录该view以在destroyItem中能找到
        mPageViews.put(position, view);
        // 添加到view pager
        container.addView(view);

        return view;
    }

    private void resetPhotoView(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                View subView = vg.getChildAt(i);
                if (subView instanceof HTPhotoView) {
                    ((HTPhotoView) subView).resetToInit(true);
                }
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mPageViews.get(position);
//        resetPhotoView(view);
        // 把要删除的view放到pool里以供复用
        pushViewToPool(view);
        // 从view pager中删除
        container.removeView(view);
    }

    /******************
     * 复用view start
     ******************************/
    private List<View> mPageViewPool = new ArrayList<View>();

    public void resetPhotoViews() {
        int size = mPageViews.size();
        for (int i = 0; i < size; i++) {
            View v = mPageViews.get(i);
            resetPhotoView(v);
        }
    }

    private View pullViewFromPool() {
        View view = null;
        for (View v : mPageViewPool) {
            view = v;
            break;
        }
        if (view != null) {
            mPageViewPool.remove(view);
        }
        return view;
    }

    private void pushViewToPool(View view) {
        if (!mPageViewPool.contains(view)) {
            mPageViewPool.add(view);
        }
    }
    /****************** 复用view end ******************************/
}
