package com.ftn.ac.rs.mobilne_2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.ac.rs.mobilne_2023.activities.FriendListActivity;
import com.ftn.ac.rs.mobilne_2023.activities.KoZnaZnaActivity;
import com.ftn.ac.rs.mobilne_2023.activities.ProfileActivity;
import com.ftn.ac.rs.mobilne_2023.activities.RankListActivity;
import com.ftn.ac.rs.mobilne_2023.activities.SignInActivity;
import com.ftn.ac.rs.mobilne_2023.config.SocketHandler;
import com.ftn.ac.rs.mobilne_2023.services.UserService;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Iterator;

import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {

    public static Socket socket;

    private UserService userService = new UserService();

    private Bundle userBundle;

    private Button btnStartGame;

    private TextView tokensView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Button btnShowRankList = findViewById(R.id.btnRankList);
        Button btnShowProfile = findViewById(R.id.btnProfile);
        btnStartGame = findViewById(R.id.btnStartGame);
        LinearLayout showFriendList = findViewById(R.id.showFriendList);

        btnShowRankList.setOnClickListener(view -> showRankList());
        btnShowProfile.setOnClickListener(view -> showProfile());
        btnStartGame.setOnClickListener(view -> startGame());
        showFriendList.setOnClickListener(view -> showFriendList());

        Bundle bundle = getIntent().getExtras();
        userBundle = bundle;

        if (bundle != null && bundle.getString("unreg-score") != null) {
            Toast.makeText(this,
                    "Total score after game: " + bundle.getString("unreg-score"),
                    Toast.LENGTH_LONG).show();
        }

        if (bundle != null && bundle.getString("user-username") != null) {
            SocketHandler.setSocket();
            socket = SocketHandler.getSocket();
            socket.connect();

            tokensView = findViewById(R.id.tokensTextView);
            int tokens = bundle.getInt("user-tokens");
            tokensView.setText(tokens + " Ⓣ");
        }
    }

    @Override
    public void onBackPressed() {
        if (userBundle != null)
            moveTaskToBack(true); //back na MainActivity minimizuje aplikaciju
        else {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);

            finish();
        }
    }

    protected void showRankList() {
        if (userBundle == null) {
            Toast.makeText(this, "Register to access", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this, RankListActivity.class);
            startActivity(intent);
        }
    }

    protected void showProfile() {
        if (userBundle == null) {
            Toast.makeText(this, "Register to access", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtras(userBundle);
            startActivity(intent);
        }
    }

    protected void startGame() {
        btnStartGame.setEnabled(false);
        if (userBundle != null && userBundle.getString("user-username") != null &&
                userBundle.getInt("user-tokens") > 0) {
            int newTokens = userBundle.getInt("user-tokens") - 1;
            tokensView.setText(newTokens + " Ⓣ");

            UserService.updateUserTokens(userBundle.getInt("user-id"), newTokens, false);

            socket.emit("joinGame", userBundle.getString("user-username"));
            socket.on("startGame", args -> {
                if (args.length > 0 && args[0] instanceof JSONObject) {
                    JSONObject data = (JSONObject) args[0];
                    Intent intent = new Intent(MainActivity.this, KoZnaZnaActivity.class);

                    for (Iterator<String> it = data.keys(); it.hasNext(); ) {
                        String socket = it.next();
                        try {
                            if (!data.get(socket).toString().equals(userBundle.getString("user-username")))
                                intent.putExtra("opponent-username", data.get(socket).toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    intent.putExtra("user-username", userBundle.getString("user-username"));
                    startActivity(intent);
                }
            });
        } else if (userBundle != null && userBundle.getString("user-username") != null) {
            Toast.makeText(this, "Not enough tokens to start a game", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this, KoZnaZnaActivity.class);
            startActivity(intent);
        }
    }

    protected void showFriendList() {
        if (userBundle == null) {
            Toast.makeText(this, "Register to access", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
            startActivity(intent);
        }
    }
}