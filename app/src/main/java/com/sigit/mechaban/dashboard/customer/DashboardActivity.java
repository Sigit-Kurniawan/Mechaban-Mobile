package com.sigit.mechaban.dashboard.customer;

import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.fragment.AccountFragment;
import com.sigit.mechaban.dashboard.customer.fragment.ActivityFragment;
import com.sigit.mechaban.dashboard.customer.fragment.GarageFragment;
import com.sigit.mechaban.dashboard.customer.fragment.HomeFragment;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {
    private Fragment homeFragment, garageFragment, activityFragment, accountFragment, activeFragment;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        homeFragment = new HomeFragment();
        garageFragment = new GarageFragment();
        activityFragment = new ActivityFragment();
        accountFragment = new AccountFragment();

        activeFragment = homeFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, accountFragment, "Account").hide(accountFragment)
                .add(R.id.frame_layout, activityFragment, "Activity").hide(activityFragment)
                .add(R.id.frame_layout, garageFragment, "Garage").hide(garageFragment)
                .add(R.id.frame_layout, homeFragment, "Home")
                .commit();

        // Reset fragments and switch to AccountFragment
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                // Reset fragments and switch to AccountFragment
                resetFragmentsToAccount();
            }
        });

        setSupportActionBar(findViewById(R.id.action_bar));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            id = item.getItemId();
            if (id == R.id.home) {
                switchFragment(homeFragment);
                Objects.requireNonNull(getSupportActionBar()).hide();
                return true;
            } else if (id == R.id.garage) {
                switchFragment(garageFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Garasi");
                getSupportActionBar().show();
                return true;
            } else if (id == R.id.activity) {
                switchFragment(activityFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Aktivitas");
                getSupportActionBar().show();
                return true;
            } else if (id == R.id.account) {
                switchFragment(accountFragment);
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

    private void resetFragmentsToAccount() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .remove(accountFragment)
                .commitNow();

        // Recreate fragments
        accountFragment = new AccountFragment();

        // Add AccountFragment and make it the active fragment
        fragmentManager.beginTransaction()
                .add(R.id.frame_layout, accountFragment, "Account").commit();

        activeFragment = accountFragment;
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}