package com.sigit.mechaban.dashboard.montir.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.booking.BookingData;
import com.sigit.mechaban.dashboard.montir.booking.ActivityOnbookingAdapter;
import com.sigit.mechaban.dashboard.montir.service.ServiceMontirActivity;
import com.sigit.mechaban.object.Booking;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeMontirFragment extends Fragment {
    private final Booking booking = new Booking();
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private LinearLayout emptyView;
    private RecyclerView recyclerView;
    private ActivityOnbookingAdapter activityOnbookingAdapter;
    private final List<ActivityOnbookingAdapter.ActivityOnbookingItem> activityItems = new ArrayList<>();
    private TextView titleText;

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
        activityOnbookingAdapter = new ActivityOnbookingAdapter(requireActivity(), activityItems);
        recyclerView.setAdapter(activityOnbookingAdapter);

        view.findViewById(R.id.service_button).setOnClickListener(v -> startActivity(new Intent(requireActivity(), ServiceMontirActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setOnBooking();
    }

    private void setOnBooking() {
        SessionManager sessionManager = new SessionManager(requireActivity());
        booking.setAction("onbooking");
        booking.setEmail(sessionManager.getUserDetail().get("email"));
        Call<BookingAPI> readOnBooking = apiInterface.bookingResponse(booking);
        readOnBooking.enqueue(new Callback<BookingAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    if (response.body().getBookingDataList() != null) {
                        titleText.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        activityItems.clear();
                        for (BookingData bookingData : response.body().getBookingDataList()) {
                            activityItems.add(new ActivityOnbookingAdapter.ActivityOnbookingItem(
                                    bookingData.getId_booking(),
                                    bookingData.getTgl_booking(),
                                    bookingData.getNopol(),
                                    bookingData.getMerk(),
                                    bookingData.getType(),
                                    bookingData.getStatus(),
                                    bookingData.getLatitude(),
                                    bookingData.getLongitude()));
                        }
                        activityOnbookingAdapter.notifyDataSetChanged();
                    } else {
                        titleText.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.e("HomeMontirFragment", t.toString(), t);
            }
        });
    }
}