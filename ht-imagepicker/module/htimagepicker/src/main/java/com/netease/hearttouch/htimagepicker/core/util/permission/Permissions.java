package com.netease.hearttouch.htimagepicker.core.util.permission;

import android.Manifest;

/**
 * Created by zyl06 on 9/13/16.
 */

public class Permissions {
    public static final String[] PERMISSIONS = new String[] {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
            "android.permission.READ_EXTERNAL_STORAGE",
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
}
