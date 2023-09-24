package com.example.wastelocator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.EditText;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.wastelocator.Utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setViewIds();

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Login authentication
            if (isValidCredentials(email, password)) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                SharedPrefManager.setLoginState(true);
                if (email.equals("admin@gmail.com")){
                    SharedPrefManager.setAdmin(true);
                } else {
                    SharedPrefManager.setAdmin(false);
                }
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                emailEditText.setError("Invalid email");
                passwordEditText.setError("Invalid password");            }
        });
    }

    private void setViewIds() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
    }

    private boolean isValidCredentials(String email, String password) {
        // Perform validation logic here
        // Return true if credentials are valid, false otherwise
        return (email.equals("admin@gmail.com") && password.equals("admin123")) || (email.equals("user@gmail.com") && password.equals("user123"));
    }
}