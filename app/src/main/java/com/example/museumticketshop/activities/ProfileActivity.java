package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
                startActivity(new Intent(this, SelectTicketsActivity.class));
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
}