/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zyl06 on 2/18/16.
 */
public enum ContextUtil {
    INSTANCE;

    private static String TAG = "HTImagePicker_Context";

    Context mContext;
    private static ContextUtil sInstance;
    private static Object sMutes = new Integer(1);

    private ContextUtil() {}

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public String getAppName() {
        String packageName = mContext.getPackageName();
        int index = packageName.lastIndexOf(".");
        if (index > -1) {
            return packageName.substring(index+1);
        }
        return null;
    }

    public String getAppVersionName() {
        PackageInfo info = getAppPackageInfo();
        return info != null ? info.versionName : null;
    }

    public int getAppVersionCode() {
        PackageInfo info = getAppPackageInfo();
        return info != null ? info.versionCode : 0;
    }

    private PackageInfo getAppPackageInfo() {
        String packageName = mContext.getPackageName();
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public void makeToast(@StringRes int stringId) {
        String content = mContext.getString(stringId);
        makeToast(content);
    }

    public void makeToast(@StringRes int stringId, Object... args) {
        String content = String.format(mContext.getString(stringId), args);
        makeToast(content);
    }

    public void makeToast(String content) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
        }
    }

    public String getString(@StringRes int stringId) {
        return mContext.getString(stringId);
    }

    public String stringFormat(@StringRes int stringId, Object... args) {
        return String.format(mContext.getString(stringId), args);
    }

    public float getDimen(int dimenId) {
        return mContext.getResources().getDimension(dimenId);
    }

    public int getColor(@ColorRes int colorId) {
        return mContext.getResources().getColor(colorId);
    }

    public float getScreenWidthDp() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.widthPixels / dm.density;
    }

    public float getScreenHeightDp() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.heightPixels / dm.density;
    }

    public int getScreenWidthPixel() {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeightPixel() {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    public String getAppDir() {
        return Environment.getExternalStorageDirectory().getPath() + "/" + getAppName() + "/";
    }

    public float getScreenDensity() {
        return mContext.getResources().getDisplayMetrics().density;
    }

    public int dip2px(float dipValue) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }

    public int px2dip(float pxValue) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }
}
