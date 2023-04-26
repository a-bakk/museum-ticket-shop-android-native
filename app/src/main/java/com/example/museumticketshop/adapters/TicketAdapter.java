package com.example.museumticketshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.museumticketshop.R;
import com.example.museumticketshop.activities.ProfileActivity;
import com.example.museumticketshop.activities.SelectTicketsActivity;
import com.example.museumticketshop.entities.Ticket;
import com.example.museumticketshop.repositories.ExhibitionDao;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private final List<Ticket> tickets;
    private final Context context;

    public TicketAdapter(Context context, List<Ticket> tickets) {
        this.context = context;
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TicketViewHolder(LayoutInflater
                .from(context).inflate(R.layout.ticket_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        holder.bindTo(tickets.get(position));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder {
        private final TextView ticketExhibitionTV;
        private final TextView ticketTypeTV;
        private final TextView ticketDateTV;
        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketExhibitionTV = itemView.findViewById(R.id.ticketExhibitionPlaceholder);
            ticketTypeTV = itemView.findViewById(R.id.ticketTypePlaceholder);
            ticketDateTV = itemView.findViewById(R.id.ticketDatePlaceholder);
        }

        public void bindTo(Ticket currentTicket) {
            ExhibitionDao dao = ExhibitionDao.getInstance();
            dao.getExhibitionById(currentTicket.getExhibitionId())
                    .addOnSuccessListener(exhibition ->
                            ticketExhibitionTV.setText(exhibition.getName()))
                    .addOnFailureListener(e ->
                            ticketExhibitionTV.setText(R.string.unknown));

            ticketTypeTV.setText(currentTicket.getTicketType());
            ticketDateTV.setText(currentTicket.getDate());

            itemView.findViewById(R.id.ticketModifyButton).setOnClickListener(view -> {
                ((ProfileActivity) context).redirectToTicketModification(currentTicket.getId());
            });

            itemView.findViewById(R.id.ticketDeleteButton).setOnClickListener(view -> {
                ((ProfileActivity) context).deleteTicket(currentTicket.getId());
            });
        }
    }
}
