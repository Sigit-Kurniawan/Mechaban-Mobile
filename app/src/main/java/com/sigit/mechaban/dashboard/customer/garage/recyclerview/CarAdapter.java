package com.sigit.mechaban.dashboard.customer.garage.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarViewHolder> {
    private final Context context;
    private final List<CarItem> carItems;

    public CarAdapter(Context context, List<CarItem> carItems) {
        this.context = context;
        this.carItems = carItems;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.car_item_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        holder.getMerkTextView().setText(carItems.get(position).getMerk());
        holder.getTypeTextView().setText(carItems.get(position).getType());
        holder.getYearTextView().setText(carItems.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        return carItems.size();
    }
}
