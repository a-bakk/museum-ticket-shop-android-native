package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;
import com.example.museumticketshop.adapters.ExhibitionArrayAdapter;
import com.example.museumticketshop.entities.Exhibition;
import com.example.museumticketshop.repositories.ExhibitionDao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SelectTicketsActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {
    private Spinner numberOfFullPriceTicketsSpinner;
    private Spinner numberOfHalfPriceTicketsSpinner;
    private Spinner chooseExhibitionSpinner;
    private EditText ticketDateET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tickets);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            finish();

        numberOfFullPriceTicketsSpinner = findViewById(R.id.chooseNumberOfFullPriceTickets);
        numberOfHalfPriceTicketsSpinner = findViewById(R.id.chooseNumberOfHalfPriceTickets);
        chooseExhibitionSpinner = findViewById(R.id.chooseExhibition);

        numberOfFullPriceTicketsSpinner.setOnItemSelectedListener(this);
        numberOfHalfPriceTicketsSpinner.setOnItemSelectedListener(this);
        chooseExhibitionSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> numberOfTicketsAdapter =
                ArrayAdapter.createFromResource(this, R.array.number_of_tickets,
                        android.R.layout.simple_spinner_item);
        numberOfTicketsAdapter.
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        numberOfHalfPriceTicketsSpinner.setAdapter(numberOfTicketsAdapter);
        numberOfFullPriceTicketsSpinner.setAdapter(numberOfTicketsAdapter);
        
        ExhibitionDao.getInstance()
                .getAllExhibitions().addOnSuccessListener(exhibitions -> {
           ArrayAdapter<Exhibition> exhibitionArrayAdapter = new ExhibitionArrayAdapter(this,
                   exhibitions);
           chooseExhibitionSpinner.setAdapter(exhibitionArrayAdapter);
           chooseExhibitionSpinner.setOnItemSelectedListener(this);
        });

        ticketDateET = findViewById(R.id.buyTicketDate);
    }

    public void buyTickets(View view) {
        String numberOfFullPriceTicketsAsString =
                numberOfFullPriceTicketsSpinner.getSelectedItem().toString();
        String numberOfHalfPriceTocketsAsString =
                numberOfHalfPriceTicketsSpinner.getSelectedItem().toString();
        String exhibitionAsString = chooseExhibitionSpinner.getSelectedItem().toString();

        String ticketDateString = ticketDateET.getText().toString();
        Date ticketDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            ticketDate = format.parse(ticketDateString);
        } catch (ParseException e) {
            ticketDate = Calendar.getInstance().getTime();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch ((String) adapterView.getTag()) {
            case "exhibition":
                // TODO asd
                break;
            case "full_price_tickets":
                // TODO
                break;
            case "half_price_tickets":
                // TODO a
                break;
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO
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
                // do nothing as we're already here
                break;
            case R.id.authenticationMenuItem:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.profileMenuItem:
                checkForAuthenticationAndRedirect();
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

        startActivity(new Intent(this, ProfileActivity.class));
    }
}