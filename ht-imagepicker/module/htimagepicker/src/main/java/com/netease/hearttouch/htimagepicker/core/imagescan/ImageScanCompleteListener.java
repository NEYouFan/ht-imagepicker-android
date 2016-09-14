package com.netease.hearttouch.htimagepicker.core.imagescan;

import java.util.List;

/**
 * Created by zyl06 on 3/28/16.
 */
public interface ImageScanCompleteListener {
    void onScanComplete(List<Thumbnail> thumbnails, List<ImageFolder> imageFolders);
}
