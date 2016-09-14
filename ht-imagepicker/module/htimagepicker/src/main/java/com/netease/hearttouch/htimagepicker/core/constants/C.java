package com.netease.hearttouch.htimagepicker.core.constants;

import android.os.Build;

/**
 * Created by zyl06 on 2/18/16.
 */
public class C {
    public final static long K = 1024;

    public final static long M = 1024 * 1024;


    // 关于文件后缀的常量
    public static final class FileSuffix {
        public static final String JPG = ".jpg";

        public static final String PNG = ".png";

        public static final String BMP = ".bmp";

        public static final String GIF = ".gif";
    }

    public static final class UrlPrefix {
        public static final String FILE = "file://";
        public static final String HTTP = "http://";
        public static final String HTTPS = "https://";
    }

    public static final int MIN_SDK_ENABLE_LAYER_TYPE_HARDWARE = Build.VERSION_CODES.ICE_CREAM_SANDWICH;

    public static final int DEFAULT_PREVIEW_IMAGE_SIZE = 500;

    public static final String EMPTY = "";
}
