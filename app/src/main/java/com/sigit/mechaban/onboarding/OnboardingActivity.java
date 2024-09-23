package com.sigit.mechaban.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sigit.mechaban.R;
import com.sigit.mechaban.auth.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    TextView skipButton;
    ImageButton backButton, nextButton;
    TabLayout tabLayout;
    Button getStartedButton;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_onboarding);

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

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, screenItemList);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = findViewById(R.id.tab_indicator);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() < screenItemList.size() - 1) {
                    reloadScreen();
                }

                if (tab.getPosition() == screenItemList.size() - 1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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

//        TODO: menambahkan animasi muncul tombol mulai sekarang
        getStartedButton = findViewById(R.id.get_startedbtn);
        getStartedButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }

    private void loadLastScreen() {
        nextButton.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        skipButton.setVisibility(View.INVISIBLE);
        getStartedButton.setVisibility(View.VISIBLE);
    }

    private void reloadScreen() {
        nextButton.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        skipButton.setVisibility(View.VISIBLE);
        getStartedButton.setVisibility(View.INVISIBLE);
    }
}