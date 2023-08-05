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
import com.ftn.ac.rs.mobilne_2023.config.SocketHandler;


import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {

    public static Socket socket;

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
        if (bundle != null) {
            Toast.makeText(this,
                    "Total score after game: " + bundle.getString("unreg-score"),
                    Toast.LENGTH_LONG).show();
        }

        SocketHandler.setSocket();
        socket = SocketHandler.getSocket();
        socket.connect();
    }

    protected void showRankList() {
        Intent intent = new Intent(MainActivity.this, RankListActivity.class);
        startActivity(intent);
    }

    protected void showProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    protected void startGame() {
        socket.emit("joinGame", "Test");
        socket.on("startGame", args -> {
            Intent intent = new Intent(MainActivity.this, KoZnaZnaActivity.class);
            startActivity(intent);
        });
    }

    protected void showFriendList() {
        Intent intent = new Intent(MainActivity.this, FriendListActivity.class);
        startActivity(intent);
    }
}