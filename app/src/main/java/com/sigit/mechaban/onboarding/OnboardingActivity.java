package com.sigit.mechaban.onboarding;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.sigit.mechaban.R;
import com.sigit.mechaban.auth.LoginActivity;
import com.sigit.mechaban.dashboard.customer.DashboardActivity;
import com.sigit.mechaban.sessionmanager.SessionManager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private DotsIndicator dotsIndicator;
    private Button getStartedButton, backButton, nextButton, skipButton;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        if (new SessionManager(this).isLoggedIn()) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

        skipButton = findViewById(R.id.skip);
        skipButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        List<ScreenItem> screenItemList = new ArrayList<>();
        screenItemList.add(new ScreenItem("Selamat Datang di Mechaban", "Siap melayani mobil Anda di dalam satu genggamanmu.", R.drawable.onboarding1));
        screenItemList.add(new ScreenItem("Servis Cepat dan Berkualitas", "Membuat mobilmu seperti baru kembali.", R.drawable.onboarding2));
        screenItemList.add(new ScreenItem("Mampu Melayani di Manapun Anda Berada", "Siap mengantarkan montir andalan ke lokasi.", R.drawable.onboarding3));
        screenItemList.add(new ScreenItem("Konsultasi Gratis dengan Montir Ahli", "Hubungi dan rasakan manfaatnya tanpa biaya tambahan.", R.drawable.onboarding4));

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(this, screenItemList));

        dotsIndicator = findViewById(R.id.dots_indicator);
        dotsIndicator.attachTo(viewPager);

        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            position = viewPager.getCurrentItem();
            if (position < screenItemList.size()) {
                position--;
                viewPager.setCurrentItem(position);
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    backButton.setVisibility(View.INVISIBLE);
                } else {
                    backButton.setVisibility(View.VISIBLE);
                }

                if (position == screenItemList.size() - 1) {
                    loadLastScreen();
                } else {
                    reloadScreen();
                }
            }
        });

        nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(v -> {
            position = viewPager.getCurrentItem();
            if (position < screenItemList.size()) {
                position++;
                viewPager.setCurrentItem(position);
            }
        });

        getStartedButton = findViewById(R.id.get_startedbtn);
        getStartedButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadLastScreen() {
        nextButton.setVisibility(View.INVISIBLE);
        dotsIndicator.setVisibility(View.INVISIBLE);
        skipButton.setVisibility(View.INVISIBLE);
        getStartedButton.setVisibility(View.VISIBLE);
    }

    private void reloadScreen() {
        nextButton.setVisibility(View.VISIBLE);
        dotsIndicator.setVisibility(View.VISIBLE);
        skipButton.setVisibility(View.VISIBLE);
        getStartedButton.setVisibility(View.INVISIBLE);
    }
}