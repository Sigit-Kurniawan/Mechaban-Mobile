package com.sigit.mechaban.dashboard.customer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.garage.AddCarActivity;

public class GarageFragment extends Fragment {

    public GarageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garage, container, false);

        Button tambahMobilBtn = view.findViewById(R.id.tambah_mobil_btn);
        tambahMobilBtn.setOnClickListener(v -> requireActivity().startActivity(new Intent(getActivity(), AddCarActivity.class)));

        return view;
    }
}