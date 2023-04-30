package com.ftn.ac.rs.mobilne_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SpojniceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_spojnice);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), SpojniceActivity.this,
                false, R.id.headSpojnice);

        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(view -> fab2());
    }

    protected void fab2() {
        Intent intent = new Intent(SpojniceActivity.this, AsocijacijeActivity.class);
        startActivity(intent);
    }
}