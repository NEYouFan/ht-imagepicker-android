package com.netease.hearttouch.htimagepicker.core.util.storage;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.constants.C;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageInfoUtil;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;
import com.netease.hearttouch.htimagepicker.core.util.image.ImageUtil;

import java.io.File;

/**
 * Created by zyl06 on 3/30/16.
 */
public class StorageUtil {
    public static final long MIN_CAPACITY = 20 * C.M;
    public static final long LOW_CAPACITY = 100 * C.M;

    public static String getAppWritePath(String folderName, boolean isToast) {
        if (TextUtils.isEmpty(folderName))
            return null;

        String folderPath = ContextUtil.INSTANCE.getAppDir() + folderName;
        File folderFile = new File(folderPath);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        if (checkSdcardCapacity(folderFile, isToast)) {
            return folderPath;
        }

        return null;
    }

    private static Boolean sIsSystemCameraPathWritable = null;
    private static boolean isSystemCameraPathWritable() {
        if (sIsSystemCameraPathWritable != null)
            return sIsSystemCameraPathWritable;

        File file = getUnexistedSystemCameraFile();
        try {
            if (file != null) {
                file.createNewFile();
                file.delete();
            } else {
                sIsSystemCameraPathWritable = false;
            }
        } catch (Exception e) { // IOException or FileNotFoundException
            sIsSystemCameraPathWritable = false;
        }

        if (sIsSystemCameraPathWritable == null)
            sIsSystemCameraPathWritable = true;

        return sIsSystemCameraPathWritable;
    }

    private static File getUnexistedSystemCameraFile() {
        String cameraPath = getSystemCameraFolderPath();
        if (TextUtils.isEmpty(cameraPath))
            return null;

        int count = 10;
        while (count-- > 0) {
            String tmpFileName = "__htimagepicker_tmp_filename_" + System.currentTimeMillis() + "_" + count;

            String tmpFilePath = cameraPath + File.separator + tmpFileName;

            File file = new File(tmpFilePath);
            if (!file.exists())
                return file;
        }

        return null;
    }

    public static String getWriteSystemCameraPath(String fileName, boolean isToast) {
        String result = null;

        if (isSystemCameraPathWritable()) {
            String folderPath = getSystemCameraFolderPath();
            if (!TextUtils.isEmpty(folderPath)) {
                File folderFile = new File(folderPath);
                if (!folderFile.exists()) {
                    folderFile.mkdirs();
                }
                if (checkSdcardCapacity(folderFile, isToast)) {
                    result = folderPath + fileName;
                }
            }
        } else {
            String newFolder = ImageUtil.getDefaultImageSaveFolderPath(isToast);
            if (!TextUtils.isEmpty(newFolder)) {
                File newFolderFile = new File(newFolder);
                if (!newFolderFile.exists()) {
                    newFolderFile.mkdirs();
                }
                if (checkSdcardCapacity(newFolderFile, isToast)) {
                    result = newFolder + File.separator + fileName;
                }
            }
        }

        return result;
    }

    private static String sCameraFilePath = null;
    private static String getSystemCameraFolderPath() {
        if (sCameraFilePath != null) return sCameraFilePath;

        sCameraFilePath = ImageInfoUtil.tryGetSystemCameraPath(ContextUtil.INSTANCE.getContext());
        if (TextUtils.isEmpty(sCameraFilePath)) {
            String filePath = getWriteSystemDCIMPath("__htimagepicker_tmp_filename__.tmp", false);
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                sCameraFilePath = file.getParent() + File.separator + "Camera" + File.separator;
            }
        }

        return sCameraFilePath;
    }

    public static String getWriteSystemDCIMPath(String fileName, boolean tip) {
        return getWriteSystemPath(fileName, Environment.DIRECTORY_DCIM, tip);
    }

    public static String getWritePicturePath(String fileName, boolean tip) {
        return getWriteSystemPath(fileName, Environment.DIRECTORY_PICTURES, tip);
    }

    public static String getWriteSystemPath(String fileName, String directoryType, boolean tip) {
        if (checkSdcardCapacity(tip)) {
            File file = Environment.getExternalStoragePublicDirectory(directoryType);
            if (file != null) {
                String path = file.getAbsolutePath();

                if (TextUtils.isEmpty(path)) {
                    return null;
                }
                File dir = new File(path).getParentFile();
                if (dir != null && !dir.exists()) {
                    dir.mkdirs();
                }
                return path + File.separator + fileName;
            }
        }
        return null;
    }

    /**
     * @param isToast 当sdcard容量小于 LOW_CAPACITY 时，是否打toast提示
     * @return 是否有空间存储内容（图片）
     */
    public static final boolean checkSdcardCapacity(boolean isToast) {
        File file = Environment.getExternalStorageDirectory();
        return checkSdcardCapacity(file, isToast);
    }

    /**
     * @param file 指定文件夹
     * @param isToast 当文件夹容量小于 LOW_CAPACITY 时，是否打toast提示
     * @return 是否有空间存储内容（图片）
     */
    public static final boolean checkSdcardCapacity(File file, boolean isToast) {
        if (!isSdcardExist())
            return false;

        long freeSize = getSdcardFreeSize(file);
        if (freeSize < MIN_CAPACITY) {
            if (isToast) {
                ContextUtil.INSTANCE.makeToast(R.string.pick_image_sdcard_not_enough_error);
            }
            return false;
        }

        if (freeSize < LOW_CAPACITY && isToast) {
            ContextUtil.INSTANCE.makeToast(R.string.pick_image_sdcard_not_enough_warning);
        }

        return true;
    }

    /**
     * 判断 sdcard 是否存在
     * @return
     */
    public static boolean isSdcardExist() {
        String state = Environment.getExternalStorageState();
        return (TextUtils.equals(state, Environment.MEDIA_MOUNTED));
    }

    /**
     * @return sdcard 的剩余空间，单位 Byte
     */
    public static long getSdcardFreeSize() {
        File file = Environment.getExternalStorageDirectory();
        return getSdcardFreeSize(file);
    }


    /**
     * @return sdcard中指定目录文件夹的剩余空间，单位 Byte
     */
    @TargetApi(18)
    public static long getSdcardFreeSize(File file) {
        StatFs sf = new StatFs(file.getPath());
        long blockSize, freeBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
            freeBlocks = sf.getAvailableBlocksLong();
        } else {
            blockSize = sf.getBlockSize();
            freeBlocks = sf.getAvailableBlocks();
        }

        return blockSize * freeBlocks;
    }
}
