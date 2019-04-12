package com.kinomisfit.recyclocator;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kinomisfit.recyclocator.Fragments.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
GoalsFragment.OnFragmentInteractionListener{


    private HomeFragment homeFragment;
    private GoalsFragment goalsFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragment(homeFragment);

                    return true;
                case R.id.navigation_dashboard:
                    setFragment(goalsFragment);
                    return true;
                case R.id.navigation_notifications:
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

        setFragment(homeFragment);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
