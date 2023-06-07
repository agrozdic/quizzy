package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.Asocijacije;
import com.ftn.ac.rs.mobilne_2023.services.AsocijacijeService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

public class AsocijacijeActivity extends AppCompatActivity {

    Asocijacije asocijacijeModel;

    CountDownTimer gameTimer;

    int round;

    TextView player1ScoreView;
    int playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_asocijacije);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), AsocijacijeActivity.this,
                false, R.id.headAsocijacije);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            playerScore = Integer.parseInt(bundle.getString("unreg-score"));
            round = bundle.getInt("round", 1);
        }

        startGame();
    }

    private void startGame() {
        AsocijacijeService.get(round, result -> {
            Log.println(Log.INFO, "Recieved data: ", result.toString());
            asocijacijeModel = new Asocijacije(result);

            player1ScoreView = findViewById(R.id.player1Score);
            player1ScoreView.setText(Integer.toString(playerScore));

            loadData();

            startTimer();
        });
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);

        gameTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                    time.setText(Long.toString(millisUntilFinished / 1000));
                }

            public void onFinish() {
                Toast.makeText(AsocijacijeActivity.this, "End of round", Toast.LENGTH_LONG).show();
                if (round == 1) {
                    Intent intent = new Intent(AsocijacijeActivity.this, AsocijacijeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("unreg-score", Integer.toString(playerScore));
                    bundle.putInt("round", ++round);
                    intent.putExtras(bundle);
                    gameTimer.cancel();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(AsocijacijeActivity.this, SkockoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("unreg-score", Integer.toString(playerScore));
                    bundle.remove("round");
                    intent.putExtras(bundle);
                    gameTimer.cancel();
                    startActivity(intent);
                }
            }
        }.start();
    }

    private void loadData() {
        Button a1 = findViewById(R.id.a1Asocijacije);
        Button a2 = findViewById(R.id.a2Asocijacije);
        Button a3 = findViewById(R.id.a3Asocijacije);
        Button a4 = findViewById(R.id.a4Asocijacije);
        EditText a5 = findViewById(R.id.aKolAsocijacije);

        a1.setOnClickListener(view -> {
            a1.setText(asocijacijeModel.getA1());
            a1.setEnabled(false);
        });

        a2.setOnClickListener(view -> {
            a2.setText(asocijacijeModel.getA2());
            a2.setEnabled(false);
        });

        a3.setOnClickListener(view -> {
            a3.setText(asocijacijeModel.getA3());
            a3.setEnabled(false);
        });

        a4.setOnClickListener(view -> {
            a4.setText(asocijacijeModel.getA4());
            a4.setEnabled(false);
        });

        a5.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                String userInput = a5.getText().toString();

                if (userInput.equals(asocijacijeModel.getA5())) {
                    playerScore += 2;

                    if (a1.isEnabled()) {
                        playerScore += 1;
                        a1.setText(asocijacijeModel.getA1());
                        a1.setEnabled(false);
                    }

                    if (a2.isEnabled()) {
                        playerScore += 1;
                        a2.setText(asocijacijeModel.getA2());
                        a2.setEnabled(false);
                    }

                    if (a3.isEnabled()) {
                        playerScore += 1;
                        a3.setText(asocijacijeModel.getA3());
                        a3.setEnabled(false);
                    }

                    if (a4.isEnabled()) {
                        playerScore += 1;
                        a4.setText(asocijacijeModel.getA4());
                        a4.setEnabled(false);
                    }

                    player1ScoreView.setText(Integer.toString(playerScore));

                    a5.setEnabled(false);

                    return true;
                } else {
                    Toast.makeText(AsocijacijeActivity.this, "Wrong guess", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        Button b1 = findViewById(R.id.b1Asocijacije);
        Button b2 = findViewById(R.id.b2Asocijacije);
        Button b3 = findViewById(R.id.b3Asocijacije);
        Button b4 = findViewById(R.id.b4Asocijacije);
        EditText b5 = findViewById(R.id.bKolAsocijacije);

        b1.setOnClickListener(view -> {
            b1.setText(asocijacijeModel.getB1());
            b1.setEnabled(false);
        });

        b2.setOnClickListener(view -> {
            b2.setText(asocijacijeModel.getB2());
            b2.setEnabled(false);
        });

        b3.setOnClickListener(view -> {
            b3.setText(asocijacijeModel.getB3());
            b3.setEnabled(false);
        });

        b4.setOnClickListener(view -> {
            b4.setText(asocijacijeModel.getB4());
            b4.setEnabled(false);
        });

        b5.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                String userInput = b5.getText().toString();

                if (userInput.equals(asocijacijeModel.getB5())) {
                    playerScore += 2;

                    if (b1.isEnabled()) {
                        playerScore += 1;
                        b1.setText(asocijacijeModel.getB1());
                        b1.setEnabled(false);
                    }

                    if (b2.isEnabled()) {
                        playerScore += 1;
                        b2.setText(asocijacijeModel.getB2());
                        b2.setEnabled(false);
                    }

                    if (b3.isEnabled()) {
                        playerScore += 1;
                        b3.setText(asocijacijeModel.getB3());
                        b3.setEnabled(false);
                    }

                    if (b4.isEnabled()) {
                        playerScore += 1;
                        b4.setText(asocijacijeModel.getB4());
                        b4.setEnabled(false);
                    }

                    player1ScoreView.setText(Integer.toString(playerScore));

                    b5.setEnabled(false);

                    return true;
                } else {
                    Toast.makeText(AsocijacijeActivity.this, "Wrong guess", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        Button c1 = findViewById(R.id.c1Asocijacije);
        Button c2 = findViewById(R.id.c2Asocijacije);
        Button c3 = findViewById(R.id.c3Asocijacije);
        Button c4 = findViewById(R.id.c4Asocijacije);
        EditText c5 = findViewById(R.id.cKolAsocijacije);

        c1.setOnClickListener(view -> {
            c1.setText(asocijacijeModel.getC1());
            c1.setEnabled(false);
        });

        c2.setOnClickListener(view -> {
            c2.setText(asocijacijeModel.getC2());
            c2.setEnabled(false);
        });

        c3.setOnClickListener(view -> {
            c3.setText(asocijacijeModel.getC3());
            c3.setEnabled(false);
        });

        c4.setOnClickListener(view -> {
            c4.setText(asocijacijeModel.getC4());
            c4.setEnabled(false);
        });

        c5.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                String userInput = c5.getText().toString();

                if (userInput.equals(asocijacijeModel.getC5())) {
                    playerScore += 2;

                    if (c1.isEnabled()) {
                        playerScore += 1;
                        c1.setText(asocijacijeModel.getC1());
                        c1.setEnabled(false);
                    }

                    if (c2.isEnabled()) {
                        playerScore += 1;
                        c2.setText(asocijacijeModel.getC2());
                        c2.setEnabled(false);
                    }

                    if (c3.isEnabled()) {
                        playerScore += 1;
                        c3.setText(asocijacijeModel.getC3());
                        c3.setEnabled(false);
                    }

                    if (c4.isEnabled()) {
                        playerScore += 1;
                        c4.setText(asocijacijeModel.getC4());
                        c4.setEnabled(false);
                    }

                    player1ScoreView.setText(Integer.toString(playerScore));

                    c5.setEnabled(false);

                    return true;
                } else {
                    Toast.makeText(AsocijacijeActivity.this, "Wrong guess", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        Button d1 = findViewById(R.id.d1Asocijacije);
        Button d2 = findViewById(R.id.d2Asocijacije);
        Button d3 = findViewById(R.id.d3Asocijacije);
        Button d4 = findViewById(R.id.d4Asocijacije);
        EditText d5 = findViewById(R.id.dKolAsocijacije);

        d1.setOnClickListener(view -> {
            d1.setText(asocijacijeModel.getD1());
            d1.setEnabled(false);
        });

        d2.setOnClickListener(view -> {
            d2.setText(asocijacijeModel.getD2());
            d2.setEnabled(false);
        });

        d3.setOnClickListener(view -> {
            d3.setText(asocijacijeModel.getD3());
            d3.setEnabled(false);
        });

        d4.setOnClickListener(view -> {
            d4.setText(asocijacijeModel.getD4());
            d4.setEnabled(false);
        });

        d5.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                String userInput = d5.getText().toString();

                if (userInput.equals(asocijacijeModel.getD5())) {
                    playerScore += 2;

                    if (d1.isEnabled()) {
                        playerScore += 1;
                        d1.setText(asocijacijeModel.getD1());
                        d1.setEnabled(false);
                    }

                    if (d2.isEnabled()) {
                        playerScore += 1;
                        d2.setText(asocijacijeModel.getD2());
                        d2.setEnabled(false);
                    }

                    if (d3.isEnabled()) {
                        playerScore += 1;
                        d3.setText(asocijacijeModel.getD3());
                        d3.setEnabled(false);
                    }

                    if (d4.isEnabled()) {
                        playerScore += 1;
                        d4.setText(asocijacijeModel.getD4());
                        d4.setEnabled(false);
                    }

                    player1ScoreView.setText(Integer.toString(playerScore));

                    d5.setEnabled(false);

                    return true;
                } else {
                    Toast.makeText(AsocijacijeActivity.this, "Wrong guess", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        EditText konacno = findViewById(R.id.konacnoAsocijacije);

        konacno.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                String userInput = konacno.getText().toString();

                if (userInput.equals(asocijacijeModel.getKonacno())) {
                    playerScore += 7;

                    if (a1.isEnabled()) {
                        playerScore += 1;
                        a1.setText(asocijacijeModel.getA1());
                        a1.setEnabled(false);
                    }

                    if (a2.isEnabled()) {
                        playerScore += 1;
                        a2.setText(asocijacijeModel.getA2());
                        a2.setEnabled(false);
                    }

                    if (a3.isEnabled()) {
                        playerScore += 1;
                        a3.setText(asocijacijeModel.getA3());
                        a3.setEnabled(false);
                    }

                    if (a4.isEnabled()) {
                        playerScore += 1;
                        a4.setText(asocijacijeModel.getA4());
                        a4.setEnabled(false);
                    }

                    if (a5.isEnabled()) {
                        playerScore += 2;
                        a5.setText(asocijacijeModel.getA5());
                        a5.setEnabled(false);
                    }

                    if (b1.isEnabled()) {
                        playerScore += 1;
                        b1.setText(asocijacijeModel.getB1());
                        b1.setEnabled(false);
                    }

                    if (b2.isEnabled()) {
                        playerScore += 1;
                        b2.setText(asocijacijeModel.getB2());
                        b2.setEnabled(false);
                    }

                    if (b3.isEnabled()) {
                        playerScore += 1;
                        b3.setText(asocijacijeModel.getB3());
                        b3.setEnabled(false);
                    }

                    if (b4.isEnabled()) {
                        playerScore += 1;
                        b4.setText(asocijacijeModel.getB4());
                        b4.setEnabled(false);
                    }

                    if (b5.isEnabled()) {
                        playerScore += 2;
                        b5.setText(asocijacijeModel.getB5());
                        b5.setEnabled(false);
                    }

                    if (c1.isEnabled()) {
                        playerScore += 1;
                        c1.setText(asocijacijeModel.getC1());
                        c1.setEnabled(false);
                    }

                    if (c2.isEnabled()) {
                        playerScore += 1;
                        c2.setText(asocijacijeModel.getC2());
                        c2.setEnabled(false);
                    }

                    if (c3.isEnabled()) {
                        playerScore += 1;
                        c3.setText(asocijacijeModel.getC3());
                        c3.setEnabled(false);
                    }

                    if (c4.isEnabled()) {
                        playerScore += 1;
                        c4.setText(asocijacijeModel.getC4());
                        c4.setEnabled(false);
                    }

                    if (c5.isEnabled()) {
                        playerScore += 2;
                        c5.setText(asocijacijeModel.getC5());
                        c5.setEnabled(false);
                    }

                    if (d1.isEnabled()) {
                        playerScore += 1;
                        d1.setText(asocijacijeModel.getD1());
                        d1.setEnabled(false);
                    }

                    if (d2.isEnabled()) {
                        playerScore += 1;
                        d2.setText(asocijacijeModel.getD2());
                        d2.setEnabled(false);
                    }

                    if (d3.isEnabled()) {
                        playerScore += 1;
                        d3.setText(asocijacijeModel.getD3());
                        d3.setEnabled(false);
                    }

                    if (d4.isEnabled()) {
                        playerScore += 1;
                        d4.setText(asocijacijeModel.getD4());
                        d4.setEnabled(false);
                    }

                    if (d5.isEnabled()) {
                        playerScore += 2;
                        d5.setText(asocijacijeModel.getD5());
                        d5.setEnabled(false);
                    }

                    player1ScoreView.setText(Integer.toString(playerScore));

                    konacno.setEnabled(false);

                    gameTimer.onFinish();

                    return true;
                }
            }
            return false;
        });
    }
}