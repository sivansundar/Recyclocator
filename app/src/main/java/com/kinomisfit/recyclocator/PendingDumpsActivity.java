package com.kinomisfit.recyclocator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kinomisfit.recyclocator.Models.PendingListModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PendingDumpsActivity extends AppCompatActivity {

    String postID, UID;

    FirebaseDatabase mDatabase;
    DatabaseReference databaseReference;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_dumps);
        ButterKnife.bind(this);

        postID = getIntent().getStringExtra("postID");
        UID = getIntent().getStringExtra("UID");


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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
