package com.netease.hearttouch.htimagepicker.core.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;

/**
 * Created by zyl06 on 2/18/16.
 */
public class BitmapDecoder {
    private static final String TAG = "HT_BitmapDecoder";

    public static Bitmap decodeFile(String imagePath) {
        return decodeFile(imagePath, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static Bitmap decodeFile(String imagePath, boolean autoRotate) {
        return decodeFile(imagePath, Integer.MAX_VALUE, Integer.MAX_VALUE, autoRotate);
    }

    public static Bitmap decodeFile(String imagePath, int desWidth, int desHeight) {
        return decodeFile(imagePath, desWidth, desHeight, false);
    }

    public static Bitmap decodeFile(String imagePath, int desWidth, int desHeight, boolean autoRotate) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        int[] bounds = decodeBounds(imagePath, bmOptions);

        int scaleFactor = 1;
        if (desWidth > 0 && desHeight > 0) {
            scaleFactor = Math.min(bounds[0] / desWidth, bounds[1] / desHeight);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor > 1 ? scaleFactor : 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);

        if (autoRotate) {
            bitmap = adjustBitmapRotation(imagePath, bitmap);
        }

        return bitmap;
    }

    public static int[] decodeBounds(String imagePath, BitmapFactory.Options bmOptions) {
        if (bmOptions == null) {
            bmOptions = new BitmapFactory.Options();
        }
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        return new int[]{bmOptions.outWidth, bmOptions.outHeight};
    }

    private static Bitmap adjustBitmapRotation(String path, Bitmap srcBitmap) {
        if (TextUtils.isEmpty(path) || srcBitmap == null) {
            return null;
        }

        float rotate = ImageUtil.getImageRotate(path);
        if (rotate != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
            if (dstBitmap == null) {
                return srcBitmap;
            } else {
                if (srcBitmap != null && !srcBitmap.isRecycled()) {
                    srcBitmap.recycle();
                }
                return dstBitmap;
            }
        } else {
            return srcBitmap;
        }
    }
}
