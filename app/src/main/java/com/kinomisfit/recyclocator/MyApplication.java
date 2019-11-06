package com.kinomisfit.recyclocator;



import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}