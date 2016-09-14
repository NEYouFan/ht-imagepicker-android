/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.util.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zyl06 on 2/19/16.
 */
public class PopupwindowMenu {
    public final static int DEFAULT_MARGIN = ContextUtil.INSTANCE.dip2px(49);

    private ArrayList<String> mItemList;
    private Context mContext;
    private PopupWindow mPopupWindow;
    private ListView lv_content;

    public PopupwindowMenu(Context context) {
        this(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
    }

    public PopupwindowMenu(Context context, int width, int height, int gravity) {
        this.mContext = context;

        mItemList = new ArrayList<String>();

        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.htimagepicker_popupwindow_menu_center, null);
        view.setGravity(gravity);

        //设置Listview
        lv_content = (ListView) view.findViewById(R.id.lv_content);
        lv_content.setAdapter(new PopupMenuAdapter());
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lv_content.getLayoutParams();
        lp.width = width;
        lp.height = height;
        lv_content.setLayoutParams(lp);

        view.findViewById(R.id.ll_mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public static PopupwindowMenu PopupwindowMenuWithMargin(Context context) {
        return new PopupwindowMenu(
                context,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                DEFAULT_MARGIN, 0, DEFAULT_MARGIN, 0, Gravity.CENTER);
    }


    public PopupwindowMenu(Context context, int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom, int gravity) {
        this.mContext = context;

        mItemList = new ArrayList<String>();

        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.htimagepicker_popupwindow_menu_center, null);
        view.setGravity(gravity);

        //设置Listview
        lv_content = (ListView) view.findViewById(R.id.lv_content);
        lv_content.setAdapter(new PopupMenuAdapter());
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) lv_content.getLayoutParams();
        lp.width = width;
        lp.height = height;
        lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        lv_content.setLayoutParams(lp);

        view.findViewById(R.id.ll_mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }


    //设置菜单项点击监听器
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        lv_content.setOnItemClickListener(listener);
    }

    //批量添加菜单项
    public void addItems(String[] items) {
        Collections.addAll(mItemList, items);
    }

    //批量添加菜单项
    public void addItems(List<String> items) {
        mItemList.addAll(items);
    }

    //单个添加菜单项
    public void addItem(String item) {
        mItemList.add(item);
    }

    //下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(View parent) {
        showAsDropDown(parent, 0, mContext.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));
    }

    //下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(View parent, int xOffset, int yOffset) {
        //保证尺寸是根据屏幕像素密度来的
        mPopupWindow.showAsDropDown(parent, xOffset, yOffset);
        //使其聚焦
        mPopupWindow.setFocusable(true);
        //设置允许在外点击消失
        mPopupWindow.setOutsideTouchable(true);
        //刷新状态
        mPopupWindow.update();
    }

    /**
     * 显示在父View正中央
     *
     * @param parent
     */
    public void showInCenter(View parent) {
        //保证尺寸是根据屏幕像素密度来的
        mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        //使其聚焦
        mPopupWindow.setFocusable(true);
        //设置允许在外点击消失
        mPopupWindow.setOutsideTouchable(true);
        //刷新状态
        mPopupWindow.update();
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        //保证尺寸是根据屏幕像素密度来的
        mPopupWindow.showAtLocation(parent, gravity, x, y);
        //使其聚焦
        mPopupWindow.setFocusable(true);
        //设置允许在外点击消失
        mPopupWindow.setOutsideTouchable(true);
        //刷新状态
        mPopupWindow.update();
    }

    public void setAnimationStyle(int anim) {
        mPopupWindow.setAnimationStyle(anim);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mPopupWindow.setOnDismissListener(listener);
    }

    public void setCustomAdapter(ListAdapter adapter) {
        lv_content.setAdapter(adapter);
    }

    private final class PopupMenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public String getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.htimagepicker_item_popupwindow_menu, null);
            }

            String title = mItemList.get(position);

            TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
            View v_divider = ViewHolder.get(convertView, R.id.v_divider);
            if (position == getCount() - 1) {
                v_divider.setVisibility(View.GONE);
            } else {
                v_divider.setVisibility(View.VISIBLE);
            }

            tv_title.setText(title);
            return convertView;
        }
    }
}
