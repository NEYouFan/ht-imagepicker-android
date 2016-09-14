package com.netease.hearttouch.htimagepicker.core.imagepick.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.netease.hearttouch.htimagepicker.core.HTImageFrom;
import com.netease.hearttouch.htimagepicker.core.HTImagePicker;
import com.netease.hearttouch.htimagepicker.core.HTPickFinishedListener;
import com.netease.hearttouch.htimagepicker.core.HTPickParamsConfig;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;
import com.netease.hearttouch.htimagepicker.core.uiconfig.HTRuntimeUIConfig;
import com.netease.hearttouch.htimagepicker.core.uiconfig.HTUIConfig;
import com.netease.hearttouch.htimagepicker.core.imagepick.listener.ImagePickerListenerCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 3/21/16.
 */
public class ImagePickHelper {
    public static void start(Context context, HTPickParamsConfig paramConfig,
                             HTRuntimeUIConfig uiConfig, HTPickFinishedListener listener) {
        HTUIConfig htuiConfig = HTImagePicker.INSTANCE.getUIConfig();
        htuiConfig.setRuntimeUIConfig(uiConfig);
        start(htuiConfig.getImagePickActivityClass(),
                context,
                paramConfig.getFrom(),
                paramConfig.getImageFolder(),
                paramConfig.getImages(),
                paramConfig.getSelectLimit(),
                paramConfig.isCut(),
                paramConfig.getCutRatio(),
                paramConfig.getTitle(),
                listener);
    }

    private static void start(@NonNull Class pickImageActivityClass,
                              Context context,
                              HTImageFrom from,
                              ImageFolder imageFolder,
                              List<Image> images,
                              int selectLimit,
                              boolean bCut,
                              float cutRatio,
                              String title,
                              HTPickFinishedListener listener) {

        Intent intent = new Intent(context, pickImageActivityClass);
        intent.putExtra(Extras.EXTRA_FROM, from);
        // 注释掉,在 HTBaseImagePickActivity 中判断输出文件路径是不是为空,如果为空则使用 HTImagePicker.INSTANCE.getUIConfig().getPhotoFileSavePath()
        // 避免在这里就要处理动态权限 READ_EXTERNAL_STORAGE
        // intent.putExtra(Extras.EXTRA_FILE_PATH, HTImagePicker.INSTANCE.getUIConfig().getPhotoFileSavePath());
        intent.putExtra(Extras.EXTRA_SELECT_LIMIT, selectLimit);
        intent.putExtra(Extras.EXTRA_NEED_CUT, bCut);
        intent.putExtra(Extras.EXTRA_CUT_RATIO, cutRatio);

        if (!TextUtils.isEmpty(title)) {
            intent.putExtra(Extras.EXTRA_PICK_TITLE, title);
        }

        if (imageFolder != null) {
            intent.putExtra(Extras.EXTRA_IMAGE_FOLDER, imageFolder);
        }
        if (images == null) images = new ArrayList<>();
        intent.putParcelableArrayListExtra(Extras.EXTRA_IMAGE_LIST, new ArrayList<Parcelable>(images));

        int key = ImagePickerListenerCache.insertImagePickerListener(context, listener);
        intent.putExtra(Extras.EXTRA_LISTENER_KEY, key);

        context.startActivity(intent);
    }
}
