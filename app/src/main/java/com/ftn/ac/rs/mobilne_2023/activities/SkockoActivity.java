package com.ftn.ac.rs.mobilne_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SkockoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_skocko);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), SkockoActivity.this,
                false, R.id.headSkocko);

        FloatingActionButton fab = findViewById(R.id.fab4);
        fab.setOnClickListener(view -> fab4());
    }

    protected void fab4() {
        Intent intent = new Intent(SkockoActivity.this, KorakPoKorakActivity.class);
        startActivity(intent);
    }
}