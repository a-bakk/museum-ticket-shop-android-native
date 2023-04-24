package com.example.museumticketshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.museumticketshop.activities.LoginActivity;
import com.example.museumticketshop.activities.ProfileActivity;
import com.example.museumticketshop.activities.SelectTicketsActivity;
import com.example.museumticketshop.adapters.ExhibitionAdapter;
import com.example.museumticketshop.entities.Exhibition;
import com.example.museumticketshop.repositories.ExhibitionDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private RecyclerView exhibitionRecyclerView;
    private List<Exhibition> exhibitionList;
    private ExhibitionAdapter exhibitionAdapter;
    private ExhibitionDao exhibitionDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exhibitionDao = ExhibitionDao.getInstance();

        exhibitionRecyclerView = findViewById(R.id.exhibitionRecyclerView);
        exhibitionRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        exhibitionList = new ArrayList<>();
        exhibitionAdapter = new ExhibitionAdapter(this, exhibitionList);
        exhibitionRecyclerView.setAdapter(exhibitionAdapter);

        getExhibitionsListWithImages();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getExhibitionsListWithImages() {
        exhibitionList.clear();

        Task<List<Exhibition>> task = exhibitionDao.getAllExhibitions();
        task.addOnSuccessListener(exhibitions -> {
            exhibitionList.addAll(exhibitions);

            int idx = 0;
            TypedArray imageResources = getResources().obtainTypedArray(R.array.exhibition_images);
            for (Exhibition res : exhibitionList) {
                res.setImageResource(imageResources.getResourceId(idx++, 0));
            }

            imageResources.recycle();
            exhibitionAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Exhibitions could not be loaded!");
        });
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
                checkForAuthenticationAndRedirect(SelectTicketsActivity.class);
                break;
            case R.id.authenticationMenuItem:
                startActivity(new Intent(this, LoginActivity.class));
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

    // can't make it static even if I pass the context as argument; going to have to be copied
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

    // from listing, make all of the buttons point to the same view
    public void redirectToBuyTickets(View view) {
        checkForAuthenticationAndRedirect(SelectTicketsActivity.class);
    }
}