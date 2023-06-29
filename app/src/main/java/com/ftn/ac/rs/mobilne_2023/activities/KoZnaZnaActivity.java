package com.ftn.ac.rs.mobilne_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.KoZnaZna;
import com.ftn.ac.rs.mobilne_2023.services.KoZnaZnaService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

public class KoZnaZnaActivity extends AppCompatActivity {

    KoZnaZna koZnaZnaModel;

    CountDownTimer gameTimer;
    CountDownTimer questionTimer;
    int timerCounter = 1;

    int round;

    TextView player1ScoreView;
    int playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ko_zna_zna);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), KoZnaZnaActivity.this,
                false, R.id.headKoZnaZna);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            playerScore = Integer.parseInt(bundle.getString("unreg-score"));
            round = bundle.getInt("round", 1);
        }

        startGame();
    }

    private void startGame() {
        KoZnaZnaService.get(1, result -> {
            Log.println(Log.INFO, "Recieved data: ", result.toString());
            koZnaZnaModel = new KoZnaZna(result);

            player1ScoreView = findViewById(R.id.player1Score);
            player1ScoreView.setText(Integer.toString(playerScore));

            loadControls();

            startTimer();
        });
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);

        gameTimer = new CountDownTimer(25000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                Toast.makeText(KoZnaZnaActivity.this, "End of game", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(KoZnaZnaActivity.this, SpojniceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("unreg-score", Integer.toString(playerScore));
                bundle.remove("round");
                intent.putExtras(bundle);
                gameTimer.cancel();
                startActivity(intent);
            }
        }.start();

        questionTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                timerCounter++;
                if (timerCounter > 5)
                    this.cancel();
                else {
                    loadData(timerCounter);
                    this.cancel();
                    this.start();
                    answer1.setEnabled(true);
                    answer2.setEnabled(true);
                    answer3.setEnabled(true);
                    answer4.setEnabled(true);
                }
            }
        }.start();
        loadData(timerCounter);
    }

    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;

    private void loadControls() {
        question = findViewById(R.id.questionKoZnaZna);
        answer1 = findViewById(R.id.answer1KoZnaZna);
        answer2 = findViewById(R.id.answer2KoZnaZna);
        answer3 = findViewById(R.id.answer3KoZnaZna);
        answer4 = findViewById(R.id.answer4KoZnaZna);

        answer1.setOnClickListener(view -> checkAnswer((String) answer1.getText()));

        answer2.setOnClickListener(view -> checkAnswer((String) answer2.getText()));

        answer3.setOnClickListener(view -> checkAnswer((String) answer3.getText()));

        answer4.setOnClickListener(view -> checkAnswer((String) answer4.getText()));
    }

    private void checkAnswer(String answer) {
        if (koZnaZnaModel.getAnswers().contains(answer)) {
            playerScore += 10;
            player1ScoreView.setText(Integer.toString(playerScore));
            answer1.setEnabled(false);
            answer2.setEnabled(false);
            answer3.setEnabled(false);
            answer4.setEnabled(false);
            Toast.makeText(KoZnaZnaActivity.this, "Correct answer", Toast.LENGTH_SHORT).show();
        } else {
            playerScore -= 5;
            player1ScoreView.setText(Integer.toString(playerScore));
            answer1.setEnabled(false);
            answer2.setEnabled(false);
            answer3.setEnabled(false);
            answer4.setEnabled(false);
            Toast.makeText(KoZnaZnaActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(int set) {
        switch (set) {
            case 1:
                question.setText(koZnaZnaModel.getQ1());
                answer1.setText(koZnaZnaModel.getQ1a1());
                answer2.setText(koZnaZnaModel.getQ1a2());
                answer3.setText(koZnaZnaModel.getQ1a3());
                answer4.setText(koZnaZnaModel.getQ1a4());
                break;
            case 2:
                question.setText(koZnaZnaModel.getQ2());
                answer1.setText(koZnaZnaModel.getQ2a1());
                answer2.setText(koZnaZnaModel.getQ2a2());
                answer3.setText(koZnaZnaModel.getQ2a3());
                answer4.setText(koZnaZnaModel.getQ2a4());
                break;
            case 3:
                question.setText(koZnaZnaModel.getQ3());
                answer1.setText(koZnaZnaModel.getQ3a1());
                answer2.setText(koZnaZnaModel.getQ3a2());
                answer3.setText(koZnaZnaModel.getQ3a3());
                answer4.setText(koZnaZnaModel.getQ3a4());
                break;
            case 4:
                question.setText(koZnaZnaModel.getQ4());
                answer1.setText(koZnaZnaModel.getQ4a1());
                answer2.setText(koZnaZnaModel.getQ4a2());
                answer3.setText(koZnaZnaModel.getQ4a3());
                answer4.setText(koZnaZnaModel.getQ4a4());
                break;
            case 5:
                question.setText(koZnaZnaModel.getQ5());
                answer1.setText(koZnaZnaModel.getQ5a1());
                answer2.setText(koZnaZnaModel.getQ5a2());
                answer3.setText(koZnaZnaModel.getQ5a3());
                answer4.setText(koZnaZnaModel.getQ5a4());
                break;
        }
    }
}