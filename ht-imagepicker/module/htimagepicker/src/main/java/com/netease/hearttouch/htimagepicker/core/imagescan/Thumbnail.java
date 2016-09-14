package com.netease.hearttouch.htimagepicker.core.imagescan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zyl06 on 3/28/16.
 */
public class Thumbnail implements Parcelable {
    int mId;
    String mAbsolutePath;
    String mUriPath;

    public int getId() {
        return mId;
    }

    public String getAbsolutePath() {
        return mAbsolutePath;
    }

    public String getUriPath() {
        return mUriPath;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setAbsolutePath(String absolutePath) {
        mAbsolutePath = absolutePath;
    }

    public void setUriPath(String uriPath) {
        mUriPath = uriPath;
    }

    public int describeContents() {
        return 2;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mAbsolutePath);
        dest.writeString(mUriPath);
    }


    public static final Parcelable.Creator<Thumbnail> CREATOR = new Creator<Thumbnail>() {
        public Thumbnail createFromParcel(Parcel source) {
            Thumbnail t = new Thumbnail();
            t.setId(source.readInt());
            t.setAbsolutePath(source.readString());
            t.setUriPath(source.readString());
            return t;
        }

        public Thumbnail[] newArray(int size) {
            return new Thumbnail[size];
        }
    };
}
