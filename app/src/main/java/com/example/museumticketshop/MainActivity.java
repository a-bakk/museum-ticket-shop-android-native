package com.example.museumticketshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.museumticketshop.activities.LoginActivity;
import com.example.museumticketshop.activities.ProfileActivity;
import com.example.museumticketshop.activities.SelectTicketsActivity;
import com.example.museumticketshop.entities.Exhibition;
import com.example.museumticketshop.repositories.ExhibitionDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private RecyclerView exhibitionRecyclerView;
//    private ArrayList<Exhibition> exhibitionList;
//    private ExhibitionAdapter exhibitionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                // do nothing as we're already here
                break;
            case R.id.buyTicketsMenuItem:
                startActivity(new Intent(this, SelectTicketsActivity.class));
                break;
            case R.id.authenticationMenuItem:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.profileMenuItem:
                startActivity(new Intent(this, ProfileActivity.class));
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