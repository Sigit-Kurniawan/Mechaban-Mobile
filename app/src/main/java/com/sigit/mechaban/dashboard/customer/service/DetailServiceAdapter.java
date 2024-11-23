package com.sigit.mechaban.dashboard.customer.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailServiceAdapter extends RecyclerView.Adapter<DetailServiceAdapter.DetailServiceViewHolder>{
    private final List<ServiceAdapter.ServiceItem> itemList;
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));

    public DetailServiceAdapter(List<ServiceAdapter.ServiceItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public DetailServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailServiceViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_detail_service_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailServiceViewHolder holder, int position) {
        ServiceAdapter.ServiceItem item = itemList.get(position);
        holder.serviceView.setText(item.getService());
        holder.priceView.setText(formatter.format(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class DetailServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceView, priceView;

        public DetailServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceView = itemView.findViewById(R.id.title_service);
            priceView = itemView.findViewById(R.id.price_text);
        }
    }
}
