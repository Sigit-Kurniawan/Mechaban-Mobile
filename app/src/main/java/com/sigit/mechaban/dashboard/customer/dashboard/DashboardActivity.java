package com.sigit.mechaban.dashboard.customer.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.fragment.AccountFragment;
import com.sigit.mechaban.dashboard.customer.fragment.ActivityFragment;
import com.sigit.mechaban.dashboard.customer.fragment.GarageFragment;
import com.sigit.mechaban.dashboard.customer.fragment.HomeFragment;
import com.sigit.mechaban.dashboard.customer.garage.AddCarActivity;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {
    private Fragment homeFragment, garageFragment, activityFragment, accountFragment, activeFragment;
    private FloatingActionButton floatingActionButton;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        homeFragment = new HomeFragment();
        garageFragment = new GarageFragment(this::updateFabVisibility);
        activityFragment = new ActivityFragment();
        accountFragment = new AccountFragment();
        floatingActionButton = findViewById(R.id.fab_button);
        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(this, AddCarActivity.class)));
        floatingActionButton.hide();

        activeFragment = homeFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, accountFragment, "Account").hide(accountFragment)
                .add(R.id.frame_layout, activityFragment, "Activity").hide(activityFragment)
                .add(R.id.frame_layout, garageFragment, "Garage").hide(garageFragment)
                .add(R.id.frame_layout, homeFragment, "Home")
                .commit();

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

        if (fragment == garageFragment) {
            floatingActionButton.show();
        } else {
            floatingActionButton.hide();
        }
    }

    private void updateFabVisibility(boolean isCarListNotEmpty) {
        if (isCarListNotEmpty) {
            floatingActionButton.show();
        } else {
            floatingActionButton.hide();
        }
    }
}