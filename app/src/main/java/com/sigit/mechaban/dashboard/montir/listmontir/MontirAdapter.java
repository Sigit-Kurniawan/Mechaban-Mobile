package com.sigit.mechaban.dashboard.montir.listmontir;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.ArrayList;
import java.util.List;

public class MontirAdapter extends RecyclerView.Adapter<MontirAdapter.MontirViewHolder> {
    private final List<MontirItem> montirItems;
    private final List<MontirItem> originalList;

    public MontirAdapter(List<MontirItem> montirItems) {
        this.montirItems = new ArrayList<>(montirItems);
        this.originalList = new ArrayList<>(montirItems);
    }

    @NonNull
    @Override
    public MontirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MontirViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_montir, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MontirViewHolder holder, int position) {
        holder.getNameText().setText(montirItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return montirItems.size();
    }

    public static class MontirViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;

        public MontirViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text1);
        }

        public TextView getNameText() {
            return nameText;
        }
    }

    public static class MontirItem {
        private final String name;

        public MontirItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        montirItems.clear();
        if (query.isEmpty()) {
            montirItems.addAll(originalList);
        } else {
            for (MontirItem item : originalList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    montirItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
