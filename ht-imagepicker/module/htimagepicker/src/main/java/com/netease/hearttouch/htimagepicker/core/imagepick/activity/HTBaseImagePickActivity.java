/*
 * Copyright by Netease (c) 2016.
 * This source code is licensed under the MIT-style license found in the LICENSE file
 *  in the root directory of this source tree.
 */

package com.netease.hearttouch.htimagepicker.core.imagepick.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.netease.hearttouch.htimagepicker.core.HTImageFrom;
import com.netease.hearttouch.htimagepicker.core.HTImagePicker;
import com.netease.hearttouch.htimagepicker.core.camera.HTCameraActivity;
import com.netease.hearttouch.htimagepicker.core.constants.C;
import com.netease.hearttouch.htimagepicker.core.imagecut.ImageCutHelper;
import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.imagescan.Image;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageFolder;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageHelper;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageInfoUtil;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageScanCompleteListener;
import com.netease.hearttouch.htimagepicker.core.imagescan.Thumbnail;
import com.netease.hearttouch.htimagepicker.core.util.image.ImageUtil;
import com.netease.hearttouch.htimagepicker.core.util.permission.Permissions;
import com.netease.hearttouch.htimagepicker.core.util.permission.PermissionsActivity;
import com.netease.hearttouch.htimagepicker.core.util.permission.PermissionsChecker;
import com.netease.hearttouch.htimagepicker.core.util.storage.StorageUtil;
import com.netease.hearttouch.htimagepicker.defaultui.imagecut.activity.HTImageCutActivity;
import com.netease.hearttouch.htimagepicker.core.imagepick.event.EventUpdateSelectedPhotosModel;
import com.netease.hearttouch.htimagepicker.core.imagepick.event.InternalUpdateSelectedPhotosListener;
import com.netease.hearttouch.htimagepicker.core.imagepick.listener.ImagePickerListenerCache;
import com.netease.hearttouch.htimagepicker.core.imagepreview.activity.ImagePreviewHelper;
import com.netease.hearttouch.htimagepicker.core.imagepreview.listener.IIntentProcess;
import com.netease.hearttouch.htimagepicker.core.imagescan.ImageScanUtil;
import com.netease.hearttouch.htimagepicker.core.util.FileUtil;
import com.netease.hearttouch.htimagepicker.core.util.image.BitmapCache;
import com.netease.hearttouch.htimagepicker.core.util.ContextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zyl06 on 2/20/16.
 */
public abstract class HTBaseImagePickActivity extends AppCompatActivity
        implements ImageScanCompleteListener,
        InternalUpdateSelectedPhotosListener,
        HTImagePickInterface {

    public static final int REQUEST_CODE_LOCAL = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    public static final int REQUEST_CODE_CUT = 3;
    public static final int REQUEST_CODE_PERMISSION = 4;


    private static final int DEFAULT_SELECT_LIMIT_SIZE = 9;
    private int mSelectLimit;
    protected String mTitle;
    private boolean mIsCut = false;
    private HTImageFrom mImageFrom = HTImageFrom.FROM_LOCAL;
    private String mFileOutPath;
    protected int mPickListenerKey;
    protected ImageFolder mCurrImageFolder;

    // 当前已经选中的图片列表
    protected List<Image> mSelectedImages = new ArrayList<>();

    private String mCameraFileName;

    private PermissionsChecker mPermissionsChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(Permissions.PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE_PERMISSION, Permissions.PERMISSIONS);
        } else {
            processFromPermission(PermissionsActivity.PERMISSIONS_GRANTED);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // 避免三星手机，拍照自动旋转照成图片不见了
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPickListenerKey != ImagePickerListenerCache.INVALID_IMAGE_PICK_KEY) {
            callFinishListener(mPickListenerKey, isConfirmUse());
            // 清空cache中的图片
            ImageUtil.removeWorksInQuene();
            BitmapCache.getDefault().clear(true, false);
            BitmapCache.getDefault().close();
//            System.gc();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                processFromCamera(data, resultCode);
                break;
//            case REQUEST_CODE_LOCAL:
//                onPickedLocal(data, requestCode);
//                break;
            case REQUEST_CODE_CUT:
                processFromCut(data, resultCode);
                break;
            case REQUEST_CODE_PERMISSION:
                processFromPermission(resultCode);
                break;
            default:
                break;
        }
    }

    // 子类调用，获取照相目录
    protected String getCameraFileName(boolean getNew) {
        // 需求修改，照片保存在Gacha的temp目录下
        if (getNew || mCameraFileName == null) {
            mCameraFileName = HTImagePicker.INSTANCE.getUIConfig().getPhotoFileSavePath();
            String parentPath = FileUtil.getFileParentPath(mCameraFileName);
            if (!TextUtils.isEmpty(parentPath)) {
                File parentFile = new File(parentPath);
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            }
        }

        return mCameraFileName;
    }

    /**
     * 当启动选择图片控件之前，sdcard中的图片还没有被扫描过，则会触发扫描，扫描结束后则会调用该函数
     *
     * @param thumbnails 缩略图信息
     * @param imageFolders 相册列表信息
     */
    @Override
    final public void onScanComplete(List<Thumbnail> thumbnails, List<ImageFolder> imageFolders) {
        updateSelectedPhotos();
        onUpdateAllImageFolders(thumbnails, imageFolders);
    }

    private void updateSelectedPhotos() {
        List<ImageFolder> imageFolder = ImageInfoUtil.getImageFolders();
        if (imageFolder != null && imageFolder.size() > 0) {
            ImageFolder currFolder = null;
            if (mCurrImageFolder != null) {
                for (ImageFolder folder : imageFolder) {
                    if (TextUtils.equals(folder.getName(), mCurrImageFolder.getName())) {
                        currFolder = folder;
                        break;
                    }
                }
            }
            mCurrImageFolder = currFolder != null ? currFolder : imageFolder.get(0);

            List<String> imagePaths = new ArrayList<>();
            for (Image p : mSelectedImages) {
                imagePaths.add(p.getAbsolutePath());
            }
            mSelectedImages.clear();

            for (String imagePath : imagePaths) {
                int selectdImageSize = mSelectedImages.size();
                if (selectdImageSize >= mSelectLimit || imagePaths.size() == selectdImageSize) {
                    break;
                }

                for (Image p : mCurrImageFolder.getImages()) {
                    if (TextUtils.equals(imagePath, p.getAbsolutePath())) {
                        mSelectedImages.add(p);
                        break;
                    }
                }
            }
        }
    }

    @Override
    final public void onInternalUpdateSelectedPhotos(final EventUpdateSelectedPhotosModel event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Image> newSelectedPhotos = event.getSelectedImages();
                if (newSelectedPhotos != null) {
                    mSelectedImages.clear();
                    for (Image newPhoto : newSelectedPhotos) {
                        Image image = ImageHelper.findImage(mCurrImageFolder, newPhoto.getId());
                        if (image != null) {
                            mSelectedImages.add(image);
                        }
                    }
                    onUpdateSelectedImages(mSelectedImages);
                }
            }
        });
    }

    /**
     * 子类调用，启动拍照界面
     */
    final public void startTakePhoto() {
        try {
            // String outPath = getIntent().getStringExtra(Extras.EXTRA_FILE_PATH);
            BitmapCache.getDefault().clear(true, false);

            String outPath = getCameraFileName(true);

            if (TextUtils.isEmpty(outPath) || !StorageUtil.checkSdcardCapacity(false)) {
                ContextUtil.INSTANCE.makeToast(R.string.pick_image_sdcard_not_enough_error);
                return;
            }
            File outputFile = new File(outPath);
            Uri uri = Uri.fromFile(outputFile);

            if (HTImagePicker.INSTANCE.getUIConfig().isUseSystemCamera()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            } else {
                HTCameraActivity.startForResult(this, uri, REQUEST_CODE_CAMERA);
            }
        } catch (ActivityNotFoundException e) {
            ContextUtil.INSTANCE.makeToast(R.string.pick_image_camera_invalid);
        } catch (Exception e) {
            ContextUtil.INSTANCE.makeToast(R.string.pick_image_sdcard_not_enough_head_error);
        }
    }

    /**
     * 子类调用，启动预览多张图片的界面
     *
     * @param selectedImages 待预览的图片
     * @param intentProcess  intent的额外处理接口
     */
    final public void startPreviewMultiImages(List<Image> selectedImages, @Nullable IIntentProcess intentProcess) {
        if (selectedImages != null && !selectedImages.isEmpty()) {
            ImagePreviewHelper.startMultiImagePreview(this, selectedImages, intentProcess);
        }
    }

    /**
     * 子类调用，启动预览单张图片的界面
     *
     * @param image     待预览的图片
     * @param intentProcess intent的额外处理接口
     */
    final public void startPreviewSingleImage(Image image, @Nullable IIntentProcess intentProcess) {
        if (image != null) {
            ImagePreviewHelper.startSingleImagePreview(this, image, intentProcess);
        }
    }

    /**
     * 子类调用，启动图片裁剪页面
     *
     * @param imageSrc      图片路径
     * @param from          来源，可选 HTImageFrom.FROM_LOCAL.toString()，HTImageFrom.FROM_CAMERA.toString()
     * @param intentProcess intent的额外处理接口
     */
    final public void startCutImage(String imageSrc, String from, @Nullable IIntentProcess intentProcess) {
        Intent intent = getIntent();
        float cutRatio = intent.getFloatExtra(Extras.EXTRA_CUT_RATIO, 1.0f);
        String outPath = intent.getStringExtra(Extras.EXTRA_FILE_PATH);

        ImageCutHelper.startForFile(this, imageSrc, cutRatio, outPath, from, REQUEST_CODE_CUT, intentProcess);
    }

    protected int getSelectLimit() {
        return mSelectLimit;
    }

    protected HTImageFrom getImageFrom() {
        return mImageFrom;
    }

    protected boolean isCut() {
        return mIsCut;
    }

    protected String getFileOutPath() {
        return mFileOutPath;
    }

    private HTImageFrom getImageFrom(String from) {
        if (HTImageFrom.FROM_LOCAL.toString().equals(from)) {
            return HTImageFrom.FROM_LOCAL;
        } else if (HTImageFrom.FROM_CAMERA.toString().equals(from)) {
            return HTImageFrom.FROM_CAMERA;
        }
        return null;
    }

    private void processFromPermission(int resultCode) {
        if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
            return;
        }

        processExtra();
        // 如果是制定从摄像机中拍照，就直接开启拍照页面
        if (mImageFrom == HTImageFrom.FROM_CAMERA) {
            startTakePhoto();
        }
        tryStartImageScanTask();
    }

    private void processFromCut(Intent data, int resultCode) {
        HTImageFrom imageFrom = null;
        if (data != null) {
            String strFrom = data.getStringExtra(Extras.EXTRA_FROM);
            imageFrom = getImageFrom(strFrom);
        }

        if (resultCode == RESULT_OK) {
            if (data == null) {
                onCompleteFromCut(imageFrom);
                finish();
            } else {
                String returnData = data.getStringExtra(Extras.EXTRA_RETURN_DATA);
                if (HTImageCutActivity.CONFIRM_CUT.equals(returnData)) {
                    // 结束图片选取
                    onCompleteFromCut(imageFrom);
                    finish();
                } else { // if (HTImageCutActivity.CANCEL_CUT.equals(returnData)) {
                    if (imageFrom == HTImageFrom.FROM_CAMERA) {
                        startTakePhoto();
                    }
                    onCancelFromCut(imageFrom);
                }
            }
        } else {
            onCancelFromCut(null);
        }
    }

    private void processFromCamera(Intent data, int resultCode) {
        try {
            if (resultCode == RESULT_OK) {
                String photoPath = pathFromCameraResult(data);
                if (photoPath != null) {
                    if (FileUtil.getExistFile(photoPath) != null) {
                        Image image = new Image();
                        image.setAbsolutePath(photoPath);
                        image.setUriPath(C.UrlPrefix.FILE + photoPath);
                        image.setLastModifiedTime(System.currentTimeMillis());
                        image.setId(new Random().nextInt());

                        if (isCut()) {
                            // 从相机拍照进入
                            startCutImage(image.getAbsolutePath(), HTImageFrom.FROM_CAMERA.toString(), null);
                            return;
                        }

                        // 将图片信息写进contentprovider
                        ImageUtil.addImageToGallery(image.getAbsolutePath(), this);
                        insertImageAtFirst(image);

                        onPickedFromCamera(image);
                    } else {
                        onPickedFromCamera(null);
                    }
                }
            } else {
                onCancelFromCamera();
            }
        } catch (Exception e) {
            ContextUtil.INSTANCE.makeToast(R.string.pick_image_get_image_info_failed);
        }
    }

    private void insertImageAtFirst(Image image) {
        List<ImageFolder> imageFolders = ImageInfoUtil.getImageFolders();
        if (imageFolders == null || imageFolders.isEmpty())
            return;

        insertImageAtFirst(imageFolders.get(0), image);

        int size = imageFolders.size();
        for (int i = 1; i < size; ++i) {
            ImageFolder imageFolder = imageFolders.get(i);
            String folderPath = imageFolder.getAbsolutePath();
            String imageParentPath = FileUtil.getFileParentPath(image.getAbsolutePath());

            if (TextUtils.equals(folderPath, imageParentPath)) {
                insertImageAtFirst(imageFolder, image);
                break;
            }
        }
    }

    private void insertImageAtFirst(ImageFolder imageFolder, Image image) {
        if (imageFolder == null) return;

        if (imageFolder.getImages() == null) {
            imageFolder.setImages(new ArrayList<Image>());
        }
        imageFolder.getImages().add(0, image);
        imageFolder.setUriPath(image.getUriPath());
        imageFolder.setAbsolutePath(FileUtil.getFileParentPath(image.getAbsolutePath()));
    }

    private String pathFromCameraResult(Intent data) {
        String outPath = getCameraFileName(false); //getIntent().getStringExtra(Extras.EXTRA_FILE_PATH);
        if (data == null || data.getData() == null) {
            return outPath;
        }

        Uri uri = data.getData();
        Cursor cursor = getContentResolver()
                .query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);

        if (cursor == null) {
            // miui 2.3 有可能为null
            return uri.getPath();
        } else {
            if (uri.toString().contains("content://com.android.providers.media.documents/document/image")) { // htc 某些手机
                // 获取图片地址
                String _id = null;
                String uridecode = uri.decode(uri.toString());
                int id_index = uridecode.lastIndexOf(":");
                _id = uridecode.substring(id_index + 1);
                Cursor mcursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, " _id = " + _id,
                        null, null);
                mcursor.moveToFirst();
                int column_index = mcursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                outPath = mcursor.getString(column_index);
                if (!mcursor.isClosed()) {
                    mcursor.close();
                }
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return outPath;

            } else {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                outPath = cursor.getString(column_index);
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                return outPath;
            }
        }
    }

    private void tryStartImageScanTask() {
        ImageScanUtil.start(this);

//        final List<ImageFolder> imageFolders = ImageInfoUtil.getImageFolders();
//        if (imageFolders == null || imageFolders.size() == 0) {
//            ImageScanUtil.start(this);
//        } else {
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    onScanComplete(null, imageFolders);
//                }
//            });
//        }
    }

    private void processExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            mSelectLimit = intent.getIntExtra(Extras.EXTRA_SELECT_LIMIT, DEFAULT_SELECT_LIMIT_SIZE);
            mIsCut = intent.getBooleanExtra(Extras.EXTRA_NEED_CUT, false);
            mImageFrom = (HTImageFrom) intent.getSerializableExtra(Extras.EXTRA_FROM);

            mFileOutPath = intent.hasExtra(Extras.EXTRA_FILE_PATH) ?
                    intent.getStringExtra(Extras.EXTRA_FILE_PATH) :
                    HTImagePicker.INSTANCE.getUIConfig().getPhotoFileSavePath();

            mPickListenerKey = intent.getIntExtra(Extras.EXTRA_LISTENER_KEY, ImagePickerListenerCache.INVALID_IMAGE_PICK_KEY);

            // 如果支持裁剪的话，那只能单选
            if (mIsCut) mSelectLimit = 1;
            // 如果直接设置从相机中选取的话，那就能单选
            if (mImageFrom == HTImageFrom.FROM_CAMERA) mSelectLimit = 1;

            mTitle = intent.getStringExtra(Extras.EXTRA_PICK_TITLE);

            mCurrImageFolder = (ImageFolder) intent.getParcelableExtra(Extras.EXTRA_IMAGE_FOLDER);

            mSelectedImages = intent.getParcelableArrayListExtra(Extras.EXTRA_IMAGE_LIST);
            if (mSelectedImages == null) mSelectedImages = new ArrayList<>();
        }
    }

    private void callFinishListener(int pickListenerKey, boolean isConfirmUse) {
        List<Image> selectedPhotos = getSelectedImages();
        if (selectedPhotos == null) {
            selectedPhotos = new ArrayList<>();
        }

        if (isConfirmUse && mIsCut && mFileOutPath != null) {
            Image image = new Image();
            String outPath = mFileOutPath;
            image.setUriPath(C.UrlPrefix.FILE + outPath);
            image.setAbsolutePath(outPath);

            ArrayList<Image> images = new ArrayList<>();
            images.add(image);
            ImagePickerListenerCache.callImagePickerFinishListener(pickListenerKey, mCurrImageFolder, images);
        } else if (isConfirmUse && selectedPhotos.size() > 0) {
            ImagePickerListenerCache.callImagePickerFinishListener(pickListenerKey, mCurrImageFolder, selectedPhotos);
        } else {
            ImagePickerListenerCache.callImagePickerCancelListener(pickListenerKey);
        }
    }
}
