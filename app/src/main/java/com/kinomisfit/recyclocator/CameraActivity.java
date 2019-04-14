package com.kinomisfit.recyclocator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "Camera Activity : ";
    @BindView(R.id.camera)
    CameraKitView camera;
    @BindView(R.id.fabcam)
    ExtendedFloatingActionButton fabcam;
    @BindView(R.id.imageView)
    ImageView imageView;

    private FirebaseModelInterpreter mInterpreter;

    private FirebaseModelInputOutputOptions mDataOptions;

    private List<String> mLabelList;


    private static final String HOSTED_MODEL_NAME = "cloud_model_1";
    private static final String LOCAL_MODEL_ASSET = "mobilenet_v1_1.0_224_quant.tflite";

    private static final String LABEL_PATH = "//assets//labels.txt";


    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;
    private static final int DIM_IMG_SIZE_X = 224;
    private static final int DIM_IMG_SIZE_Y = 224;

    private static final int RESULTS_TO_SHOW = 3;


    private final PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float>
                                o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });

    private List<String> loadLabelList(Activity activity) {
        List<String> labelList = new ArrayList<>();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(activity.getAssets().open
                             (LABEL_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                labelList.add(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to read label list.", e);
        }
        return labelList;
    }


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

        // Open Alert Dialog with custom view. Name and number. Upload.
        camera.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(bytes).into(imageView);
                camera.setVisibility(View.INVISIBLE);




                FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                        .setWidth(480)   // 480x360 is typically sufficient for
                        .setHeight(360)  // image recognition
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .build();

                FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(bytes, metadata);


                FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                        .getOnDeviceImageLabeler();

                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                                for (FirebaseVisionImageLabel label : firebaseVisionImageLabels) {
                                    String text = label.getText();
                                    String entityId = label.getEntityId();
                                    float confidence = label.getConfidence();


                                    Log.d(TAG, "onSuccess: " + text + "\n" + entityId + "\n" + confidence);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: ", e);
                            }
                        });


                //processbyte data to bitmap
                //set up custom model using the final package code in the other window
                //set up local model values.
                //run the model with the bitmap
                //obtain values and just Toast them


            }
        });
    }
}
