package com.ftn.ac.rs.mobilne_2023.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftn.ac.rs.mobilne_2023.R;
import com.ftn.ac.rs.mobilne_2023.model.User;
import com.ftn.ac.rs.mobilne_2023.services.UserService;

public class SignUpActivity extends AppCompatActivity {

    UserService userService = new UserService();

    EditText usernameText;
    EditText emailText;
    EditText passText;
    EditText passRepeatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(view -> register());

        usernameText = findViewById(R.id.editUsernameUp);
        emailText = findViewById(R.id.editEmailUp);
        passText = findViewById(R.id.editPassUp);
        passRepeatText = findViewById(R.id.editRepeatPassUp);
    }

    protected void register() {
        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String pass = passText.getText().toString();
        String passRepeat = passRepeatText.getText().toString();

        if (pass.equals(passRepeat)) {
            User user = new User(email, username, pass);
            boolean success = userService.registerUser(user);

            if (success) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT).show();
            }
        } else if (username.equals("")) {
            Toast.makeText(this, "Username can not be empty", Toast.LENGTH_SHORT).show();
        } else if (email.equals("")) {
            Toast.makeText(this, "Email can not be empty", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }
}