package com.netease.hearttouch.htimagepicker.core.imagescan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zyl06 on 3/28/16.
 */
public class Image implements Parcelable {
    private int mId;
    private String mName;
    private String mAbsolutePath;
    private String mUriPath;
    private long mLastModifiedTime;

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getAbsolutePath() {
        return mAbsolutePath;
    }

    public String getUriPath() {
        return mUriPath;
    }

    public long getLastModifiedTime() {
        return mLastModifiedTime;
    }

    public void setId(int id) {
        mId = id;
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

    public void setLastModifiedTime(long lastModifiedTime) {
        mLastModifiedTime = lastModifiedTime;
    }

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mAbsolutePath);
        dest.writeString(mUriPath);
        dest.writeLong(mLastModifiedTime);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            Image pic = new Image();
            pic.setId(source.readInt());
            pic.setName(source.readString());
            pic.setAbsolutePath(source.readString());
            pic.setUriPath(source.readString());
            pic.setLastModifiedTime(source.readLong());

            return pic;
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
