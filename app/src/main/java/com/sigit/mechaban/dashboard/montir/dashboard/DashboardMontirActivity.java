package com.sigit.mechaban.dashboard.montir.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.fragment.AccountFragment;
import com.sigit.mechaban.dashboard.montir.fragment.BookingMontirFragment;
import com.sigit.mechaban.dashboard.montir.fragment.HomeMontirFragment;

import java.util.Objects;

public class DashboardMontirActivity extends AppCompatActivity {
    private TextView toolbarTitle;
    private Fragment homeMontirFragment, bookingMontirFragment, accountMontirFragment, activeFragment;
    private int id;

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
}
