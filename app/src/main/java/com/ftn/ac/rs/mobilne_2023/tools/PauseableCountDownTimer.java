package com.ftn.ac.rs.mobilne_2023.tools;

import android.os.CountDownTimer;

public class PauseableCountDownTimer extends CountDownTimer {

    private long remainingTime;
    private boolean isPaused;

    public PauseableCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        isPaused = false;
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {

    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
        start();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public long getRemainingTime() {
        return remainingTime;
    }
}
