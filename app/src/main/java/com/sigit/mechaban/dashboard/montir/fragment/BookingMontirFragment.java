package com.sigit.mechaban.dashboard.montir.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.booking.BookingData;
import com.sigit.mechaban.dashboard.montir.booking.ActivityOnbookingAdapter;
import com.sigit.mechaban.object.Booking;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingMontirFragment extends Fragment {
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private ChipGroup chipGroup;
    private final List<BookingMontirFragment.ChipItem> chipItems = new ArrayList<>();
    private int activeChipIndex = -1;
    private final Booking booking = new Booking();
    private final List<ActivityOnbookingAdapter.ActivityOnbookingItem> activityOnbookingItems = new ArrayList<>();
    private ActivityOnbookingAdapter activityOnbookingAdapter;
    private RecyclerView recyclerView;
    private SessionManager sessionManager;
    private LinearLayout emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_montir, container, false);

        sessionManager = new SessionManager(requireContext());
        emptyView = view.findViewById(R.id.empty_view);
        chipGroup = view.findViewById(R.id.chipGroup);
        recyclerView = view.findViewById(R.id.activity_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activityOnbookingAdapter = new ActivityOnbookingAdapter(requireActivity(), activityOnbookingItems);
        recyclerView.setAdapter(activityOnbookingAdapter);

        setupChips();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActivityItems();
    }

    private void setActivityItems() {
        booking.setAction("list_montir");
        booking.setEmail(sessionManager.getUserDetail().get("email"));
        Call<BookingAPI> readListBooking = apiInterface.bookingResponse(booking);
        readListBooking.enqueue(new Callback<BookingAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    activityOnbookingItems.clear();
                    for (BookingData bookingData : response.body().getBookingDataList()) {
                        activityOnbookingItems.add(new ActivityOnbookingAdapter.ActivityOnbookingItem(bookingData.getId_booking(), bookingData.getTgl_booking(), bookingData.getNopol(), bookingData.getMerk(), bookingData.getType(), bookingData.getStatus(), bookingData.getLatitude(), bookingData.getLongitude()));
                    }
                    activityOnbookingAdapter.notifyDataSetChanged();
                } else if (response.body() != null && response.body().getCode() == 404) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.e("ActivityFragment", t.toString(), t);
            }
        });
    }

    private void setupChips() {
        chipItems.add(new BookingMontirFragment.ChipItem("Menunggu Konfirmasi", "pending"));
        chipItems.add(new BookingMontirFragment.ChipItem("Booking Sedang Dijemput", "diterima"));
        chipItems.add(new BookingMontirFragment.ChipItem("Booking Dikerjakan", "dikerjakan"));
        chipItems.add(new BookingMontirFragment.ChipItem("Booking Selesai", "selesai"));
        chipItems.add(new BookingMontirFragment.ChipItem("Booking Batal", "batal"));

        chipGroup.removeAllViews();
        for (int i = 0; i < chipItems.size(); i++) {
            BookingMontirFragment.ChipItem chipItem = chipItems.get(i);
            Chip chip = createChip(chipItem, i, chipItems);
            chipGroup.addView(chip);
        }
    }

    private Chip createChip(BookingMontirFragment.ChipItem chipItem, int index, List<BookingMontirFragment.ChipItem> chipItems) {
        Chip chip = new Chip(getContext());
        chip.setText(chipItem.getDisplayName());
        chip.setCheckable(true);

        chip.setOnClickListener(v -> {
            if (activeChipIndex == index) {
                chipItem.setActive(false);
                activeChipIndex = -1;
                activityOnbookingAdapter.setFilteredList(activityOnbookingItems);
            } else {
                for (int i = 0; i < chipItems.size(); i++) {
                    BookingMontirFragment.ChipItem item = chipItems.get(i);
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
        List<ActivityOnbookingAdapter.ActivityOnbookingItem> filteredItems = new ArrayList<>();

        for (ActivityOnbookingAdapter.ActivityOnbookingItem item : activityOnbookingItems) {
            if (item.getStatus().equals(filterValue)) {
                filteredItems.add(item);
            }
        }

        activityOnbookingAdapter.setFilteredList(filteredItems);
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