package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.MainActivity;
import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.fragments.GameHeaderFragment;
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.Random;

public class MojBrojActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnResenje1;
    private Button btnResenje2;
    private Button btnResenje;
    private Button btnStop;
    private TextView txtResenje;
    private Button btnDel;
    private Button btnConfirm;
    private Button btnBr1;
    private Button btnBr2;
    private Button btnBr3;
    private Button btnBr4;
    private Button btnBr5;
    private Button btnBr6;
    private Button btnPlus;
    private Button btnMinus;
    private Button btnMultiply;
    private Button btnDivide;
    private Button btnOpenBracket;
    private Button btnClosedBracket;

    private CountDownTimer gameTimer;
    private int round;
    private TextView player1ScoreView;
    private int playerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_moj_broj);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), MojBrojActivity.this,
                false, R.id.headMojBroj);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            playerScore = Integer.parseInt(bundle.getString("unreg-score"));
            round = bundle.getInt("round", 1);
        }

        initializeView();

        Handler handler = new Handler();
        handler.postDelayed(() -> startGame(), 1000); // nasty hack
    }

    private void startGame() {
        player1ScoreView = findViewById(R.id.player1Score);
        player1ScoreView.setText(Integer.toString(playerScore));

        startTimer();
    }

    private void startTimer() {
        TextView time = findViewById(R.id.time);

        gameTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(Long.toString(millisUntilFinished / 1000));

                if (millisUntilFinished < 55000 && btnStop.isEnabled())
                    btnStopLogic();
            }

            public void onFinish() {
                Toast.makeText(MojBrojActivity.this, "End of round", Toast.LENGTH_LONG).show();
                if (round == 1) {
                    Intent intent = new Intent(MojBrojActivity.this, MojBrojActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("unreg-score", Integer.toString(playerScore));
                    bundle.putInt("round", ++round);
                    intent.putExtras(bundle);
                    gameTimer.cancel();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MojBrojActivity.this, MainActivity.class);
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

    //ovde staviti listener-e
    private void initializeView() {

        btnResenje1 = findViewById(R.id.btnResenje1);
        btnResenje2 = findViewById(R.id.btnResenje2);
        btnResenje = findViewById(R.id.btnResenjeMB);
        btnStop = findViewById(R.id.btnStopMB);
        txtResenje = findViewById(R.id.txtResenjeMB);
        btnDel = findViewById(R.id.btnDelete);
        btnConfirm = findViewById(R.id.btnConfirmMB);
        btnBr1 = findViewById(R.id.btnBr1);
        btnBr2 = findViewById(R.id.btnBr2);
        btnBr3 = findViewById(R.id.btnBr3);
        btnBr4 = findViewById(R.id.btnBr4);
        btnBr5 = findViewById(R.id.btnBr5);
        btnBr6 = findViewById(R.id.btnBr6);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        btnOpenBracket = findViewById(R.id.btnOpenBracket);
        btnClosedBracket = findViewById(R.id.btnClosedBracket);

        btnResenje1.setEnabled(false);
        btnResenje2.setEnabled(false);
        txtResenje.setEnabled(false);
        btnDel.setEnabled(false);
        btnConfirm.setEnabled(false);
        btnBr1.setEnabled(false);
        btnBr2.setEnabled(false);
        btnBr3.setEnabled(false);
        btnBr4.setEnabled(false);
        btnBr5.setEnabled(false);
        btnBr6.setEnabled(false);
        btnPlus.setEnabled(false);
        btnMinus.setEnabled(false);
        btnMultiply.setEnabled(false);
        btnDivide.setEnabled(false);
        btnOpenBracket.setEnabled(false);
        btnClosedBracket.setEnabled(false);

        btnStop.setOnClickListener(MojBrojActivity.this);
        btnConfirm.setOnClickListener(MojBrojActivity.this);
        btnDel.setOnClickListener(MojBrojActivity.this);
        btnPlus.setOnClickListener(MojBrojActivity.this);
        btnMinus.setOnClickListener(MojBrojActivity.this);
        btnMultiply.setOnClickListener(MojBrojActivity.this);
        btnDivide.setOnClickListener(MojBrojActivity.this);
        btnOpenBracket.setOnClickListener(MojBrojActivity.this);
        btnClosedBracket.setOnClickListener(MojBrojActivity.this);
        btnBr1.setOnClickListener(MojBrojActivity.this);
        btnBr2.setOnClickListener(MojBrojActivity.this);
        btnBr3.setOnClickListener(MojBrojActivity.this);
        btnBr4.setOnClickListener(MojBrojActivity.this);
        btnBr5.setOnClickListener(MojBrojActivity.this);
        btnBr6.setOnClickListener(MojBrojActivity.this);
    }

    //delete
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStopMB:
                btnStopLogic();
                break;
            case R.id.btnConfirmMB:
                btnConfirmLogic();
                break;
            case R.id.btnDelete:
                btnDeleteLogic();
                break;
            case R.id.btnPlus: case R.id.btnMinus: case R.id.btnMultiply: case R.id.btnDivide:
                btnOperationsLogic(v);
                break;
            case R.id.btnOpenBracket:
                btnOpenedBracketLogic();
                break;
            case R.id.btnClosedBracket:
                btnClosedBracketLogic();
                break;
            case R.id.btnBr1: case R.id.btnBr2: case R.id.btnBr3: case R.id.btnBr4: case R.id.btnBr5: case R.id.btnBr6:
                btnBrLogic(v);
                break;
            default:
                break;
        }
    }

    private void btnStopLogic() {
        btnStop.setEnabled(false);
        btnConfirm.setEnabled(true);

        int num = new Random().nextInt(1000);
        btnResenje.setText(String.valueOf(num));
        txtResenje.setEnabled(true);
        btnDel.setEnabled(true);

        num = new Random().nextInt(8) + 1;
        btnBr1.setText(String.valueOf(num));
        btnBr1.setEnabled(true);

        num = new Random().nextInt(8) + 1;
        btnBr2.setText(String.valueOf(num));
        btnBr2.setEnabled(true);

        num = new Random().nextInt(8) + 1;
        btnBr3.setText(String.valueOf(num));
        btnBr3.setEnabled(true);

        num = new Random().nextInt(8) + 1;
        btnBr4.setText(String.valueOf(num));
        btnBr4.setEnabled(true);

        int[] arr = new int[] {10, 15, 20};
        num = new Random().nextInt(3);
        int num2 = arr[num];
        btnBr5.setText(String.valueOf(num2));
        btnBr5.setEnabled(true);

        arr = new int[] {25, 50, 75, 100};
        num = new Random().nextInt(4);
        num2 = arr[num];
        btnBr6.setText(String.valueOf(num2));
        btnBr6.setEnabled(true);

        btnPlus.setEnabled(true);
        btnMinus.setEnabled(true);
        btnMultiply.setEnabled(true);
        btnDivide.setEnabled(true);
        btnOpenBracket.setEnabled(true);
        btnClosedBracket.setEnabled(true);
    }

    //redo sve osim evaluateMathExpression
    private void btnConfirmLogic() {
        btnResenje1.setEnabled(true);
        String resultString = txtResenje.getText().toString();
        int result = (int) evaluateMathExpression(resultString);
        btnResenje1.setText(String.valueOf(result));

        if (btnResenje1.getText().toString().equals(btnResenje.getText().toString()))
            playerScore += 20;
        else
            playerScore += 5;

        gameTimer.onFinish();
    }

    private void btnDeleteLogic(){
        String resultString = (String) txtResenje.getText();
        if(resultString.length() != 0) {
            resultString = resultString.substring(0, resultString.length() - 1);
            txtResenje.setText(resultString);
        }
    }

    public void btnOperationsLogic(View v){
        String resultString = (String) txtResenje.getText();
        if(resultString.length() != 0){
            String lastChar = resultString.substring(resultString.length() - 1);
            if( (!lastChar.equals("+")) && (!lastChar.equals("-")) && (!lastChar.equals("*")) && (!lastChar.equals("/")) && (!lastChar.equals("(")) ){
                Button clickedButton = findViewById(v.getId());
                resultString += clickedButton.getText().toString();
                txtResenje.setText(resultString);
            }
        }
    }

    public void btnOpenedBracketLogic(){
        String resultString = txtResenje.getText().toString();
        resultString += "(";
        txtResenje.setText(resultString);
    }

    public void btnClosedBracketLogic(){
        String resultString = txtResenje.getText().toString();
        resultString += ")";
        txtResenje.setText(resultString);
    }

    public void btnBrLogic(View v){

        String resultString = (String) txtResenje.getText();
        if(resultString.length() != 0){
            String lastChar = resultString.substring(resultString.length() - 1);
            if( ( (lastChar.equals("+")) || (lastChar.equals("-")) || (lastChar.equals("*")) || (lastChar.equals("/")) || (lastChar.equals("(")) ) && (!lastChar.equals(")")) ){
                Button clickedButton = findViewById(v.getId());
                resultString += clickedButton.getText().toString();
                clickedButton.setEnabled(false);
                txtResenje.setText(resultString);
            }
        }
        else{
            Button clickedButton = findViewById(v.getId());
            resultString += clickedButton.getText().toString();
            clickedButton.setEnabled(false);
            txtResenje.setText(resultString);
        }
    }

    public static double evaluateMathExpression(String expression) {
        try {
            Expression mathExpression = new ExpressionBuilder(expression).build();
            return mathExpression.evaluate();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 0;
        }
    }
}