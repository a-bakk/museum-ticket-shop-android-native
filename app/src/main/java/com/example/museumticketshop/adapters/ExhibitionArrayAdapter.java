package com.example.museumticketshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.museumticketshop.entities.Exhibition;

import java.util.List;

public class ExhibitionArrayAdapter extends ArrayAdapter<Exhibition> {
    private final LayoutInflater layoutInflater;

    public ExhibitionArrayAdapter(@NonNull Context context,
                                  @NonNull List<Exhibition> exhibitions) {
        super(context, 0, exhibitions);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = layoutInflater.
                    inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(getItem(position).getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.
                    inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(getItem(position).getName());

        return view;
    }
}
