package com.netease.hearttouch.htimagepicker.core.imagescan;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.netease.hearttouch.htimagepicker.core.constants.C;
import com.netease.hearttouch.htimagepicker.core.util.storage.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zyl06 on 3/28/16.
 */
public class ImageInfoUtil {
    public static final String ALL_IMAGE_FOLDER_NAME = "allImages";

    private static final String TAG = "ImageInfoUtil";
    // 第0个位置是全部图片的虚拟文件夹
    private static List<ImageFolder> sImageFolders = new ArrayList<>();
    private static LinkedHashMap<Integer, Thumbnail> sThumbnails = new LinkedHashMap<>();

    public static List<ImageFolder> getImageFolders() {
        return sImageFolders;
    }

    public static LinkedHashMap<Integer, Thumbnail> getThumbnails() {
        return sThumbnails;
    }

    public static String getThumbnailPathWithId(int thumbnailId, String defaultPath) {
        Thumbnail t = sThumbnails.get(thumbnailId);
        if (t != null && t.getAbsolutePath() != null) {
            return t.getAbsolutePath();
        }
        return defaultPath;
    }

    public static void updateThumbnails(Context context) {
        sThumbnails.clear();

        ContentResolver cr = context.getContentResolver();

        Uri imageUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        String str[] = {
                MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA
        };
        Cursor cursor = cr.query(imageUri, str, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String imagePath = cursor.getString(1);
                    String uriPath = C.UrlPrefix.FILE + imagePath;
                    Thumbnail thumbnail = new Thumbnail();
                    thumbnail.setId(id);
                    thumbnail.setUriPath(uriPath);
                    thumbnail.setAbsolutePath(imagePath);
                    sThumbnails.put(id, thumbnail);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            safeClose(cursor);
        }
    }

    public static void updateImages(Context context) {
        sImageFolders.clear();

        HashMap<String, ImageFolder> name2Folders = new HashMap<>();
        ArrayList<Image> allImages = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String str[] = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };
        String order = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        Cursor cursor = cr.query(imageUri, str, null, null, order);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String displayName = cursor.getString(1);
                    long timestamp = cursor.getLong(2);
                    String bucketDisplayName = cursor.getString(3);
                    String absolutePath = cursor.getString(4);

                    if (!TextUtils.isEmpty(absolutePath)) {
                        File file = new File(absolutePath);
                        if (file.exists()) {
                            Image pic = updatePictureFolder(name2Folders, id, displayName, timestamp, bucketDisplayName, absolutePath);
                            allImages.add(pic);
                        }
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            safeClose(cursor);
        }

        for (HashMap.Entry<String, ImageFolder> entry : name2Folders.entrySet()) {
            sImageFolders.add(entry.getValue());
        }

        ImageFolder allImageFolder = new ImageFolder();
        allImageFolder.setName(ALL_IMAGE_FOLDER_NAME);
        if (!allImages.isEmpty()) {
            allImageFolder.setLastModifiedTime(allImages.get(0).getLastModifiedTime());
        }
        allImageFolder.setImages(allImages);
        sImageFolders.add(0, allImageFolder);
    }

    private static String sCameraFilePath = null;
    private static final String sCameraFilename = "Camera";
    public static String tryGetSystemCameraPath(Context context) {
        if (sCameraFilePath != null)
            return sCameraFilename;

        Cursor cursor = getCameraImages(context);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String desPathName = null;
                do {
                    String pathName = getFolderName(cursor.getString(1));

                    if (desPathName == null) {
                        desPathName = pathName;
                    }
                    if (pathName.contains(sCameraFilename)) {
                        desPathName = pathName;
                        break;
                    }
                } while (cursor.moveToNext());

                if (!TextUtils.isEmpty(desPathName)) {
                    File file = new File(desPathName);
                    if (file.exists()) {
                        sCameraFilePath = file.getParent();
                        if (!sCameraFilePath.contains(sCameraFilename)) {
                            sCameraFilePath = sCameraFilePath + File.separator + sCameraFilename + File.separator;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            safeClose(cursor);
        }

        return sCameraFilePath;
    }

    private static Cursor getCameraImages(final Context context) {
        final String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA
        };
        final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String query = MediaStore.Images.Media.DATA + " LIKE \"%DCIM%\"";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(images, projection, query,
                    null, MediaStore.Images.Media.DATE_ADDED + " DESC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    private static Image updatePictureFolder(HashMap<String, ImageFolder> name2Folders,
                                             int id,
                                             String name,
                                             long timestamp,
                                             String bucketDisplayName,
                                             String absolutePath) {
        ImageFolder folder = name2Folders.get(bucketDisplayName);
        if (folder == null) {
            folder = new ImageFolder();
            name2Folders.put(bucketDisplayName, folder);
        }
        folder.setName(bucketDisplayName);
        if (timestamp > folder.getLastModifiedTime()) {
            folder.setLastModifiedTime(timestamp);
        }
        if (folder.getAbsolutePath() == null) {
            String folderName = getFolderName(absolutePath);
            folder.setAbsolutePath(folderName);
            folder.setUriPath(C.UrlPrefix.FILE + folderName);
            folder.setImages(new ArrayList<Image>());
        }

        Image pic = new Image();
        pic.setId(id);
        pic.setName(name);
        pic.setLastModifiedTime(timestamp);
        pic.setAbsolutePath(absolutePath);
        pic.setUriPath(C.UrlPrefix.FILE + absolutePath);

        folder.getImages().add(pic);

        return pic;
    }

    private static String getFolderName(String fileName) {
        if (TextUtils.isEmpty(fileName)) return null;
        File file = new File(fileName);
        if (file.isDirectory())
            return fileName;

        return file.exists() ? file.getParent() : null;
    }

    private static void safeClose(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
