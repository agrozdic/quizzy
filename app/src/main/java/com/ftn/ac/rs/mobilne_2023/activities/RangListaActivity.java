package com.ftn.ac.rs.mobilne_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ftn.ac.rs.mobilne_2023.R;

public class RangListaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_rang_lista);
    }
}