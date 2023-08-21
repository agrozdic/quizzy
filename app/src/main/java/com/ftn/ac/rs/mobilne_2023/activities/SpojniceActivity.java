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
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.Spojnice;
import com.ftn.ac.rs.mobilne_2023.services.SpojniceService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

public class SpojniceActivity extends AppCompatActivity {

    Spojnice spojniceModel;

    CountDownTimer gameTimer;

    int round;

    TextView player1ScoreView;
    int playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_spojnice);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), SpojniceActivity.this,
                false, R.id.headSpojnice);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            playerScore = Integer.parseInt(bundle.getString("unreg-score"));
            round = bundle.getInt("round", 1);
        }

        startGame();
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

    private void startGame() {
        SpojniceService.get(round, result -> {
            Log.println(Log.INFO, "Recieved data: ", result.toString());
            spojniceModel = new Spojnice(result);

            player1ScoreView = findViewById(R.id.player1Score);
            player1ScoreView.setText(Integer.toString(playerScore));

            loadControls();

            startTimer();
        });
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);

        gameTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                Toast.makeText(SpojniceActivity.this, "End of round", Toast.LENGTH_LONG).show();
                if (round == 1) {
                    Intent intent = new Intent(SpojniceActivity.this, SpojniceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("unreg-score", Integer.toString(playerScore));
                    bundle.putInt("round", ++round);
                    intent.putExtras(bundle);
                    gameTimer.cancel();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SpojniceActivity.this, AsocijacijeActivity.class);
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

    TextView prompt;
    Button pair1a;
    Button pair2a;
    Button pair3a;
    Button pair4a;
    Button pair5a;
    Button pair1b;
    Button pair2b;
    Button pair3b;
    Button pair4b;
    Button pair5b;

    private void loadControls() {
        prompt = findViewById(R.id.promptSpojnice);
        pair1a = findViewById(R.id.pair1aSpojnice);
        pair2a = findViewById(R.id.pair2aSpojnice);
        pair3a = findViewById(R.id.pair3aSpojnice);
        pair4a = findViewById(R.id.pair4aSpojnice);
        pair5a = findViewById(R.id.pair5aSpojnice);
        pair1b = findViewById(R.id.pair1bSpojnice);
        pair2b = findViewById(R.id.pair2bSpojnice);
        pair3b = findViewById(R.id.pair3bSpojnice);
        pair4b = findViewById(R.id.pair4bSpojnice);
        pair5b = findViewById(R.id.pair5bSpojnice);

        pair1a.setEnabled(false);

        pair1b.setOnClickListener(view -> checkSolution("1b"));
        pair2b.setOnClickListener(view -> checkSolution("2b"));
        pair3b.setOnClickListener(view -> checkSolution("3b"));
        pair4b.setOnClickListener(view -> checkSolution("4b"));
        pair5b.setOnClickListener(view -> checkSolution("5b"));

        loadData();
    }

    private void loadData() {
        prompt.setText(spojniceModel.getPrompt());
        pair1a.setText(spojniceModel.getPair1a());
        pair2a.setText(spojniceModel.getPair2a());
        pair3a.setText(spojniceModel.getPair3a());
        pair4a.setText(spojniceModel.getPair4a());
        pair5a.setText(spojniceModel.getPair5a());
        pair1b.setText(spojniceModel.getPair1b());
        pair2b.setText(spojniceModel.getPair2b());
        pair3b.setText(spojniceModel.getPair3b());
        pair4b.setText(spojniceModel.getPair4b());
        pair5b.setText(spojniceModel.getPair5b());
    }

    private void checkSolution(String buttonPressed) {
        String currentPair;
        if (!pair5a.isEnabled())
            currentPair = "5a";
        else if (!pair4a.isEnabled())
            currentPair = "4a";
        else if (!pair3a.isEnabled())
            currentPair = "3a";
        else if (!pair2a.isEnabled())
            currentPair = "2a";
        else
            currentPair = "1a";

        String solution = currentPair + "->" + buttonPressed;
        boolean contains = spojniceModel.getSolutions().contains(solution);
        if (contains) {
            playerScore += 2;
            player1ScoreView.setText(Integer.toString(playerScore));
            switch (buttonPressed) {
                case "1b":
                    pair1b.setEnabled(false);
                    break;
                case "2b":
                    pair2b.setEnabled(false);
                    break;
                case "3b":
                    pair3b.setEnabled(false);
                    break;
                case "4b":
                    pair4b.setEnabled(false);
                    break;
                case "5b":
                    pair5b.setEnabled(false);
                    break;
            }
        }

        progressGame(currentPair);
    }

    private void progressGame(String currentPair) {
        switch (currentPair) {
            case "1a":
                pair2a.setEnabled(false);
                break;
            case "2a":
                pair3a.setEnabled(false);
                break;
            case "3a":
                pair4a.setEnabled(false);
                break;
            case "4a":
                pair5a.setEnabled(false);
                break;
            case "5a":
                gameTimer.onFinish();
                gameTimer.cancel();
                break;
        }
    }
}
