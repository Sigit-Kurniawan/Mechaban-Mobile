package com.sigit.mechaban.dashboard.customer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

//        RecyclerView recyclerView = view.findViewById(R.id.car_recycler_view);

//        List<CarItem> carItemList = new ArrayList<>();
//        carItemList.add(new CarItem("Toyota", "Avanza", "2012"));
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new CarAdapter(requireActivity().getApplicationContext(), carItemList));

        view.findViewById(R.id.tambah_mobil_btn).setOnClickListener(v -> requireActivity().startActivity(new Intent(getActivity(), AddCarActivity.class)));

        return view;
    }
}