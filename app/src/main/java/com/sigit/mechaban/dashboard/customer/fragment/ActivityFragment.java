package com.sigit.mechaban.dashboard.customer.fragment;

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

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.booking.BookingData;
import com.sigit.mechaban.dashboard.customer.activity.recyclerview.ActivityAdapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        SessionManager sessionManager = new SessionManager(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.activity_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ActivityAdapter activityAdapter = new ActivityAdapter(requireActivity().getApplicationContext(), activityItems);
        recyclerView.setAdapter(activityAdapter);

        booking.setAction("list");
        booking.setEmail(sessionManager.getUserDetail().get("email"));
        Call<BookingAPI> readListBooking = apiInterface.bookingResponse(booking);
        readListBooking.enqueue(new Callback<BookingAPI>() {
            @Override
            public void onResponse(@NonNull Call<BookingAPI> call, @NonNull Response<BookingAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    for (BookingData bookingData : response.body().getBookingDataList()) {
                        activityItems.add(new ActivityAdapter.ActivityItem(bookingData.getId_booking(), bookingData.getTgl_booking(), bookingData.getMerk(), bookingData.getType(), bookingData.getStatus_pengerjaan(), bookingData.getTotal_biaya()));
                    }
                    activityAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingAPI> call, @NonNull Throwable t) {
                Log.e("ActivityFragment", t.toString(), t);
            }
        });

        return view;
    }
}