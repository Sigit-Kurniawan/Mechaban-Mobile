package com.sigit.mechaban.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sigit.mechaban.R;

public class DetailBottomSheet extends BottomSheetDialogFragment {
    private final int price;

    public DetailBottomSheet(int price) {
        this.price = price;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_bottom_sheet, container, false);

        RecyclerView detailRecycler = view.findViewById(R.id.detail_recycler);

        TextView priceTextView = view.findViewById(R.id.price_text);
        priceTextView.setText(String.valueOf(price));
        view.findViewById(R.id.button).setOnClickListener(v -> dismiss());

        return view;
    }
}
