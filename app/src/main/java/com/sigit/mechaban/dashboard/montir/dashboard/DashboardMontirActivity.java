package com.sigit.mechaban.dashboard.montir.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.booking.BookingData;
import com.sigit.mechaban.dashboard.customer.fragment.AccountFragment;
import com.sigit.mechaban.dashboard.montir.booking.ActivityOnbookingAdapter;
import com.sigit.mechaban.dashboard.montir.booking.SharedBookingViewModel;
import com.sigit.mechaban.dashboard.montir.fragment.BookingMontirFragment;
import com.sigit.mechaban.dashboard.montir.fragment.HomeMontirFragment;
import com.sigit.mechaban.object.Booking;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardMontirActivity extends AppCompatActivity {
    private TextView toolbarTitle;
    private Fragment homeMontirFragment, bookingMontirFragment, accountMontirFragment, activeFragment;
    private int id;
    private final Booking booking = new Booking();
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private final List<ActivityOnbookingAdapter.ActivityOnbookingItem> activityOnbookingItems = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_montir);

        homeMontirFragment = new HomeMontirFragment();
        bookingMontirFragment = new BookingMontirFragment();
        accountMontirFragment = new AccountFragment();

        activeFragment = homeMontirFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, accountMontirFragment, "Account").hide(accountMontirFragment)
                .add(R.id.frame_layout, bookingMontirFragment, "Booking").hide(bookingMontirFragment)
                .add(R.id.frame_layout, homeMontirFragment, "Home")
                .commit();

        setSupportActionBar(findViewById(R.id.action_bar));
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbarTitle = findViewById(R.id.toolbar_title);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            id = item.getItemId();
            if (id == R.id.home) {
                switchFragment(homeMontirFragment);
                Objects.requireNonNull(getSupportActionBar()).hide();
                return true;
            } else if (id == R.id.booking) {
                switchFragment(bookingMontirFragment);
                toolbarTitle.setText("Booking");
                getSupportActionBar().show();
                return true;
            } else if (id == R.id.account) {
                switchFragment(accountMontirFragment);
                Objects.requireNonNull(getSupportActionBar()).hide();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBookingList();
    }

    private void switchFragment(Fragment fragment) {
        if (fragment != activeFragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(activeFragment)
                    .show(fragment)
                    .commit();
            activeFragment = fragment;
        }
    }

    private void setBookingList() {
        SharedBookingViewModel viewModel = new ViewModelProvider(this).get(SharedBookingViewModel.class);

        SessionManager sessionManager = new SessionManager(this);
        booking.setAction("list_montir");
        booking.setEmail(sessionManager.getUserDetail().get("email"));
        Call<BookingAPI> readListBooking = apiInterface.bookingResponse(booking);
        readListBooking.enqueue(new Callback<BookingAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    activityOnbookingItems.clear();
                    for (BookingData bookingData : response.body().getBookingDataList()) {
                        activityOnbookingItems.add(new ActivityOnbookingAdapter.ActivityOnbookingItem(
                                bookingData.getId_booking(),
                                bookingData.getTgl_booking(),
                                bookingData.getNopol(),
                                bookingData.getMerk(),
                                bookingData.getType(),
                                bookingData.getRole(),
                                bookingData.getStatus(),
                                bookingData.getLatitude(),
                                bookingData.getLongitude()));
                    }
                    viewModel.updateBookingList(activityOnbookingItems);
                } else {
                    Toast.makeText(DashboardMontirActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.e("ActivityFragment", t.toString(), t);
            }
        });
    }
}
