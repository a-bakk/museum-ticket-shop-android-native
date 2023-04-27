package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;
import com.example.museumticketshop.adapters.ExhibitionArrayAdapter;
import com.example.museumticketshop.entities.Exhibition;
import com.example.museumticketshop.entities.Ticket;
import com.example.museumticketshop.repositories.ExhibitionDao;
import com.example.museumticketshop.repositories.TicketDao;
import com.example.museumticketshop.services.NotificationService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SelectTicketsActivity extends AppCompatActivity {
    private Spinner numberOfFullPriceTicketsSpinner;
    private Spinner numberOfHalfPriceTicketsSpinner;
    private Spinner chooseExhibitionSpinner;
    private DatePicker ticketDatePicker;
    private NotificationService notificationService;

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
        });

        ticketDatePicker = findViewById(R.id.buyTicketDatePicker);

        notificationService = new NotificationService(this);
    }

    public void buyTickets(View view) {
        String numberOfFullPriceTicketsAsString =
                numberOfFullPriceTicketsSpinner.getSelectedItem().toString();
        String numberOfHalfPriceTicketsAsString =
                numberOfHalfPriceTicketsSpinner.getSelectedItem().toString();
        Exhibition exhibition = (Exhibition) chooseExhibitionSpinner.getSelectedItem();

        LocalDate ticketDate = LocalDate.of(
            ticketDatePicker.getYear(),
            ticketDatePicker.getMonth() + 1, // indexed by 0
            ticketDatePicker.getDayOfMonth()
        );

        if (checkForEmptyString(new String[] {numberOfFullPriceTicketsAsString,
                numberOfHalfPriceTicketsAsString}) || exhibition == null || ticketDate == null) {
            Toast.makeText(this, "Ordering tickets was not successful!",
                    Toast.LENGTH_LONG).show();
            return; // shouldn't ever run as all things have default values
        }

        int fullPrice = Integer.parseInt(numberOfFullPriceTicketsAsString);
        int halfPrice = Integer.parseInt(numberOfHalfPriceTicketsAsString);

        while (fullPrice > 0) {
            createTicketTask(40L, "full price", ticketDate, exhibition.getId())
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Problem with ordering tickets!",
                                Toast.LENGTH_LONG).show();
                    });
            fullPrice--;
        }

        while (halfPrice > 0) {
            createTicketTask(20L, "half price", ticketDate, exhibition.getId())
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Problem with ordering tickets!",
                                Toast.LENGTH_LONG).show();
            });
            halfPrice--;
        }

        notificationService.sendNotification(String.format
                (Locale.ENGLISH, "Congrats on acquiring %d tickets for %s!",
                        Integer.parseInt(numberOfFullPriceTicketsAsString) +
                                Integer.parseInt(numberOfHalfPriceTicketsAsString),
                        exhibition.getName()));

        checkForAuthenticationAndRedirect();
    }

    private Task<Void> createTicketTask(Long price, String ticketType, LocalDate date, String exhibitionId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Ticket ticket = new Ticket();
        ticket.setPrice(price);
        ticket.setTicketType(ticketType);
        ticket.setDate(formatter.format(date));
        ticket.setExhibitionId(exhibitionId);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            ticket.setUserEmail(user.getEmail());

        return TicketDao.getInstance().createTicket(ticket);
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

    private boolean checkForEmptyString(String[] args) {
        for (String str : args) {
            if (str.isEmpty())
                return true;
        }
        return false;
    }
}