package com.sigit.mechaban.dashboard.customer.service;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.booking.BookingData;
import com.sigit.mechaban.api.model.service.ServiceData;
import com.sigit.mechaban.object.Booking;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {
    private final Booking booking = new Booking();
    private TextView idBookingText, dateText, nameText, addressText, nopolText, merkText, typeText, transmitionText, yearText, priceText;
    private RecyclerView serviceConfirmList;
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
    private ConfirmServiceAdapter confirmServiceAdapter;
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        idBookingText = findViewById(R.id.id_text);
        dateText = findViewById(R.id.date_text);
        nameText = findViewById(R.id.name_text);
        addressText = findViewById(R.id.address_text);
        nopolText = findViewById(R.id.nopol_text);
        merkText = findViewById(R.id.merk_text);
        typeText = findViewById(R.id.type_text);
        transmitionText = findViewById(R.id.transmition_text);
        yearText = findViewById(R.id.year_text);
        priceText = findViewById(R.id.price_text);
        serviceConfirmList = findViewById(R.id.service_list);
        serviceConfirmList.setLayoutManager(new LinearLayoutManager(this));

        booking.setAction("read");
        booking.setId_booking(getIntent().getStringExtra("id_booking"));
        Call<BookingAPI> readBooking = apiInterface.bookingResponse(booking);
        readBooking.enqueue(new Callback<BookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    BookingData bookingData = response.body().getBookingData();
                    idBookingText.setText(bookingData.getId_booking());
                    dateText.setText(bookingData.getTgl_booking());
                    nameText.setText(bookingData.getName());

                    Geocoder geocoder = new Geocoder(ConfirmationActivity.this);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(bookingData.getLatitude(), bookingData.getLongitude(), 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            addressText.setText(address.getAddressLine(0));
                        }
                    } catch (Exception e) {
                        Log.e("ConfirmationActivity", e.toString(), e);
                    }

                    nopolText.setText(bookingData.getNopol());
                    merkText.setText(bookingData.getMerk());
                    typeText.setText(bookingData.getType());
                    transmitionText.setText(bookingData.getTransmition());
                    yearText.setText(bookingData.getYear());

                    List<ServiceData> serviceData = bookingData.getServiceData();
                    confirmServiceAdapter = new ConfirmServiceAdapter(serviceData);
                    serviceConfirmList.setAdapter(confirmServiceAdapter);

                    priceText.setText(formatter.format(bookingData.getTotal_biaya()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.w("ConfirmationActivity", t.toString(), t);
            }
        });

        findViewById(R.id.cancel_button).setOnClickListener(v -> {
            booking.setAction("cancel");
            booking.setId_booking(idBookingText.getText().toString());
            Call<BookingAPI> cancelBooking = apiInterface.bookingResponse(booking);
            cancelBooking.enqueue(new Callback<BookingAPI>() {
                @Override
                public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        finish();
                    } else {
                        Toast.makeText(ConfirmationActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                    Log.e("ConfirmationActivity", t.toString(), t);
                }
            });
        });

        findViewById(R.id.close_button).setOnClickListener(v -> finish());
    }
}