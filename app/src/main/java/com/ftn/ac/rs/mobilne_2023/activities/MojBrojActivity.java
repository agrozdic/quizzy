package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.ftn.ac.rs.mobilne_2023.tools.FragmentTransition;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.socket.client.Socket;

public class MojBrojActivity extends AppCompatActivity implements View.OnClickListener , SensorEventListener {


    public static Socket socket = SocketHandler.getSocket();

    boolean twoPlayer;

    int switcher;

    private Bundle gameBundle;
    private Button btnResenje1;
    private Button btnResenje2;
    private Button btnResenje;
    private Button btnStop;
    private TextView txtResenje;
    private ArrayList<Button> arrResenje = new ArrayList<Button>();
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

    private HashMap<String, Integer> results = new HashMap<String, Integer>();

    private int globalBr = -1;

    private CountDownTimer gameTimer;
    private int round;
    TextView player1ScoreView;
    TextView player2ScoreView;
    TextView player1NameView;
    TextView player2NameView;
    int playerScore;
    int player2Score;
    String playerName;
    String player2Name;

    // senzori

    SensorManager sensorManager;
    private int globalResult;


    // senzori

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_moj_broj);

        FragmentTransition.to(GameHeaderFragment.newInstance("param1", "param2"), MojBrojActivity.this,
                false, R.id.headMojBroj);

        Bundle bundle = getIntent().getExtras();
        gameBundle = bundle;
        if (bundle != null && bundle.getString("unreg-score") != null) {
            playerScore = bundle.getInt("unreg-score");
            round = bundle.getInt("round", 1);
        } else if (bundle != null) {
            playerName = bundle.getString("user-username");
            player2Name = bundle.getString("opponent-username");
            playerScore = bundle.getInt("user-score");
            player2Score = bundle.getInt("opponent-score");
        } else {
            System.out.println("NO BUNDLE");
        }

        //round = bundle.getInt("round", 1);


        twoPlayer = (gameBundle != null && gameBundle.getString("user-username") != null);

        initializeView();

        startGame(playerName, playerScore, player2Name, player2Score);

        // senzori

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

//        tvAccelerometer = (TextView) findViewById(R.id.tvAccelerometar);
//        tvLinearAccelerometer = (TextView) findViewById(R.id.tvLinearAccelerometer);
//        tvMagneticField = (TextView) findViewById(R.id.tvMagneticField);
//        tvProximitySensor = (TextView) findViewById(R.id.tvProximitySensor);
//        tvGyroscope = (TextView) findViewById(R.id.tvGyroscope);

        // senzori

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


        if (player2Name != null)
            blockInput();

        startTimer();
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

    private void startTimer() {
        TextView time = findViewById(R.id.time);
        final int[] end = new int[1];

        gameTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(Long.toString(millisUntilFinished / 1000));

                if (millisUntilFinished < 55000 && btnStop.isEnabled())
                    btnStopLogic();
            }

            public void onFinish() {
                Toast.makeText(MojBrojActivity.this, "End of round", Toast.LENGTH_SHORT).show();
                if (round == 1) {
                    Intent intent = new Intent(MojBrojActivity.this, MojBrojActivity.class);
                    Bundle bundle = new Bundle();
                    if (gameBundle == null || gameBundle.getString("user-username") == null) {
                        bundle.putInt("unreg-score",playerScore);
                        Log.println(Log.INFO, "set-unreg", String.valueOf(playerScore));
                        bundle.remove("round");
                    } else if (gameBundle != null && gameBundle.getString("user-username") != null) {
                        bundle.putString("user-username", playerName);
                        bundle.putString("opponent-username", player2Name);
                        bundle.putInt("user-score", playerScore);
                        bundle.putInt("opponent-score", player2Score);
                    }
                    intent.putExtras(bundle);
                    gameTimer.cancel();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MojBrojActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    if (gameBundle == null || gameBundle.getString("user-username") == null) {
                        bundle.putInt("unreg-score",playerScore);
                        Log.println(Log.INFO, "set-unreg", String.valueOf(playerScore));
                        bundle.remove("round");
                    } else if (gameBundle != null && gameBundle.getString("user-username") != null) {
                        bundle.putString("user-username", playerName);
                        bundle.putString("opponent-username", player2Name);
                        bundle.putInt("user-score", playerScore);
                        bundle.putInt("opponent-score", player2Score);
                    }
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
                globalBr++;
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
        //System.out.println("globalbr === " + globalBr);
        if(globalBr == 6) {
            btnStop.setEnabled(false);
            btnConfirm.setEnabled(true);
            txtResenje.setEnabled(true);
            btnDel.setEnabled(true);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        int num;

        if(globalBr == 0) {
            num = new Random().nextInt(1000);
            globalResult = num;
            socket.emit("mbResenje", num);
            socket.on("putMbResenje", args -> {
                String num1 = (String) args[1];
                btnResenje.setText(num1);
                //btnResenje.setText(String.valueOf(num));
            });

        }

        if(globalBr == 1) {
            num = new Random().nextInt(8) + 1;
            socket.emit("mb1", num);
            socket.on("putMb1", args -> {
                String num1 = (String) args[1];
                btnBr1.setText(num1);
            });
            //btnBr1.setText(String.valueOf(num));
            //btnBr1.setEnabled(true);
            //System.out.println("globalbr SHOULD BE 1 === " + globalBr);
        }

        if(globalBr == 2) {
            num = new Random().nextInt(8) + 1;
            socket.emit("mb2", num);
            socket.on("putMb2", args -> {
                String num1 = (String) args[1];
                btnBr2.setText(num1);
            });
            //btnBr2.setText(String.valueOf(num));
            //btnBr2.setEnabled(true);
        }

        if(globalBr == 3) {
            num = new Random().nextInt(8) + 1;
            socket.emit("mb3", num);
            socket.on("putMb3", args -> {
                String num1 = (String) args[1];
                btnBr3.setText(num1);
            });
            //btnBr3.setText(String.valueOf(num));
            //btnBr3.setEnabled(true);
        }

        if(globalBr == 4) {
            num = new Random().nextInt(8) + 1;
            socket.emit("mb4", num);
            socket.on("putMb4", args -> {
                String num1 = (String) args[1];
                btnBr4.setText(num1);
            });
            //btnBr4.setText(String.valueOf(num));
            //btnBr4.setEnabled(true);
        }

        int[] arr = new int[] {10, 15, 20};
        int num2;

        if(globalBr == 5) {
            num = new Random().nextInt(3);
            num2 = arr[num];
            socket.emit("mb5", num2);
            socket.on("putMb5", args -> {
                String num1 = (String) args[1];
                btnBr5.setText(num1);
            });
            //btnBr5.setText(String.valueOf(num2));
            //btnBr5.setEnabled(true);
        }

        if(globalBr == 6) {
            arr = new int[]{25, 50, 75, 100};
            num = new Random().nextInt(4);
            num2 = arr[num];
            socket.emit("mb6", num2);
            socket.on("putMb6", args -> {
                String num1 = (String) args[1];
                btnBr6.setText(num1);
            });
            //btnBr6.setText(String.valueOf(num2));
            //btnBr6.setEnabled(true);


            btnPlus.setEnabled(true);
            btnMinus.setEnabled(true);
            btnMultiply.setEnabled(true);
            btnDivide.setEnabled(true);
            btnOpenBracket.setEnabled(true);
            btnClosedBracket.setEnabled(true);
            btnBr1.setEnabled(true);
            btnBr2.setEnabled(true);
            btnBr3.setEnabled(true);
            btnBr4.setEnabled(true);
            btnBr5.setEnabled(true);
            btnBr6.setEnabled(true);



        }
    }

    //redo sve osim evaluateMathExpression
    private void btnConfirmLogic() {
        btnConfirm.setEnabled(false);
        String resultString = txtResenje.getText().toString();
        int result = (int) evaluateMathExpression(resultString);
        if(!twoPlayer) {
            btnResenje1.setText(String.valueOf(result));

            if (btnResenje1.getText().toString().equals(btnResenje.getText().toString()))
                playerScore += 20;
            else
                playerScore += 5;
        }
        else{
            twoPlayerLogic(resultString);
        }

        gameTimer.onFinish();
    }

    private void btnDeleteLogic(){
        String resultString = (String) txtResenje.getText();
        if(resultString.length() != 0) {
            String lastEl = resultString.substring(resultString.length() - 1);
            System.out.println("lastel - " + lastEl);
            if("0123456789".indexOf(lastEl) != -1) {
                Button btn = (Button) arrResenje.get(arrResenje.size() - 1);
                resultString = resultString.substring(0, resultString.length() - btn.getText().length());
                btn.setEnabled(true);
                txtResenje.setText(resultString);
                System.out.println(btn.getText());
                arrResenje.remove(btn);
            }
            else{
                resultString = resultString.substring(0, resultString.length() - 1);
                txtResenje.setText(resultString);
            }
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
                arrResenje.add(clickedButton);
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

    public void twoPlayerLogic(String result){

        socket.emit("mbConfirm", result, gameBundle.getString("user-username"));
        socket.on("mbResult", args  -> {
            results.put(args[1].toString(), (Integer) args[0]);
        });

        if((results.get(playerName) != null) && (results.get(player2Name) != null)){
            int result1 = results.get(playerName);
            int result2 = results.get(player2Name);
            btnResenje1.setText(String.valueOf(result1));
            btnResenje2.setText(String.valueOf(result1));
            if(Math.abs(result1 - globalResult) < Math.abs(result2 - globalResult)){
                playerScore += 20;
            }
            if((result1 == globalResult) && (result2 != globalResult)){
                playerScore += 20;
            } else if ((result2 == globalResult) && (result1 != globalResult)) {
                player2Score += 20;
            } else if ((result1 == globalResult) && (result2 == globalResult)) {
                // cija je igra += 20;
            } else if(Math.abs(result1 - globalResult) < Math.abs(result2 - globalResult)){
                playerScore += 20;
            } else if(Math.abs(result1 - globalResult) > Math.abs(result2 - globalResult)){
                player2Score += 20;
            }
        }

        socket.emit("mbScoreUpdate", playerName, playerScore, player2Name, player2Score);
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



    };





    // senzori


    /*
     * Bitno je da zakacimo/otkacimo listener na pravom mestu. Kada zelmo da koristimo
     * odredjeni senzor, najbolje mesto da se listener zakaci, da bi dobijali merenja,
     * jeste metoda onResume.
     * */
    @Override
    protected void onResume() {
        super.onResume();

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor s : sensors){
            Log.i("REZ_TYPE_ALL", s.getName());
        }

        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);

        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // registering our sensor with sensor manager
            sensorManager.registerListener(this,
                    proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * Najbolje mesto da otkacimo listener jeste onPause. Voditi racuna
     * da se svi listener-i koji rade sa senzorima otkace, kada
     * vise ne zelimo da ih koristimo.
     * */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    private static final int SHAKE_THRESHOLD = 1500;
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = sensorEvent.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("REZ", "shake detected w/ speed: " + speed);
                    //tvAccelerometer.setText("Accelerometer: shaking \n [" + x + ", " + y + ", " + z + "]");
                    //Toast.makeText(this, "SHAKING", Toast.LENGTH_SHORT).show();
                    globalBr++;
                    onPause();
                    if(globalBr < 7){
                        System.out.println("globalbr === " + globalBr);
                        btnStopLogic();
                        try {
                            TimeUnit.SECONDS.sleep(1);
                            onResume();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    else {
                        onPause();
                    }
                }else{
                    //tvAccelerometer.setText("Accelerometer: not shaking \n [" + x + ", " + y + ", " + z + "]");
                    //Toast.makeText(this, "NOT SHAKING", Toast.LENGTH_SHORT).show();

                }
                last_x = x;
                last_y = y;
                last_z = z;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            Log.i("REZ_ACCELEROMETER", String.valueOf(accuracy));
//        }else if(sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
//            Log.i("REZ_LINEAR_ACCELERATION", String.valueOf(accuracy));
//        }else if(sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
//            Log.i("REZ_MAGNETIC_FIELD", String.valueOf(accuracy));
//        }else if(sensor.getType() == Sensor.TYPE_GYROSCOPE){
//            Log.i("REZ_GYROSCOPE", String.valueOf(accuracy));
//        }else if(sensor.getType() == Sensor.TYPE_PROXIMITY){
//            Log.i("REZ_TYPE_PROXIMITY", String.valueOf(accuracy));
//        }else{
//            Log.i("REZ_OTHER_SENSOR", String.valueOf(accuracy));
//        }

    }
}