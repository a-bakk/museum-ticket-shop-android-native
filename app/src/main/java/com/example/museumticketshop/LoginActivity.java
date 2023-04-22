package com.example.museumticketshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private static final Long SECRET_KEY = 852195325434L;

    private EditText emailAddressET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddressET = findViewById(R.id.loginEmailAddress);
        passwordET = findViewById(R.id.loginPassword);
    }

    public void login(View view) {
        String emailAddress = emailAddressET.getText().toString();
        String password = passwordET.getText().toString();
    }

    public void redirectToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("secretKey", SECRET_KEY);
        startActivity(intent);
    }
}