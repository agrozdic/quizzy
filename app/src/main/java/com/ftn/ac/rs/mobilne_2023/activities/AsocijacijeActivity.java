package com.ftn.ac.rs.mobilne_2023.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.Asocijacije;
import com.ftn.ac.rs.mobilne_2023.services.AsocijacijeService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;
import com.ftn.ac.rs.mobilne_2023.tools.PauseableCountDownTimer;

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
            asocijacijeModel = new Asocijacije(result);

            TextView time = findViewById(R.id.time);
            ProgressBar timePlayer = findViewById(R.id.playerTimeBar);

            CountDownTimer timer = new PauseableCountDownTimer(120000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (!isPaused())
                        time.setText(Long.toString(millisUntilFinished / 1000));
                }

                public void onFinish() {
                    // TODO
                }
            }.start();
        });
    }
}