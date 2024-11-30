package com.sigit.mechaban.dashboard.customer.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.service.ConfirmationActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private final Context context;
    private List<ActivityItem> activityItemList;
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));

    public ActivityAdapter(Context context, List<ActivityItem> activityItemList) {
        this.context = context;
        this.activityItemList = activityItemList;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.recyclerview_activity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityItem activityItem = activityItemList.get(position);
        holder.getDateTextView().setText(activityItem.getDate());
        holder.getMerkTextView().setText(activityItem.getMerk());
        holder.getTypeTextView().setText(activityItem.getType());
        holder.getStatusTextView().setText(activityItem.getStatus());
        holder.getTotalTextView().setText(formatter.format(activityItem.getTotal()));
        holder.getCancelButton().setOnClickListener(v -> showBottomSheetDialog(position));
        holder.getDetailButton().setOnClickListener(v -> {
            Intent intent = new Intent(context, ConfirmationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id_booking", activityItem.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return activityItemList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, merkTextView, typeTextView, statusTextView, totalTextView;
        Button detailButton, cancelButton;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text);
            merkTextView = itemView.findViewById(R.id.merk_text);
            typeTextView = itemView.findViewById(R.id.type_text);
            statusTextView = itemView.findViewById(R.id.status_text);
            totalTextView = itemView.findViewById(R.id.total_text);
            cancelButton = itemView.findViewById(R.id.cancel_button);
            detailButton = itemView.findViewById(R.id.detail_button);
        }

        public TextView getDateTextView() {
            return dateTextView;
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

        public TextView getTotalTextView() {
            return totalTextView;
        }

        public Button getCancelButton() {
            return cancelButton;
        }

        public Button getDetailButton() {
            return detailButton;
        }
    }

    public static class ActivityItem {
        private final String id, date, merk, type, status;
        private final double total;

        public ActivityItem(String id, String date, String merk, String type, String status, double total) {
            this.id = id;
            this.date = date;
            this.merk = merk;
            this.type = type;
            this.status = status;
            this.total = total;
        }

        public String getId() {
            return id;
        }

        public String getDate() {
            return date;
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

        public double getTotal() {
            return total;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<ActivityItem> filteredList) {
        this.activityItemList = filteredList;
        notifyDataSetChanged();
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
        Button cancelButton = dialogView.findViewById(R.id.negative_button);

        imageView.setImageResource(R.drawable.cancel);

        title.setText("Batalkan Booking?");

        description.setText("Ini akan menghilangkan bookingmu untuk selamanya");

        confirmButton.setText("Batalkan");
        confirmButton.setOnClickListener(v -> {
            activityItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, activityItemList.size());
            bottomSheetDialog.dismiss();
        });
        confirmButton.setBackgroundColor(Color.TRANSPARENT);
        confirmButton.setStrokeColor(ContextCompat.getColorStateList(context, R.color.md_theme_error));
        confirmButton.setStrokeWidth(4);
        confirmButton.setRippleColor(ContextCompat.getColorStateList(context, R.color.md_theme_errorContainer));
        confirmButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_error));

        cancelButton.setText("Gak jadi deh");
        cancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());
        cancelButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_background));

        bottomSheetDialog.show();
    }
}
