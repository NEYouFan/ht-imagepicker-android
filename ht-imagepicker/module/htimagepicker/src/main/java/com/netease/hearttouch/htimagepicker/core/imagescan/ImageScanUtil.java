package com.netease.hearttouch.htimagepicker.core.imagescan;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/23/15.
 */
public class ImageScanUtil {
    private static final String TAG = "ImageScan";

    private static class ImageScanTask extends AsyncTask<Void, Void, Object> {
        ImageScanCompleteListener mScanComplete = null;
        private Context mContext;

        public ImageScanTask(Context context, ImageScanCompleteListener scanComplete) {
            mContext = context;
            mScanComplete = scanComplete;
        }

        @Override
        protected Object doInBackground(Void... params) {
            ImageInfoUtil.updateThumbnails(mContext);
            ImageInfoUtil.updateImages(mContext);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (mScanComplete != null) {
                List<Thumbnail> thumbnails = new ArrayList<>(ImageInfoUtil.getThumbnails().values());
                mScanComplete.onScanComplete(thumbnails, ImageInfoUtil.getImageFolders());
            }
        }
    }

    public static void start(@Nullable ImageScanCompleteListener listener) {
        ImageScanTask task = new ImageScanTask(ContextUtil.INSTANCE.getContext(), listener);
        task.execute();
    }
}
