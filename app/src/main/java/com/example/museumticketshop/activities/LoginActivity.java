package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final Long SECRET_KEY = 852195325434L;
    private static final String PREF_KEY = Objects.requireNonNull(LoginActivity.class.getPackage(),
                    "Error happened during loading package name for LoginActivity")
            .toString();
    private static final int RC_SIGN_IN = 999;
    private EditText emailAddressET;
    private EditText passwordET;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        emailAddressET = findViewById(R.id.loginEmailAddress);
        passwordET = findViewById(R.id.loginPassword);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions mSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, mSignInOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
            } catch (ApiException e) {
                Toast.makeText(this, "Authentication with Google unsuccessful!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                checkForAuthenticationAndRedirect(ProfileActivity.class);
            } else {
                Toast.makeText(LoginActivity.this,
                        "Authentication with Google unsuccessful!",
                        Toast.LENGTH_LONG).show();
            }
        });
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

        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkForAuthenticationAndRedirect(ProfileActivity.class);
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Authentication unsuccessful! Double check your details or " +
                                        "register an account if it does not exist yet!",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void loginWithGoogle(View view) {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    public void redirectToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("secretKey", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // not using secret key as "important" pages require authentication
        switch (menuItem.getItemId()) {
            case R.id.exhibitionsMenuItem:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.buyTicketsMenuItem:
                checkForAuthenticationAndRedirect(SelectTicketsActivity.class);
                break;
            case R.id.authenticationMenuItem:
                // do nothing as we're already here
                break;
            case R.id.profileMenuItem:
                checkForAuthenticationAndRedirect(ProfileActivity.class);
                break;
            case R.id.logoutMenuItem:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default: return super.onOptionsItemSelected(menuItem);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private <T extends AppCompatActivity>
    void checkForAuthenticationAndRedirect(Class<T> clazz) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "This feature requires authentication!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(new Intent(this, clazz));
    }
}