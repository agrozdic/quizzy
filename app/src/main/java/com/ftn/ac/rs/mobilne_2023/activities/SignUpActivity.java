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

import java.util.regex.Pattern;

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

        boolean usernameValid = Pattern.matches("[._]*[A-Za-z]{3,}[\\w._]*", username);
        boolean emailValid = Pattern.matches("[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", email);
        boolean passValid = Pattern.matches("[\\w-!#$%]*[A-Za-z]{3,}[\\w-!#$%]*", pass);

        if (pass.equals(passRepeat) && passValid && pass.length() >= 6) {
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
        } else if (!usernameValid) {
            Toast.makeText(this,
                    "Username must contain 3 letters. Alphanumeric characters and dot are allowed",
                    Toast.LENGTH_SHORT).show();
        } else if (email.equals("")) {
            Toast.makeText(this, "Email can not be empty", Toast.LENGTH_SHORT).show();
        } else if (!emailValid) {
            Toast.makeText(this, "Email format not valid", Toast.LENGTH_SHORT).show();
        } else if (!passValid || pass.length() < 6) {
            Toast.makeText(this,
                    "Password must contain 3 letters and have minimum length of 6." +
                            "Alphanumeric characters and -, !, #, $, %, are allowed",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }
}