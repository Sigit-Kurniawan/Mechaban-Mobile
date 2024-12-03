package com.sigit.mechaban.dashboard.montir.listmontir;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class MontirConfirmationAdapter extends RecyclerView.Adapter<MontirConfirmationAdapter.MontirConfirmationViewHolder>{
    private final List<MontirConfirmationItem> montirConfirmationItems;

    public MontirConfirmationAdapter(List<MontirConfirmationItem> montirConfirmationItems) {
        this.montirConfirmationItems = montirConfirmationItems;
    }

    @NonNull
    @Override
    public MontirConfirmationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MontirConfirmationViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recyclerview_montir_confirmation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MontirConfirmationViewHolder holder, int position) {
        MontirConfirmationItem montirConfirmationItem = montirConfirmationItems.get(position);
        holder.getNameText().setText(montirConfirmationItem.getName());
    }

    @Override
    public int getItemCount() {
        return montirConfirmationItems.size();
    }

    public static class MontirConfirmationViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;

        public MontirConfirmationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name_montir);
        }

        public TextView getNameText() {
            return nameText;
        }
    }

    public static class MontirConfirmationItem {
        private final String name;

        public MontirConfirmationItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
