package com.kinomisfit.recyclocator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kinomisfit.recyclocator.Fragments.DashboardFragment;
import com.kinomisfit.recyclocator.Fragments.GoalsFragment;
import com.kinomisfit.recyclocator.Fragments.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        GoalsFragment.OnFragmentInteractionListener,
        DashboardFragment.OnFragmentInteractionListener {


    private HomeFragment homeFragment;
    private GoalsFragment goalsFragment;
    private DashboardFragment dashboardFragment;

    public static int MY_PERMISSIONS_REQUEST_CAMERA = 100;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(homeFragment);
                    return true;
                case R.id.navigation_goals:
                    setFragment(goalsFragment);
                    return true;
                case R.id.navigation_dashboard:
                    setFragment(dashboardFragment);
                    return true;
            }
            return false;
        }
    };

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, fragment);
        fragmentTransaction1.commit();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        homeFragment = new HomeFragment();
        goalsFragment = new GoalsFragment();
        dashboardFragment = new DashboardFragment();

        setFragment(homeFragment);

        getPermissions();

    }

    private void getPermissions() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            Log.i("Camera", "G : " + grantResults[0]);
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted,

                //openCamera();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.

                if (ActivityCompat.shouldShowRequestPermissionRationale
                        (this, Manifest.permission.CAMERA)) {

                    //showAlert();

                } else {

                }
            }
            return;
        }

        // other 'case' lines to check for other
        // permissions this app might request


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        finishAffinity();
    }
}
