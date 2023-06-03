package com.ftn.ac.rs.mobilne_2023.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.Asocijacije;
import com.ftn.ac.rs.mobilne_2023.services.AsocijacijeService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

public class AsocijacijeActivity extends AppCompatActivity {

    Asocijacije asocijacijeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_asocijacije);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), AsocijacijeActivity.this,
                false, R.id.headAsocijacije);

        startGame();
    }

    private void startGame() {
        AsocijacijeService.get(1, result -> {
            Log.println(Log.INFO, "Recieved data: ", result.toString());
        });
    }
}