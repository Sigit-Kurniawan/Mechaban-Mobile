package com.sigit.mechaban.dashboard.montir.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.booking.BookingData;
import com.sigit.mechaban.api.model.montir.MontirData;
import com.sigit.mechaban.api.model.service.ServiceData;
import com.sigit.mechaban.dashboard.customer.service.ConfirmServiceAdapter;
import com.sigit.mechaban.dashboard.montir.listmontir.MontirConfirmationAdapter;
import com.sigit.mechaban.object.Booking;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ConfirmationMontirActivity extends AppCompatActivity {
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private final Booking booking = new Booking();
    private TextView idText, dateText, nameText, addressText, nopolText, merkText, typeText, transmitionText, yearText, priceText, leaderText, titleAnggota;
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
    private RecyclerView serviceList, anggotaMontirList;
    private ConfirmServiceAdapter confirmServiceAdapter;
    private final List<MontirConfirmationAdapter.MontirConfirmationItem> montirConfirmationItems = new ArrayList<>();
    private String id, status;
    private Button processButton, doneButton, locationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_montir);

        processButton = findViewById(R.id.process_button);
        doneButton = findViewById(R.id.done_button);
        locationButton = findViewById(R.id.location_button);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("latest", false)) {
            processButton.setVisibility(View.GONE);
        }

        idText = findViewById(R.id.id_text);
        dateText = findViewById(R.id.date_text);
        nameText = findViewById(R.id.name_text);
        addressText = findViewById(R.id.address_text);

        nopolText = findViewById(R.id.nopol_text);
        merkText = findViewById(R.id.merk_text);
        typeText = findViewById(R.id.type_text);
        transmitionText = findViewById(R.id.transmition_text);
        yearText = findViewById(R.id.year_text);

        serviceList = findViewById(R.id.service_list);
        serviceList.setLayoutManager(new LinearLayoutManager(this));

        priceText = findViewById(R.id.price_text);

        leaderText = findViewById(R.id.leader_text);

        titleAnggota = findViewById(R.id.title_anggota);
        anggotaMontirList = findViewById(R.id.montir_list);
        anggotaMontirList.setLayoutManager(new LinearLayoutManager(this));

        booking.setAction("read");
        booking.setId_booking(intent.getStringExtra("id_booking"));
        Call<BookingAPI> takeBooking = apiInterface.bookingResponse(booking);
        takeBooking.enqueue(new Callback<BookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    BookingData bookingData = response.body().getBookingData();
                    id = bookingData.getId_booking();
                    status = bookingData.getStatus();

                    if (status.equals("dikerjakan")) {
                        processButton.setVisibility(View.GONE);
                        doneButton.setVisibility(View.VISIBLE);
                    } else if (status.equals("selesai")) {
                        processButton.setVisibility(View.GONE);
                        doneButton.setVisibility(View.GONE);
                        locationButton.setVisibility(View.GONE);
                    }

                    idText.setText(id);
                    dateText.setText(bookingData.getTgl_booking());
                    nameText.setText(bookingData.getName());

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(bookingData.getLatitude(), bookingData.getLongitude(), 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            addressText.setText(address.getAddressLine(0));
                        }
                    } catch (Exception e) {
                        Log.e("ConfirmationMontirActivity", e.toString(), e);
                    }

                    nopolText.setText(bookingData.getNopol());
                    merkText.setText(bookingData.getMerk());
                    typeText.setText(bookingData.getType());
                    transmitionText.setText(bookingData.getTransmition());
                    yearText.setText(bookingData.getYear());

                    List<ServiceData> serviceData = bookingData.getServiceData();
                    confirmServiceAdapter = new ConfirmServiceAdapter(serviceData);
                    serviceList.setAdapter(confirmServiceAdapter);

                    priceText.setText(formatter.format(bookingData.getTotal_biaya()));

                    leaderText.setText(bookingData.getKetua_montir());
                    for (MontirData montirData : bookingData.getMontirData()) {
                        if (montirData.getNamaAnggota() == null) {
                            titleAnggota.setVisibility(View.GONE);
                            anggotaMontirList.setVisibility(View.GONE);
                        }
                        montirConfirmationItems.add(new MontirConfirmationAdapter.MontirConfirmationItem(montirData.getNamaAnggota()));
                    }
                    MontirConfirmationAdapter montirConfirmationAdapter = new MontirConfirmationAdapter(montirConfirmationItems);
                    anggotaMontirList.setAdapter(montirConfirmationAdapter);

                    locationButton.setOnClickListener(v -> {
                        String mapsUrl = "https://www.google.com/maps/dir/?api=1&destination="
                                + bookingData.getLatitude() + "," + bookingData.getLongitude();

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl));
                        startActivity(browserIntent);
                    });
                } else {
                    Toast.makeText(ConfirmationMontirActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.e("ConfirmationMontirActivity", t.toString(), t);
            }
        });

        processButton.setOnClickListener(v -> setStatus());

        doneButton.setOnClickListener(v -> setStatus());

        findViewById(R.id.close_button).setOnClickListener(v -> finish());
    }

    @SuppressLint("SetTextI18n")
    private void setStatus() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_modal_two_button, null);
        bottomSheetDialog.setContentView(dialogView);

        ImageView imageView = dialogView.findViewById(R.id.photo);
        TextView title = dialogView.findViewById(R.id.title);
        TextView description = dialogView.findViewById(R.id.description);
        MaterialButton confirmButton = dialogView.findViewById(R.id.positive_button);
        MaterialButton cancelButton = dialogView.findViewById(R.id.negative_button);

        imageView.setImageResource(R.drawable.done);

        String titleBottom = "", descBottom = "", buttonBottom = "";
        if (status.equals("diterima")) {
            titleBottom = "Booking Sudah Diterima?";
            descBottom = "Booking yang sudah diterima tidak bisa dikembalikan.";
            buttonBottom = "Terima";
        } else if (status.equals("dikerjakan")) {
            titleBottom = "Booking Sudah Selesai?";
            descBottom = "Booking yang sudah selesai tidak bisa dikembalikan.";
            buttonBottom = "Selesai";
        }

        title.setText(titleBottom);

        description.setText(descBottom);

        confirmButton.setText(buttonBottom);
        confirmButton.setOnClickListener(v -> {
            booking.setAction("status");
            booking.setId_booking(id);
            booking.setStatus(status);
            Call<BookingAPI> setStatus = apiInterface.bookingResponse(booking);
            setStatus.enqueue(new Callback<BookingAPI>() {
                @Override
                public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        String title = "", message = "";
                        if (status.equals("diterima")) {
                            title = "Booking Telah Dijemput";
                            message = "Selamat Mengerjakan";
                        } else if (status.equals("dikerjakan")) {
                            title = "Booking Telah Selesai";
                            message = "Selamat Telah Dikerjakan";
                        }
                        MotionToast.Companion.createColorToast(ConfirmationMontirActivity.this,
                                title,
                                message,
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(ConfirmationMontirActivity.this, R.font.montserrat_semibold));
                        finish();
                    } else {
                        Toast.makeText(ConfirmationMontirActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                    Log.e("ConfirmationMontirActivity", t.toString(), t);
                }
            });
        });
        confirmButton.setTextColor(ContextCompat.getColorStateList(this, R.color.md_theme_background));

        cancelButton.setText("Batal");
        cancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());
        cancelButton.setBackgroundColor(Color.TRANSPARENT);
        cancelButton.setStrokeWidth(4);
        cancelButton.setTextColor(ContextCompat.getColorStateList(this, R.color.md_theme_primary));
        cancelButton.setStrokeColor(ContextCompat.getColorStateList(this, R.color.md_theme_primary));
        cancelButton.setRippleColor(ContextCompat.getColorStateList(this, R.color.md_theme_primaryContainer));
        cancelButton.setTextColor(ContextCompat.getColorStateList(this, R.color.md_theme_primary));

        bottomSheetDialog.show();
    }
}