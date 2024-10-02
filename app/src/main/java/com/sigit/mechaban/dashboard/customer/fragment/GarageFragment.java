package com.sigit.mechaban.dashboard.customer.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.garage.recyclerview.CarAdapter;
import com.sigit.mechaban.dashboard.customer.garage.recyclerview.CarItem;

import java.util.ArrayList;
import java.util.List;

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

        RecyclerView recyclerView = view.findViewById(R.id.car_recycler_view);

        List<CarItem> carItemList = new ArrayList<>();
        carItemList.add(new CarItem("Toyota", "Avanza", "2012"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CarAdapter(requireActivity().getApplicationContext(), carItemList));

//        Button tambahMobilBtn = view.findViewById(R.id.tambah_mobil_btn);
//        tambahMobilBtn.setOnClickListener(v -> requireActivity().startActivity(new Intent(getActivity(), AddCarActivity.class)));

        return view;
    }
}