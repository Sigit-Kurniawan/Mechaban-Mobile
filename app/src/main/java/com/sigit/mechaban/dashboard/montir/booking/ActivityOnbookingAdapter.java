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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.dashboard.montir.service.ConfirmationMontirActivity;
import com.sigit.mechaban.object.Booking;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ActivityOnbookingAdapter extends RecyclerView.Adapter<ActivityOnbookingAdapter.ActivityOnbookingViewHolder>{
    private final Context context;
    private List<ActivityOnbookingItem> activityOnbookingItems;
    private final Booking booking = new Booking();
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private final OnBookingStatusChangedListener callback;

    public ActivityOnbookingAdapter(Context context, List<ActivityOnbookingItem> activityOnbookingItems, OnBookingStatusChangedListener callback) {
        this.context = context;
        this.activityOnbookingItems = activityOnbookingItems;
        this.callback = callback;
    }

    public interface OnBookingStatusChangedListener {
        void onBookingStatusChanged(ActivityOnbookingItem updatedItem, int position);
    }

    @NonNull
    @Override
    public ActivityOnbookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActivityOnbookingViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.recyclerview_activity_onbooking, parent, false));
    }

    @SuppressLint({"QueryPermissionsNeeded", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ActivityOnbookingViewHolder holder, int position) {
        ActivityOnbookingItem activityOnbookingItem = activityOnbookingItems.get(position);
        holder.getDateTextView().setText(activityOnbookingItem.getDate());
        holder.getNopolTextView().setText(activityOnbookingItem.getNopol());
        holder.getMerkTextView().setText(activityOnbookingItem.getMerk());
        holder.getTypeTextView().setText(activityOnbookingItem.getType());
        String status = activityOnbookingItem.getStatus();
        if (activityOnbookingItem.getRole().equals("ketua")) {
            if (status.equals("selesai") || status.equals("batal")) {
                holder.getLocationButton().setVisibility(View.GONE);
                holder.getDoneButton().setVisibility(View.GONE);
            } else {
                holder.getLocationButton().setVisibility(View.VISIBLE);
                holder.getDoneButton().setVisibility(View.VISIBLE);
            }
        } else if (activityOnbookingItem.getRole().equals("anggota")) {
            holder.getDoneButton().setVisibility(View.GONE);
            if (status.equals("selesai")) {
                holder.getLocationButton().setVisibility(View.GONE);
            }
        }
        if (status.equals("diterima")) {
            holder.getStatusTextView().setText("Proses Penjemputan");
        } else {
            holder.getStatusTextView().setText(status.substring(0, 1).toUpperCase() + status.substring(1));
        }
        holder.getDoneButton().setOnClickListener(v -> showBottomSheetDialog(activityOnbookingItems.get(position), position));
        holder.getDetailButton().setOnClickListener(v -> {
            Intent intent = new Intent(context, ConfirmationMontirActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id_booking", activityOnbookingItem.getId());
            context.startActivity(intent);
        });
        holder.getLocationButton().setOnClickListener(v -> {
            String mapsUrl = "https://www.google.com/maps/dir/?api=1&destination="
                    + activityOnbookingItem.getLatitude() + "," + activityOnbookingItem.getLongitude();

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return activityOnbookingItems.size();
    }

    public static class ActivityOnbookingViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView, nopolTextView, merkTextView, typeTextView, statusTextView;
        private final Button detailButton, doneButton, locationButton;

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
    }

    public static class ActivityOnbookingItem {
        private final String id, date, nopol, merk, type, role;
        private String status;
        private final double latitude, longitude;

        public ActivityOnbookingItem(String id, String date, String nopol, String merk, String type, String role, String status, double latitude, double longitude) {
            this.id = id;
            this.date = date;
            this.nopol = nopol;
            this.merk = merk;
            this.type = type;
            this.role = role;
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

        public String getRole() {
            return role;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showBottomSheetDialog(ActivityOnbookingItem activityOnbookingItem, int position) {
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

        String titleBottom = "", descBottom = "", buttonBottom = "";
        if (activityOnbookingItem.getStatus().equals("diterima")) {
            titleBottom = "Booking Sudah Dijemput?";
            descBottom = "Booking yang sudah dijemput tidak bisa dikembalikan.";
            buttonBottom = "Jemput";
        } else if (activityOnbookingItem.getStatus().equals("dikerjakan")) {
            titleBottom = "Booking Sudah Selesai?";
            descBottom = "Booking yang sudah selesai tidak bisa dikembalikan.";
            buttonBottom = "Selesai";
        }

        title.setText(titleBottom);

        description.setText(descBottom);

        confirmButton.setText(buttonBottom);
        confirmButton.setOnClickListener(v -> {
            booking.setAction("status");
            booking.setId_booking(activityOnbookingItem.getId());
            booking.setStatus(activityOnbookingItem.getStatus());
            Call<BookingAPI> setStatus = apiInterface.bookingResponse(booking);
            setStatus.enqueue(new Callback<BookingAPI>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        String title = "", message = "";
                        if (activityOnbookingItem.getStatus().equals("diterima")) {
                            title = "Booking Telah Dijemput";
                            message = "Selamat Mengerjakan";
                            activityOnbookingItem.setStatus("dikerjakan");
                        } else if (activityOnbookingItem.getStatus().equals("dikerjakan")) {
                            title = "Booking Telah Selesai";
                            message = "Selamat Telah Dikerjakan";
                            activityOnbookingItem.setStatus("selesai");
                        }
                        MotionToast.Companion.createColorToast(activity,
                                title,
                                message,
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(context, R.font.montserrat_semibold));
                        notifyItemChanged(position);
                        callback.onBookingStatusChanged(activityOnbookingItem, position);
                        bottomSheetDialog.dismiss();
                    } else {
                        Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                    Log.e("ConfirmationMontirActivity", t.toString(), t);
                }
            });

        });
        confirmButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_background));

        cancelButton.setText("Batal");
        cancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());
        cancelButton.setBackgroundColor(Color.TRANSPARENT);
        cancelButton.setStrokeWidth(4);
        cancelButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
        cancelButton.setStrokeColor(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
        cancelButton.setRippleColor(ContextCompat.getColorStateList(context, R.color.md_theme_primaryContainer));
        cancelButton.setTextColor(ContextCompat.getColorStateList(context, R.color.md_theme_primary));

        bottomSheetDialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<ActivityOnbookingItem> filteredList) {
        this.activityOnbookingItems = filteredList;
        notifyDataSetChanged();
    }
}
