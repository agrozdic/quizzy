package com.ftn.ac.rs.mobilne_2023.activities;

import androidx.appcompat.app.AppCompatActivity;

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

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.model.Asocijacije;
import com.ftn.ac.rs.mobilne_2023.model.KorakPoKorak;
import com.ftn.ac.rs.mobilne_2023.services.AsocijacijeService;
import com.ftn.ac.rs.mobilne_2023.services.KorakPoKorakService;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

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

//        FloatingActionButton fab = findViewById(R.id.fab5);
//        fab.setOnClickListener(view -> fab5());

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            playerScore = Integer.parseInt(bundle.getString("unreg-score"));
//            round = bundle.getInt("round", 1);
//        }

        startGame();

    }

    private void startGame() {
        KorakPoKorakService.get(1, result -> {
            Log.println(Log.INFO, "Recieved data: ", result.toString());
            kpkModel = new KorakPoKorak(result);
            initializeView();

//            player1ScoreView = findViewById(R.id.player1Score);
//            player1ScoreView.setText(Integer.toString(playerScore));
//
//            startTimer();
            initializeView();
        });
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);

        gameTimer = new CountDownTimer(70, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                Toast.makeText(KorakPoKorakActivity.this, "End of round", Toast.LENGTH_LONG).show();
                if (round == 1) {
                    Intent intent = new Intent(KorakPoKorakActivity.this, MojBrojActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("unreg-score", Integer.toString(playerScore));
                    bundle.putInt("round", ++round);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(KorakPoKorakActivity.this, MojBrojActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("unreg-score", Integer.toString(playerScore));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }.start();
    }

    private void initializeView() {

        korak1 = (Button) findViewById(R.id.step1);
        korak2 = (Button) findViewById(R.id.step2);
        korak3 = (Button) findViewById(R.id.step3);
        korak4 = (Button) findViewById(R.id.step4);
        korak5 = (Button) findViewById(R.id.step5);
        korak6 = (Button) findViewById(R.id.step6);
        korak7 = (Button) findViewById(R.id.step7);
        resenje = (EditText) findViewById(R.id.editAnswerKPK);
        submit = (ImageButton) findViewById(R.id.submitKPK);

        korak1.setEnabled(false);
        korak2.setEnabled(false);
        korak3.setEnabled(false);
        korak4.setEnabled(false);
        korak5.setEnabled(false);
        korak6.setEnabled(false);
        korak7.setEnabled(false);
        resenje.setEnabled(true);
        submit.setEnabled(true);

        buttons = new Button[] {korak1, korak2, korak3, korak4, korak5, korak6, korak7};

        gameLogic();

    }

    private void gameLogic() {

        int addTime = -2000;

        for(Button button : buttons){
            addTime += 2000;
            button.setEnabled(true);

            Toast.makeText(this, "hehehes", Toast.LENGTH_SHORT).show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
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
                }
            }, 2000 + addTime);
        }

        submit.setOnClickListener(view -> {
            btnSubmitLogic();
        });

    }

    private void btnSubmitLogic() {

//        String res1 = resenje.getText();
//        String res2 = kpkModel.getResenje();
        if(String.valueOf(resenje.getText()).equals(kpkModel.getResenje())) {
            korak1.setEnabled(true);
            korak1.setText(kpkModel.getKorak1());
            korak2.setEnabled(true);
            korak2.setText(kpkModel.getKorak2());
            korak3.setEnabled(true);
            korak3.setText(kpkModel.getKorak3());
            korak4.setEnabled(true);
            korak4.setText(kpkModel.getKorak4());
            korak5.setEnabled(true);
            korak5.setText(kpkModel.getKorak5());
            korak6.setEnabled(true);
            korak6.setText(kpkModel.getKorak6());
            korak7.setEnabled(true);
            korak7.setText(kpkModel.getKorak7());
        }
        else {
            Toast.makeText(this, resenje.getText() + " - " + kpkModel.getResenje(), Toast.LENGTH_SHORT).show();
        }

    }


//    protected void fab5() {
//        Intent intent = new Intent(KorakPoKorakActivity.this, MojBrojActivity.class);
//        startActivity(intent);
//    }
}