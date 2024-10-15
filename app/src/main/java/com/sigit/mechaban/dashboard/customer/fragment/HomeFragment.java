package com.sigit.mechaban.dashboard.customer.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sigit.mechaban.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view.findViewById(R.id.car_button).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setScaleX(0.98f);
                    v.setScaleY(0.98f);
                    break;
                case MotionEvent.ACTION_UP:
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);
                    v.performClick();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);
                    break;
            }
            return true;
        });

        RelativeLayout relativeLayout = view.findViewById(R.id.menu_home);
        GridLayout mainItems = view.findViewById(R.id.main_items);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Handle different states of bottom sheet
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Ubah margin bottom dari RelativeLayout secara real-time
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) relativeLayout.getLayoutParams();

                // Tentukan jarak gap yang diinginkan antara RelativeLayout dan BottomSheet (misal 20dp)
                int gap = (int) (8 * getResources().getDisplayMetrics().density);

                // Ubah margin bottom berdasarkan posisi slideOffset (0 = peek, 1 = full expand)
                layoutParams.bottomMargin = (int) (slideOffset * bottomSheet.getHeight());
                relativeLayout.setLayoutParams(layoutParams);

                // Animasi mainItems agar terdorong ke atas secara realistis
                float maxTranslationY = bottomSheet.getHeight() - gap; // Translasikan secara halus dengan tetap memperhatikan gap
                mainItems.setTranslationY(-slideOffset * maxTranslationY);
            }
        });

        view.findViewById(R.id.car_service_card).setBackgroundResource(R.drawable.background_component);
        return view;
    }
}