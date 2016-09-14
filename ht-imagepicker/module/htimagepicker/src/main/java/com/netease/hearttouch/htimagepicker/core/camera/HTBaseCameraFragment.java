package com.netease.hearttouch.htimagepicker.core.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ht.com.commonsware.cwac.camera.CameraFragment;
import ht.com.commonsware.cwac.camera.CameraUtils;
import ht.com.commonsware.cwac.camera.CameraView;
import ht.com.commonsware.cwac.camera.PictureTransaction;
import ht.com.commonsware.cwac.camera.SimpleCameraHost;
import com.netease.hearttouch.htimagepicker.R;

import java.io.File;

/**
 * Created by zyl06 on 3/5/16.
 */
public abstract class HTBaseCameraFragment extends CameraFragment
        implements HTCameraFragmentInterface {
    private static final String KEY_USE_FFC = "HTBaseCameraFragment";

    private static final String TAG = HTBaseCameraFragment.class.getSimpleName();

    private MenuItem autoFocusItem = null;
    private MenuItem recordItem = null;
    private String flashMode = null;

    private CameraView mCameraView;

    private boolean mIsTakePictureEnabled = true;
    private boolean mIsConfirmUseEnabled = false;
    private boolean mIsCancelUseEnabled = false;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);

        SimpleCameraHost.Builder builder = new SimpleCameraHost.Builder(new DemoCameraHost(getActivity()));
        setCameraHost(builder.useFullBleedPreview(true).build());
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCameraView = (CameraView) super.onCreateView(inflater, container, savedInstanceState);
        View view = onCreateCameraLayout(mCameraView, inflater, container, savedInstanceState);

        view.setKeepScreenOn(true);
        setRecordingItemVisibility();

        return view;
    }

    /**
     * 提供给子类使用，点击拍照按钮时候调用
     */
    public void takePhoto() {
        if (mIsTakePictureEnabled) {
            // 拍照成功还是失败会有回调，所以设置是为了避免重复重用的问题
            setTakePictureEnabled(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                autoFocus();
                // calling above method will lead to callback
                // onAutoFocus()
            } else {
                // dont attempt autofocus for version less than 16.
                PictureTransaction pictureTransaction = new PictureTransaction(getCameraHost());
                pictureTransaction.needBitmap(true);
                pictureTransaction.flashMode(flashMode);
                super.takePicture(pictureTransaction);
            }
        }
    }

    /**
     * 提供给子类使用
     * 拍照完成时，点击确认按钮时候调用
     */
    public void confirmUsePicture() {
        if (mIsConfirmUseEnabled) {
            // 马上销毁页面
//            setTakePictureValid(true);

            File file = getPhotoPath();
            if (file != null) {
                doWhenSaveSucceed(Uri.fromFile(file));
            } else {
                doWhenSaveFail();
            }
        }
    }

    /**
     * 提供给子类使用
     * 拍照完成时，点击重拍或者取消按钮时候调用
     */
    public void cancelUsePicture() {
        if (mIsCancelUseEnabled) {
            setTakePictureValid(true);

            mCameraView.startPreview();
            ((HTCameraActivity) getActivity()).removePhoto();
        }
    }

    private void setTakePictureEnabled(boolean takePictureEnabled) {
        if (mIsTakePictureEnabled != takePictureEnabled) {
            mIsTakePictureEnabled = takePictureEnabled;
            onTakePicureEnabledChanged(takePictureEnabled);
        }
    }

    private void setConfirmUseEnabled(boolean confirmUseEnabled) {
        if (mIsConfirmUseEnabled != confirmUseEnabled) {
            mIsConfirmUseEnabled = confirmUseEnabled;
            onConfirmPictureEnabledChanged(confirmUseEnabled);
        }
    }

    private void setCancelUseEnabled(boolean cancelUseEnabled) {
        if (mIsCancelUseEnabled != cancelUseEnabled) {
            mIsCancelUseEnabled = cancelUseEnabled;
            onCancelPicureEnabledChanged(cancelUseEnabled);
        }
    }

    private void setTakePictureValid(boolean isEnabled) {
        setTakePictureEnabled(isEnabled);
        setConfirmUseEnabled(!isEnabled);
        setCancelUseEnabled(!isEnabled);
    }

    private File getPhotoPath() {
        return ((HTCameraActivity) getActivity()).getPhotoPath();
    }

    private void doWhenSaveSucceed(final Uri uri) {
        // run the media scanner service
        // MediaScannerConnection.scanFile(getActivity(), new String[]{path}, new String[]{"image/jpeg"}, null);
//        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, image.mUri));

        ((HTCameraActivity) getActivity()).finish(uri);
    }

    private void doWhenSaveFail() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), getString(R.string.pick_image_photo_save_error_toast), Toast.LENGTH_SHORT).show();
                ((HTCameraActivity) getActivity()).finish(null);
            }
        });
    }

    private void setRecordingItemVisibility() {
        if (recordItem != null) {
            if (getDisplayOrientation() != 0
                    && getDisplayOrientation() != 180) {
                recordItem.setVisible(false);
            }
        }
    }

    private class DemoCameraHost extends SimpleCameraHost implements Camera.FaceDetectionListener {
        boolean supportsFaces = false;

        public DemoCameraHost(Context _ctxt) {
            super(_ctxt);
        }

        @Override
        public boolean useFrontFacingCamera() {
            if (getArguments() == null) {
                return (false);
            }

            return getArguments().getBoolean(KEY_USE_FFC);
        }

        @Override
        public boolean useSingleShotMode() {
            return false;
        }

        @Override
        public void saveImage(PictureTransaction xact, byte[] image) {
            super.saveImage(xact, image);
            File file = getPhotoPath();
            if (file != null && file.exists()) {
                doWhenTakePhotoSucceed(file.getAbsolutePath());
            } else {
                doWhenTakePhotoFail();
            }
        }

        private void doWhenTakePhotoSucceed(final String imageFilePath) {
            // the current method is an async. call.
            // so make changes to the UI on the main thread.
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTakePictureValid(false);
                    mCameraView.stopPreview();
//                    mCameraView.stopPreview();
                }
            });
        }

        private void doWhenTakePhotoFail() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), getString(R.string.pick_image_photo_save_error_toast), Toast.LENGTH_SHORT).show();
                    setTakePictureValid(true);
                }
            });
        }

        @Override
        protected File getPhotoPath() {
            return HTBaseCameraFragment.this.getPhotoPath();
        }

        @Override
        public void autoFocusAvailable() {
            if (autoFocusItem != null) {
                autoFocusItem.setEnabled(true);

                if (supportsFaces) {
                    startFaceDetection();
                }
            }
        }

        @Override
        public void autoFocusUnavailable() {
            if (autoFocusItem != null) {
                stopFaceDetection();

                if (supportsFaces) {
                    autoFocusItem.setEnabled(false);
                }
            }
        }

        @Override
        public void onCameraFail(FailureReason reason) {
            super.onCameraFail(reason);
            Toast.makeText(getActivity(), "Sorry, but you cannot use the camera now!", Toast.LENGTH_LONG).show();
        }

        @Override
        public Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters) {
            flashMode = CameraUtils.findBestFlashModeMatch(parameters,
                    Camera.Parameters.FLASH_MODE_RED_EYE,
                    Camera.Parameters.FLASH_MODE_AUTO,
                    Camera.Parameters.FLASH_MODE_ON);

            if (parameters.getMaxNumDetectedFaces() > 0) {
                supportsFaces = true;
            } else {
                Log.w(TAG, "Face detection not available for this camera");
            }

            return super.adjustPreviewParameters(parameters);
        }

        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            // ignore face detection.
        }

        @Override
        @TargetApi(16)
        public void onAutoFocus(boolean success, Camera camera) {
            super.onAutoFocus(success, camera);

            // whether success=true/false just let it be.
            // we got to take picture no matter what.
            takePicture();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
