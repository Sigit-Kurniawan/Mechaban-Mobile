package com.sigit.mechaban.dashboard.customer.activity.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityViewHolder> {
    private final Context context;
    private final List<ActivityItem> activityItemList;

    public ActivityAdapter(Context context, List<ActivityItem> activityItemList) {
        this.context = context;
        this.activityItemList = activityItemList;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.activity_item_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        holder.getDateTextView().setText(activityItemList.get(position).getDate());
        holder.getTitleTextView().setText(activityItemList.get(position).getTitle());
        holder.getDescTextView().setText(activityItemList.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return activityItemList.size();
    }
}
