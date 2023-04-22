package com.example.museumticketshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.museumticketshop.activities.LoginActivity;
import com.example.museumticketshop.activities.SelectTicketsActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final Long SECRET_KEY = 8376985473L;
    private RecyclerView exhibitionRecyclerView;
//    private ArrayList<Exhibiton> exhibitionList;
//    private ExhibitionAdapter exhibitionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Objects.equals(getIntent().getLongExtra("secretKey", 0L),
                852195325434L) || // redirected from LoginActivity
            !Objects.equals(getIntent().getLongExtra("secretKey", 0),
                    89534634862L)) { // redirected from SelectTicketsActivity
            finish();
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
        switch (menuItem.getItemId()) {
            case R.id.exhibitionsMenuItem: // do nothing as we're already here
                break;
            case R.id.authenticationMenuItem:
                redirectToLogin();
                break;
            case R.id.buyTicketsMenuItem:
                redirectToTickets();
                break;
            case R.id.logoutMenuItem:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            default: return super.onOptionsItemSelected(menuItem);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("secretKey", SECRET_KEY);
        startActivity(intent);
    }

    private void redirectToTickets() {
        // TODO: check if user is authenticated
        Intent intent = new Intent(this, SelectTicketsActivity.class);
        intent.putExtra("secretKey", SECRET_KEY);
        startActivity(intent);
    }

}