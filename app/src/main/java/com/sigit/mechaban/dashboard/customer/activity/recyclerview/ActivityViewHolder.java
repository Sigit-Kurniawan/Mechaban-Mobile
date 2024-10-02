package com.sigit.mechaban.dashboard.customer.activity.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

public class ActivityViewHolder extends RecyclerView.ViewHolder {
    private final TextView dateTextView, titleTextView, descTextView;

    public ActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.date_text);
        titleTextView = itemView.findViewById(R.id.title_text);
        descTextView = itemView.findViewById(R.id.desc_text);
    }

    public TextView getDateTextView() {
        return dateTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getDescTextView() {
        return descTextView;
    }
}
