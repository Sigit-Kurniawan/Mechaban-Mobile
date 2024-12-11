package com.sigit.mechaban.dashboard.customer.service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.sigit.mechaban.components.ModalBottomSheet;
import com.sigit.mechaban.dashboard.montir.listmontir.MontirConfirmationAdapter;
import com.sigit.mechaban.object.Booking;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ConfirmationActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {
    private final Booking booking = new Booking();
    private TextView titleText, idBookingText, dateText, nameText, addressText, nopolText, merkText, typeText, transmitionText, yearText, priceText, leaderText, titleAnggota, reviewText;
    private RecyclerView serviceConfirmList, anggotaMontirList;
    private final NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
    private ConfirmServiceAdapter confirmServiceAdapter;
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private final List<MontirConfirmationAdapter.MontirConfirmationItem> montirConfirmationItems = new ArrayList<>();
    private Button cancelButton, ratingButton, contactButton;
    private LinearLayout rincianMontir, ratingLayout;
    private BookingData bookingData;
    private RatingBar ratingBar;
    private ImageView logo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent = getIntent();

        logo = findViewById(R.id.icon);
        titleText = findViewById(R.id.title);

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
        leaderText = findViewById(R.id.leader_text);

        rincianMontir = findViewById(R.id.rincian_montir);
        titleAnggota = findViewById(R.id.title_anggota);
        anggotaMontirList = findViewById(R.id.montir_list);
        anggotaMontirList.setLayoutManager(new LinearLayoutManager(this));

        contactButton = findViewById(R.id.contact_button);

        ratingLayout = findViewById(R.id.review);
        ratingBar = findViewById(R.id.rating);
        reviewText = findViewById(R.id.comment_text);

        cancelButton = findViewById(R.id.cancel_button);
        ratingButton = findViewById(R.id.rating_button);

        SessionManager sessionManager = new SessionManager(this);
        booking.setAction("read");
        booking.setId_booking(intent.getStringExtra("id_booking"));
        booking.setEmail(sessionManager.getUserDetail().get("email"));
        Call<BookingAPI> readBooking = apiInterface.bookingResponse(booking);
        readBooking.enqueue(new Callback<BookingAPI>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    bookingData = response.body().getBookingData();

                    switch (bookingData.getStatus()) {
                        case "pending":
                            logo.setImageResource(R.drawable.baseline_access_time_filled_24);
                            titleText.setText("Kami Carikan Montir Terbaik Untukmu, Sabar ya!");
                            break;
                        case "diterima":
                            logo.setImageResource(R.drawable.baseline_location_on_24);
                            titleText.setText(R.string.montir_segera_menuju_ke_lokasimu);
                            break;
                        case "dikerjakan":
                            logo.setImageResource(R.drawable.baseline_construction_24);
                            titleText.setText("Montir Kami Sedang Perbaiki Mobilmu, Sabar ya!");
                            break;
                        case "selesai":
                            logo.setImageResource(R.drawable.baseline_check_24);
                            titleText.setText("Terima Kasih Telah Memakai Jasa Kami");
                            break;
                        case "batal":
                            logo.setImageResource(R.drawable.baseline_close_24);
                            titleText.setText("Kami Akan Perbaiki ke Depannya");
                            break;
                    }

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
                    String transmition = bookingData.getTransmition();
                    transmitionText.setText(transmition.substring(0, 1).toUpperCase() + transmition.substring(1));
                    yearText.setText(bookingData.getYear());

                    List<ServiceData> serviceData = bookingData.getServiceData();
                    confirmServiceAdapter = new ConfirmServiceAdapter(serviceData);
                    serviceConfirmList.setAdapter(confirmServiceAdapter);

                    priceText.setText(formatter.format(bookingData.getTotal_biaya()));

                    if (bookingData.getKetua_montir() == null) {
                        rincianMontir.setVisibility(View.GONE);
                    }
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

                    if (bookingData.getRating() == null) {
                        ratingLayout.setVisibility(View.GONE);
                    } else if (bookingData.getRating() != null) {
                        ratingButton.setVisibility(View.GONE);
                        ratingBar.setRating(Integer.parseInt(bookingData.getRating()));
                        reviewText.setText(bookingData.getTeks_review());
                    }

                    if (!(bookingData.getStatus().equals("diterima") || bookingData.getStatus().equals("dikerjakan"))) {
                        contactButton.setVisibility(View.GONE);
                    }

                    if (!bookingData.getStatus().equals("pending")) {
                        cancelButton.setVisibility(View.GONE);
                    }

                    if (!bookingData.getStatus().equals("selesai")) {
                        ratingButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.w("ConfirmationActivity", t.toString(), t);
            }
        });

        cancelButton.setOnClickListener(v -> showBottomSheetDialog(bookingData));

        findViewById(R.id.close_button).setOnClickListener(v -> finish());

        findViewById(R.id.rating_button).setOnClickListener(v -> showRatingBottom(bookingData));

        contactButton.setOnClickListener(v -> showOptionContact(bookingData));
    }

    @SuppressLint("SetTextI18n")
    private void showBottomSheetDialog(BookingData bookingData) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_modal_two_button, null);
        bottomSheetDialog.setContentView(dialogView);

        ImageView imageView = dialogView.findViewById(R.id.photo);
        TextView title = dialogView.findViewById(R.id.title);
        TextView description = dialogView.findViewById(R.id.description);
        MaterialButton confirmButton = dialogView.findViewById(R.id.positive_button);
        Button cancelButton = dialogView.findViewById(R.id.negative_button);

        imageView.setImageResource(R.drawable.cancel);

        title.setText("Batalkan Booking?");

        description.setText("Ini akan membatalkan bookingmu untuk selamanya");

        confirmButton.setText("Batalkan");
        confirmButton.setOnClickListener(v -> {
            booking.setAction("cancel");
            booking.setId_booking(bookingData.getId_booking());
            Call<BookingAPI> cancelBooking = apiInterface.bookingResponse(booking);
            cancelBooking.enqueue(new Callback<BookingAPI>() {
                @Override
                public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        MotionToast.Companion.createColorToast(ConfirmationActivity.this,
                                "Booking Berhasil Dibatalkan",
                                "Booking Dibatalkan",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(ConfirmationActivity.this, R.font.montserrat_semibold));
                        bottomSheetDialog.dismiss();
                        finish();
                    } else if (response.body() != null && response.body().getCode() == 400) {
                        new ModalBottomSheet(
                                R.drawable.done,
                                "Booking Sudah Diterima",
                                "Mohon Ditunggu Montir Kami Segera Datang",
                                "Tutup",
                                ConfirmationActivity.this)
                                .show(getSupportFragmentManager(), "ModalBottomSheet");
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
        confirmButton.setBackgroundColor(Color.TRANSPARENT);
        confirmButton.setStrokeColor(ContextCompat.getColorStateList(this, R.color.md_theme_error));
        confirmButton.setStrokeWidth(4);
        confirmButton.setRippleColor(ContextCompat.getColorStateList(this, R.color.md_theme_errorContainer));
        confirmButton.setTextColor(ContextCompat.getColorStateList(this, R.color.md_theme_error));

        cancelButton.setText("Gak jadi deh");
        cancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());
        cancelButton.setTextColor(ContextCompat.getColorStateList(this, R.color.md_theme_background));

        bottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showRatingBottom(BookingData bookingData) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_rating, null);
        bottomSheetDialog.setContentView(dialogView);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        RatingBar ratingBar = dialogView.findViewById(R.id.rating);
        TextView comment = dialogView.findViewById(R.id.review_field);
        Button send = dialogView.findViewById(R.id.send_button);
        send.setOnClickListener(v -> {
            booking.setAction("rating");
            booking.setId_booking(bookingData.getId_booking());
            booking.setReview(comment.getText().toString());
            booking.setRating( (int) ratingBar.getRating());
            booking.setTgl_booking(now.format(formatterDate));
            Call<BookingAPI> sendRating = apiInterface.bookingResponse(booking);
            sendRating.enqueue(new Callback<BookingAPI>() {
                @Override
                public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        MotionToast.Companion.createColorToast(ConfirmationActivity.this,
                                "Rating Telah Dikirim",
                                "Terima Kasih Atas Penilaiannya",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(ConfirmationActivity.this, R.font.montserrat_semibold));
                        bottomSheetDialog.dismiss();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                    Log.e("Rating", t.toString(), t);
                }
            });
        });

        bottomSheetDialog.show();
    }

    private void showOptionContact(BookingData bookingData) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_contact, null);
        bottomSheetDialog.setContentView(dialogView);

        LinearLayout emailButton = dialogView.findViewById(R.id.email_button);
        emailButton.setOnClickListener(v -> {
            String subject = "Menghubungi Montir tentang Booking Mechaban";
            String message = "";

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{bookingData.getEmail_ketua_montir()});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(intent);
        });

        LinearLayout waButton = dialogView.findViewById(R.id.whatsapp_button);
        waButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/62" + bookingData.getNo_hp_ketua()));
                startActivity(intent);
            } catch (Exception e) {
                Log.e("ContactUs", e.toString(), e);
            }
        });

        bottomSheetDialog.show();
    }

    @Override
    public void buttonBottomSheet() {
        finish();
    }
}