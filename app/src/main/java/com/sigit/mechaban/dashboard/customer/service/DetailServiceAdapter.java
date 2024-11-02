package com.sigit.mechaban.dashboard.customer.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class DetailServiceAdapter extends RecyclerView.Adapter<DetailServiceAdapter.DetailServiceViewHolder>{
    private final List<DetailServiceItem> itemList;

    public DetailServiceAdapter(List<DetailServiceItem> itemList) {
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

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class DetailServiceViewHolder extends RecyclerView.ViewHolder {

        public DetailServiceViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class DetailServiceItem {
        private final String service;
        private final int price;

        public DetailServiceItem(String service, int price) {
            this.service = service;
            this.price = price;
        }

        public String getService() {
            return service;
        }

        public int getPrice() {
            return price;
        }
    }
}
