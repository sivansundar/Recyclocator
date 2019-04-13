package com.kinomisfit.recyclocator.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camerakit.CameraKitView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kinomisfit.recyclocator.CameraActivity;
import com.kinomisfit.recyclocator.Models.PendingListModel;
import com.kinomisfit.recyclocator.NavigateDumpsActivity;
import com.kinomisfit.recyclocator.PendingDumpsActivity;
import com.kinomisfit.recyclocator.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

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

    private RecyclerView pendingRecyclerView;


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

    int click = 1;


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


        pendingRecyclerView = (RecyclerView) view.findViewById(R.id.pending_recyclerView);
        pendingRecyclerView.setHasFixedSize(true);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fabcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), CameraActivity.class));

            }
        });

        return view;
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


                        pendingListViewHolder.setTitle(pendingListModel.getTitle());
                        pendingListViewHolder.setDesc(pendingListModel.getTimestamp());

                        ImageView navigationImg = (ImageView) pendingListViewHolder.mView.findViewById(R.id.navigate);

                        navigationImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getContext(), NavigateDumpsActivity.class));
                            }
                        });

                        String postID = pendingListModel.getId();

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
