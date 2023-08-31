package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.MainActivity;
import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.KorakPoKorak;
import com.ftn.ac.rs.mobilne_2023.services.KorakPoKorakService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

public class KorakPoKorakActivity extends AppCompatActivity {

    private KorakPoKorak kpkModel;
    private Button korak1;
    private Button korak2;
    private Button korak3;
    private Button korak4;
    private Button korak5;
    private Button korak6;
    private Button korak7;
    private EditText resenje;
    private ImageButton submit;
    private CountDownTimer gameTimer;

    private int round;
    private TextView player1ScoreView;
    private int playerScore;

    private Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_korak_po_korak);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), KorakPoKorakActivity.this,
                false, R.id.headMojBroj);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            playerScore = bundle.getInt("unreg-score");
            round = bundle.getInt("round", 1);
        }

        initializeView();

        Handler handler = new Handler();
        handler.postDelayed(() -> startGame(), 1000); // nasty hack
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

    private void startGame() {
        KorakPoKorakService.get(round, result -> {
            Log.println(Log.INFO, "Recieved data: ", result.toString());
            kpkModel = new KorakPoKorak(result);

            player1ScoreView = findViewById(R.id.player1Score);
            player1ScoreView.setText(Integer.toString(playerScore));

            startTimer();
        });
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);

        gameTimer = new CountDownTimer(75000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                Toast.makeText(KorakPoKorakActivity.this, "End of round", Toast.LENGTH_SHORT).show();
                if (round == 1) {
                    Intent intent = new Intent(KorakPoKorakActivity.this, KorakPoKorakActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("unreg-score",playerScore);
                    bundle.putInt("round", ++round);
                    intent.putExtras(bundle);
                    gameTimer.cancel();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(KorakPoKorakActivity.this, MojBrojActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("unreg-score",playerScore);
                    bundle.remove("round");
                    intent.putExtras(bundle);
                    gameTimer.cancel();
                    startActivity(intent);
                }
            }
        }.start();
    }

    private void initializeView() {

        korak1 = findViewById(R.id.step1);
        korak2 = findViewById(R.id.step2);
        korak3 = findViewById(R.id.step3);
        korak4 = findViewById(R.id.step4);
        korak5 = findViewById(R.id.step5);
        korak6 = findViewById(R.id.step6);
        korak7 = findViewById(R.id.step7);
        resenje = findViewById(R.id.editAnswerKPK);
        submit = findViewById(R.id.submitKPK);

        buttons = new Button[] {korak1, korak2, korak3, korak4, korak5, korak6, korak7};

        gameLogic();

    }

    private void gameLogic() {

        int addTime = -15000;

        for(Button button : buttons){
            addTime += 10000;
            button.setEnabled(true);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                switch (button.getId()){
                    case R.id.step1:
                        button.setText(kpkModel.getKorak1());
                        break;
                    case R.id.step2:
                        button.setText(kpkModel.getKorak2());
                        break;
                    case R.id.step3:
                        button.setText(kpkModel.getKorak3());
                        break;
                    case R.id.step4:
                        button.setText(kpkModel.getKorak4());
                        break;
                    case R.id.step5:
                        button.setText(kpkModel.getKorak5());
                        break;
                    case R.id.step6:
                        button.setText(kpkModel.getKorak6());
                        break;
                    case R.id.step7:
                        button.setText(kpkModel.getKorak7());
                        break;
                    default:
                        break;
                }
            }, 10000 + addTime);
        }

        submit.setOnClickListener(view -> {
            btnSubmitLogic();
        });

    }

    private void btnSubmitLogic() {

        if(String.valueOf(resenje.getText()).equals(kpkModel.getResenje())) {
            playerScore += 8;

            if (korak2.getText().toString().contains("Step"))
                playerScore += 2;

            if (korak3.getText().toString().contains("Step"))
                playerScore += 2;

            if (korak4.getText().toString().contains("Step"))
                playerScore += 2;

            if (korak5.getText().toString().contains("Step"))
                playerScore += 2;

            if (korak6.getText().toString().contains("Step"))
                playerScore += 2;

            if (korak7.getText().toString().contains("Step"))
                playerScore += 2;

            korak1.setText(kpkModel.getKorak1());
            korak2.setText(kpkModel.getKorak2());
            korak3.setText(kpkModel.getKorak3());
            korak4.setText(kpkModel.getKorak4());
            korak5.setText(kpkModel.getKorak5());
            korak6.setText(kpkModel.getKorak6());
            korak7.setText(kpkModel.getKorak7());

            gameTimer.onFinish();
        }
        else {
            Toast.makeText(this, "Wrong guess", Toast.LENGTH_SHORT).show();
        }
    }
}