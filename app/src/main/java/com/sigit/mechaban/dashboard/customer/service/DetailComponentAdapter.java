package com.sigit.mechaban.dashboard.customer.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class DetailComponentAdapter extends RecyclerView.Adapter<DetailComponentAdapter.DetailComponentViewHolder>{
    private final List<ServiceAdapter.ServiceItem> itemList;

    public DetailComponentAdapter(List<ServiceAdapter.ServiceItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public DetailComponentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailComponentViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_detail_component_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailComponentViewHolder holder, int position) {
        ServiceAdapter.ServiceItem item = itemList.get(position);
        holder.serviceView.setText(item.getService());
        holder.priceVIew.setText(String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class DetailComponentViewHolder extends RecyclerView.ViewHolder {
        TextView serviceView, priceVIew;

        public DetailComponentViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceView = itemView.findViewById(R.id.title_service);
            priceVIew = itemView.findViewById(R.id.price_text);
        }
    }

//    public static class DetailComponentItem {
//        private final String component;
//        private final int subtotal;
////        private final List<DetailServiceAdapter.DetailServiceItem> item;
//
//        public DetailComponentItem(String component, int price) {
//            this.component = component;
//            this.subtotal = price;
////            this.item = item;
//        }
//
//        public String getComponent() {
//            return component;
//        }
//
//        public int getSubtotal() {
//            return subtotal;
//        }
//
////        public List<DetailServiceAdapter.DetailServiceItem> getItem() {
////            return item;
////        }
//    }
}
