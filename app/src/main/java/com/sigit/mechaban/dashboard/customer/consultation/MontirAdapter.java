package com.sigit.mechaban.dashboard.customer.consultation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.sigit.mechaban.R;

import java.util.List;

public class MontirAdapter extends RecyclerView.Adapter<MontirAdapter.MontirViewHolder>{
    private final List<MontirItem> montirItems;

    public MontirAdapter(List<MontirItem> montirItems) {
        this.montirItems = montirItems;
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
        holder.getNameText().setText(montirItem.getName());
        holder.getEmailText().setText(montirItem.getEmail());
        holder.getNoHpText().setText(montirItem.getNoHp());
    }

    @Override
    public int getItemCount() {
        return montirItems.size();
    }

    public static class MontirViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView photoView;
        TextView nameText, emailText, noHpText;

        public MontirViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.profile_icon);
            nameText = itemView.findViewById(R.id.tv_name);
            emailText = itemView.findViewById(R.id.tv_email);
            noHpText = itemView.findViewById(R.id.tv_no_hp);
        }

        public ShapeableImageView getPhotoView() {
            return photoView;
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getEmailText() {
            return emailText;
        }

        public TextView getNoHpText() {
            return noHpText;
        }
    }

    public static class MontirItem {
        private final int photo;
        private final String name, email, noHp;

        public MontirItem(int photo, String name, String email, String noHp) {
            this.photo = photo;
            this.name = name;
            this.email = email;
            this.noHp = noHp;
        }

        public int getPhoto() {
            return photo;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getNoHp() {
            return noHp;
        }
    }
}
