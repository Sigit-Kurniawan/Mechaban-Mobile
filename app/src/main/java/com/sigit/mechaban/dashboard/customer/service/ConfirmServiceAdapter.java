package com.sigit.mechaban.dashboard.customer.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.model.service.ServiceData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ConfirmServiceAdapter extends RecyclerView.Adapter<ConfirmServiceAdapter.ConfirmServiceViewHolder>{
    private final List<ServiceData> itemList;
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));

    public ConfirmServiceAdapter(List<ServiceData> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ConfirmServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfirmServiceViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_detail_service_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmServiceViewHolder holder, int position) {
        ServiceData item = itemList.get(position);
        holder.serviceView.setText(item.getService());
        holder.priceView.setText(formatter.format(item.getPriceService()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ConfirmServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceView, priceView;

        public ConfirmServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceView = itemView.findViewById(R.id.title_service);
            priceView = itemView.findViewById(R.id.price_text);
        }
    }
}
