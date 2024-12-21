package com.sigit.mechaban.dashboard.montir.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.status.StatusAPI;
import com.sigit.mechaban.dashboard.montir.booking.ActivityOnbookingAdapter;
import com.sigit.mechaban.dashboard.montir.booking.SharedBookingViewModel;
import com.sigit.mechaban.dashboard.montir.service.ServiceMontirActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeMontirFragment extends Fragment {
    private LinearLayout emptyView;
    private RecyclerView recyclerView;
    private ActivityOnbookingAdapter activityOnbookingAdapter;
    private TextView titleText;
    private Button bookingButton;
    private SharedBookingViewModel viewModel;
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private int statusBengkel;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_montir, container, false);

        titleText = view.findViewById(R.id.title_text);

        emptyView = view.findViewById(R.id.empty_view);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookingButton = view.findViewById(R.id.service_button);

        bookingButton.setOnClickListener(v -> {
            if (statusBengkel == 1) {
                startActivity(new Intent(requireActivity(), ServiceMontirActivity.class));
            } else {
                bottomSheetDialog = new BottomSheetDialog(requireActivity());
                @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.bottom_sheet_modal, null, false);
                ImageView imageView = view1.findViewById(R.id.photo);
                imageView.setImageResource(R.drawable.close);

                TextView title = view1.findViewById(R.id.title);
                title.setText("Bengkel Sudah Tutup");

                TextView desc = view1.findViewById(R.id.description);
                desc.setText("Lanjut esok hari");

                Button button = view1.findViewById(R.id.button);
                button.setText("Tutup");
                button.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setBooking();

        Call<StatusAPI> statusAPICall = apiInterface.statusResponse();
        statusAPICall.enqueue(new Callback<StatusAPI>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<StatusAPI> call, @NonNull Response<StatusAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    statusBengkel = response.body().getData();
                } else {
                    Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<StatusAPI> call, @NonNull Throwable t) {
                Log.e("Status", t.toString(), t);
            }
        });
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