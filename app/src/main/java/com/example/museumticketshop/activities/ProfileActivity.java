package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView profileNameET = findViewById(R.id.profileNamePlaceholder);
        TextView emailAddressET = findViewById(R.id.profileEmailAddressPlaceholder);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null)
            finish();
        else {
            if (user.getProviderData().stream()
                    .anyMatch(i -> GoogleAuthProvider.PROVIDER_ID.equals(i.getProviderId()))) {
                // if user is authenticated via google, we do not know their name
                // (ig we could but whatever)
                profileNameET.setText(R.string.google_authentication);
            } else {
                profileNameET.setText(user.getDisplayName());
            }
            emailAddressET.setText(user.getEmail());
        }
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
                checkForAuthenticationAndRedirect();
                break;
            case R.id.authenticationMenuItem:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.profileMenuItem:
                // do nothing as we're already here
                break;
            case R.id.logoutMenuItem:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default: return super.onOptionsItemSelected(menuItem);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void checkForAuthenticationAndRedirect() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "This feature requires authentication!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(new Intent(this, SelectTicketsActivity.class));
    }
}