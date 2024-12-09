package com.sigit.mechaban.dashboard.montir.listmontir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.sigit.mechaban.BuildConfig;
import com.sigit.mechaban.R;

import java.util.ArrayList;
import java.util.List;

public class MontirAdapter extends RecyclerView.Adapter<MontirAdapter.MontirViewHolder> {
    private final Context context;
    private final List<MontirItem> montirItems;
    private final List<MontirItem> originalList;
    private final List<String> selectedMontirs = new ArrayList<>();
    private final OnMontirAddListener listener;

    public MontirAdapter(Context context, List<MontirItem> montirItems, OnMontirAddListener listener) {
        this.context = context;
        this.montirItems = new ArrayList<>(montirItems);
        this.originalList = new ArrayList<>(montirItems);
        this.listener = listener;
    }

    public interface OnMontirAddListener {
        void onMontirAdd(List<String> selectedMontirs);
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
        MontirItem montirItem = montirItems.get(position);
        Glide.with(context)
                .load("http://" + BuildConfig.ip + "/api/src/" + montirItem.getPhoto())
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(holder.getPhotoProfile());
        holder.getNameText().setText(montirItem.getName());

        if (selectedMontirs.contains(montirItem.getEmail())) {
            holder.addMontirButton.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_check_24));
        } else {
            holder.addMontirButton.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_add_24));
        }

        holder.getItemLayout().setOnClickListener(v -> toggleSelection(holder, montirItem));
        holder.getAddMontirButton().setOnClickListener(v -> toggleSelection(holder, montirItem));
    }

    @Override
    public int getItemCount() {
        return montirItems.size();
    }

    public static class MontirViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout itemLayout;
        private final ShapeableImageView photoProfile;
        private final TextView nameText;
        private final MaterialButton addMontirButton;

        public MontirViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item);
            photoProfile = itemView.findViewById(R.id.profile_icon);
            nameText = itemView.findViewById(R.id.text1);
            addMontirButton = itemView.findViewById(R.id.add_button);
        }

        public ConstraintLayout getItemLayout() {
            return itemLayout;
        }

        public ShapeableImageView getPhotoProfile() {
            return photoProfile;
        }

        public TextView getNameText() {
            return nameText;
        }

        public Button getAddMontirButton() {
            return addMontirButton;
        }
    }

    public static class MontirItem {
        private final String email, name, photo;

        public MontirItem(String email, String name, String photo) {
            this.email = email;
            this.name = name;
            this.photo = photo;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public boolean filter(String query) {
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

        return montirItems.isEmpty();
    }

    private void toggleSelection(@NonNull MontirViewHolder holder, @NonNull MontirItem montirItem) {
        if (selectedMontirs.contains(montirItem.getEmail())) {
            selectedMontirs.remove(montirItem.getEmail());
            holder.addMontirButton.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_add_24));
        } else {
            selectedMontirs.add(montirItem.getEmail());
            holder.addMontirButton.setIcon(ContextCompat.getDrawable(context, R.drawable.baseline_check_24));
        }

        if (listener != null) {
            listener.onMontirAdd(selectedMontirs);
        }
    }
}
