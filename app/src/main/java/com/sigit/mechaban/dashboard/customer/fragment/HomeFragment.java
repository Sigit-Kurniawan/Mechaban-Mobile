package com.sigit.mechaban.dashboard.customer.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.search.SearchBar;
import com.sigit.mechaban.R;

public class HomeFragment extends Fragment {
    private RelativeLayout menuHomeLayout;
    private SearchBar searchBar;
    private int initialHeight;
    private int searchBarHeight;
    private final int collapsedHeight = 200;
    private final int marginBetween = 16;

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

        menuHomeLayout = view.findViewById(R.id.menu_home);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        searchBar = view.findViewById(R.id.search_bar);

        menuHomeLayout.post(() -> initialHeight = menuHomeLayout.getHeight());
        searchBar.post(() -> searchBarHeight = searchBar.getHeight());
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {}

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Hitung tinggi baru berdasarkan slideOffset
                int newHeight = (int) (initialHeight - (initialHeight - collapsedHeight) * slideOffset);

                // Gunakan dpToPx untuk mengonversi jarak dp ke piksel
                int newMargin = dpToPx((int) (marginBetween * slideOffset));

                // Set tinggi baru dan margin bottom dengan animasi
                animateHeightAndMargin(menuHomeLayout, newHeight, newMargin);

                // Kurangi tinggi SearchBar saat BottomSheet di-expand penuh
                int newSearchBarHeight = (int) (searchBarHeight * (1 - slideOffset));
                animateHeight(searchBar, newSearchBarHeight);
            }
        });
        return view;
    }

    private void animateHeightAndMargin(final View view, int endHeight, int marginBottom) {
        ValueAnimator animator = ValueAnimator.ofInt(view.getHeight(), endHeight);
        animator.setDuration(200); // Durasi animasi 200ms
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = animatedValue;
            layoutParams.bottomMargin = marginBottom;
            view.setLayoutParams(layoutParams);
        });
        animator.start();
    }

    private void animateHeight(final View view, int endHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(view.getHeight(), endHeight);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = animatedValue;
            view.setLayoutParams(layoutParams);
        });
        animator.start();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}