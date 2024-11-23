package com.sigit.mechaban.dashboard.customer.activity.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.model.booking.BookingData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private final Context context;
    private final List<ActivityItem> activityItemList;
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
        holder.getDateTextView().setText(activityItemList.get(position).getDate());
        holder.getMerkTextView().setText(activityItemList.get(position).getMerk());
        holder.getTypeTextView().setText(activityItemList.get(position).getType());
        holder.getStatusTextView().setText(activityItemList.get(position).getStatus());
        holder.getTotalTextView().setText(formatter.format(activityItemList.get(position).getTotal()));
    }

    @Override
    public int getItemCount() {
        return activityItemList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, merkTextView, typeTextView, statusTextView, totalTextView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text);
            merkTextView = itemView.findViewById(R.id.merk_text);
            typeTextView = itemView.findViewById(R.id.type_text);
            statusTextView = itemView.findViewById(R.id.status_text);
            totalTextView = itemView.findViewById(R.id.total_text);
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
}
