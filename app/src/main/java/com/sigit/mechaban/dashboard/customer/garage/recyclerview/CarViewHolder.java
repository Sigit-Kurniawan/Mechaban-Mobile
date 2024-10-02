package com.sigit.mechaban.dashboard.customer.garage.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

public class CarViewHolder extends RecyclerView.ViewHolder {
    private final TextView merkTextView, typeTextView, yearTextView;

    public CarViewHolder(@NonNull View itemView) {
        super(itemView);
        merkTextView = itemView.findViewById(R.id.merk_text);
        typeTextView = itemView.findViewById(R.id.type_text);
        yearTextView = itemView.findViewById(R.id.year_text);
    }

    public TextView getMerkTextView() {
        return merkTextView;
    }

    public TextView getTypeTextView() {
        return typeTextView;
    }

    public TextView getYearTextView() {
        return yearTextView;
    }
}
