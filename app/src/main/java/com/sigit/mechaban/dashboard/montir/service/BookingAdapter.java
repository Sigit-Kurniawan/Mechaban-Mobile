package com.sigit.mechaban.dashboard.montir.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.model.service.ServiceData;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder>{
    private final Context context;
    private final List<BookingItem> bookingItemList;
    private OnItemClickListener onItemClickListener;

    public BookingAdapter(Context context, List<BookingItem> bookingItemList) {
        this.context = context;
        this.bookingItemList = bookingItemList;
    }

    public interface OnItemClickListener {
        void onItemClick(BookingItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.recyclerview_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingItem item = bookingItemList.get(position);
        holder.getMerkText().setText(item.getMerk());
        holder.getTypeText().setText(item.getType());
        holder.getNameText().setText(item.getName());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingItemList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout item;
        TextView merkText, typeText, nameText;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            merkText = itemView.findViewById(R.id.merk_text);
            typeText = itemView.findViewById(R.id.type_text);
            nameText = itemView.findViewById(R.id.name_text);
        }

        public ConstraintLayout getItem() {
            return item;
        }

        public TextView getMerkText() {
            return merkText;
        }

        public TextView getTypeText() {
            return typeText;
        }

        public TextView getNameText() {
            return nameText;
        }
    }

    public static class BookingItem {
        private final String id, nopol, merk, type, transmition, year, name, email, noHP;
        private final double latitude, longitude, price;
        private final List<ServiceData> bookingItemServices;

        public BookingItem(String id, String nopol, String merk, String type, String transmition, String year, String name, String email, String noHP, double latitude, double longitude, double price, List<ServiceData> bookingItemServices) {
            this.id = id;
            this.nopol = nopol;
            this.merk = merk;
            this.type = type;
            this.transmition = transmition;
            this.year = year;
            this.name = name;
            this.email = email;
            this.noHP = noHP;
            this.latitude = latitude;
            this.longitude = longitude;
            this.price = price;
            this.bookingItemServices = bookingItemServices;
        }

        public String getId() {
            return id;
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

        public String getTransmition() {
            return transmition;
        }

        public String getYear() {
            return year;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getNoHP() {
            return noHP;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getPrice() {
            return price;
        }

        public List<ServiceData> getBookingItemServices() {
            return bookingItemServices;
        }
    }
}
