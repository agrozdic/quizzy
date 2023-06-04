package com.ftn.ac.rs.mobilne_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class KoZnaZnaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ko_zna_zna);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), KoZnaZnaActivity.this,
                false, R.id.headKoZnaZna);

        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(view -> fab1());
    }

    protected void fab1() {
        Intent intent = new Intent(KoZnaZnaActivity.this, SpojniceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("unreg-score", "0");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}