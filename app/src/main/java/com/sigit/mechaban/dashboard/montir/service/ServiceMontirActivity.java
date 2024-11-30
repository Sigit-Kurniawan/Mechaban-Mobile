package com.sigit.mechaban.dashboard.montir.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.booking.BookingData;
import com.sigit.mechaban.api.model.service.ServiceData;
import com.sigit.mechaban.components.ModalBottomSheetTwoButton;
import com.sigit.mechaban.dashboard.customer.service.DetailServiceAdapter;
import com.sigit.mechaban.dashboard.customer.service.ServiceAdapter;
import com.sigit.mechaban.object.Booking;
import com.sigit.mechaban.sessionmanager.SessionManager;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceMontirActivity extends AppCompatActivity implements ModalBottomSheetTwoButton.ModalBottomSheetListener{
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1, REQUEST_CHECK_SETTINGS = 1001;
    private final GeoPoint centerPoint = new GeoPoint(-8.159934162579518, 113.72307806355391);
    private final Booking booking = new Booking();
    private final List<BookingAdapter.BookingItem> bookingItemList = new ArrayList<>();
    private final List<ServiceAdapter.ServiceItem> serviceItems = new ArrayList<>();
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private Marker selectedMarker;
    private LinearLayout bookingLayout, navigation;
    private ConstraintLayout confirmLayout;
    private TextView nameText, emailText, noHpText, merkText, nopolText, typeText, transmitionText, yearText, priceText;
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
    private Button takeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_montir);

        findViewById(R.id.iconButton).setOnClickListener(v -> finish());

        mapView = findViewById(R.id.mapview);
        mapView.getController().setCenter(centerPoint);
        mapView.getController().setZoom(17.5);
        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER);
        checkLocationSettings();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            checkLocationSettings();
        }

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setMaxHeight(900);

        bookingLayout = findViewById(R.id.booking_layout);
        confirmLayout = findViewById(R.id.confirm_layout);
        navigation = findViewById(R.id.navigation);
        RecyclerView recyclerView = findViewById(R.id.booking_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookingAdapter bookingAdapter = new BookingAdapter(this, bookingItemList);
        recyclerView.setAdapter(bookingAdapter);

        bookingAdapter.setOnItemClickListener(this::showConfirmLayout);

        findViewById(R.id.back_button).setOnClickListener(v -> {
            if (confirmLayout.getVisibility() == View.VISIBLE) {
                confirmLayout.setVisibility(View.GONE);
                bookingLayout.setVisibility(View.VISIBLE);
                navigation.setVisibility(View.GONE);

                if (selectedMarker != null) {
                    mapView.getOverlays().remove(selectedMarker);
                    selectedMarker = null;
                    mapView.invalidate();
                }
            }
        });

        nameText = findViewById(R.id.tv_name);
        emailText = findViewById(R.id.tv_email);
        noHpText = findViewById(R.id.tv_no_hp);

        merkText = findViewById(R.id.tv_merk);
        nopolText = findViewById(R.id.tv_nopol);
        typeText = findViewById(R.id.tv_type);
        transmitionText = findViewById(R.id.tv_transmition);
        yearText = findViewById(R.id.tv_year);

        priceText = findViewById(R.id.price_text);

        booking.setAction("all");
        Call<BookingAPI> readBooking = apiInterface.bookingResponse(booking);
        readBooking.enqueue(new Callback<BookingAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null & response.isSuccessful() && response.body().getCode() == 200) {
                    for (BookingData bookingData : response.body().getBookingDataList()) {
                        bookingItemList.add(new BookingAdapter.BookingItem(
                                bookingData.getId_booking(),
                                bookingData.getNopol(),
                                bookingData.getMerk(),
                                bookingData.getType(),
                                bookingData.getTransmition(),
                                bookingData.getYear(),
                                bookingData.getName(),
                                bookingData.getEmail(),
                                bookingData.getNo_hp(),
                                bookingData.getLatitude(),
                                bookingData.getLongitude(),
                                bookingData.getTotal_biaya(),
                                bookingData.getServiceData()));
                    }
                    bookingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.e("ServiceMontirActivity", t.toString(), t);
            }
        });

        takeButton = findViewById(R.id.take_button);
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            mapView.getController().setCenter(centerPoint);
            mapView.getController().setZoom(17.5);
        });

        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    Log.e("ServiceActivity", "Tidak dapat menyelesaikan pengaturan lokasi.", sendEx);
                }
            } else {
                new ModalBottomSheetTwoButton(
                        R.drawable.no_location,
                        "Tidak Bisa Menemukanmu",
                        "Aktifkan lokasimu",
                        "Aktifkan",
                        "Tidak jadi",
                        ServiceMontirActivity.this).show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
    }

    @Override
    public void positiveButtonBottomSheet() {
        checkLocationSettings();
    }

    @Override
    public void negativeButtonBottomSheet() {
        finish();
    }

    private void showConfirmLayout(BookingAdapter.BookingItem item) {
        bookingLayout.setVisibility(View.GONE);
        confirmLayout.setVisibility(View.VISIBLE);
        navigation.setVisibility(View.VISIBLE);

        if (selectedMarker != null) {
            mapView.getOverlays().remove(selectedMarker);
        }

        GeoPoint location = new GeoPoint(item.getLatitude(), item.getLongitude());
        selectedMarker = new Marker(mapView);
        selectedMarker.setPosition(location);
        mapView.getOverlays().add(selectedMarker);
        mapView.getController().setCenter(location);
        mapView.getController().setZoom(17.5);
        mapView.getController().animateTo(location);
        mapView.invalidate();

        nameText.setText(item.getName());
        emailText.setText(item.getEmail());
        noHpText.setText(item.getNoHP());

        merkText.setText(item.getMerk());
        nopolText.setText(item.getNopol());
        typeText.setText(item.getType());
        transmitionText.setText(item.getTransmition());
        yearText.setText(item.getYear());

        RecyclerView view = findViewById(R.id.service_confirmation);
        view.setLayoutManager(new LinearLayoutManager(this));
        for (ServiceData service : item.getBookingItemServices()) {
            ServiceAdapter.ServiceItem serviceItem = new ServiceAdapter.ServiceItem(
                    service.getIdService(),
                    service.getService(),
                    service.getPriceService()
            );
            serviceItems.add(serviceItem);
        }
        DetailServiceAdapter detailServiceAdapter = new DetailServiceAdapter(serviceItems);
        view.setAdapter(detailServiceAdapter);
        priceText.setText(formatter.format(item.getPrice()));

        takeButton.setOnClickListener(v -> {
            SessionManager sessionManager = new SessionManager(this);
            booking.setAction("diterima");
            booking.setId_booking(item.getId());
            booking.setEmail(sessionManager.getUserDetail().get("email"));
            Call<BookingAPI> takeBooking = apiInterface.bookingResponse(booking);
            takeBooking.enqueue(new Callback<BookingAPI>() {
                @Override
                public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        Intent intent = new Intent(ServiceMontirActivity.this, ConfirmationMontirActivity.class);
                        intent.putExtra("id_booking", item.getId());
                        startActivity(intent);
                    } else {
                        Toast.makeText(ServiceMontirActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                    Log.e("ServiceMontirActivity", t.toString(), t);
                }
            });
        });
    }
}