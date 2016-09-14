package com.netease.hearttouch.htimagepicker.core.imagecut;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.netease.hearttouch.htimagepicker.core.HTImageFrom;
import com.netease.hearttouch.htimagepicker.core.view.HTImageCutView;
import com.netease.hearttouch.htimagepicker.core.imagepick.activity.Extras;
import com.netease.hearttouch.htimagepicker.core.util.image.ImageUtil;

import java.io.File;

/**
 * Created by zyl06 on 2/23/16.
 */
public abstract class HTBaseImageCutActivity
        extends AppCompatActivity
        implements HTImageCutInterface {
    public static final String CANCEL_CUT = "HTBaseImageCropActivity_cancelCut";
    public static final String CONFIRM_CUT = "HTBaseImageCropActivity_confirmCut";

    protected boolean mReturnData;
    protected String mFilePath;
    protected String mFrom;
    protected String mSrcFile;
    protected float mCutRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processIntent();
        postInitCutImageView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            HTImageCutView icv = getImageCutView();
            if (icv != null) {
                icv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        initCutImageView();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        cancelCutFinish();
    }

    @Override
    final public void confirmCutFinish() {
        HTImageCutView imageCutView = getImageCutView();

        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_RETURN_DATA, CONFIRM_CUT);
        if (mFrom != null) intent.putExtra(Extras.EXTRA_FROM, mFrom);

        if (mReturnData) {
            byte[] data = imageCutView.getCuttedBitmapData();
            if (data != null) {
                intent.putExtra(Extras.EXTRA_DATA, data);
                setResult(RESULT_OK, intent);
            }
        } else {
            Bitmap bitmap = imageCutView.getCuttedBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                ImageUtil.saveBitmap(bitmap, mFilePath, true);

                ImageUtil.addImageToGallery(mFilePath, this);
                setResult(RESULT_OK, intent);
            }
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (TextUtils.equals(mFrom, HTImageFrom.FROM_CAMERA.toString())) {
            if (!TextUtils.isEmpty(mSrcFile)) {
                File file = new File(mSrcFile);
                if (file.exists()) {
                    file.delete();
                }
            }

        }
    }

    @Override
    final public void cancelCutFinish() {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_RETURN_DATA, CANCEL_CUT);
        if (mFrom != null) intent.putExtra(Extras.EXTRA_FROM, mFrom);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void postInitCutImageView() {
        // 抛到下一个UI循环，等到我们的activity真正到了前台
        // 否则可能会获取不到openGL的最大texture size，导致解出的bitmap过大，显示不了
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initCutImageView();
            }
        });
    }

    private void initCutImageView() {
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            HTImageCutView cutImageView = getImageCutView();
            cutImageView.setCutWidthHeightRatio(1.0f);
            cutImageView.setBitmapFilePath(mSrcFile);
        }
    }

    private void processIntent() {
        Intent intent = getIntent();
        mReturnData = intent.getBooleanExtra(Extras.EXTRA_RETURN_DATA, false);
        mFilePath = intent.getStringExtra(Extras.EXTRA_FILE_PATH);
        mFrom = intent.getStringExtra(Extras.EXTRA_FROM);

        mSrcFile = intent.getStringExtra(Extras.EXTRA_SRC_FILE);
        mCutRatio = intent.getFloatExtra(Extras.EXTRA_CUT_RATIO, 1.0f);
    }
}
