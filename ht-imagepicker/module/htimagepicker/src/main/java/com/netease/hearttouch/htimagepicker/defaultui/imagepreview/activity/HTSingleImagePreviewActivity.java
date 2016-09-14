package com.netease.hearttouch.htimagepicker.defaultui.imagepreview.activity;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.constants.C;
import com.netease.hearttouch.htimagepicker.core.imagepreview.activity.HTBaseImagePreviewActivity;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.core.util.matrix.OnMatrixChangedListener;
import com.netease.hearttouch.htimagepicker.core.view.NavigationBar;
import com.netease.hearttouch.htimagepicker.core.view.ViewWithNavationBar;
import com.netease.hearttouch.htimagepicker.core.view.photoview.HTPhotoView;

import java.util.List;

/**
 * Created by zyl06 on 2/22/16.
 */
public class HTSingleImagePreviewActivity extends HTBaseImagePreviewActivity {
    private static final int IMG_WIDTH = ContextUtil.INSTANCE.getScreenWidthPixel();
    private static final int IMG_HEIGHT = ContextUtil.INSTANCE.getScreenHeightPixel();

    FrameLayout mNavigationBarContainer;
    FrameLayout mContentView;
    NavigationBar mNavigationBar;
    String mImagePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 确保有一张图片
        mImagePath = mImages.get(0).getAbsolutePath();

        ViewWithNavationBar view = new ViewWithNavationBar(this);
        setContentView(view);
        mNavigationBarContainer = view.getNavigationBarContainer();
        mNavigationBar = view.getNavigationBar();
        mContentView = view.getContentView();

        initContentView(mContentView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initContentView(FrameLayout contentView) {
        View view = LayoutInflater.from(this).inflate(R.layout.htimagepicker_fragment_single_image_preview, contentView, false);
        final HTPhotoView imageView = (HTPhotoView) view.findViewById(R.id.preview_image_view);

        if (mImagePath != null) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            ImageUtil.setImagePath(imageView, mImagePath, IMG_WIDTH, IMG_HEIGHT);
        }
        mContentView.addView(view);

        // The MAGIC happens here!
        imageView.setMatrixChangeListener(new MatrixChangeListener());

        mNavigationBarContainer.setBackgroundResource(R.color.transparent);
        mNavigationBar.setBackgroundResource(R.color.transparent);
        mNavigationBar.setBackButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTSingleImagePreviewActivity.this.finish();
            }
        });
        mNavigationBar.setLeftBackImage(R.drawable.ic_back_arrow);
    }

    @Override
    public List<Image> getSelectedImages() {
        return null;
    }

    private static class MatrixChangeListener implements OnMatrixChangedListener {
        @Override
        public void onMatrixChanged(Matrix m) {
        }
    }
}
