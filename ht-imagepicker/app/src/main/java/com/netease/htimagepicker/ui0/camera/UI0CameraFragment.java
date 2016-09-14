package com.netease.htimagepicker.ui0.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.hearttouch.htimagepicker.R;
import com.netease.hearttouch.htimagepicker.core.camera.HTBaseCameraFragment;
import com.netease.hearttouch.htimagepicker.core.camera.HTCameraFragmentInterface;

/**
 * Created by zyl06 on 3/15/16.
 */
public class UI0CameraFragment extends HTBaseCameraFragment
    implements HTCameraFragmentInterface, View.OnClickListener {

    View mCancelBtn;
    View mConfirmBtn;
    View mTakePictureBtn;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }

    @Override
    public View onCreateCameraLayout(View cameraView, LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.htimagepicker_fragment_camera, container, false);
        ((ViewGroup) v.findViewById(R.id.pp_camera)).addView(cameraView);

        mCancelBtn = v.findViewById(R.id.camera_cancel);
        mCancelBtn.setOnClickListener(this);

        mConfirmBtn = v.findViewById(R.id.camera_complete);
        mConfirmBtn.setOnClickListener(this);

        mTakePictureBtn = v.findViewById(R.id.pp_take_picture);
        mTakePictureBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pp_take_picture) {
            takePhoto();
        } else if (v.getId() == R.id.camera_complete) {
            confirmUsePicture();
        } else if (v.getId() == R.id.camera_cancel) {
            cancelUsePicture();
        }
    }

    @Override
    public void onTakePicureEnabledChanged(boolean isEnabled) {
        mTakePictureBtn.setVisibility(isEnabled ? View.VISIBLE : View.INVISIBLE);
        mTakePictureBtn.setClickable(isEnabled);
    }

    @Override
    public void onConfirmPictureEnabledChanged(boolean isEnabled) {
        mConfirmBtn.setVisibility(isEnabled ? View.VISIBLE : View.INVISIBLE);
        mConfirmBtn.setClickable(isEnabled);
    }

    @Override
    public void onCancelPicureEnabledChanged(boolean isEnabled) {
        mCancelBtn.setVisibility(isEnabled ? View.VISIBLE : View.INVISIBLE);
        mCancelBtn.setClickable(isEnabled);
    }
}
