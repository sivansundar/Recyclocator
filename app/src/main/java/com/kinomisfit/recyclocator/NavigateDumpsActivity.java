package com.kinomisfit.recyclocator;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kinomisfit.recyclocator.Models.DumpLocationsModel;

public class NavigateDumpsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public FirebaseDatabase mDatabase;
    public DatabaseReference databaseReference;

    private static final String TAG = "NavigateDumps : ";

    AlertDialog.Builder alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_dumps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference();

       alert = new AlertDialog.Builder(this);



        getDumpSpots();
    }

    private void getDumpSpots() {

        Toast.makeText(this, "getDumpSpots clicked", Toast.LENGTH_SHORT).show();

        databaseReference.child("dumpLocations").child("BLR").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    DumpLocationsModel model = new DumpLocationsModel();
                    model.setId(ds.getValue(DumpLocationsModel.class).getId());
                    model.setLatitude(ds.getValue(DumpLocationsModel.class).getLatitude());
                    model.setLongitude(ds.getValue(DumpLocationsModel.class).getLongitude());
                    model.setTitle(ds.getValue(DumpLocationsModel.class).getTitle());


                    String title = ds.getValue(DumpLocationsModel.class).getTitle();


                    LatLng location = new LatLng(model.getLatitude(), model.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(location).title(model.getTitle()));

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            alert.setTitle(marker.getTitle());
                            alert.setMessage("Navigate to '" + marker.getTitle()  + "'?");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + location.latitude + ", " + location.longitude);

                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                                    mapIntent.setPackage("com.google.android.apps.maps");


                                    startActivity(mapIntent);

                                }
                            });
                            alert.setNeutralButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alert.show();

/*
*/




                            Toast.makeText(NavigateDumpsActivity.this, "Navigating to "+ marker.getPosition().latitude + ", " +  marker.getPosition().longitude,Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                    Log.d(TAG, "onDataChange: " + model.getLatitude() + "\n" + model.getId());
                }
}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng blore = new LatLng(12.97, 77.59);
        mMap.addMarker(new MarkerOptions().position(blore).title("Dump spot in Bangalore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(blore));
        mMap.setMinZoomPreference(12);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);




    }
}
