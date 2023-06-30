package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.MainActivity;
import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.services.UserService;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);

        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        Button btnContGuest = findViewById(R.id.btnContGuest);

        btnSignIn.setOnClickListener(view -> login());
        btnCreateAccount.setOnClickListener(view -> createAccount());
        btnContGuest.setOnClickListener(view -> contGuest());
    }

    protected void login() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        //UserService.loginUser("test", "test");
        startActivity(intent);
    }

    protected void createAccount() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    protected void contGuest() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }
}