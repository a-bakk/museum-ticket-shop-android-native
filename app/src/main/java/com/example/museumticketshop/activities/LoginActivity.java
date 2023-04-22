package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.museumticketshop.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final Long SECRET_KEY = 852195325434L;
    private static final String PREF_KEY = Objects.requireNonNull(LoginActivity.class.getPackage(),
                    "Error happened during loading package name for LoginActivity")
            .toString();
    private EditText emailAddressET;
    private EditText passwordET;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        emailAddressET = findViewById(R.id.loginEmailAddress);
        passwordET = findViewById(R.id.loginPassword);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginEmailAddress", emailAddressET.getText().toString());
        editor.apply();
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