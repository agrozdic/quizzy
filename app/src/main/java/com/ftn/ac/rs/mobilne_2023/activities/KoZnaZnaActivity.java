package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.MainActivity;
import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.config.SocketHandler;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.KoZnaZna;
import com.ftn.ac.rs.mobilne_2023.services.KoZnaZnaService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;

import io.socket.client.Socket;

public class KoZnaZnaActivity extends AppCompatActivity {

    public static Socket socket = SocketHandler.getSocket();;

    private Bundle gameBundle;

    KoZnaZna koZnaZnaModel;
    CountDownTimer gameTimer;
    CountDownTimer questionTimer;

    int timerCounter = 1;
    int round;

    TextView player1ScoreView;
    TextView player2ScoreView;
    TextView player1NameView;
    TextView player2NameView;
    int playerScore;
    int player2Score;
    String playerName;
    String player2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ko_zna_zna);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), KoZnaZnaActivity.this,
                false, R.id.headKoZnaZna);

        Bundle bundle = getIntent().getExtras();
        gameBundle = bundle;
        if (bundle != null && bundle.getString("unreg-score") != null) {
            playerScore = Integer.parseInt(bundle.getString("unreg-score"));
            round = bundle.getInt("round", 1);
        } else if (bundle != null) {
            playerName = bundle.getString("user-username");
            player2Name = bundle.getString("opponent-username");
        }

        startGame(playerName, player2Name);
    }

    @Override
    public void onBackPressed() {
        //konfiguracija dijaloga
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit Game");
        builder.setMessage("Are you sure you want to quit?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        // prikaz dijaloga
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startGame(String playerName, String player2Name) {
        KoZnaZnaService.get(1, result -> {
            Log.println(Log.INFO, "Received data: ", result.toString());
            koZnaZnaModel = new KoZnaZna(result);

            player1ScoreView = findViewById(R.id.player1Score);
            player1ScoreView.setText(Integer.toString(0));
            player2ScoreView = findViewById(R.id.player2Score);
            player2ScoreView.setText(Integer.toString(0));

            if (playerName != null && player2Name != null) {
                player1NameView = findViewById(R.id.player1Name);
                player1NameView.setText(playerName);
                player2NameView = findViewById(R.id.player2Name);
                player2NameView.setText(player2Name);
            }

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
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                updateView();
                timerCounter++;
                if (timerCounter > 5)
                    this.cancel();
                else {
                    loadData(timerCounter);
                    this.cancel();
                    this.start();
                    enableAnswerButtons();
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
            if (gameBundle == null || gameBundle.getString("user-username") == null) {
                playerScore += 10;
                player1ScoreView.setText(Integer.toString(playerScore));
            } else {
                socket.emit("koZnaZnaAnswer", true, LocalDateTime.now().toString(),
                        gameBundle.getString("user-username"));
            }
            disableAnswerButtons();
            Toast.makeText(KoZnaZnaActivity.this, "Correct answer", Toast.LENGTH_SHORT).show();
        } else {
            if (gameBundle == null || gameBundle.getString("user-username") == null) {
                playerScore -= 5;
                player1ScoreView.setText(Integer.toString(playerScore));
            } else {
                socket.emit("koZnaZnaAnswer", false, LocalDateTime.now().toString(),
                        gameBundle.getString("user-username"));
            }
            disableAnswerButtons();
            Toast.makeText(KoZnaZnaActivity.this, "Wrong answer", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateView() {
        if (gameBundle == null || gameBundle.getString("user-username") == null)
            return;

        socket.emit("koZnaZnaAnswerCheck");
        socket.on("scoreUpdate", args -> {
            if (args.length > 0 && args[0] instanceof JSONObject) {
                JSONObject data = (JSONObject) args[0];
                Log.println(Log.INFO, "args-score", Arrays.toString(args));
                try {
                    for (Iterator<String> it = data.keys(); it.hasNext(); ) {
                        String playerName = it.next();
                        if (playerName.equals(gameBundle.getString("user-username"))) {
                            playerScore = data.getInt(playerName);
                            runOnUiThread(() ->
                                    player1ScoreView.setText(Integer.toString(playerScore)));
                        }
                        else if (playerName.equals(gameBundle.getString("opponent-username"))) {
                            player2Score = data.getInt(playerName);
                            runOnUiThread(() ->
                                    player2ScoreView.setText(Integer.toString(player2Score)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void enableAnswerButtons() {
        answer1.setEnabled(true);
        answer2.setEnabled(true);
        answer3.setEnabled(true);
        answer4.setEnabled(true);
    }

    private void disableAnswerButtons() {
        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);
        answer4.setEnabled(false);
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
