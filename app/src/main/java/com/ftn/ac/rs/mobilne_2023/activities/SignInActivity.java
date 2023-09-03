package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.MainActivity;
import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.model.User;
import com.ftn.ac.rs.mobilne_2023.services.UserService;
import com.ftn.ac.rs.mobilne_2023.tools.NetworkUtils;

import java.time.LocalDate;

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
        if (!NetworkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, "You are not connected to the internet", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        EditText loginText = findViewById(R.id.editUsernameIn);
        String loginIdentifier = loginText.getText().toString();
        EditText passText = findViewById(R.id.editPassIn);
        String password = passText.getText().toString();

        User user = userService.loginUser(loginIdentifier, password);

        LocalDate tokensLastReceived;
        if (user.getTokensLastReceived() == null)
            tokensLastReceived = null;
        else
            tokensLastReceived = LocalDate.parse(user.getTokensLastReceived());

        if (tokensLastReceived == null || tokensLastReceived.isBefore(LocalDate.now())) {
            UserService.updateUserTokens(user.getId(), user.getTokens() + 5, true);
            user.setTokens(user.getTokens() + 5);
        }

        if (user != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("user-id", user.getId());
            bundle.putString("user-username", user.getUsername());
            bundle.putString("user-email", user.getEmail());
            bundle.putInt("user-tokens", user.getTokens());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Wrong login data", Toast.LENGTH_SHORT).show();
    }

    protected void createAccount() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            Toast.makeText(this, "You are not connected to the internet", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    protected void contGuest() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }
}