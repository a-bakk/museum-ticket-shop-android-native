package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.museumticketshop.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        // TODO: dinamically deal with this
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
}