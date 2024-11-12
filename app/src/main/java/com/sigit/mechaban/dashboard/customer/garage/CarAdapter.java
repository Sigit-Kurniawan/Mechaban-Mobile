package com.sigit.mechaban.dashboard.customer.garage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private final Context context;
    private final List<CarItem> carItems;
    private int selectedPosition;
    private final SessionManager sessionManager;
    private OnCarSelectedListener listener;

    public CarAdapter(Context context, List<CarItem> carItems, int selectedPosition, OnCarSelectedListener listener) {
        this.context = context;
        this.carItems = carItems;
        this.sessionManager = new SessionManager(context);
        this.selectedPosition = selectedPosition;
        this.listener = listener;
    }

    public CarAdapter(Context context, List<CarItem> carItems, int selectedPosition) {
        this.context = context;
        this.carItems = carItems;
        this.sessionManager = new SessionManager(context);
        this.selectedPosition = selectedPosition;
    }

    public interface OnCarSelectedListener {
        void onCarSelected(String nopol, String merk);
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.recyclerview_car_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        holder.getRadioButton().setChecked(position == selectedPosition);

        View.OnClickListener selectionListener = v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            sessionManager.updateCar(carItems.get(position).getNopol());

            if (listener != null) {
                listener.onCarSelected(carItems.get(position).getNopol(), carItems.get(position).getMerk());
            }
        };

        holder.getItem().setOnClickListener(selectionListener);
        holder.getRadioButton().setOnClickListener(selectionListener);

        holder.getMerkTextView().setText(carItems.get(position).getMerk());
        holder.getTypeTextView().setText(carItems.get(position).getType());
        holder.getYearTextView().setText(carItems.get(position).getYear());

        holder.getEditButton().setOnClickListener(v -> {
            Intent intent = new Intent(context, CarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("nopol", carItems.get(position).getNopol());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carItems.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout item;
        private final RadioButton radioButton;
        private final TextView merkTextView, typeTextView, yearTextView;
        private final ImageView editButton;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            radioButton = itemView.findViewById(R.id.enabled_selected);
            merkTextView = itemView.findViewById(R.id.merk_text);
            typeTextView = itemView.findViewById(R.id.type_text);
            yearTextView = itemView.findViewById(R.id.year_text);
            editButton = itemView.findViewById(R.id.edit_button);
        }

        public RelativeLayout getItem() {
            return item;
        }

        public RadioButton getRadioButton() {
            return radioButton;
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

        public ImageView getEditButton() {
            return editButton;
        }
    }

    public static class CarItem {
        private final String nopol, merk, type, year;

        public CarItem(String nopol, String merk, String type, String year) {
            this.nopol = nopol;
            this.merk = merk;
            this.type = type;
            this.year = year;
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

        public String getYear() {
            return year;
        }
    }
}
