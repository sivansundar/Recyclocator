package com.kinomisfit.recyclocator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kinomisfit.recyclocator.Fragments.HomeFragment;
import com.kinomisfit.recyclocator.Models.PendingListModel;
import com.kinomisfit.recyclocator.Models.PreviousDumpsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviousDumpsActivity extends AppCompatActivity {

    @BindView(R.id.previousDumps_recyclerView)
    RecyclerView previousDumpsRecyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;

    String UID;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_dumps);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();



        previousDumpsRecyclerView.setHasFixedSize(true);
        previousDumpsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    protected void onStart() {
        super.onStart();

        UID = mAuth.getCurrentUser().getUid();

        getPreviousDumps();
    }

    private void getPreviousDumps() {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("previous_dumps")
                .child(UID);


        FirebaseRecyclerOptions<PreviousDumpsModel> options =
                new FirebaseRecyclerOptions.Builder<PreviousDumpsModel>()
                        .setQuery(query, PreviousDumpsModel.class)
                        .build();



        FirebaseRecyclerAdapter<PreviousDumpsModel, PreviousDumpsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PreviousDumpsModel, PreviousDumpsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PreviousDumpsViewHolder previousDumpsViewHolder, int i, @NonNull PreviousDumpsModel previousDumpsModel) {

                previousDumpsViewHolder.setTitle(previousDumpsModel.getTitle());
            }

            @NonNull
            @Override
            public PreviousDumpsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.previous_dump_item, parent, false);

                return new PreviousDumpsViewHolder(view);
            }
        };

        previousDumpsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();



    }


    public static class PreviousDumpsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PreviousDumpsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            TextView titletext = (TextView) mView.findViewById(R.id.title);
            titletext.setText(title);

        }




    }

}



