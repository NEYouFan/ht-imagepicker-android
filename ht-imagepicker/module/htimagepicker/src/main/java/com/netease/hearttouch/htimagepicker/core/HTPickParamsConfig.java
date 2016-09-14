package com.netease.hearttouch.htimagepicker.core;

import com.netease.hearttouch.htimagepicker.core.constants.C;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;

import java.util.List;

/**
 * Created by zyl06 on 3/21/16.
 */
public class HTPickParamsConfig {
    private HTImageFrom from = HTImageFrom.FROM_LOCAL;
    private ImageFolder mImageFolder;
    private List<Image> mImages;
    private int selectLimit = 1;
    private boolean isCut = false;
    private float cutRatio = 1.0f;
    private String title = C.EMPTY;

    public HTPickParamsConfig(HTImageFrom from, ImageFolder imageFolder, List<Image> images) {
        this.from = from;
        this.mImageFolder = imageFolder;
        this.mImages = images;
    }

    public HTPickParamsConfig(HTImageFrom from, ImageFolder imageFolder, List<Image> images,
                              boolean bCut) {
        this.from = from;
        this.mImageFolder = imageFolder;
        this.mImages = images;
        this.isCut = bCut;
    }

    public HTPickParamsConfig(HTImageFrom from, ImageFolder imageFolder, List<Image> images,
                              int selectLimit, String title) {
        this.from = from;
        this.mImageFolder = imageFolder;
        this.mImages = images;
        this.selectLimit = selectLimit;
        this.title = title;
    }

    public HTPickParamsConfig(HTImageFrom from, ImageFolder imageFolder, List<Image> images,
                              boolean bCut, float cutRatio, String title) {
        this.from = from;
        this.mImageFolder = imageFolder;
        this.mImages = images;
        this.isCut = bCut;
        this.cutRatio = cutRatio;
        this.title = title;
    }

    public HTPickParamsConfig(HTImageFrom from, ImageFolder imageFolder, List<Image> images,
                              int selectLimit, boolean bCut, float cutRatio,
                              String title) {
        this.from = from;
        this.mImageFolder = imageFolder;
        this.mImages = images;
        this.selectLimit = selectLimit;
        this.isCut = bCut;
        this.cutRatio = cutRatio;
        this.title = title;
    }

    public HTImageFrom getFrom() {
        return from;
    }

    public ImageFolder getImageFolder() {
        return mImageFolder;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public boolean isCut() {
        return isCut;
    }

    public float getCutRatio() {
        return cutRatio;
    }

    public String getTitle() {
        return title;
    }

    public static HTPickParamsConfigBuilder newBuilder() {
        return new HTPickParamsConfigBuilder();
    }

    private static class HTPickParamsConfigBuilder {
        private HTPickParamsConfig mParamsConfig;

        public HTPickParamsConfigBuilder() {
            mParamsConfig = new HTPickParamsConfig(HTImageFrom.FROM_LOCAL, null, null);
        }

        public HTPickParamsConfigBuilder setFrom(HTImageFrom from) {
            mParamsConfig.from = from;
            return this;
        }

        public HTPickParamsConfigBuilder setImageFolder(ImageFolder imageFolder) {
            mParamsConfig.mImageFolder = imageFolder;
            return this;
        }

        public HTPickParamsConfigBuilder setImages(List<Image> images) {
            mParamsConfig.mImages = images;
            return this;
        }

        public HTPickParamsConfigBuilder setSelectLimit(int selectLimit) {
            mParamsConfig.selectLimit = selectLimit;
            return this;
        }

        public HTPickParamsConfigBuilder setCut(boolean bCut) {
            mParamsConfig.isCut = bCut;
            return this;
        }

        public HTPickParamsConfigBuilder setCutRatio(float cutRatio) {
            mParamsConfig.cutRatio = cutRatio;
            return this;
        }

        public HTPickParamsConfigBuilder setTitle(String title) {
            mParamsConfig.title = title;
            return this;
        }

        public HTPickParamsConfig build() {
            return mParamsConfig;
        }
    }
}
