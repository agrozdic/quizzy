package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.MainActivity;
import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.config.SocketHandler;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.Spojnice;
import com.ftn.ac.rs.mobilne_2023.services.SpojniceService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;

import io.socket.client.Socket;

public class SpojniceActivity extends AppCompatActivity {

    public static Socket socket = SocketHandler.getSocket();;

    private Bundle gameBundle;

    Spojnice spojniceModel;

    CountDownTimer gameTimer;

    int round;
    int switcher;

    TextView player1ScoreView;
    TextView player2ScoreView;
    TextView player1NameView;
    TextView player2NameView;
    int playerScore;
    int player2Score;
    int playerStartScore;
    int player2StartScore;
    String playerName;
    String player2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_spojnice);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), SpojniceActivity.this,
                false, R.id.headSpojnice);

        Bundle bundle = getIntent().getExtras();
        gameBundle = bundle;
        if (gameBundle != null && gameBundle.getString("user-username") == null) {
            playerScore = bundle.getInt("unreg-score");
        } else if (bundle != null) {
            playerName = bundle.getString("user-username");
            player2Name = bundle.getString("opponent-username");
            playerScore = bundle.getInt("user-score");
            playerStartScore = playerScore;
            player2Score = bundle.getInt("opponent-score");
            player2StartScore = player2Score;
        }
        round = bundle.getInt("round", 1);

        startGame(playerName, playerScore, player2Name, player2Score);
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

            gameTimer.cancel();
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        // prikaz dijaloga
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startGame(String playerName, int playerScore, String player2Name, int player2Score) {
        SpojniceService.get(round, result -> {
            Log.println(Log.INFO, "Recieved data: ", result.toString());
            spojniceModel = new Spojnice(result);

            if (playerName == null) {
                player1ScoreView = findViewById(R.id.player1Score);
                player1ScoreView.setText(Integer.toString(playerScore));
            } else {
                player1ScoreView = findViewById(R.id.player1Score);
                player1ScoreView.setText(Integer.toString(playerScore));
                player2ScoreView = findViewById(R.id.player2Score);
                player2ScoreView.setText(Integer.toString(player2Score));
                player1NameView = findViewById(R.id.player1Name);
                player1NameView.setText(playerName);
                player2NameView = findViewById(R.id.player2Name);
                player2NameView.setText(player2Name);
            }

            loadControls();
            if (player2Name != null)
                blockInput();
            startTimer();
        });
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);
        final int[] end = new int[1];

        gameTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(Long.toString(millisUntilFinished / 1000));
                if (gameBundle != null && gameBundle.getString("user-username") != null) {
                    socket.on("spojniceUpdate", args -> {
                        String solved = args[0].toString();
                        String[] solutions = solved.split(";");
                        for (String solution: solutions) {
                            if (!solution.equals("")) {
                                runOnUiThread(() -> {
                                    if (solution.contains("1a"))
                                        pair1a.setEnabled(false);
                                    if (solution.contains("2a"))
                                        pair2a.setEnabled(false);
                                    if (solution.contains("3a"))
                                        pair3a.setEnabled(false);
                                    if (solution.contains("4a"))
                                        pair4a.setEnabled(false);
                                    if (solution.contains("5a"))
                                        pair5a.setEnabled(false);
                                    if (solution.contains("1b"))
                                        pair1b.setEnabled(false);
                                    if (solution.contains("2b"))
                                        pair2b.setEnabled(false);
                                    if (solution.contains("3b"))
                                        pair3b.setEnabled(false);
                                    if (solution.contains("4b"))
                                        pair4b.setEnabled(false);
                                    if (solution.contains("5b"))
                                        pair5b.setEnabled(false);
                                });
                            }
                        }
                        String currentPair = "";
                        if (!pair5a.isEnabled())
                            currentPair = "5a";
                        if (!pair4a.isEnabled())
                            currentPair = "4a";
                        if (!pair3a.isEnabled())
                            currentPair = "3a";
                        if (!pair2a.isEnabled())
                            currentPair = "2a";
                        if (!pair1a.isEnabled())
                            currentPair = "1a";
                        if ((pair1a.isEnabled() && pair2a.isEnabled() && pair3a.isEnabled()
                                && pair4a.isEnabled() && pair5a.isEnabled()) ||
                                pair1a.isEnabled()) {
                            currentPair = "0a";
                        }
                        progressGame(currentPair);
                    });
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
                    socket.on("endSpojnice", args -> {
                        if (end[0] != 1) {
                            gameTimer.onFinish();
                            gameTimer.cancel();
                        }
                    });
                }
            }

            public void onFinish() {
                end[0] = 1;
                runOnUiThread(() -> {
                    Toast.makeText(SpojniceActivity.this, "End of round", Toast.LENGTH_SHORT).show();
                });
                Intent intent;
                Bundle bundle = new Bundle();
                if (round == 1) {
                    intent = new Intent(SpojniceActivity.this, SpojniceActivity.class);
                    if (gameBundle != null && gameBundle.getString("user-username") == null) {
                        bundle.putInt("unreg-score",playerScore);
                    } else if (gameBundle != null && gameBundle.getString("user-username") != null) {
                        bundle.putString("user-username", playerName);
                        bundle.putString("opponent-username", player2Name);
                        bundle.putInt("user-score", playerScore);
                        bundle.putInt("opponent-score", player2Score);

                        // sansa da imaju isti rezultat posle ko zna zna i 1 runde spojnica
                        // (u teoriji) je oko 0.85%
                        // ako imaju isti rezultat, gleda se duzina imena, ako je i to isto (0.425%)
                        // onda svaka cast
                        if (playerScore > player2Score
                                || (playerScore == player2Score &&
                                    playerName.length() > player2Name.length())) {
                            socket.emit("invert");
                        }
                    }
                    bundle.putInt("round", ++round);
                } else {
                    socket.emit("endSpojnice");
                    intent = new Intent(SpojniceActivity.this, AsocijacijeActivity.class);
                    if (gameBundle != null && gameBundle.getString("user-username") == null) {
                        bundle.putInt("unreg-score",playerScore);
                    } else if (gameBundle != null && gameBundle.getString("user-username") != null) {
                        bundle.putString("user-username", playerName);
                        bundle.putString("opponent-username", player2Name);
                        bundle.putInt("user-score", playerScore);
                        bundle.putInt("opponent-score", player2Score);
                    }
                    bundle.remove("round");
                }
                intent.putExtras(bundle);
                gameTimer.cancel();
                socket.emit("resetTurn");
                socket.emit("resetSwitcher");
                startActivity(intent);
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

        pair1b.setOnClickListener(view -> checkSolution("1b"));
        pair2b.setOnClickListener(view -> checkSolution("2b"));
        pair3b.setOnClickListener(view -> checkSolution("3b"));
        pair4b.setOnClickListener(view -> checkSolution("4b"));
        pair5b.setOnClickListener(view -> checkSolution("5b"));

        loadData();
    }

    private void blockInput() {
        socket.emit("getTurn");
        socket.on("turn", args -> {
            String username = args[0].toString();
            runOnUiThread(() -> {
                if (gameBundle.getString("user-username") != null &&
                        username.equals(gameBundle.getString("user-username"))) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(this, "Inputs allowed. Your turn",
                            Toast.LENGTH_SHORT).show();
                    progressGame("0a");
                } else {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(this, "Your inputs are blocked. Wait for your turn",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
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
        runOnUiThread(() -> {
            switch (currentPair) {
                case "0a":
                    pair1a.setEnabled(false);
                    break;
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
                    if (gameBundle != null && gameBundle.getString("user-username") != null) {
                        socket.emit("getSwitcher");
                        socket.on("switcher", args -> {
                            switcher = Integer.parseInt(args[0].toString());
                            Log.println(Log.INFO, "swithcer -> ", Integer.toString(switcher));
                        });
                        if (switcher == 2) {
                            gameTimer.onFinish();
                            gameTimer.cancel();
                        } else {
                            StringBuilder temp = new StringBuilder();
                            for (String solution: spojniceModel.getSolutions()) {
                                if (!pair1b.isEnabled() && solution.contains("1b"))
                                    temp.append(solution).append(";");
                                if (!pair2b.isEnabled() && solution.contains("2b"))
                                    temp.append(solution).append(";");
                                if (!pair3b.isEnabled() && solution.contains("3b"))
                                    temp.append(solution).append(";");
                                if (!pair4b.isEnabled() && solution.contains("4b"))
                                    temp.append(solution).append(";");
                                if (!pair5b.isEnabled() && solution.contains("5b"))
                                    temp.append(solution).append(";");
                            }
                            String solved = temp.toString();
                            socket.emit("spojniceSolution", solved);
                            socket.emit("spojniceScoreUpdate", playerName, playerScore, player2Name, player2Score);
                            blockInput();
                        }
                    } else {
                        gameTimer.onFinish();
                        gameTimer.cancel();
                    }
                    break;
            }
        });
    }
}
