/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.htimagepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.netease.hearttouch.htimagepicker.core.HTImageFrom;
import com.netease.hearttouch.htimagepicker.core.HTImagePicker;
import com.netease.hearttouch.htimagepicker.core.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.core.HTPickParamsConfig;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;
import com.netease.hearttouch.htimagepicker.core.uiconfig.HTRuntimeUIConfig;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
        HTPickFinishedListener {

    ImageFolder mImageFolder;
    List<Image> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.pick_multi_image).setOnClickListener(this);
        findViewById(R.id.pick_single_image).setOnClickListener(this);
        findViewById(R.id.pick_single_image_cut).setOnClickListener(this);
        findViewById(R.id.take_photo).setOnClickListener(this);
        findViewById(R.id.take_photo_cut).setOnClickListener(this);
        findViewById(R.id.custom_take_photo).setOnClickListener(this);
        findViewById(R.id.custom_take_photo_cut).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        HTPickParamsConfig paramConfig = null;
        HTRuntimeUIConfig runtimeUIConfig = null;
        switch (v.getId()) {
            case R.id.pick_multi_image:
                // TODO 改成builder形式
                paramConfig = new HTPickParamsConfig(HTImageFrom.FROM_LOCAL, mImageFolder, mImages, 9, false, 1.0f, "");
                break;
            case R.id.pick_single_image:
                paramConfig = new HTPickParamsConfig(HTImageFrom.FROM_LOCAL, mImageFolder, mImages, false);
                break;
            case R.id.pick_single_image_cut:
                paramConfig = new HTPickParamsConfig(HTImageFrom.FROM_LOCAL, mImageFolder, mImages, true);
                break;
            case R.id.take_photo:
                paramConfig = new HTPickParamsConfig(HTImageFrom.FROM_CAMERA, mImageFolder, mImages, false);
                break;
            case R.id.take_photo_cut:
                paramConfig = new HTPickParamsConfig(HTImageFrom.FROM_CAMERA, mImageFolder, mImages, true);
                break;
            case R.id.custom_take_photo:
                paramConfig = new HTPickParamsConfig(HTImageFrom.FROM_CAMERA, mImageFolder, mImages, false);
                runtimeUIConfig = new HTRuntimeUIConfig(null, null, null, null, null, false);
                break;
            case R.id.custom_take_photo_cut:
                paramConfig = new HTPickParamsConfig(HTImageFrom.FROM_CAMERA, mImageFolder, mImages, true);
                runtimeUIConfig = new HTRuntimeUIConfig(null, null, null, null, null, false);
                break;
        }

        HTImagePicker.INSTANCE.start(this, paramConfig, runtimeUIConfig, this);
    }

    @Override
    public void onImagePickFinished(@Nullable ImageFolder imageFolder, List<Image> images) {
        mImageFolder = imageFolder;
        mImages = images;
        Toast.makeText(this, getString(R.string.confirm_select), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImagePickCanceled() {
        Toast.makeText(this, getString(R.string.cancel_select), Toast.LENGTH_SHORT).show();
    }
}
