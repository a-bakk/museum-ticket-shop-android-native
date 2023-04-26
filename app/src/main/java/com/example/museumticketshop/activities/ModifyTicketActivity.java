package com.example.museumticketshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;
import com.example.museumticketshop.entities.Ticket;
import com.example.museumticketshop.repositories.ExhibitionDao;
import com.example.museumticketshop.repositories.TicketDao;
import com.google.android.gms.tasks.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ModifyTicketActivity extends AppCompatActivity {
    private String currentTicketId;
    private DatePicker modifyTicketDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_ticket);

        if (!Objects.equals(getIntent().getLongExtra("secretKey", 0L),
                88437216746L)) {
            finish();
        }

        String ticketIdFromProfileActivity = getIntent().getStringExtra("currentTicketId");
        if (ticketIdFromProfileActivity == null || ticketIdFromProfileActivity.isEmpty())
            finish();

        currentTicketId = ticketIdFromProfileActivity;
        modifyTicketDatePicker = findViewById(R.id.modTicketDatePicker);

        TextView modifyTicketIdTV = findViewById(R.id.modTicketIdPlaceholder);
        TextView modifyTicketExhibitionTV = findViewById(R.id.modTicketExhibitionPlaceholder);
        TextView modifyTicketTypeTV = findViewById(R.id.modTicketTypePlaceholder);
        TextView modifyTicketCurrentDateTV = findViewById(R.id.modTicketCurrentDatePlaceholder);

        TicketDao.getInstance().readTicketById(currentTicketId)
                .addOnSuccessListener(ticket -> {
                    ExhibitionDao.getInstance().getExhibitionById(ticket.getExhibitionId())
                            .addOnSuccessListener(exhibition -> {
                                modifyTicketIdTV.setText(ticket.getId());
                                modifyTicketExhibitionTV.setText(exhibition.getName());
                                modifyTicketTypeTV.setText(ticket.getTicketType());
                                modifyTicketCurrentDateTV.setText(ticket.getDate());
                            }).addOnFailureListener(e -> {
                                Toast.makeText(this, "Exhibition name cannot be loaded!",
                                        Toast.LENGTH_LONG).show();
                                finish();
                            });
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Ticket details cannot be loaded!",
                            Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    public void modifyDate(View view) {
        LocalDate updatedDate = LocalDate.of(
                modifyTicketDatePicker.getYear(),
                modifyTicketDatePicker.getMonth() + 1, // indexed by 0
                modifyTicketDatePicker.getDayOfMonth()
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        TicketDao.getInstance().readTicketById(currentTicketId)
                .addOnSuccessListener(ticket ->
                        updateTicketTask(ticket, formatter.format(updatedDate))
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Ticket updated successfully!",
                                    Toast.LENGTH_LONG).show();
                            recreate();
                        }).addOnFailureListener(unused ->
                                Toast.makeText(this, "Ticket could not be updated!",
                                        Toast.LENGTH_LONG).show())).addOnFailureListener(e ->
                        Toast.makeText(this, "Ticket date could not be updated!",
                                Toast.LENGTH_LONG).show());
    }

    private Task<Void> updateTicketTask(Ticket curr_ticket, String updatedDate) {
        curr_ticket.setDate(updatedDate);
        return TicketDao.getInstance().updateTicket(curr_ticket.getId(), curr_ticket);
    }

    public void backToExhibitions(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}