package com.kinomisfit.recyclocator.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kinomisfit.recyclocator.Models.GoalsModel;
import com.kinomisfit.recyclocator.Models.PendingListModel;
import com.kinomisfit.recyclocator.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoalsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;

    private RecyclerView goalsRecyclerView;


    private OnFragmentInteractionListener mListener;

    public GoalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalsFragment newInstance(String param1, String param2) {
        GoalsFragment fragment = new GoalsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        mDatabase = FirebaseDatabase.getInstance(); // Instance of DB
        databaseReference = mDatabase.getReference(); // Points to the root node

        goalsRecyclerView = (RecyclerView) view.findViewById(R.id.goals_recyclerView);
        goalsRecyclerView.setHasFixedSize(true);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        return view;
    }

    @Override
    public void onStart() {

        refreshGoalsList();
        super.onStart();
    }

    public void refreshGoalsList() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("goals");

        FirebaseRecyclerOptions<GoalsModel> options =
                new FirebaseRecyclerOptions.Builder<GoalsModel>()
                        .setQuery(query, GoalsModel.class)
                        .build();

        FirebaseRecyclerAdapter<GoalsModel, GoalsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<GoalsModel, GoalsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GoalsViewHolder goalsViewHolder, int i, @NonNull GoalsModel goalsModel) {

                goalsViewHolder.setDesc(goalsModel.getDesc());
                goalsViewHolder.setTarget(goalsModel.getTarget());
            }

            @NonNull
            @Override
            public GoalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.goal_list_item, parent, false);

                return new GoalsViewHolder(view);
            }
        };


        goalsRecyclerView .setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


    }


    public static class GoalsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public GoalsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setTitle(String title) {
            TextView titletext = (TextView) mView.findViewById(R.id.title_pl);
            titletext.setText(title);

        }

        public void setTarget(String target) {
            ProgressBar progressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
            int targetlong = Integer.parseInt(target);
            progressBar.setMax(targetlong);


        }

        public void setDesc(String desc) {


            TextView timestamptext = (TextView) mView.findViewById(R.id.desc);
            timestamptext.setText(desc);
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
