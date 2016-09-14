/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by zyl06 on 2/18/16.
 */
public class FileUtil {
    private static final String TAG = "HT_FileUtil";

    public static String getFileParentPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            Log.e(TAG, "getFileParentPath, filePath is empty");
            return null;
        }

        File file = new File(filePath);
//        if (!file.exists()) {
//            Log.e(TAG, "getFileParentPath, filePath is invalid, file does not exist");
//            return null;
//        }

        return file.getParent();
    }

    public static File getExistFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }

        return null;
    }

    public static void copyFile(File fromFile, File toFile, Boolean rewrite ) {
        if (!fromFile.exists()) {
            return;
        }

        if (!fromFile.isFile()) {
            return ;
        }

        if (!fromFile.canRead()) {
            return ;
        }

        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }

        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte bt[] = new byte[1024];
            int c;

            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }

            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            Log.e("readfile", ex.getMessage());
        }
    }
}
