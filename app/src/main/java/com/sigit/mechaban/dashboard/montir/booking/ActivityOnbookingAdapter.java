package com.sigit.mechaban.dashboard.montir.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.sigit.mechaban.R;
import com.sigit.mechaban.components.ModalSeachMontir;
import com.sigit.mechaban.dashboard.montir.service.ConfirmationMontirActivity;

import java.util.List;

public class ActivityOnbookingAdapter extends RecyclerView.Adapter<ActivityOnbookingAdapter.ActivityOnbookingViewHolder>{
    private final Context context;
    private final List<ActivityOnbookingItem> activityOnbookingItems;

    public ActivityOnbookingAdapter(Context context, List<ActivityOnbookingItem> activityOnbookingItems) {
        this.context = context;
        this.activityOnbookingItems = activityOnbookingItems;
    }

    @NonNull
    @Override
    public ActivityOnbookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityOnbookingViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.recyclerview_activity_onbooking, parent, false));
    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public void onBindViewHolder(@NonNull ActivityOnbookingViewHolder holder, int position) {
        ActivityOnbookingItem activityOnbookingItem = activityOnbookingItems.get(position);
        holder.getDateTextView().setText(activityOnbookingItem.getDate());
        holder.getNopolTextView().setText(activityOnbookingItem.getNopol());
        holder.getMerkTextView().setText(activityOnbookingItem.getMerk());
        holder.getTypeTextView().setText(activityOnbookingItem.getType());
        holder.getStatusTextView().setText(activityOnbookingItem.getStatus());
        holder.getDoneButton().setOnClickListener(v -> showBottomSheetDialog(position));
        holder.getDetailButton().setOnClickListener(v -> {
            Intent intent = new Intent(context, ConfirmationMontirActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id_booking", activityOnbookingItem.getId());
            context.startActivity(intent);
        });
        holder.getLocationButton().setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + activityOnbookingItem.getLatitude() + "," + activityOnbookingItem.getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Cek apakah Google Maps tersedia di perangkat
            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            } else {
                Log.e("ActivityOnbookingAdapter", "Google Maps tidak tersedia di perangkat ini.");
                showInstallGoogleMapsDialog();
            }
        });
        holder.getAddMontirButton().setOnClickListener(v -> {
            if (context instanceof AppCompatActivity) {
                new ModalSeachMontir(context).show(((AppCompatActivity) context).getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityOnbookingItems.size();
    }

    public static class ActivityOnbookingViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView, nopolTextView, merkTextView, typeTextView, statusTextView;
        private final Button detailButton, doneButton, locationButton, addMontirButton;

        public ActivityOnbookingViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text);
            nopolTextView = itemView.findViewById(R.id.nopol_text);
            merkTextView = itemView.findViewById(R.id.merk_text);
            typeTextView = itemView.findViewById(R.id.type_text);
            statusTextView = itemView.findViewById(R.id.status_text);
            doneButton = itemView.findViewById(R.id.done_button);
            detailButton = itemView.findViewById(R.id.detail_button);
            locationButton = itemView.findViewById(R.id.location_button);
            addMontirButton = itemView.findViewById(R.id.add_montir_button);
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        public TextView getNopolTextView() {
            return nopolTextView;
        }

        public TextView getMerkTextView() {
            return merkTextView;
        }

        public TextView getTypeTextView() {
            return typeTextView;
        }

        public TextView getStatusTextView() {
            return statusTextView;
        }

        public Button getDetailButton() {
            return detailButton;
        }

        public Button getDoneButton() {
            return doneButton;
        }

        public Button getLocationButton() {
            return locationButton;
        }

        public Button getAddMontirButton() {
            return addMontirButton;
        }
    }
    public static class ActivityOnbookingItem {
        private final String id, date, nopol, merk, type, status;
        private final double latitude, longitude;

        public ActivityOnbookingItem(String id, String date, String nopol, String merk, String type, String status, double latitude, double longitude) {
            this.id = id;
            this.date = date;
            this.nopol = nopol;
            this.merk = merk;
            this.type = type;
            this.status = status;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getId() {
            return id;
        }

        public String getDate() {
            return date;
        }

        public String getNopol() {
            return nopol;
        }

        public String getMerk() {
            return merk;
        }

        public String getType() {
            return type;
        }

        public String getStatus() {
            return status;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showBottomSheetDialog(int position) {
        AppCompatActivity activity = (AppCompatActivity) context;

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_modal_two_button, null);
        bottomSheetDialog.setContentView(dialogView);

        ImageView imageView = dialogView.findViewById(R.id.photo);
        TextView title = dialogView.findViewById(R.id.title);
        TextView description = dialogView.findViewById(R.id.description);
        MaterialButton confirmButton = dialogView.findViewById(R.id.positive_button);
        MaterialButton cancelButton = dialogView.findViewById(R.id.negative_button);

        imageView.setImageResource(R.drawable.done);

        title.setText("Selesaikan Booking?");

        description.setText("Ini akan menyelesaikan booking secara permanen tidak bisa diubah kembali.");

        confirmButton.setText("Selesaikan");
        confirmButton.setOnClickListener(v -> {
            activityOnbookingItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, activityOnbookingItems.size());
            bottomSheetDialog.dismiss();
        });
        confirmButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_background));

        cancelButton.setText("Gak jadi deh");
        cancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());
        cancelButton.setBackgroundColor(Color.TRANSPARENT);
        cancelButton.setStrokeWidth(4);
        cancelButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
        cancelButton.setStrokeColor(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
        cancelButton.setRippleColor(ContextCompat.getColorStateList(context, R.color.md_theme_primaryContainer));
        cancelButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_primary));

        bottomSheetDialog.show();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void showInstallGoogleMapsDialog() {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Google Maps Tidak Tersedia")
                .setMessage("Aplikasi Google Maps tidak terinstal di perangkat Anda. Silakan unduh dari Play Store.")
                .setPositiveButton("Unduh", (dialog, which) -> {
                    // Arahkan pengguna ke Play Store
                    Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        Log.e("ActivityOnbookingAdapter", "Tidak dapat membuka Play Store.");
                    }
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
