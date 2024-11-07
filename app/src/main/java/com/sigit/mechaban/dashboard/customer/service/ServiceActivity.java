package com.sigit.mechaban.dashboard.customer.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.shuhart.stepview.StepView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.service.ServiceAPI;
import com.sigit.mechaban.api.model.service.ServiceData;
import com.sigit.mechaban.components.DetailBottomSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class ServiceActivity extends AppCompatActivity implements ServiceAdapter.OnItemSelectedListener {
    private TextView priceTextView;
    private int totalPrice = 0, currentStep = 0;
    private final List<ServiceAdapter.ServiceItem> selectedServices = new ArrayList<>();
    private StepView stepView;
    private ViewFlipper viewFlipper;
    private final List<AccordionAdapter.AccordionItem> itemList = new ArrayList<>();
    private AccordionAdapter accordionAdapter;
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker userMarker;
    private static final int REQUEST_CHECK_SETTINGS = 1001;
    private boolean isLocationSettingsEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_service);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        stepView = findViewById(R.id.step_view);
        stepView.getState()
                .steps(new ArrayList<String>() {{
                    add("Pilih Servis");
                    add("Pilih Lokasi");
                    add("Konfirmasi");
                }})
                .commit();

        viewFlipper = findViewById(R.id.view_flipper);

        // View pilih service
        RecyclerView serviceListRecyclerView = findViewById(R.id.service_list);
        serviceListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        accordionAdapter = new AccordionAdapter(itemList, this);
        serviceListRecyclerView.setAdapter(accordionAdapter);

        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ServiceAPI> serviceCall = apiInterface.serviceResponse();
        serviceCall.enqueue(new Callback<ServiceAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ServiceAPI> call, @NonNull Response<ServiceAPI> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Map<String, List<ServiceData>> serviceMap = response.body().getComponent();

                    for (String key : serviceMap.keySet()) {
                        List<ServiceAdapter.ServiceItem> serviceItems = new ArrayList<>();
                        for (ServiceData serviceData : Objects.requireNonNull(serviceMap.get(key))) {
                            serviceItems.add(new ServiceAdapter.ServiceItem(
                                    serviceData.getService(),
                                    serviceData.getPriceService()
                            ));
                        }

                        itemList.add(new AccordionAdapter.AccordionItem(
                                R.drawable.oil,
                                key,
                                serviceItems
                        ));
                    }

                    accordionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceAPI> call, @NonNull Throwable t) {
                Log.e("ServiceActivity", t.toString(), t);
            }
        });

        priceTextView = findViewById(R.id.price);

        findViewById(R.id.detail_service).setOnClickListener(v -> new DetailBottomSheet(totalPrice, selectedServices).show(getSupportFragmentManager(), "ModalBottomSheet"));

        findViewById(R.id.next_button).setOnClickListener(v -> onNextClicked());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backPressed();
            }
        });

        // View pilih lokasi
        checkLocationSettings();

        mapView = findViewById(R.id.map);
//        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                addMarkerAtLocation(p);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });
        mapView.getOverlays().add(mapEventsOverlay);

//        GeoPoint startPoint = new GeoPoint(-6.1751, 106.8650); // Jakarta, sebagai contoh
//        mapView.getController().setZoom(15.0); // Setel tingkat zoom awal
//        mapView.getController().setCenter(startPoint);
    }

    private void onNextClicked() {
        if (currentStep < stepView.getStepCount() - 1) {
            currentStep++;
            stepView.go(currentStep, true);
            viewFlipper.showNext();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String service, int price, boolean isSelected) {
        if (isSelected) {
            totalPrice += price;
            selectedServices.add(new ServiceAdapter.ServiceItem(service, price));
        } else {
            totalPrice -= price;
            selectedServices.removeIf(item -> item.getService().equals(service));
        }
        priceTextView.setText(String.valueOf(totalPrice));
    }

    private void backPressed() {
        if (currentStep == 1) {
            currentStep--;
            stepView.go(currentStep, true);
            viewFlipper.showPrevious();
        } else if (currentStep == 0) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLocationSettingsEnabled) {
            mapView.onResume();
            getDeviceLocation(); // Mendapatkan lokasi terkini dan menampilkan di peta
            isLocationSettingsEnabled = false; // Reset agar tidak terus-menerus reload map
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        // Set posisi awal peta ke lokasi perangkat
                        GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                        mapView.getController().setZoom(15.0);
                        mapView.getController().setCenter(userLocation);

                        // Tambahkan marker di lokasi perangkat
                        addMarkerAtLocation(userLocation);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation();
            }
        }
    }

    private void addMarkerAtLocation(GeoPoint location) {
        if (userMarker != null) {
            mapView.getOverlays().remove(userMarker); // Hapus marker sebelumnya
        }

        // Buat marker baru di lokasi yang dipilih
        userMarker = new Marker(mapView);
        userMarker.setPosition(location);
        userMarker.setTitle("Lokasi Pilihan");
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); // Mengatur posisi anchor
        mapView.getOverlays().add(userMarker);

        // Simpan lokasi marker yang dipilih pengguna

        // Tampilkan lokasi dalam Toast
        Toast.makeText(this, "Lokasi Marker: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();

        // Pindahkan peta ke lokasi marker baru
        mapView.getController().animateTo(location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                isLocationSettingsEnabled = true;
                // Pengguna mengaktifkan layanan lokasi
                Toast.makeText(this, "Layanan lokasi diaktifkan", Toast.LENGTH_SHORT).show();
                getDeviceLocation();
            } else {
                // Pengguna menolak mengaktifkan layanan lokasi
                Toast.makeText(this, "Layanan lokasi tidak diaktifkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        // Buat LocationSettingsRequest menggunakan LocationRequest
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);

        // Periksa pengaturan lokasi
        client.checkLocationSettings(builder.build())
                .addOnSuccessListener(locationSettingsResponse -> {
                    // Pengaturan lokasi sudah sesuai, Anda bisa memulai mendapatkan lokasi di sini
                    Toast.makeText(ServiceActivity.this, "Pengaturan lokasi aktif", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        // Pengaturan lokasi tidak sesuai, tampilkan dialog bawaan untuk mengaktifkannya
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(ServiceActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Tangani kesalahan
                            sendEx.printStackTrace();
                        }
                    } else {
                        // Tangani kegagalan lainnya
                        Toast.makeText(ServiceActivity.this, "Tidak bisa mengaktifkan lokasi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}