/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagescan;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 3/28/16.
 */
public class ImageFolder implements Parcelable {
    private String mName;
    private String mAbsolutePath;
    private String mUriPath;
    private List<Image> mImages;
    private long mLastModifiedTime;

    public String getName() {
        return mName;
    }

    public String getAbsolutePath() {
        return mAbsolutePath;
    }

    public String getUriPath() {
        return mUriPath;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public long getLastModifiedTime() {
        return mLastModifiedTime;
    }

    public Image getLastestModifiedPhoto() {
        if (mImages == null) return null;
        Image result = null;
        int size = mImages.size();
        for (int i=0; i<size; ++i) {
            Image image = mImages.get(i);
            if (result == null || result.getLastModifiedTime() < image.getLastModifiedTime()) {
                result = image;
            }
        }
        return result;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAbsolutePath(String absolutePath) {
        mAbsolutePath = absolutePath;
    }

    public void setUriPath(String uriPath) {
        mUriPath = uriPath;
    }

    public void setImages(ArrayList<Image> images) {
        mImages = images;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        mLastModifiedTime = lastModifiedTime;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mAbsolutePath);
        dest.writeString(mUriPath);
        dest.writeList(mImages);
        dest.writeLong(mLastModifiedTime);
    }

    public static final Parcelable.Creator<ImageFolder> CREATOR = new Creator<ImageFolder>() {
        public ImageFolder createFromParcel(Parcel source) {
            ImageFolder folder = new ImageFolder();
            folder.setName(source.readString());
            folder.setAbsolutePath(source.readString());
            folder.setUriPath(source.readString());
            folder.setImages(source.readArrayList(Image.class.getClassLoader()));
            folder.setLastModifiedTime(source.readLong());
            return folder;
        }

        public ImageFolder[] newArray(int size) {
            return new ImageFolder[size];
        }
    };
}
