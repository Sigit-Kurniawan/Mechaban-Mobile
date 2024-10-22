package com.sigit.mechaban.dashboard.customer.garage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
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

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        private final TextView merkTextView, typeTextView, yearTextView;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            merkTextView = itemView.findViewById(R.id.merk_text);
            typeTextView = itemView.findViewById(R.id.type_text);
            yearTextView = itemView.findViewById(R.id.year_text);
        }

        public TextView getMerkTextView() {
            return merkTextView;
        }

        public TextView getTypeTextView() {
            return typeTextView;
        }

        public TextView getYearTextView() {
            return yearTextView;
        }
    }

    public static class CarItem {
        private final String merk, type, year;

        public CarItem(String merk, String type, String year) {
            this.merk = merk;
            this.type = type;
            this.year = year;
        }

        public String getMerk() {
            return merk;
        }

        public String getType() {
            return type;
        }

        public String getYear() {
            return year;
        }
    }
}
