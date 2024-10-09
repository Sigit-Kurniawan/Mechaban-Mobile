package com.sigit.mechaban.dashboard.customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.fragment.AccountFragment;
import com.sigit.mechaban.dashboard.customer.fragment.ActivityFragment;
import com.sigit.mechaban.dashboard.customer.fragment.GarageFragment;
import com.sigit.mechaban.dashboard.customer.fragment.HomeFragment;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        replaceFragment(new HomeFragment());

        setSupportActionBar(findViewById(R.id.action_bar));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
                Objects.requireNonNull(getSupportActionBar()).hide();
                return true;
            } else if (id == R.id.garage) {
                replaceFragment(new GarageFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle("Garasi");
                getSupportActionBar().show();
                return true;
            } else if (id == R.id.activity) {
                replaceFragment(new ActivityFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle("Aktivitas");
                getSupportActionBar().show();
                return true;
            } else if (id == R.id.account) {
                replaceFragment(new AccountFragment());
                Objects.requireNonNull(getSupportActionBar()).hide();
                return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}