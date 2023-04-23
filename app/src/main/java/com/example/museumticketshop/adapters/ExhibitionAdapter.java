package com.example.museumticketshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.museumticketshop.R;
import com.example.museumticketshop.entities.Exhibition;

import java.util.List;

public class ExhibitionAdapter extends RecyclerView.Adapter<ExhibitionAdapter.ExhibitionViewHolder> {
    private List<Exhibition> exhibitions;
    private Context context;

    public ExhibitionAdapter(Context context, List<Exhibition> exhibitions) {
        this.context = context;
        this.exhibitions = exhibitions;
    }

    @NonNull
    @Override
    public ExhibitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExhibitionViewHolder(LayoutInflater
                .from(context).inflate(R.layout.exhib_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExhibitionViewHolder holder, int position) {
        Exhibition currentExhibition = exhibitions.get(position);

        holder.bindTo(currentExhibition);
    }

    @Override
    public int getItemCount() {
        return exhibitions.size();
    }

    class ExhibitionViewHolder extends RecyclerView.ViewHolder {
        private ImageView exhibitionIV;
        private TextView exhibitionNameTV;
        private TextView exhibitionDescriptionTV;

        public ExhibitionViewHolder(@NonNull View itemView) {
            super(itemView);
            exhibitionNameTV = itemView.findViewById(R.id.exhibitionName);
            exhibitionDescriptionTV = itemView.findViewById(R.id.exhibitionDescription);
            exhibitionIV = itemView.findViewById(R.id.exhibitionImageView);
        }

        public void bindTo(Exhibition currentExhibition) {
            exhibitionNameTV.setText(currentExhibition.getName());
            exhibitionDescriptionTV.setText(currentExhibition.getDescription());

            Glide.with(context).load(currentExhibition.getImageResource()).into(exhibitionIV);
        }
    }
}
