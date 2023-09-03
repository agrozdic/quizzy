package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.config.SocketHandler;

import io.socket.client.Socket;

public class ProfileActivity extends AppCompatActivity {

    public static Socket socket = SocketHandler.getSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();

        TextView usernameView = findViewById(R.id.usernameProfile);
        usernameView.setText(bundle.getString("user-username"));
        TextView emailView = findViewById(R.id.emailProfile);
        emailView.setText(bundle.getString("user-email"));

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(view -> logout());
    }

    protected void logout() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        socket.disconnect();

        finish();
    }
}