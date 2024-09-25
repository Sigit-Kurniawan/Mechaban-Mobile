package com.sigit.mechaban.dashboard.customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.fragment.GarageFragment;
import com.sigit.mechaban.dashboard.customer.fragment.HomeFragment;

public class DashboardActivity extends AppCompatActivity {
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        replaceFragment(new HomeFragment());

        Toolbar actionBar = findViewById(R.id.action_bar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            id = item.getItemId();
            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (id == R.id.garage) {
                replaceFragment(new GarageFragment());
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