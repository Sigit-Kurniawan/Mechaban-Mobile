package com.sigit.mechaban.dashboard.customer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.booking.BookingData;
import com.sigit.mechaban.dashboard.customer.activity.ActivityAdapter;
import com.sigit.mechaban.object.Booking;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFragment extends Fragment {
    private final Booking booking = new Booking();
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private final List<ActivityAdapter.ActivityItem> activityItems = new ArrayList<>();
    private SessionManager sessionManager;
    private ActivityAdapter activityAdapter;
    private RecyclerView recyclerView;
    private ChipGroup chipGroup;
    private int activeChipIndex = -1;
    private final List<ChipItem> chipItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        sessionManager = new SessionManager(requireContext());
        chipGroup = view.findViewById(R.id.chipGroup);
        recyclerView = view.findViewById(R.id.activity_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        activityAdapter = new ActivityAdapter(requireActivity(), activityItems);
        recyclerView.setAdapter(activityAdapter);

        setupChips();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActivityItems();
    }

    private void setActivityItems() {
        booking.setAction("list");
        booking.setEmail(sessionManager.getUserDetail().get("email"));
        Call<BookingAPI> readListBooking = apiInterface.bookingResponse(booking);
        readListBooking.enqueue(new Callback<BookingAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    if (response.body().getBookingDataList() != null) {
                        requireView().findViewById(R.id.empty_view).setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        activityItems.clear();
                        for (BookingData bookingData : response.body().getBookingDataList()) {
                            activityItems.add(new ActivityAdapter.ActivityItem(bookingData.getId_booking(), bookingData.getTgl_booking(), bookingData.getMerk(), bookingData.getType(), bookingData.getStatus(), bookingData.getTotal_biaya()));
                        }
                        activityAdapter.notifyDataSetChanged();
                    } else {
                        requireView().findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.e("ActivityFragment", t.toString(), t);
            }
        });
    }

    private void setupChips() {
        chipItems.add(new ActivityFragment.ChipItem("Menunggu Konfirmasi", "pending"));
        chipItems.add(new ActivityFragment.ChipItem("Booking Sedang Dijemput", "diterima"));
        chipItems.add(new ActivityFragment.ChipItem("Booking Dikerjakan", "dikerjakan"));
        chipItems.add(new ActivityFragment.ChipItem("Booking Selesai", "selesai"));
        chipItems.add(new ActivityFragment.ChipItem("Booking Batal", "batal"));

        chipGroup.removeAllViews();
        for (int i = 0; i < chipItems.size(); i++) {
            ChipItem chipItem = chipItems.get(i);
            Chip chip = createChip(chipItem, i, chipItems);
            chipGroup.addView(chip);
        }
    }

    private Chip createChip(ChipItem chipItem, int index, List<ChipItem> chipItems) {
        Chip chip = new Chip(getContext());
        chip.setText(chipItem.getDisplayName());
        chip.setCheckable(true);

        chip.setOnClickListener(v -> {
            if (activeChipIndex == index) {
                chipItem.setActive(false);
                activeChipIndex = -1;
                activityAdapter.setFilteredList(activityItems);
            } else {
                for (int i = 0; i < chipItems.size(); i++) {
                    ChipItem item = chipItems.get(i);
                    item.setActive(false);
                    Chip chipAtIndex = (Chip) chipGroup.getChildAt(i);
                    if (chipAtIndex != null) chipAtIndex.setChecked(false);
                }

                chipItem.setActive(true);
                activeChipIndex = index;
                applyFilter(chipItem.getStatus());
            }

            chip.setChecked(chipItem.isActive());
        });

        return chip;
    }

    private void applyFilter(String filterValue) {
        List<ActivityAdapter.ActivityItem> filteredItems = new ArrayList<>();

        for (ActivityAdapter.ActivityItem item : activityItems) {
            if (item.getStatus().equals(filterValue)) {
                filteredItems.add(item);
            }
        }

        activityAdapter.setFilteredList(filteredItems);
    }

    public static class ChipItem {
        private final String displayName, status;
        private boolean isActive;

        public ChipItem(String displayName, String status) {
            this.displayName = displayName;
            this.status = status;
            this.isActive = false;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getStatus() {
            return status;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }
}