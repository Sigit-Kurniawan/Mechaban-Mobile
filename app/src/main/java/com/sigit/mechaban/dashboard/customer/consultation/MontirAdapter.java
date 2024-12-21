package com.sigit.mechaban.dashboard.customer.consultation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;

import java.util.List;

public class MontirAdapter extends RecyclerView.Adapter<MontirAdapter.MontirViewHolder>{
    private final Context context;
    private final List<MontirItem> montirItems;

    public MontirAdapter(Context context, List<MontirItem> montirItems) {
        this.context = context;
        this.montirItems = montirItems;
    }

    @NonNull
    @Override
    public MontirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MontirViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.recyclerview_consultation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MontirViewHolder holder, int position) {
        MontirItem montirItem = montirItems.get(position);
        holder.getNameText().setText(montirItem.getName());
        holder.getEmailText().setText(montirItem.getEmail());
        holder.getNoHpText().setText(montirItem.getNoHp());
        if (montirItem.getPhoto() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(ApiClient.getPhotoUrl() + montirItem.getPhoto())
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.getPhotoView());
        }
        holder.getWaButton().setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/62" + montirItem.getNoHp()));
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("ContactUs", e.toString(), e);
            }
        });
        holder.getEmailButton().setOnClickListener(v -> {
            String subject = "Konsultasi Mechaban";
            String message = "Konsultasi saya: ";

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{montirItem.getEmail()});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return montirItems.size();
    }

    public static class MontirViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView photoView;
        private final TextView nameText, emailText, noHpText;
        private final Button waButton, emailButton;

        public MontirViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.profile_icon);
            nameText = itemView.findViewById(R.id.tv_name);
            emailText = itemView.findViewById(R.id.tv_email);
            noHpText = itemView.findViewById(R.id.tv_no_hp);
            waButton = itemView.findViewById(R.id.wa_button);
            emailButton = itemView.findViewById(R.id.email_button);
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

        public Button getWaButton() {
            return waButton;
        }

        public Button getEmailButton() {
            return emailButton;
        }
    }

    public static class MontirItem {
        private final String name, email, noHp, photo;

        public MontirItem(String photo, String name, String email, String noHp) {
            this.photo = photo;
            this.name = name;
            this.email = email;
            this.noHp = noHp;
        }

        public String getPhoto() {
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
