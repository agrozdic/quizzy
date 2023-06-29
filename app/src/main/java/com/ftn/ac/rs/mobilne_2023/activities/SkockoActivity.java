package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class SkockoActivity extends AppCompatActivity {

    private ImageButton field1x1;
    private ImageButton field1x2;
    private ImageButton field1x3;
    private ImageButton field1x4;
    private ImageButton hit1x1;
    private ImageButton hit1x2;
    private ImageButton hit1x3;
    private ImageButton hit1x4;
    private ImageButton field2x1;
    private ImageButton field2x2;
    private ImageButton field2x3;
    private ImageButton field2x4;
    private ImageButton hit2x1;
    private ImageButton hit2x2;
    private ImageButton hit2x3;
    private ImageButton hit2x4;
    private ImageButton field3x1;
    private ImageButton field3x2;
    private ImageButton field3x3;
    private ImageButton field3x4;
    private ImageButton hit3x1;
    private ImageButton hit3x2;
    private ImageButton hit3x3;
    private ImageButton hit3x4;
    private ImageButton field4x1;
    private ImageButton field4x2;
    private ImageButton field4x3;
    private ImageButton field4x4;
    private ImageButton hit4x1;
    private ImageButton hit4x2;
    private ImageButton hit4x3;
    private ImageButton hit4x4;
    private ImageButton field5x1;
    private ImageButton field5x2;
    private ImageButton field5x3;
    private ImageButton field5x4;
    private ImageButton hit5x1;
    private ImageButton hit5x2;
    private ImageButton hit5x3;
    private ImageButton hit5x4;
    private ImageButton field6x1;
    private ImageButton field6x2;
    private ImageButton field6x3;
    private ImageButton field6x4;
    private ImageButton hit6x1;
    private ImageButton hit6x2;
    private ImageButton hit6x3;
    private ImageButton hit6x4;
    private ImageButton field8x1;
    private ImageButton field8x2;
    private ImageButton field8x3;
    private ImageButton field8x4;
    private ImageButton iconSkocko;
    private ImageButton iconTref;
    private ImageButton iconPik;
    private ImageButton iconHerc;
    private ImageButton iconKaro;
    private ImageButton iconZvezda;
    private Button submit;

    private int attemptLine = 1;
    private ArrayList<String> attempt = new ArrayList<>();
    private ArrayList<String> solution = new ArrayList<>();

    CountDownTimer gameTimer;
    int round;
    TextView player1ScoreView;
    int playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_skocko);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), SkockoActivity.this,
                false, R.id.headSkocko);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            playerScore = Integer.parseInt(bundle.getString("unreg-score"));
            round = bundle.getInt("round", 1);
        }

        initializeView();
        initializeListeners();

        Handler handler = new Handler();
        handler.postDelayed(() -> startGame(), 1000); // nasty hack
    }

    private void startGame() {
        if (round == 1) {
            solution.add("skocko");
            solution.add("tref");
            solution.add("pik");
            solution.add("pik");
        } else {
            solution.add("skocko");
            solution.add("skocko");
            solution.add("zvezda");
            solution.add("karo");
        }

        player1ScoreView = findViewById(R.id.player1Score);
        player1ScoreView.setText(Integer.toString(playerScore));

        startTimer();
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);

        gameTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                Toast.makeText(SkockoActivity.this, "End of round", Toast.LENGTH_LONG).show();
                if (round == 1) {
                    Intent intent = new Intent(SkockoActivity.this, SkockoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("unreg-score", Integer.toString(playerScore));
                    bundle.putInt("round", ++round);
                    intent.putExtras(bundle);
                    gameTimer.cancel();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SkockoActivity.this, KorakPoKorakActivity.class);
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

    private void initializeView() {
        field1x1 = findViewById(R.id.field1x1);
        field1x2 = findViewById(R.id.field1x2);
        field1x3 = findViewById(R.id.field1x3);
        field1x4 = findViewById(R.id.field1x4);
        hit1x1 = findViewById(R.id.hit1x1);
        hit1x2 = findViewById(R.id.hit1x2);
        hit1x3 = findViewById(R.id.hit1x3);
        hit1x4 = findViewById(R.id.hit1x4);
        field2x1 = findViewById(R.id.field2x1);
        field2x2 = findViewById(R.id.field2x2);
        field2x3 = findViewById(R.id.field2x3);
        field2x4 = findViewById(R.id.field2x4);
        hit2x1 = findViewById(R.id.hit2x1);
        hit2x2 = findViewById(R.id.hit2x2);
        hit2x3 = findViewById(R.id.hit2x3);
        hit2x4 = findViewById(R.id.hit2x4);
        field3x1 = findViewById(R.id.field3x1);
        field3x2 = findViewById(R.id.field3x2);
        field3x3 = findViewById(R.id.field3x3);
        field3x4 = findViewById(R.id.field3x4);
        hit3x1 = findViewById(R.id.hit3x1);
        hit3x2 = findViewById(R.id.hit3x2);
        hit3x3 = findViewById(R.id.hit3x3);
        hit3x4 = findViewById(R.id.hit3x4);
        field4x1 = findViewById(R.id.field4x1);
        field4x2 = findViewById(R.id.field4x2);
        field4x3 = findViewById(R.id.field4x3);
        field4x4 = findViewById(R.id.field4x4);
        hit4x1 = findViewById(R.id.hit4x1);
        hit4x2 = findViewById(R.id.hit4x2);
        hit4x3 = findViewById(R.id.hit4x3);
        hit4x4 = findViewById(R.id.hit4x4);
        field5x1 = findViewById(R.id.field5x1);
        field5x2 = findViewById(R.id.field5x2);
        field5x3 = findViewById(R.id.field5x3);
        field5x4 = findViewById(R.id.field5x4);
        hit5x1 = findViewById(R.id.hit5x1);
        hit5x2 = findViewById(R.id.hit5x2);
        hit5x3 = findViewById(R.id.hit5x3);
        hit5x4 = findViewById(R.id.hit5x4);
        field6x1 = findViewById(R.id.field6x1);
        field6x2 = findViewById(R.id.field6x2);
        field6x3 = findViewById(R.id.field6x3);
        field6x4 = findViewById(R.id.field6x4);
        hit6x1 = findViewById(R.id.hit6x1);
        hit6x2 = findViewById(R.id.hit6x2);
        hit6x3 = findViewById(R.id.hit6x3);
        hit6x4 = findViewById(R.id.hit6x4);
        field8x1 = findViewById(R.id.field8x1);
        field8x2 = findViewById(R.id.field8x2);
        field8x3 = findViewById(R.id.field8x3);
        field8x4 = findViewById(R.id.field8x4);
        iconSkocko = findViewById(R.id.iconSkocko);
        iconTref = findViewById(R.id.iconTref);
        iconPik = findViewById(R.id.iconPik);
        iconHerc= findViewById(R.id.iconHerc);
        iconKaro = findViewById(R.id.iconKaro);
        iconZvezda = findViewById(R.id.iconZvezda);
        submit = findViewById(R.id.submitSkocko);
    }

    private void initializeListeners() {
        iconSkocko.setOnClickListener(view -> {
            if (attempt.size() < 4) {

                if (attempt.isEmpty()) {
                    switch (attemptLine) {
                        case 1:
                            field1x1.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 2:
                            field2x1.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 3:
                            field3x1.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 4:
                            field4x1.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 5:
                            field5x1.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 6:
                            field6x1.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                    }
                }

                if (attempt.size() == 1) {
                    switch (attemptLine) {
                        case 1:
                            field1x2.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 2:
                            field2x2.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 3:
                            field3x2.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 4:
                            field4x2.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 5:
                            field5x2.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 6:
                            field6x2.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                    }
                }

                if (attempt.size() == 2) {
                    switch (attemptLine) {
                        case 1:
                            field1x3.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 2:
                            field2x3.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 3:
                            field3x3.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 4:
                            field4x3.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 5:
                            field5x3.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 6:
                            field6x3.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                    }
                }

                if (attempt.size() == 3) {
                    switch (attemptLine) {
                        case 1:
                            field1x4.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 2:
                            field2x4.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 3:
                            field3x4.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 4:
                            field4x4.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 5:
                            field5x4.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                        case 6:
                            field6x4.setImageResource(R.drawable.ic_android_black_24dp);
                            break;
                    }
                }

                attempt.add("skocko");
            }
        });

        iconTref.setOnClickListener(view -> {
            if (attempt.size() < 4) {

                if (attempt.isEmpty()) {
                    switch (attemptLine) {
                        case 1:
                            field1x1.setImageResource(R.drawable.cards_club);
                            break;
                        case 2:
                            field2x1.setImageResource(R.drawable.cards_club);
                            break;
                        case 3:
                            field3x1.setImageResource(R.drawable.cards_club);
                            break;
                        case 4:
                            field4x1.setImageResource(R.drawable.cards_club);
                            break;
                        case 5:
                            field5x1.setImageResource(R.drawable.cards_club);
                            break;
                        case 6:
                            field6x1.setImageResource(R.drawable.cards_club);
                            break;
                    }
                }

                if (attempt.size() == 1) {
                    switch (attemptLine) {
                        case 1:
                            field1x2.setImageResource(R.drawable.cards_club);
                            break;
                        case 2:
                            field2x2.setImageResource(R.drawable.cards_club);
                            break;
                        case 3:
                            field3x2.setImageResource(R.drawable.cards_club);
                            break;
                        case 4:
                            field4x2.setImageResource(R.drawable.cards_club);
                            break;
                        case 5:
                            field5x2.setImageResource(R.drawable.cards_club);
                            break;
                        case 6:
                            field6x2.setImageResource(R.drawable.cards_club);
                            break;
                    }
                }

                if (attempt.size() == 2) {
                    switch (attemptLine) {
                        case 1:
                            field1x3.setImageResource(R.drawable.cards_club);
                            break;
                        case 2:
                            field2x3.setImageResource(R.drawable.cards_club);
                            break;
                        case 3:
                            field3x3.setImageResource(R.drawable.cards_club);
                            break;
                        case 4:
                            field4x3.setImageResource(R.drawable.cards_club);
                            break;
                        case 5:
                            field5x3.setImageResource(R.drawable.cards_club);
                            break;
                        case 6:
                            field6x3.setImageResource(R.drawable.cards_club);
                            break;
                    }
                }

                if (attempt.size() == 3) {
                    switch (attemptLine) {
                        case 1:
                            field1x4.setImageResource(R.drawable.cards_club);
                            break;
                        case 2:
                            field2x4.setImageResource(R.drawable.cards_club);
                            break;
                        case 3:
                            field3x4.setImageResource(R.drawable.cards_club);
                            break;
                        case 4:
                            field4x4.setImageResource(R.drawable.cards_club);
                            break;
                        case 5:
                            field5x4.setImageResource(R.drawable.cards_club);
                            break;
                        case 6:
                            field6x4.setImageResource(R.drawable.cards_club);
                            break;
                    }
                }

                attempt.add("tref");
            }
        });

        iconPik.setOnClickListener(view -> {
            if (attempt.size() < 4) {

                if (attempt.isEmpty()) {
                    switch (attemptLine) {
                        case 1:
                            field1x1.setImageResource(R.drawable.cards_spade);
                            break;
                        case 2:
                            field2x1.setImageResource(R.drawable.cards_spade);
                            break;
                        case 3:
                            field3x1.setImageResource(R.drawable.cards_spade);
                            break;
                        case 4:
                            field4x1.setImageResource(R.drawable.cards_spade);
                            break;
                        case 5:
                            field5x1.setImageResource(R.drawable.cards_spade);
                            break;
                        case 6:
                            field6x1.setImageResource(R.drawable.cards_spade);
                            break;
                    }
                }

                if (attempt.size() == 1) {
                    switch (attemptLine) {
                        case 1:
                            field1x2.setImageResource(R.drawable.cards_spade);
                            break;
                        case 2:
                            field2x2.setImageResource(R.drawable.cards_spade);
                            break;
                        case 3:
                            field3x2.setImageResource(R.drawable.cards_spade);
                            break;
                        case 4:
                            field4x2.setImageResource(R.drawable.cards_spade);
                            break;
                        case 5:
                            field5x2.setImageResource(R.drawable.cards_spade);
                            break;
                        case 6:
                            field6x2.setImageResource(R.drawable.cards_spade);
                            break;
                    }
                }

                if (attempt.size() == 2) {
                    switch (attemptLine) {
                        case 1:
                            field1x3.setImageResource(R.drawable.cards_spade);
                            break;
                        case 2:
                            field2x3.setImageResource(R.drawable.cards_spade);
                            break;
                        case 3:
                            field3x3.setImageResource(R.drawable.cards_spade);
                            break;
                        case 4:
                            field4x3.setImageResource(R.drawable.cards_spade);
                            break;
                        case 5:
                            field5x3.setImageResource(R.drawable.cards_spade);
                            break;
                        case 6:
                            field6x3.setImageResource(R.drawable.cards_spade);
                            break;
                    }
                }

                if (attempt.size() == 3) {
                    switch (attemptLine) {
                        case 1:
                            field1x4.setImageResource(R.drawable.cards_spade);
                            break;
                        case 2:
                            field2x4.setImageResource(R.drawable.cards_spade);
                            break;
                        case 3:
                            field3x4.setImageResource(R.drawable.cards_spade);
                            break;
                        case 4:
                            field4x4.setImageResource(R.drawable.cards_spade);
                            break;
                        case 5:
                            field5x4.setImageResource(R.drawable.cards_spade);
                            break;
                        case 6:
                            field6x4.setImageResource(R.drawable.cards_spade);
                            break;
                    }
                }

                attempt.add("pik");
            }
        });

        iconHerc.setOnClickListener(view -> {
            if (attempt.size() < 4) {

                if (attempt.isEmpty()) {
                    switch (attemptLine) {
                        case 1:
                            field1x1.setImageResource(R.drawable.cards_heart);
                            break;
                        case 2:
                            field2x1.setImageResource(R.drawable.cards_heart);
                            break;
                        case 3:
                            field3x1.setImageResource(R.drawable.cards_heart);
                            break;
                        case 4:
                            field4x1.setImageResource(R.drawable.cards_heart);
                            break;
                        case 5:
                            field5x1.setImageResource(R.drawable.cards_heart);
                            break;
                        case 6:
                            field6x1.setImageResource(R.drawable.cards_heart);
                            break;
                    }
                }

                if (attempt.size() == 1) {
                    switch (attemptLine) {
                        case 1:
                            field1x2.setImageResource(R.drawable.cards_heart);
                            break;
                        case 2:
                            field2x2.setImageResource(R.drawable.cards_heart);
                            break;
                        case 3:
                            field3x2.setImageResource(R.drawable.cards_heart);
                            break;
                        case 4:
                            field4x2.setImageResource(R.drawable.cards_heart);
                            break;
                        case 5:
                            field5x2.setImageResource(R.drawable.cards_heart);
                            break;
                        case 6:
                            field6x2.setImageResource(R.drawable.cards_heart);
                            break;
                    }
                }

                if (attempt.size() == 2) {
                    switch (attemptLine) {
                        case 1:
                            field1x3.setImageResource(R.drawable.cards_heart);
                            break;
                        case 2:
                            field2x3.setImageResource(R.drawable.cards_heart);
                            break;
                        case 3:
                            field3x3.setImageResource(R.drawable.cards_heart);
                            break;
                        case 4:
                            field4x3.setImageResource(R.drawable.cards_heart);
                            break;
                        case 5:
                            field5x3.setImageResource(R.drawable.cards_heart);
                            break;
                        case 6:
                            field6x3.setImageResource(R.drawable.cards_heart);
                            break;
                    }
                }

                if (attempt.size() == 3) {
                    switch (attemptLine) {
                        case 1:
                            field1x4.setImageResource(R.drawable.cards_heart);
                            break;
                        case 2:
                            field2x4.setImageResource(R.drawable.cards_heart);
                            break;
                        case 3:
                            field3x4.setImageResource(R.drawable.cards_heart);
                            break;
                        case 4:
                            field4x4.setImageResource(R.drawable.cards_heart);
                            break;
                        case 5:
                            field5x4.setImageResource(R.drawable.cards_heart);
                            break;
                        case 6:
                            field6x4.setImageResource(R.drawable.cards_heart);
                            break;
                    }
                }

                attempt.add("herc");
            }
        });

        iconKaro.setOnClickListener(view -> {
            if (attempt.size() < 4) {

                if (attempt.isEmpty()) {
                    switch (attemptLine) {
                        case 1:
                            field1x1.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 2:
                            field2x1.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 3:
                            field3x1.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 4:
                            field4x1.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 5:
                            field5x1.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 6:
                            field6x1.setImageResource(R.drawable.cards_diamond);
                            break;
                    }
                }

                if (attempt.size() == 1) {
                    switch (attemptLine) {
                        case 1:
                            field1x2.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 2:
                            field2x2.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 3:
                            field3x2.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 4:
                            field4x2.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 5:
                            field5x2.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 6:
                            field6x2.setImageResource(R.drawable.cards_diamond);
                            break;
                    }
                }

                if (attempt.size() == 2) {
                    switch (attemptLine) {
                        case 1:
                            field1x3.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 2:
                            field2x3.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 3:
                            field3x3.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 4:
                            field4x3.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 5:
                            field5x3.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 6:
                            field6x3.setImageResource(R.drawable.cards_diamond);
                            break;
                    }
                }

                if (attempt.size() == 3) {
                    switch (attemptLine) {
                        case 1:
                            field1x4.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 2:
                            field2x4.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 3:
                            field3x4.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 4:
                            field4x4.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 5:
                            field5x4.setImageResource(R.drawable.cards_diamond);
                            break;
                        case 6:
                            field6x4.setImageResource(R.drawable.cards_diamond);
                            break;
                    }
                }

                attempt.add("karo");
            }
        });

        iconZvezda.setOnClickListener(view -> {
            if (attempt.size() < 4) {

                if (attempt.isEmpty()) {
                    switch (attemptLine) {
                        case 1:
                            field1x1.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 2:
                            field2x1.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 3:
                            field3x1.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 4:
                            field4x1.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 5:
                            field5x1.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 6:
                            field6x1.setImageResource(R.drawable.baseline_star_24);
                            break;
                    }
                }

                if (attempt.size() == 1) {
                    switch (attemptLine) {
                        case 1:
                            field1x2.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 2:
                            field2x2.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 3:
                            field3x2.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 4:
                            field4x2.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 5:
                            field5x2.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 6:
                            field6x2.setImageResource(R.drawable.baseline_star_24);
                            break;
                    }
                }

                if (attempt.size() == 2) {
                    switch (attemptLine) {
                        case 1:
                            field1x3.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 2:
                            field2x3.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 3:
                            field3x3.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 4:
                            field4x3.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 5:
                            field5x3.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 6:
                            field6x3.setImageResource(R.drawable.baseline_star_24);
                            break;
                    }
                }

                if (attempt.size() == 3) {
                    switch (attemptLine) {
                        case 1:
                            field1x4.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 2:
                            field2x4.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 3:
                            field3x4.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 4:
                            field4x4.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 5:
                            field5x4.setImageResource(R.drawable.baseline_star_24);
                            break;
                        case 6:
                            field6x4.setImageResource(R.drawable.baseline_star_24);
                            break;
                    }
                }

                attempt.add("zvezda");
            }
        });

        submit.setOnClickListener(view -> {
            if (attempt.size() == 4) {
                ArrayList<String> solutionCloned = new ArrayList<>(solution);
                ArrayList<String> showColors = new ArrayList<>();

                //prvo izbacivanje crvenih
                for (int i = 0; i < 4; i++) {
                    if (attempt.get(i).equals(solution.get(i))) {
                        showColors.add("red");
                        solutionCloned.remove(attempt.get(i));
                    }
                }

                //pa sta ostane u zute
                for (int i = 0; i < 4; i++) {
                    if (!attempt.get(i).equals(solution.get(i)) && solutionCloned.contains(attempt.get(i))) {
                        showColors.add("yellow");
                        solutionCloned.remove(attempt.get(i));
                    }
                }

                showColors.sort(Comparator.naturalOrder()); //za ispis prvo crvenih pa zutih

                for (int i = 0; i < showColors.size(); i++) {

                    if (i == 0) {
                        switch (attemptLine) {
                            case 1:
                                if (showColors.get(i).equals("red"))
                                    hit1x1.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit1x1.setColorFilter(Color.YELLOW);
                                break;
                            case 2:
                                if (showColors.get(i).equals("red"))
                                    hit2x1.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit2x1.setColorFilter(Color.YELLOW);
                                break;
                            case 3:
                                if (showColors.get(i).equals("red"))
                                    hit3x1.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit3x1.setColorFilter(Color.YELLOW);
                                break;
                            case 4:
                                if (showColors.get(i).equals("red"))
                                    hit4x1.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit4x1.setColorFilter(Color.YELLOW);
                                break;
                            case 5:
                                if (showColors.get(i).equals("red"))
                                    hit5x1.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit5x1.setColorFilter(Color.YELLOW);
                                break;
                            case 6:
                                if (showColors.get(i).equals("red"))
                                    hit6x1.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit6x1.setColorFilter(Color.YELLOW);
                                break;
                        }
                    } else if (i == 1) {
                        switch (attemptLine) {
                            case 1:
                                if (showColors.get(i).equals("red"))
                                    hit1x2.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit1x2.setColorFilter(Color.YELLOW);
                                break;
                            case 2:
                                if (showColors.get(i).equals("red"))
                                    hit2x2.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit2x2.setColorFilter(Color.YELLOW);
                                break;
                            case 3:
                                if (showColors.get(i).equals("red"))
                                    hit3x2.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit3x2.setColorFilter(Color.YELLOW);
                                break;
                            case 4:
                                if (showColors.get(i).equals("red"))
                                    hit4x2.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit4x2.setColorFilter(Color.YELLOW);
                                break;
                            case 5:
                                if (showColors.get(i).equals("red"))
                                    hit5x2.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit5x2.setColorFilter(Color.YELLOW);
                                break;
                            case 6:
                                if (showColors.get(i).equals("red"))
                                    hit6x2.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit6x2.setColorFilter(Color.YELLOW);
                                break;
                        }
                    } else if (i == 2) {
                        switch (attemptLine) {
                            case 1:
                                if (showColors.get(i).equals("red"))
                                    hit1x3.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit1x3.setColorFilter(Color.YELLOW);
                                break;
                            case 2:
                                if (showColors.get(i).equals("red"))
                                    hit2x3.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit2x3.setColorFilter(Color.YELLOW);
                                break;
                            case 3:
                                if (showColors.get(i).equals("red"))
                                    hit3x3.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit3x3.setColorFilter(Color.YELLOW);
                                break;
                            case 4:
                                if (showColors.get(i).equals("red"))
                                    hit4x3.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit4x3.setColorFilter(Color.YELLOW);
                                break;
                            case 5:
                                if (showColors.get(i).equals("red"))
                                    hit5x3.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit5x3.setColorFilter(Color.YELLOW);
                                break;
                            case 6:
                                if (showColors.get(i).equals("red"))
                                    hit6x3.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit6x3.setColorFilter(Color.YELLOW);
                                break;
                        }
                    } else if (i == 3) {
                        switch (attemptLine) {
                            case 1:
                                if (showColors.get(i).equals("red"))
                                    hit1x4.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit1x4.setColorFilter(Color.YELLOW);
                                break;
                            case 2:
                                if (showColors.get(i).equals("red"))
                                    hit2x4.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit2x4.setColorFilter(Color.YELLOW);
                                break;
                            case 3:
                                if (showColors.get(i).equals("red"))
                                    hit3x4.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit3x4.setColorFilter(Color.YELLOW);
                                break;
                            case 4:
                                if (showColors.get(i).equals("red"))
                                    hit4x4.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit4x4.setColorFilter(Color.YELLOW);
                                break;
                            case 5:
                                if (showColors.get(i).equals("red"))
                                    hit5x4.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit5x4.setColorFilter(Color.YELLOW);
                                break;
                            case 6:
                                if (showColors.get(i).equals("red"))
                                    hit6x4.setColorFilter(Color.RED);
                                else if (showColors.get(i).equals("yellow"))
                                    hit6x4.setColorFilter(Color.YELLOW);
                                break;
                        }
                    }
                }

                int numOfCorrectGuesses = 0;
                for (int i = 0; i < 4; i++) {
                    if (Objects.equals(attempt.get(i), solution.get(i)))
                        numOfCorrectGuesses++;
                }

                if (numOfCorrectGuesses == 4) {
                    switch (attemptLine) {
                        case 1:
                        case 2:
                            playerScore += 20;
                            break;
                        case 3:
                        case 4:
                            playerScore += 15;
                            break;
                        case 5:
                        case 6:
                            playerScore += 10;
                            break;
                    }
                    gameTimer.onFinish();
                }

                attemptLine++;
                attempt.clear();
            }
        });

    }
}