package com.example.museumticketshop.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.museumticketshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private static final String PREF_KEY = Objects.requireNonNull(RegisterActivity.class.getPackage(),
                    "Error happened during loading package name for RegisterActivity")
            .toString();
    private static final String VALUE_DOES_NOT_EXIST = "default value";
    private EditText nameET;
    private EditText emailAddressET;
    private EditText addressET;
    private EditText passwordET;
    private EditText rePasswordET;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (!Objects.equals(getIntent().getLongExtra("secretKey", 0L),
                852195325434L)) {
            finish();
        }

        nameET = findViewById(R.id.registerName);
        emailAddressET = findViewById(R.id.registerEmailAddress);
        addressET = findViewById(R.id.registerAddress);
        passwordET = findViewById(R.id.registerPassword);
        rePasswordET = findViewById(R.id.registerRePassword);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        if (!Objects.equals(
                sharedPreferences.getString("loginEmailAddress", VALUE_DOES_NOT_EXIST),
                VALUE_DOES_NOT_EXIST)) {
            emailAddressET.setText
                    (sharedPreferences.getString("loginEmailAddress", VALUE_DOES_NOT_EXIST));
        }

        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view) {
        String firstName = extractStringFromEditText(nameET);
        String emailAddress = extractStringFromEditText(emailAddressET);
        String streetAndNumber = extractStringFromEditText(addressET);
        String rawPassword = extractStringFromEditText(passwordET);
        String rawRePassword = extractStringFromEditText(rePasswordET);

        // TODO: get it out of here
        mAuth.createUserWithEmailAndPassword(emailAddress, rawPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // TODO: redirect to buy tickets
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Problem with creating user", Toast.LENGTH_LONG).show();
                            // task.getException().getMessage();
                        }
                    }
                });

    }

    public void redirectToLogin(View view) {
        finish();
    }

    private String extractStringFromEditText(EditText editText) {
        return editText == null ? "" : editText.getText().toString();
    }

//    private AuthResult registerNewUser();
}