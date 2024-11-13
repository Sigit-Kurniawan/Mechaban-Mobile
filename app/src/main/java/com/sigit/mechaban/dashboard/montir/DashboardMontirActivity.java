package com.sigit.mechaban.dashboard.montir;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.montir.fragment.AccountMontirFragment;
import com.sigit.mechaban.dashboard.montir.fragment.BookingMontirFragment;
import com.sigit.mechaban.dashboard.montir.fragment.HomeMontirFragment;
import com.sigit.mechaban.dashboard.montir.fragment.InspeksiMontirFragment;

import java.util.Objects;

public class DashboardMontirActivity extends AppCompatActivity {
    private Fragment bookingmontirFragment, accountmontirFragment, inspeksiactiveFragment,homemontirFragment, activeFragment;
//    private FloatingActionButton floatingActionButton;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_montir);

        // Initializing Fragments
        homemontirFragment = new HomeMontirFragment();
        bookingmontirFragment = new BookingMontirFragment(); // Ensure GarageFragment does not need arguments
        inspeksiactiveFragment = new InspeksiMontirFragment();
        accountmontirFragment = new AccountMontirFragment();


//        floatingActionButton = findViewById(R.id.fab_button);
//        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(this, AddCarActivity.class)));
//        floatingActionButton.hide();

        activeFragment = homemontirFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, accountmontirFragment, "Account").hide(accountmontirFragment)
                .add(R.id.frame_layout, inspeksiactiveFragment, "Inspeksi").hide(inspeksiactiveFragment)
                .add(R.id.frame_layout, bookingmontirFragment, "Booking").hide(bookingmontirFragment)
                .add(R.id.frame_layout, homemontirFragment, "Home")
                .commit();

        setSupportActionBar(findViewById(R.id.action_bar));

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            id = item.getItemId();
            if (id == R.id.home) {
                switchFragment(homemontirFragment);
                Objects.requireNonNull(getSupportActionBar()).hide();
                return true;
            } else if (id == R.id.Booking) {
                switchFragment(bookingmontirFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Garasi");
                getSupportActionBar().show();
                return true;
            } else if (id == R.id.activity) {
                switchFragment(inspeksiactiveFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Aktivitas");
                getSupportActionBar().show();
                return true;
            } else if (id == R.id.account) {
                switchFragment(accountmontirFragment);
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

        // Show FloatingActionButton only on GarageFragment
//        if (fragment == bookingmontirFragment) {
//            floatingActionButton.show();
//        } else {
//            floatingActionButton.hide();
//        }
    }
}
