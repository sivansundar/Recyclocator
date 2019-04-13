package com.kinomisfit.recyclocator;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.camerakit.CameraKitView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraKitView camera;
    @BindView(R.id.fabcam)
    ExtendedFloatingActionButton fabcam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        camera.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.onResume();
    }

    @Override
    protected void onPause() {
        camera.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        camera.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.fabcam)
    public void onViewClicked() {

        camera.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {

                //processbyte data to bitmap
                //set up custom model using the final package code in the other window
                //set up local model values.
                //run the model with the bitmap
                //obtain values and just Toast them



                            }
        });
    }
}
