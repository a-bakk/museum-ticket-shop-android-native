package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;
import com.example.museumticketshop.adapters.TicketAdapter;
import com.example.museumticketshop.entities.Ticket;
import com.example.museumticketshop.repositories.TicketDao;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getName();
    private static final Long SECRET_KEY = 88437216746L;
    private List<Ticket> tickets;
    private TicketAdapter ticketAdapter;
    private TicketDao ticketDao;
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
                // (ig we could but not for now)
                profileNameET.setText(R.string.google_authentication);
            } else {
                profileNameET.setText(user.getDisplayName());
            }
            emailAddressET.setText(user.getEmail());
        }

        ticketDao = TicketDao.getInstance();

        RecyclerView ticketRecyclerView = findViewById(R.id.ticketRecyclerView);
        ticketRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        tickets = new ArrayList<>();
        ticketAdapter = new TicketAdapter(this, tickets);
        ticketRecyclerView.setAdapter(ticketAdapter);

        if (user != null)
            readTicketsForUser(user.getEmail());

        findViewById(R.id.profileBaseMyTickets)
                .startAnimation(AnimationUtils.loadAnimation(this, R.anim.dropdown_5_times));
    }

    public void redirectToTicketModification(String ticketId) {
        Intent intent = new Intent(this, ModifyTicketActivity.class);
        intent.putExtra("secretKey", SECRET_KEY);
        intent.putExtra("currentTicketId", ticketId);
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteTicket(String ticketId) {
        deleteTicketTask(ticketId).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Ticket has successfully been deleted!",
                    Toast.LENGTH_LONG).show();
            ticketAdapter.notifyDataSetChanged();
            recreate();
        }
        ).addOnFailureListener(unused ->
                Toast.makeText(this,
                        "There was a problem while attempting to delete the ticket",
                        Toast.LENGTH_LONG).show()
        );
    }

    private Task<Void> deleteTicketTask(String id) {
        return TicketDao.getInstance().deleteTicket(id);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void readTicketsForUser(String email) {
        tickets.clear();

        Task<List<Ticket>> ticketTask = ticketDao.readTicketsByEmail(email);
        ticketTask.addOnSuccessListener(readTickets -> {
            tickets.addAll(readTickets);
            ticketAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e ->
                Log.e(TAG, "Tickets could not be loaded for user: " + email));
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