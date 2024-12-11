package com.sigit.mechaban.dashboard.montir.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.montir.booking.ActivityOnbookingAdapter;
import com.sigit.mechaban.dashboard.montir.booking.SharedBookingViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookingMontirFragment extends Fragment {
    private ChipGroup chipGroup;
    private final List<BookingMontirFragment.ChipItem> chipItems = new ArrayList<>();
    private int activeChipIndex = -1;
    private final List<ActivityOnbookingAdapter.ActivityOnbookingItem> activityOnbookingItems = new ArrayList<>();
    private ActivityOnbookingAdapter activityOnbookingAdapter;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private SharedBookingViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_montir, container, false);

        emptyView = view.findViewById(R.id.empty_view);
        chipGroup = view.findViewById(R.id.chipGroup);
        recyclerView = view.findViewById(R.id.activity_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setupChips();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActivityItems();
    }

    private void setActivityItems() {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedBookingViewModel.class);
        activityOnbookingAdapter = new ActivityOnbookingAdapter(requireContext(), new ArrayList<>(), (updatedItem, position) -> {
            List<ActivityOnbookingAdapter.ActivityOnbookingItem> currentList = new ArrayList<>(viewModel.getBookings().getValue());
            currentList.set(position, updatedItem);
            viewModel.updateBookingList(currentList);
        });

        recyclerView.setAdapter(activityOnbookingAdapter);
        viewModel.getBookings().observe(getViewLifecycleOwner(), bookingList -> {
            List<ActivityOnbookingAdapter.ActivityOnbookingItem> filteredList = new ArrayList<>(bookingList);
            activityOnbookingAdapter.setFilteredList(filteredList);

            if (filteredList.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
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