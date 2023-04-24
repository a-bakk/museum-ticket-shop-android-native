package com.example.museumticketshop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.museumticketshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final String PREF_KEY = Objects.requireNonNull(RegisterActivity.class.getPackage(),
                    "Error happened during loading package name for RegisterActivity")
            .toString();
    private static final String VALUE_DOES_NOT_EXIST = "default value";
    private EditText nameET;
    private EditText emailAddressET;
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
        String name = extractStringFromEditText(nameET);
        String emailAddress = extractStringFromEditText(emailAddressET);
        String rawPassword = extractStringFromEditText(passwordET);
        String rawRePassword = extractStringFromEditText(rePasswordET);

        String[] required = new String[]{
                name, emailAddress, rawPassword, rawRePassword
        };
        if (checkForEmptyString(required)) {
            Toast.makeText(this, "One of the fields is empty!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidEmail(emailAddress)) {
            Toast.makeText(this, "Email address is invalid!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (!Objects.equals(rawPassword, rawRePassword)) {
            Toast.makeText(this, "Passwords do not match!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        registerUser(name, emailAddress, rawPassword);
    }

    public void redirectToLogin(View view) {
        finish();
    }

    private String extractStringFromEditText(EditText editText) {
        return editText == null ? "" : editText.getText().toString();
    }

    private void registerUser(String name, String email, String password) {
        // set the name with a UserProfileChangeRequest
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.updateProfile(profileChangeRequest)
                                    .addOnSuccessListener(unused ->
                                            startActivity(new Intent(RegisterActivity.this,
                                            ProfileActivity.class))).addOnFailureListener(unused ->
                                            Toast.makeText(RegisterActivity.this,
                                            "Problem with creating user",
                                            Toast.LENGTH_LONG).show());
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Problem with creating user", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Problem with creating user", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z\\d_+&*-]+(?:\\.[a-zA-Z\\d_+&*-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private boolean checkForEmptyString(String[] args) {
        for (String str : args) {
            if (str.isEmpty())
                return true;
        }
        return false;
    }
}