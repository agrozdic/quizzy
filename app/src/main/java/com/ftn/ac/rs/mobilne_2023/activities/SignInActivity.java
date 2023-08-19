package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.MainActivity;
import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.services.UserService;

public class SignInActivity extends AppCompatActivity {

    UserService userService = new UserService();

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
        EditText loginText = findViewById(R.id.editUsernameIn);
        String loginIdentifier = loginText.getText().toString();
        EditText passText = findViewById(R.id.editPassIn);
        String password = passText.getText().toString();

        boolean success = userService.loginUser(loginIdentifier, password);

        if (success)
            startActivity(intent);
        else
            Toast.makeText(this, "Wrong login data", Toast.LENGTH_SHORT).show();
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