package com.sigit.mechaban.dashboard.customer.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDragHandleView;
import com.google.android.material.button.MaterialButton;
import com.shuhart.stepview.StepView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.car.CarAPI;
import com.sigit.mechaban.api.model.service.ServiceAPI;
import com.sigit.mechaban.api.model.service.ServiceData;
import com.sigit.mechaban.components.DetailBottomSheet;
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.components.ModalBottomSheetTwoButton;
import com.sigit.mechaban.object.Account;
import com.sigit.mechaban.object.Booking;
import com.sigit.mechaban.object.Car;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class ServiceActivity extends AppCompatActivity implements ServiceAdapter.OnItemSelectedListener, ModalBottomSheetTwoButton.ModalBottomSheetListener {
    // Variabel di service activity
    private StepView stepView;
    private ViewFlipper viewFlipper;
    private int currentStep = 0;
    private final List<BottomSheetDialog> activeDialogs = new ArrayList<>();
    // Variabel pilih servis
    private TextView priceTextView, addressTextView;
    private final List<AccordionAdapter.AccordionItem> itemList = new ArrayList<>();
    private AccordionAdapter accordionAdapter;
    private final List<ServiceAdapter.ServiceItem> selectedServices = new ArrayList<>();
    private int totalPrice = 0;
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
    // Variabel pilih lokasi
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1, REQUEST_CHECK_SETTINGS = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker userMarker;
    private String addressDecode;
    private RequestQueue requestQueue;
    private final GeoPoint centerPoint = new GeoPoint(-8.159934162579518, 113.72307806355391);
    private final LoadingDialog loadingDialog = new LoadingDialog(this);
    // Variabel konfirmasi
    private TextView nameTextView, emailTextView, noHPTextView, addressConfirmationTextView, merkTextView, nopolTextView, typeTextView, transmitionTextView, yearTextView, confirmPriceTextView;
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private final Account account = new Account();
    private final Car car = new Car();
    private double latitude, longitude;
    private final List<Booking.BookingService> services = new ArrayList<>();
    private SessionManager sessionManager;
    private final Booking booking = new Booking();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Configuration.getInstance().setUserAgentValue(getPackageName());

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

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backPressed();
            }
        });

        // View pilih service
        RecyclerView serviceListRecyclerView = findViewById(R.id.service_list);
        serviceListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        accordionAdapter = new AccordionAdapter(itemList, this);
        serviceListRecyclerView.setAdapter(accordionAdapter);

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
                                    serviceData.getIdService(),
                                    serviceData.getService(),
                                    serviceData.getPriceService()
                            ));
                        }

                        itemList.add(new AccordionAdapter.AccordionItem(
                                R.drawable.baseline_construction_24,
                                key,
                                serviceItems
                        ));
                    }

                    accordionAdapter.notifyDataSetChanged();

                    if (response.body().getMessage() != null) {
                        Log.e("ServiceActivity", response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceAPI> call, @NonNull Throwable t) {
                Log.e("ServiceActivity", t.toString(), t);
            }
        });

        priceTextView = findViewById(R.id.price);

        findViewById(R.id.detail_service).setOnClickListener(v -> new DetailBottomSheet(totalPrice, selectedServices).show(getSupportFragmentManager(), "ModalBottomSheet"));

        findViewById(R.id.to_location).setOnClickListener(v -> onNextClicked());

        // View pilih lokasi
        checkLocationSettings();
        requestQueue = Volley.newRequestQueue(this);

        if (currentStep == 1) { // Asumsi langkah 1 adalah halaman lokasi
            getDeviceLocation(); // Mulai mendapatkan lokasi perangkat
        }

        mapView = findViewById(R.id.map);
        addressTextView = findViewById(R.id.address);
        mapView.setMultiTouchControls(true);
        mapView.getZoomController().setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER);

        findViewById(R.id.fab_center_location).setOnClickListener(v -> getDeviceLocation());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getDeviceLocation();
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
        mapView.getController().setZoom(17.5); // Setel tingkat zoom awal
        mapView.getController().setCenter(centerPoint);

        findViewById(R.id.to_confirmation).setOnClickListener(v -> validateAndAddMarker(new GeoPoint(latitude, longitude)));

        // View konfirmasi
        sessionManager = new SessionManager(this);
        String nopol = sessionManager.getUserDetail().get("nopol");
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));

        DateTimeFormatter formatterId = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        nameTextView = findViewById(R.id.tv_name);
        emailTextView = findViewById(R.id.tv_email);
        noHPTextView = findViewById(R.id.tv_no_hp);
        addressConfirmationTextView = findViewById(R.id.tv_address);

        account.setAction("read");
        account.setEmail(sessionManager.getUserDetail().get("email"));
        Call<AccountAPI> readAccount = apiInterface.accountResponse(account);
        readAccount.enqueue(new Callback<AccountAPI>() {
            @Override
            public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    nameTextView.setText(response.body().getAccountData().getName());
                    emailTextView.setText(response.body().getAccountData().getEmail());
                    noHPTextView.setText(response.body().getAccountData().getNoHp());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                Log.e("Confirmation", t.toString(), t);
            }
        });

        merkTextView = findViewById(R.id.tv_merk);
        nopolTextView = findViewById(R.id.tv_nopol);
        typeTextView = findViewById(R.id.tv_type);
        transmitionTextView = findViewById(R.id.tv_transmition);
        yearTextView = findViewById(R.id.tv_year);

        car.setAction("detail");
        car.setNopol(nopol);
        Call<CarAPI> readDetailCar = apiInterface.carResponse(car);
        readDetailCar.enqueue(new Callback<CarAPI>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    merkTextView.setText(response.body().getCarData().getMerk());
                    nopolTextView.setText(response.body().getCarData().getNopol());
                    typeTextView.setText(response.body().getCarData().getType());
                    String transmitionResponse = response.body().getCarData().getTransmition();
                    transmitionTextView.setText(transmitionResponse.substring(0, 1).toUpperCase() + transmitionResponse.substring(1));
                    yearTextView.setText(response.body().getCarData().getYear());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                Log.e("Confirmation", t.toString(), t);
            }
        });

        confirmPriceTextView = findViewById(R.id.price_text);

        findViewById(R.id.booking_button).setOnClickListener(v -> {
            booking.setAction("create");
            booking.setId_booking(now.format(formatterId) + sessionManager.getUserDetail().get("nopol"));
            booking.setTgl_booking(now.format(formatterDate));
            booking.setNopol(nopol);
            booking.setLatitude(latitude);
            booking.setLongitude(longitude);
            booking.setServices(services);
            Call<BookingAPI> createBooking = apiInterface.bookingResponse(booking);
            createBooking.enqueue(new Callback<BookingAPI>() {
                @Override
                public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        sessionManager.deleteCar();
                        Intent intent = new Intent(ServiceActivity.this, ConfirmationActivity.class);
                        intent.putExtra("id_booking", booking.getId_booking());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ServiceActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                    Log.e("Booking", t.toString(), t);
                }
            });
        });
    }

    private void onNextClicked() {
        if (currentStep == 1) {
            currentStep++;
            stepView.go(currentStep, true);
            viewFlipper.showNext();

            addressConfirmationTextView.setText(addressDecode);

            DetailServiceAdapter detailServiceAdapter = new DetailServiceAdapter(selectedServices);
            RecyclerView serviceSelected = findViewById(R.id.service_confirmation);
            serviceSelected.setLayoutManager(new LinearLayoutManager(this));
            serviceSelected.setAdapter(detailServiceAdapter);
        } else if (currentStep < stepView.getStepCount() - 1) {
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
    public void onItemSelected(String id, String service, int price, boolean isSelected) {
        if (isSelected) {
            totalPrice += price;
            selectedServices.add(new ServiceAdapter.ServiceItem(id, service, price));
            services.add(new Booking.BookingService(id));
            if (totalPrice != 0) findViewById(R.id.keterangan).setVisibility(View.VISIBLE);
        } else {
            totalPrice -= price;
            selectedServices.removeIf(item -> item.getService().equals(service));
            services.removeIf(item -> item.getId_data_servis().equals(id));
            if (totalPrice == 0) findViewById(R.id.keterangan).setVisibility(View.INVISIBLE);
        }
        priceTextView.setText(formatter.format(totalPrice));
        confirmPriceTextView.setText(formatter.format(totalPrice));
    }

    private void backPressed() {
        if (currentStep == 1 || currentStep == 2) {
            currentStep--;
            stepView.go(currentStep, true);
            viewFlipper.showPrevious();
        } else if (currentStep == 0) {
            finish();
        }
    }

    private void getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                        // Tambahkan marker pada lokasi perangkat
                        addMarkerAtLocation(currentLocation);

                        // Pusatkan peta pada lokasi perangkat
                        mapView.getController().setCenter(currentLocation);
                        mapView.getController().setZoom(17.5); // Atur zoom
                    } else {
                        Log.e("ServiceActivity", "Lokasi perangkat tidak ditemukan.");
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }

            requestCurrentLocation();
        } catch (SecurityException e) {
            Log.e("ServiceActivity", "Izin lokasi tidak diberikan.", e);
        }
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
            mapView.getOverlays().remove(userMarker);
        }

        // Buat marker baru di lokasi yang dipilih
        userMarker = new Marker(mapView);
        userMarker.setPosition(location);
        userMarker.setTitle("Lokasi Pilihan");
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM); // Mengatur posisi anchor
        mapView.getOverlays().add(userMarker);
        mapView.invalidate();

        // Konversi koordinat ke alamat menggunakan Geocoder
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                addressDecode = address.getAddressLine(0); // Mendapatkan baris alamat pertama
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                addressTextView.setText(addressDecode);
            } else {
                Toast.makeText(this, "Alamat tidak ditemukan untuk lokasi ini", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("ServiceActivity", e.toString(), e);
            Toast.makeText(this, "Tidak terhubung dengan internet", Toast.LENGTH_LONG).show();
        }

        // Pindahkan peta ke lokasi marker baru
        mapView.getController().animateTo(location);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
                for (BottomSheetDialog dialog : activeDialogs) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                activeDialogs.clear();
            } else if (resultCode == RESULT_CANCELED) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
                if (activeDialogs.isEmpty()) {
                    activeDialogs.add(bottomSheetDialog);
                    showBottomSheetDialog(bottomSheetDialog);
                }
            }
        }
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();

        // Buat LocationSettingsRequest menggunakan LocationRequest
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            getDeviceLocation(); // Mulai mendapatkan lokasi
        });

        // Periksa pengaturan lokasi
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
                        ServiceActivity.this).show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
    }

    private void requestCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
                        .setWaitForAccurateLocation(true) // Tunggu lokasi yang akurat
                        .setMaxUpdates(1) // Batasi hanya satu pembaruan lokasi
                        .setMinUpdateIntervalMillis(5000L) // Interval pembaruan minimum
                        .build();

                LocationCallback locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        if (locationResult.getLastLocation() != null) {
                            Location location = locationResult.getLastLocation();
                            GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

                            // Tambahkan marker pada lokasi terkini
                            addMarkerAtLocation(currentLocation);

                            // Pusatkan peta pada lokasi terkini
                            mapView.getController().setCenter(currentLocation);
                            Log.i("ServiceActivity", "Lokasi terkini diperbarui.");
                        }
                    }
                };

                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } catch (SecurityException e) {
            Log.e("ServiceActivity", "Izin lokasi tidak diberikan.", e);
        }
    }

    @Override
    public void positiveButtonBottomSheet() {
        checkLocationSettings();
    }

    @Override
    public void negativeButtonBottomSheet() {
        finish();
    }

    private void validateAndAddMarker(GeoPoint destination) {
        loadingDialog.startLoadingDialog();
        String osrmUrl = "https://router.project-osrm.org/route/v1/driving/"
                + centerPoint.getLongitude() + "," + centerPoint.getLatitude() + ";"
                + destination.getLongitude() + "," + destination.getLatitude()
                + "?overview=false&geometries=geojson";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, osrmUrl, null,
                response -> {
                    try {
                        JSONArray routes = response.getJSONArray("routes");
                        if (routes.length() > 0) {
                            JSONObject route = routes.getJSONObject(0);
                            double distanceInMeters = route.getDouble("distance");
                            if (distanceInMeters > 50000) {
                                MotionToast.Companion.createColorToast(this,
                                        "Ups, kelewatan batas",
                                        "Lokasimu sudah melebihi jangkauan sejauh 50 km",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(this,R.font.montserrat_semibold));
                            } else {
                                loadingDialog.dismissDialog();
                                onNextClicked();
                            }
                        }
                    } catch (JSONException e) {
                        loadingDialog.dismissDialog();
                        Log.e("Error OSRM", e.toString(), e);
                        Toast.makeText(this, "Gagal memproses data OSRM", Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismissDialog();
                },
                error -> {
                    loadingDialog.dismissDialog();
                    Toast.makeText(this, "Gagal menghubungi OSRM: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("SetTextI18n")
    private void showBottomSheetDialog(BottomSheetDialog bottomSheetDialog) {
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_modal_two_button, null);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setDraggable(false);
                behavior.setHideable(false);
            }
        });

        BottomSheetDragHandleView dragHandleView = dialogView.findViewById(R.id.drag_handle);
        ImageView imageView = dialogView.findViewById(R.id.photo);
        TextView title = dialogView.findViewById(R.id.title);
        TextView description = dialogView.findViewById(R.id.description);
        MaterialButton confirmButton = dialogView.findViewById(R.id.positive_button);
        MaterialButton cancelButton = dialogView.findViewById(R.id.negative_button);

        dragHandleView.setVisibility(View.GONE);

        imageView.setImageResource(R.drawable.cancel);

        title.setText("Tidak Bisa Menemukanmu");

        description.setText("Aktifkan lokasimu agar kami bisa menuju ke lokasimu dengan akurat");

        confirmButton.setText("Aktifkan");
        confirmButton.setOnClickListener(v -> checkLocationSettings());
        confirmButton.setTextColor(ContextCompat.getColorStateList(this, R.color.md_theme_background));

        cancelButton.setText("Gak Jadi Deh");
        cancelButton.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            finish();
        });
        cancelButton.setBackgroundColor(Color.TRANSPARENT);
        cancelButton.setStrokeColor(ContextCompat.getColorStateList(this, R.color.md_theme_error));
        cancelButton.setStrokeWidth(4);
        cancelButton.setRippleColor(ContextCompat.getColorStateList(this, R.color.md_theme_errorContainer));
        cancelButton.setTextColor(ContextCompat.getColorStateList(this, R.color.md_theme_error));

        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
    }
}