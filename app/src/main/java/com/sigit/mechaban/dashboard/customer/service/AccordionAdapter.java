package com.sigit.mechaban.dashboard.customer.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class AccordionAdapter extends RecyclerView.Adapter<AccordionAdapter.AccordionViewHolder>{
    private final List<AccordionItem> itemList;
    private final ServiceAdapter.OnItemSelectedListener itemSelectedListener;

    public AccordionAdapter(List<AccordionItem> itemList, ServiceAdapter.OnItemSelectedListener itemSelectedListener) {
        this.itemList = itemList;
        this.itemSelectedListener = itemSelectedListener;
    }

    @NonNull
    @Override
    public AccordionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccordionViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_accordion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccordionViewHolder holder, int position) {
        AccordionItem item = itemList.get(position);
        holder.logoView.setImageResource(item.getLogo());
        holder.textViewHeader.setText(item.getComponent());

        holder.serviceList.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);

        holder.accordion.setOnClickListener(v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(position);
        });

        if (item.isExpanded()) {
            ServiceAdapter serviceAdapter = new ServiceAdapter(item.getItem(), itemSelectedListener);
            holder.serviceList.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            holder.serviceList.setAdapter(serviceAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class AccordionViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout accordion;
        ImageView logoView, arrowDown;
        TextView textViewHeader;
        RecyclerView serviceList;

        public AccordionViewHolder(@NonNull View itemView) {
            super(itemView);
            accordion = itemView.findViewById(R.id.accordion);
            logoView = itemView.findViewById(R.id.logo_service);
            arrowDown = itemView.findViewById(R.id.arrow_down);
            textViewHeader = itemView.findViewById(R.id.textViewHeader);
            serviceList = itemView.findViewById(R.id.service_list);
        }
    }

    public static class AccordionItem {
        private final int logo;
        private final String component;
        private boolean isExpanded;
        private final List<ServiceAdapter.ServiceItem> item;

        public AccordionItem(int logo, String component, List<ServiceAdapter.ServiceItem> item) {
            this.logo = logo;
            this.component = component;
            this.isExpanded = false;
            this.item = item;
        }

        public int getLogo() {
            return logo;
        }

        public String getComponent() {
            return component;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

        public List<ServiceAdapter.ServiceItem> getItem() {
            return item;
        }
    }
}
