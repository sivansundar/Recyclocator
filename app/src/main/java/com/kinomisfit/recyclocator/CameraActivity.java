package com.kinomisfit.recyclocator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    public FirebaseDatabase mDatabase;
    public DatabaseReference databaseReference;

    public FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

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


                AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                alert.setTitle("Add trash to your list?");
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.add_trash_alertdialog, null);
                EditText titletext = (EditText) dialogLayout.findViewById(R.id.title_edittext);
                EditText numbertext = (EditText) dialogLayout.findViewById(R.id.number_edittext);

                alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String title = titletext.getText().toString().trim();
                        String number = numbertext.getText().toString().trim();
                        byte[] imgArray = bytes;

                        String UID = mAuth.getCurrentUser().getUid();


                        String key = mDatabase.getReference("pending_list/" + UID).push().getKey();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh-mm-ss");
                        String format = simpleDateFormat.format(new Date());
                        String time = format.replaceAll("-", ":");

                        HashMap<String, Object> item = new HashMap<>();
                        item.put("id", key);
                        item.put("title", title);
                        item.put("quantity", number);
                        item.put("status", "Incomplete");
                        item.put("type", "Non-Biodegradable");
                        item.put("timestamp", time);
                        //item.put("imgArray", imgArray);
                        

                        databaseReference.child("pending_list").child(UID).child(key).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CameraActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: ",e );
                                        Toast.makeText(CameraActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                });

                alert.setView(dialogLayout);
                alert.show();


                //processbyte data to bitmap
                //set up custom model using the final package code in the other window
                //set up local model values.
                //run the model with the bitmap
                //obtain values and just Toast them


            }
        });
    }


}
