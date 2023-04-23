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

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

        // TODO: dynamically deal with this
        List<String> placeholderExhibs = new ArrayList<>();
        placeholderExhibs.add("1: A");
        placeholderExhibs.add("2: B");

        ArrayAdapter<String> exhibitionsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                        placeholderExhibs);
        exhibitionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        chooseExhibitionSpinner.setAdapter(exhibitionsAdapter);

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