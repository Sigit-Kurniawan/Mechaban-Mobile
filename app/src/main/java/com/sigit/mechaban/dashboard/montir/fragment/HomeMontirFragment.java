package com.sigit.mechaban.dashboard.montir.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.montir.booking.BookingActivity;
import com.sigit.mechaban.dashboard.montir.service.ServiceMontirActivity;

public class HomeMontirFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_montir, container, false);

        view.findViewById(R.id.service_button).setOnClickListener(v -> startActivity(new Intent(requireActivity(), ServiceMontirActivity.class)));

        view.findViewById(R.id.booking_button).setOnClickListener(v -> startActivity(new Intent(requireActivity(), BookingActivity.class)));

        return view;
    }
}