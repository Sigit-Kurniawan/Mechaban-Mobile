package com.sigit.mechaban.dashboard.customer.service;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>{
    private final List<ServiceItem> itemList;
    private final OnItemSelectedListener listener;

    public ServiceAdapter(List<ServiceItem> itemList, OnItemSelectedListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int price, boolean isChoosen);
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.service_item_recyclerview, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceItem item = itemList.get(position);
        holder.serviceLayout.setOnClickListener(v -> selectedItem(item, holder));
        holder.serviceTextView.setText(item.getService());
        holder.priceTextView.setText(String.valueOf(item.getPrice()));
        holder.checkBox.setChecked(item.isChoosen());
        holder.checkBox.setOnClickListener(v -> selectedItem(item, holder));
    }

    private void selectedItem(ServiceItem item, ServiceViewHolder holder) {
        if (!item.isChoosen()) {
            item.setChoosen(true);
            holder.checkBox.setChecked(true);
            listener.onItemSelected(item.getPrice(), true);
        } else {
            item.setChoosen(false);
            holder.checkBox.setChecked(false);
            listener.onItemSelected(item.getPrice(), false);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout serviceLayout;
        TextView serviceTextView, priceTextView;
        CheckBox checkBox;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceLayout = itemView.findViewById(R.id.service_layout);
            serviceTextView = itemView.findViewById(R.id.service);
            priceTextView = itemView.findViewById(R.id.price);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public static class ServiceItem {
        private final String service;
        private final int price;
        private boolean isChoosen;

        public ServiceItem(String service, int price) {
            this.service = service;
            this.price = price;
            this.isChoosen = false;
        }

        public String getService() {
            return service;
        }

        public int getPrice() {
            return price;
        }

        public boolean isChoosen() {
            return isChoosen;
        }

        public void setChoosen(boolean choosen) {
            isChoosen = choosen;
        }
    }
}
