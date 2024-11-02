package com.sigit.mechaban.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.service.DetailComponentAdapter;
import com.sigit.mechaban.dashboard.customer.service.ServiceAdapter;

import java.util.List;

public class DetailBottomSheet extends BottomSheetDialogFragment {
    private final int price;
    private final List<ServiceAdapter.ServiceItem> selectedServices;

    public DetailBottomSheet(int price, List<ServiceAdapter.ServiceItem> selectedServices) {
        this.price = price;
        this.selectedServices = selectedServices;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_detail_service, container, false);

        RecyclerView detailRecycler = view.findViewById(R.id.detail_recycler);
        detailRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        DetailComponentAdapter adapter = new DetailComponentAdapter(selectedServices);
        detailRecycler.setAdapter(adapter);

        TextView priceTextView = view.findViewById(R.id.price_text);
        priceTextView.setText(String.valueOf(price));
        view.findViewById(R.id.button).setOnClickListener(v -> dismiss());

        return view;
    }
}
