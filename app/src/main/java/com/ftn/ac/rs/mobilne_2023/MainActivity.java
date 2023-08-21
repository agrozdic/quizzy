package com.ftn.ac.rs.mobilne_2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ftn.ac.rs.mobilne_2023.activities.FriendListActivity;
import com.ftn.ac.rs.mobilne_2023.activities.KoZnaZnaActivity;
import com.ftn.ac.rs.mobilne_2023.activities.ProfileActivity;
import com.ftn.ac.rs.mobilne_2023.activities.RankListActivity;
import com.ftn.ac.rs.mobilne_2023.activities.SignInActivity;
import com.ftn.ac.rs.mobilne_2023.config.SocketHandler;


import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {

    public static Socket socket;

    private Bundle userBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Button btnShowRankList = findViewById(R.id.btnRankList);
        Button btnShowProfile = findViewById(R.id.btnProfile);
        Button btnStartGame = findViewById(R.id.btnStartGame);
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
        if (userBundle != null && userBundle.getString("user-username") != null) {
            socket.emit("joinGame", "Test");
            socket.on("startGame", args -> {
                Intent intent = new Intent(MainActivity.this, KoZnaZnaActivity.class);
                startActivity(intent);
            });
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