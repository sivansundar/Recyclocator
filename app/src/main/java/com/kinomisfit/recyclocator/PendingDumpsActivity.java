package com.kinomisfit.recyclocator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kinomisfit.recyclocator.Models.PendingListModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PendingDumpsActivity extends AppCompatActivity {

    String postID, UID;

    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public FirebaseAuth mAuth;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title_pendingItem)
    TextView titlePendingItem;
    @BindView(R.id.status_pendingItem)
    Chip statusPendingItem;
    @BindView(R.id.quantity_pendingItem)
    TextView quantityPendingItem;
    @BindView(R.id.typeText)
    Chip typeText;
    @BindView(R.id.navigationHolder)
    RelativeLayout navigationHolder;
    @BindView(R.id.TrashHolder)
    RelativeLayout DeleteHolder;

    private static final String TAG = "Pending dump";


    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_dumps);
        ButterKnife.bind(this);

        postID = getIntent().getStringExtra("postID");
        UID = getIntent().getStringExtra("UID");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference();


        databaseReference.child("pending_list").child(UID).child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PendingListModel model = dataSnapshot.getValue(PendingListModel.class);

                titlePendingItem.setText(model.getTitle());
                quantityPendingItem.setText(model.getQuantity());
                statusPendingItem.setText(model.getStatus());
                typeText.setText(model.getType());

                String id = model.getId();
                imageURL = model.getImgUrl();

                Glide.with(PendingDumpsActivity.this).load(imageURL).into(image);

                //Refer the storage class to pull out images // KinoMisfits




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DeleteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("pending_list").child(UID).child(postID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(PendingDumpsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.e(TAG, "onFailure: ", e);
                            }
                        });
            }
        });

    }

    @OnClick({R.id.navigationHolder, R.id.TrashHolder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.navigationHolder:

                startActivity(new Intent(PendingDumpsActivity.this, NavigateDumpsActivity.class));

                break;
            case R.id.TrashHolder:
                break;
        }
    }
}
