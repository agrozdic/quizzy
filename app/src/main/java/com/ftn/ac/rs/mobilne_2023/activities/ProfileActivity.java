package com.ftn.ac.rs.mobilne_2023.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
    }
}