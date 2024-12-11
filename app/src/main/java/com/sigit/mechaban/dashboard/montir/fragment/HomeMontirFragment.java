package com.sigit.mechaban.dashboard.montir.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.montir.booking.ActivityOnbookingAdapter;
import com.sigit.mechaban.dashboard.montir.booking.SharedBookingViewModel;
import com.sigit.mechaban.dashboard.montir.service.ServiceMontirActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeMontirFragment extends Fragment {
    private LinearLayout emptyView;
    private RecyclerView recyclerView;
    private ActivityOnbookingAdapter activityOnbookingAdapter;
    private TextView titleText;
    private Button bookingButton;
    private SharedBookingViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_montir, container, false);

        titleText = view.findViewById(R.id.title_text);

        emptyView = view.findViewById(R.id.empty_view);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookingButton = view.findViewById(R.id.service_button);

        bookingButton.setOnClickListener(v -> startActivity(new Intent(requireActivity(), ServiceMontirActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setBooking();
    }

    private void setBooking() {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedBookingViewModel.class);
        activityOnbookingAdapter = new ActivityOnbookingAdapter(requireContext(), new ArrayList<>(), (updatedItem, position) -> {
            List<ActivityOnbookingAdapter.ActivityOnbookingItem> currentList = new ArrayList<>(viewModel.getBookings().getValue());
            currentList.set(position, updatedItem);
            viewModel.updateBookingList(currentList);
        });

        recyclerView.setAdapter(activityOnbookingAdapter);
        viewModel.getBookings().observe(getViewLifecycleOwner(), bookingList -> {
            List<ActivityOnbookingAdapter.ActivityOnbookingItem> filteredList = new ArrayList<>();
            for (ActivityOnbookingAdapter.ActivityOnbookingItem item : bookingList) {
                if (item.getStatus().equals("diterima") || item.getStatus().equals("dikerjakan")) {
                    filteredList.add(item);
                }
            }
            activityOnbookingAdapter.setFilteredList(filteredList);

            if (filteredList.isEmpty()) {
                titleText.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                bookingButton.setVisibility(View.VISIBLE);
            } else {
                titleText.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                bookingButton.setVisibility(View.GONE);
            }
        });
    }
}