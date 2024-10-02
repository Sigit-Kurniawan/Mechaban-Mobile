package com.sigit.mechaban.dashboard.customer.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sigit.mechaban.R;
import com.sigit.mechaban.dashboard.customer.activity.recyclerview.ActivityAdapter;
import com.sigit.mechaban.dashboard.customer.activity.recyclerview.ActivityItem;

import java.util.ArrayList;
import java.util.List;


public class ActivityFragment extends Fragment {

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.activity_recyclerview);

        List<ActivityItem> activityItemList = new ArrayList<>();
        activityItemList.add(new ActivityItem("1 Okt 2023", "Perbaikan Mesin", "Rp100.000"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ActivityAdapter(requireActivity().getApplicationContext(), activityItemList));

        return view;
    }
}