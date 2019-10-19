package com.kinomisfit.recyclocator.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camerakit.CameraKitView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kinomisfit.recyclocator.Models.PendingListModel;
import com.kinomisfit.recyclocator.NavigateDumpsActivity;
import com.kinomisfit.recyclocator.PendingDumpsActivity;
import com.kinomisfit.recyclocator.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ExtendedFloatingActionButton fabcam;
    @BindView(R.id.camera)
    CameraKitView camera;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;

    String UID = "";

    private static final String TAG = "Home Fragment : ";


    private RecyclerView pendingRecyclerView;
    public TextView pendingDumpNumber;


    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private ArrayList permissions = new ArrayList();

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment

        mDatabase = FirebaseDatabase.getInstance(); // Instance of DB
        databaseReference = mDatabase.getReference(); // Points to the root node
        mAuth = FirebaseAuth.getInstance();

        fabcam = (ExtendedFloatingActionButton) view.findViewById(R.id.fabcam);

        mainLayout = (RelativeLayout) view.findViewById(R.id.mainLayout);
        camera = (CameraKitView) view.findViewById(R.id.camera);

        pendingDumpNumber = view.findViewById(R.id.pendingDumpNumber);

        getPendingDumpList();


        pendingRecyclerView = (RecyclerView) view.findViewById(R.id.pending_recyclerView);
        pendingRecyclerView.setHasFixedSize(true);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fabcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getContext(), CameraActivity.class));


                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 1001);


            }
        });


        return view;
    }

    private void getPendingDumpList() {

        databaseReference.child("pending_list").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                Log.d(TAG, "onDataChange: Count : " + count);
                if (count > 0) {

                    pendingDumpNumber.setVisibility(View.GONE);
                }
                else {
                    pendingDumpNumber.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 1001) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");


                Log.d(TAG, "onActivityResult: uri : " + data.getData());
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.add_trash_alertdialog, null);
                EditText titletext = (EditText) dialogLayout.findViewById(R.id.title_edittext);
                EditText numbertext = (EditText) dialogLayout.findViewById(R.id.number_edittext);
                Button yesAddTrashBtn = dialogLayout.findViewById(R.id.yesButton);
                ImageView trashImage = dialogLayout.findViewById(R.id.trashImage);

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Add trash to your list?");
                alert.setView(dialogLayout);
                alert.setCancelable(false);

                AlertDialog alertDialog = alert.show();


                yesAddTrashBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = titletext.getText().toString().trim();
                        String number = numbertext.getText().toString().trim();

                        if (title.isEmpty() && number.isEmpty()) {
                            Toast.makeText(getContext(), "What do you want to dump & how many?", Toast.LENGTH_LONG).show();
                        } else {
                            String UID = mAuth.getCurrentUser().getUid();

                            String key = databaseReference.child("pending_list").child(UID).push().getKey();

                            HashMap<String, Object> item = new HashMap<>();
                            item.put("id", key);
                            item.put("title", title);
                            item.put("quantity", number);
                            item.put("status", "Incomplete");
                            item.put("type", "Non-Biodegradable");
                            item.put("timestamp", getTime());
                            item.put("date", getDate());

                            addPost(item, UID, key);
                            alertDialog.dismiss();
                        }
                    }
                });


                trashImage.setImageBitmap(imageBitmap);


            }
        }

    }

    private void addPost(HashMap<String, Object> item, String userID, String postKey) {

        databaseReference.child("pending_list").child(userID).child(postKey).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Trash added! Go DUMP now :D", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Error - Could not add trash");
                    alert.setMessage("Could not add trash because - " + task.getException().getLocalizedMessage());
                    alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }
            }
        });
    }

    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(calendar.getTime());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 4; i++) {

            sb.append(time.charAt(i));


        }

        String currentTime = sb.toString();

        return currentTime;
    }

    private String getDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d(TAG, "onClick: DATE : " + date);

        return date;
    }

    @Override
    public void onStart() {
        UID = mAuth.getCurrentUser().getUid();

        refreshPendingList();
        super.onStart();
    }


    private void refreshPendingList() {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("pending_list")
                .child(UID);

        FirebaseRecyclerOptions<PendingListModel> options =
                new FirebaseRecyclerOptions.Builder<PendingListModel>()
                        .setQuery(query, PendingListModel.class)
                        .build();

        FirebaseRecyclerAdapter<PendingListModel, PendingListViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PendingListModel, PendingListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PendingListViewHolder pendingListViewHolder, int i, @NonNull PendingListModel pendingListModel) {

                        String postID = pendingListModel.getId();

                        pendingListViewHolder.setTitle(pendingListModel.getTitle());
                        pendingListViewHolder.setDesc(pendingListModel.getTimestamp());

                        ImageView navigationImg = (ImageView) pendingListViewHolder.mView.findViewById(R.id.navigate);

                        ImageView trashImg = (ImageView) pendingListViewHolder.mView.findViewById(R.id.trashIcon);

                        trashImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference.child("pending_list").child(UID).child(postID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
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

                        navigationImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getContext(), NavigateDumpsActivity.class));
                            }
                        });


                        pendingListViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), PendingDumpsActivity.class);
                                intent.putExtra("UID", UID);
                                intent.putExtra("postID", postID);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public PendingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.pending_list_item, parent, false);

                        return new PendingListViewHolder(view);
                    }
                };

        pendingRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }


    public static class PendingListViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PendingListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            TextView titletext = (TextView) mView.findViewById(R.id.title_pl);
            titletext.setText(title);

        }

        public void setDesc(String timestamp) {


            TextView timestamptext = (TextView) mView.findViewById(R.id.timestamp_pl);
            timestamptext.setText(timestamp);
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
