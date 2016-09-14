/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.netease.hearttouch.htimagepicker.core.HTImagePicker;
import com.netease.hearttouch.htimagepicker.core.util.storage.StorageUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zyl06 on 3/6/16.
 */
public class HTCameraActivity extends AppCompatActivity {

    private static final String TAG = "HTCameraActivity";
    private Uri mPhotoUri;

    public static void startForResult(Activity activity, Uri outputFile, int requestId) {
        Intent intent = new Intent(activity, HTCameraActivity.class);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFile);
        activity.startActivityForResult(intent, requestId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        HTBaseCameraFragment fragment = null;
        try {
            Class fragmentClazz = HTImagePicker.INSTANCE.getUIConfig().getCameraFragmentClazz();
            Constructor<HTBaseCameraFragment> fragmentConstructor = fragmentClazz.getConstructor();
            fragment = fragmentConstructor.newInstance();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        // 如果发生错误，就直接返回
        if (fragment == null) {
            finish(null);
            return;
        }

        Uri photoPath = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        if (photoPath != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MediaStore.EXTRA_OUTPUT, photoPath);
            fragment.setArguments(bundle);
        }

        fragmentTransaction.add(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    /* package */ void finish(Uri uri) {
        Intent intent = getIntent();
        if (uri != null) {
            intent.setData(uri);
            setResult(RESULT_OK, intent);
        } else {
            removePhoto();
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        finish(null);
    }

    final public File getPhotoPath() {
        if (mPhotoUri == null) {

            String filePath = StorageUtil.getWriteSystemCameraPath(getPhotoFilename(), false);
            if (filePath != null) {
                File file = new File(filePath);
                mPhotoUri = Uri.fromFile(file);
            }
        }
        return (mPhotoUri != null) ?
                new File(mPhotoUri.getPath()) :
                null;
    }

    public String getPhotoFilename() {
        if (mPhotoUri != null) {
            File file = new File(mPhotoUri.getPath());
            return file.getName();
        } else {
            String ts = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)).format(new Date());
            return "Photo_" + ts + "_.jpg";
        }
    }

    /*package*/ void removePhoto() {
        File file = getPhotoPath();
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}