package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.MainActivity;
import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.config.SocketHandler;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.Asocijacije;
import com.ftn.ac.rs.mobilne_2023.services.AsocijacijeService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;

import io.socket.client.Socket;

public class AsocijacijeActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    public static Socket socket = SocketHandler.getSocket();;

    private Bundle gameBundle;

    Asocijacije asocijacijeModel;

    CountDownTimer gameTimer;

    int round;
    int switcher;
    int clicker = 1;

    TextView player1ScoreView;
    TextView player2ScoreView;
    TextView player1NameView;
    TextView player2NameView;
    int playerScore;
    int player2Score;
    String playerName;
    String player2Name;

    Button a1;
    Button a2;
    Button a3;
    Button a4;
    EditText a5;

    Button b1;
    Button b2;
    Button b3;
    Button b4;
    EditText b5;

    Button c1;
    Button c2;
    Button c3;
    Button c4;
    EditText c5;

    Button d1;
    Button d2;
    Button d3;
    Button d4;
    EditText d5;

    EditText konacno;

    boolean twoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_asocijacije);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), AsocijacijeActivity.this,
                false, R.id.headAsocijacije);

        Bundle bundle = getIntent().getExtras();
        gameBundle = bundle;
        if (gameBundle != null && gameBundle.getString("user-username") == null) {
            playerScore = bundle.getInt("unreg-score");
        } else if (bundle != null) {
            playerName = bundle.getString("user-username");
            player2Name = bundle.getString("opponent-username");
            playerScore = bundle.getInt("user-score");
            player2Score = bundle.getInt("opponent-score");
        }
        round = bundle.getInt("round", 1);
        twoPlayer = (gameBundle != null && gameBundle.getString("user-username") != null);

        startGame(playerName, playerScore, player2Name, player2Score);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device.", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this,
                    proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float[] values = sensorEvent.values;
            float x = values[0];
            if (x == 0) {
                if (a5.isEnabled())
                    a5.getText().clear();
                if (b5.isEnabled())
                    b5.getText().clear();
                if (c5.isEnabled())
                    c5.getText().clear();
                if (d5.isEnabled())
                    c5.getText().clear();
                if (konacno.isEnabled())
                    konacno.getText().clear();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    @Override
    public void onBackPressed() {
        //konfiguracija dijaloga
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit Game");
        builder.setMessage("Are you sure you want to quit?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("user-username", gameBundle.getString("user-username"));
            bundle.putString("user-email", gameBundle.getString("user-email"));
            bundle.putInt("user-id", gameBundle.getInt("user-id"));
            bundle.putInt("user-tokens", gameBundle.getInt("user-tokens"));
            intent.putExtras(bundle);
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
        AsocijacijeService.get(round, result -> {
            Log.println(Log.INFO, "Recieved data: ", result.toString());
            asocijacijeModel = new Asocijacije(result);

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

            loadData();
            if (player2Name != null)
                blockInput();

            startTimer();
        });
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);
        final int[] end = new int[1];
        final int[] inverted = {0};

        gameTimer = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                    time.setText(Long.toString(millisUntilFinished / 1000));
                if (twoPlayer) {
                    socket.on("asocijacijeOpen", args -> {
                       String opened = args[0].toString(); // jedno polje
                       runOnUiThread(() -> {
                           switch (opened) {
                               case "a1":
                                   a1.setText(asocijacijeModel.getA1());
                                   a1.setEnabled(false);
                                   break;
                               case "a2":
                                   a2.setText(asocijacijeModel.getA2());
                                   a2.setEnabled(false);
                                   break;
                               case "a3":
                                   a3.setText(asocijacijeModel.getA3());
                                   a3.setEnabled(false);
                                   break;
                               case "a4":
                                   a4.setText(asocijacijeModel.getA4());
                                   a4.setEnabled(false);
                                   break;
                               case "b1":
                                   b1.setText(asocijacijeModel.getB1());
                                   b1.setEnabled(false);
                                   break;
                               case "b2":
                                   b2.setText(asocijacijeModel.getB2());
                                   b2.setEnabled(false);
                                   break;
                               case "b3":
                                   b3.setText(asocijacijeModel.getB3());
                                   b3.setEnabled(false);
                                   break;
                               case "b4":
                                   b4.setText(asocijacijeModel.getB4());
                                   b4.setEnabled(false);
                                   break;
                               case "c1":
                                   c1.setText(asocijacijeModel.getC1());
                                   c1.setEnabled(false);
                                   break;
                               case "c2":
                                   c2.setText(asocijacijeModel.getC2());
                                   c2.setEnabled(false);
                                   break;
                               case "c3":
                                   c3.setText(asocijacijeModel.getC3());
                                   c3.setEnabled(false);
                                   break;
                               case "c4":
                                   c4.setText(asocijacijeModel.getC4());
                                   c4.setEnabled(false);
                                   break;
                               case "d1":
                                   d1.setText(asocijacijeModel.getD1());
                                   d1.setEnabled(false);
                                   break;
                               case "d2":
                                   d2.setText(asocijacijeModel.getD2());
                                   d2.setEnabled(false);
                                   break;
                               case "d3":
                                   d3.setText(asocijacijeModel.getD3());
                                   d3.setEnabled(false);
                                   break;
                               case "d4":
                                   d4.setText(asocijacijeModel.getD4());
                                   d4.setEnabled(false);
                                   break;
                           }
                       });
                    });
                    socket.on("asocijacijeSolved", args ->  {
                        String solved = args[0].toString(); // samo jedno konacno
                        runOnUiThread(() -> {
                            switch (solved) {
                                case "a5":
                                    a1.setText(asocijacijeModel.getA1());
                                    a1.setEnabled(false);
                                    a2.setText(asocijacijeModel.getA2());
                                    a2.setEnabled(false);
                                    a3.setText(asocijacijeModel.getA3());
                                    a3.setEnabled(false);
                                    a4.setText(asocijacijeModel.getA4());
                                    a4.setEnabled(false);
                                    a5.setText(asocijacijeModel.getA5());
                                    a5.setEnabled(false);
                                    break;
                                case "b5":
                                    b1.setText(asocijacijeModel.getB1());
                                    b1.setEnabled(false);
                                    b2.setText(asocijacijeModel.getB2());
                                    b2.setEnabled(false);
                                    b3.setText(asocijacijeModel.getB3());
                                    b3.setEnabled(false);
                                    b4.setText(asocijacijeModel.getB4());
                                    b4.setEnabled(false);
                                    b5.setText(asocijacijeModel.getB5());
                                    b5.setEnabled(false);
                                    break;
                                case "c5":
                                    c1.setText(asocijacijeModel.getC1());
                                    c1.setEnabled(false);
                                    c2.setText(asocijacijeModel.getC2());
                                    c2.setEnabled(false);
                                    c3.setText(asocijacijeModel.getC3());
                                    c3.setEnabled(false);
                                    c4.setText(asocijacijeModel.getC4());
                                    c4.setEnabled(false);
                                    c5.setText(asocijacijeModel.getC5());
                                    c5.setEnabled(false);
                                    break;
                                case "d5":
                                    d1.setText(asocijacijeModel.getD1());
                                    d1.setEnabled(false);
                                    d2.setText(asocijacijeModel.getD2());
                                    d2.setEnabled(false);
                                    d3.setText(asocijacijeModel.getD3());
                                    d3.setEnabled(false);
                                    d4.setText(asocijacijeModel.getD4());
                                    d4.setEnabled(false);
                                    d5.setText(asocijacijeModel.getD5());
                                    d5.setEnabled(false);
                                    break;
                                case "konacno":
                                    a1.setText(asocijacijeModel.getA1());
                                    a1.setEnabled(false);
                                    a2.setText(asocijacijeModel.getA2());
                                    a2.setEnabled(false);
                                    a3.setText(asocijacijeModel.getA3());
                                    a3.setEnabled(false);
                                    a4.setText(asocijacijeModel.getA4());
                                    a4.setEnabled(false);
                                    a5.setText(asocijacijeModel.getA5());
                                    a5.setEnabled(false);
                                    b1.setText(asocijacijeModel.getB1());
                                    b1.setEnabled(false);
                                    b2.setText(asocijacijeModel.getB2());
                                    b2.setEnabled(false);
                                    b3.setText(asocijacijeModel.getB3());
                                    b3.setEnabled(false);
                                    b4.setText(asocijacijeModel.getB4());
                                    b4.setEnabled(false);
                                    b5.setText(asocijacijeModel.getB5());
                                    b5.setEnabled(false);
                                    c1.setText(asocijacijeModel.getC1());
                                    c1.setEnabled(false);
                                    c2.setText(asocijacijeModel.getC2());
                                    c2.setEnabled(false);
                                    c3.setText(asocijacijeModel.getC3());
                                    c3.setEnabled(false);
                                    c4.setText(asocijacijeModel.getC4());
                                    c4.setEnabled(false);
                                    c5.setText(asocijacijeModel.getC5());
                                    c5.setEnabled(false);
                                    d1.setText(asocijacijeModel.getD1());
                                    d1.setEnabled(false);
                                    d2.setText(asocijacijeModel.getD2());
                                    d2.setEnabled(false);
                                    d3.setText(asocijacijeModel.getD3());
                                    d3.setEnabled(false);
                                    d4.setText(asocijacijeModel.getD4());
                                    d4.setEnabled(false);
                                    d5.setText(asocijacijeModel.getD5());
                                    d5.setEnabled(false);
                                    if (twoPlayer)
                                        socket.emit("endAsocijacije");
                                    break;
                            }
                        });
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
                    socket.on("endAsocijacije", args -> {
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
                    Toast.makeText(AsocijacijeActivity.this, "End of round", Toast.LENGTH_SHORT).show();
                });
                Intent intent;
                Bundle bundle = new Bundle();
                if (round == 1) {
                    intent = new Intent(AsocijacijeActivity.this, AsocijacijeActivity.class);
                    if (gameBundle != null && gameBundle.getString("user-username") == null) {
                        bundle.putInt("unreg-score",playerScore);
                    } else if (twoPlayer) {
                        bundle.putString("user-username", playerName);
                        bundle.putString("opponent-username", player2Name);
                        bundle.putInt("user-score", playerScore);
                        bundle.putInt("opponent-score", player2Score);
                        bundle.putInt("user-id", gameBundle.getInt("user-id"));
                        bundle.putString("user-email", gameBundle.getString("user-email"));
                        bundle.putInt("user-tokens", gameBundle.getInt("user-tokens"));

                        if ((playerScore > player2Score || (playerScore == player2Score &&
                                playerName.length() > player2Name.length())) && inverted[0] == 0) {
                            inverted[0]++;
                            socket.emit("invert");
                        }
                    }
                    bundle.putInt("round", ++round);
                } else {
                    intent = new Intent(AsocijacijeActivity.this, SkockoActivity.class);
                    if (gameBundle != null && gameBundle.getString("user-username") == null) {
                        bundle.putInt("unreg-score",playerScore);
                    } else if (twoPlayer) {
                        socket.emit("endAsocijacije");

                        bundle.putString("user-username", playerName);
                        bundle.putString("opponent-username", player2Name);
                        bundle.putInt("user-score", playerScore);
                        bundle.putInt("opponent-score", player2Score);
                        bundle.putInt("user-id", gameBundle.getInt("user-id"));
                        bundle.putString("user-email", gameBundle.getString("user-email"));
                        bundle.putInt("user-tokens", gameBundle.getInt("user-tokens"));
                    }
                    bundle.remove("round");
                }
                intent.putExtras(bundle);
                gameTimer.cancel();

                if (twoPlayer) {
                    socket.emit("resetTurn");
                    socket.emit("resetSwitcher");
                }

                startActivity(intent);
            }
        }.start();
    }

    private void loadData() {
        a1 = findViewById(R.id.a1Asocijacije);
        a2 = findViewById(R.id.a2Asocijacije);
        a3 = findViewById(R.id.a3Asocijacije);
        a4 = findViewById(R.id.a4Asocijacije);
        a5 = findViewById(R.id.aKolAsocijacije);

        b1 = findViewById(R.id.b1Asocijacije);
        b2 = findViewById(R.id.b2Asocijacije);
        b3 = findViewById(R.id.b3Asocijacije);
        b4 = findViewById(R.id.b4Asocijacije);
        b5 = findViewById(R.id.bKolAsocijacije);

        c1 = findViewById(R.id.c1Asocijacije);
        c2 = findViewById(R.id.c2Asocijacije);
        c3 = findViewById(R.id.c3Asocijacije);
        c4 = findViewById(R.id.c4Asocijacije);
        c5 = findViewById(R.id.cKolAsocijacije);

        d1 = findViewById(R.id.d1Asocijacije);
        d2 = findViewById(R.id.d2Asocijacije);
        d3 = findViewById(R.id.d3Asocijacije);
        d4 = findViewById(R.id.d4Asocijacije);
        d5 = findViewById(R.id.dKolAsocijacije);

        konacno = findViewById(R.id.konacnoAsocijacije);

        a1.setOnClickListener(view -> {
            if (clicker == 1) {
                a1.setText(asocijacijeModel.getA1());
                a1.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "a1");

                clicker--;
            }
        });

        a2.setOnClickListener(view -> {
            if (clicker == 1) {
                a2.setText(asocijacijeModel.getA2());
                a2.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "a2");

                clicker--;
            }
        });

        a3.setOnClickListener(view -> {
            if (clicker == 1) {
                a3.setText(asocijacijeModel.getA3());
                a3.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "a3");

                clicker--;
            }
        });

        a4.setOnClickListener(view -> {
            if (clicker == 1) {
                a4.setText(asocijacijeModel.getA4());
                a4.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "a4");

                clicker--;
            }
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

                    if (twoPlayer) {
                        socket.emit("asocijacijeSolution", "a5");
                        socket.emit("asocijacijeScoreUpdate", playerName, playerScore, player2Name, player2Score);
                    }

                    return true;
                } else {
                    Toast.makeText(AsocijacijeActivity.this, "Wrong guess", Toast.LENGTH_SHORT).show();
                    if (twoPlayer)
                        blockInput();
                }
                clicker++;
            }
            return false;
        });

        b1.setOnClickListener(view -> {
            if (clicker == 1) {
                b1.setText(asocijacijeModel.getB1());
                b1.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "b1");

                clicker--;
            }
        });

        b2.setOnClickListener(view -> {
            if (clicker == 1) {
                b2.setText(asocijacijeModel.getB2());
                b2.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "b2");

                clicker--;
            }
        });

        b3.setOnClickListener(view -> {
            if (clicker == 1) {
                b3.setText(asocijacijeModel.getB3());
                b3.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "b3");

                clicker--;
            }
        });

        b4.setOnClickListener(view -> {
            if (clicker == 1) {
                b4.setText(asocijacijeModel.getB4());
                b4.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "b4");

                clicker--;
            }
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

                    if (twoPlayer) {
                        socket.emit("asocijacijeSolution", "b5");
                        socket.emit("asocijacijeScoreUpdate", playerName, playerScore, player2Name, player2Score);
                    }

                    return true;
                } else {
                    Toast.makeText(AsocijacijeActivity.this, "Wrong guess", Toast.LENGTH_SHORT).show();
                    if (twoPlayer)
                        blockInput();
                }
                clicker++;
            }
            return false;
        });

        c1.setOnClickListener(view -> {
            if (clicker == 1) {
                c1.setText(asocijacijeModel.getC1());
                c1.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "c1");

                clicker--;
            }
        });

        c2.setOnClickListener(view -> {
            if (clicker == 1) {
                c2.setText(asocijacijeModel.getC2());
                c2.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "c2");

                clicker--;
            }
        });

        c3.setOnClickListener(view -> {
            if (clicker == 1) {
                c3.setText(asocijacijeModel.getC3());
                c3.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "c3");

                clicker--;
            }
        });

        c4.setOnClickListener(view -> {
            if (clicker == 1) {
                c4.setText(asocijacijeModel.getC4());
                c4.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "c4");

                clicker--;
            }
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

                    if (twoPlayer) {
                        socket.emit("asocijacijeSolution", "c5");
                        socket.emit("asocijacijeScoreUpdate", playerName, playerScore, player2Name, player2Score);
                    }

                    return true;
                } else {
                    Toast.makeText(AsocijacijeActivity.this, "Wrong guess", Toast.LENGTH_SHORT).show();
                    if (twoPlayer)
                        blockInput();
                }
                clicker++;
            }
            return false;
        });

        d1.setOnClickListener(view -> {
            if (clicker == 1) {
                d1.setText(asocijacijeModel.getD1());
                d1.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "d1");

                clicker--;
            }
        });

        d2.setOnClickListener(view -> {
            if (clicker == 1) {
                d2.setText(asocijacijeModel.getD2());
                d2.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "d2");

                clicker--;
            }
        });

        d3.setOnClickListener(view -> {
            if (clicker == 1) {
                d3.setText(asocijacijeModel.getD3());
                d3.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "d3");

                clicker--;
            }
        });

        d4.setOnClickListener(view -> {
            if (clicker == 1) {
                d4.setText(asocijacijeModel.getD4());
                d4.setEnabled(false);

                if (twoPlayer)
                    socket.emit("asocijacijeOpened", "d4");

                clicker--;
            }
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

                    if (twoPlayer) {
                        socket.emit("asocijacijeSolution", "d5");
                        socket.emit("asocijacijeScoreUpdate", playerName, playerScore, player2Name, player2Score);
                    }

                    return true;
                } else {
                    Toast.makeText(AsocijacijeActivity.this, "Wrong guess", Toast.LENGTH_SHORT).show();
                    if (twoPlayer)
                        blockInput();
                }
                clicker++;
            }
            return false;
        });

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

                    if (twoPlayer) {
                        socket.emit("asocijacijeSolution", "konacno");
                        socket.emit("asocijacijeScoreUpdate", playerName, playerScore, player2Name, player2Score);
                    }

                    gameTimer.onFinish();

                    return true;
                } else {
                    Toast.makeText(this, "Wrong guess", Toast.LENGTH_SHORT).show();
                    if (twoPlayer)
                        blockInput();
                    clicker = 1;
                }
            }
            return false;
        });
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
                } else {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(this, "Your inputs are blocked. Wait for your turn",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}