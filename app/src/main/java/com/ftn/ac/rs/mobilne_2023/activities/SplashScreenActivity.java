package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    public Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        int SPLASH_TIME_OUT = 3000;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}