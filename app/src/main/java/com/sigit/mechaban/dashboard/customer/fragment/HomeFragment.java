package com.sigit.mechaban.dashboard.customer.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.car.CarAPI;
import com.sigit.mechaban.api.model.car.CarData;
import com.sigit.mechaban.dashboard.customer.garage.CarAdapter;
import com.sigit.mechaban.dashboard.customer.service.ServiceActivity;
import com.sigit.mechaban.object.Car;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private TextView merkTextView, nopolTextView;
    private final Car car = new Car();
    private SessionManager sessionManager;
    private RecyclerView carList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout carButton = view.findViewById(R.id.car_button);

        sessionManager = new SessionManager(requireActivity());

        carButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setScaleX(0.98f);
                    v.setScaleY(0.98f);
                    break;
                case MotionEvent.ACTION_UP:
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);
                    v.performClick();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setScaleX(1.0f);
                    v.setScaleY(1.0f);
                    break;
            }
            return true;
        });

        carButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.bottom_sheet_car_list, null, false);
            carList = view1.findViewById(R.id.car_recycler);

            car.setAction("read");
            car.setEmail(sessionManager.getUserDetail().get("email"));
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<CarAPI> readCarCall = apiInterface.carResponse(car);
            readCarCall.enqueue(new Callback<CarAPI>() {
                @Override
                public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        if (response.body().isExist()) {
                            List<CarAdapter.CarItem> carItemList = new ArrayList<>();
                            for (CarData carData : response.body().getListCarData()) {
                                carItemList.add(new CarAdapter.CarItem(carData.getNopol(), carData.getMerk(), carData.getType(), carData.getYear()));
                            }

                            String savedNopol = sessionManager.getUserDetail().get("nopol");
                            int savedPosition = -1;
                            if (savedNopol != null) {
                                for (int i = 0; i < carItemList.size(); i++) {
                                    if (carItemList.get(i).getNopol().equals(savedNopol)) {
                                        savedPosition = i;
                                        break;
                                    }
                                }
                            }

                            carList.setLayoutManager(new LinearLayoutManager(getContext()));
                            carList.setAdapter(new CarAdapter(requireActivity().getApplicationContext(), carItemList, savedPosition));

                            bottomSheetDialog.setContentView(view1);
                            bottomSheetDialog.show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                    Log.e("GarageFragment", t.toString(), t);
                }
            });
        });

        view.findViewById(R.id.service_button).setOnClickListener(v -> startActivity(new Intent(getActivity(), ServiceActivity.class)));

        view.findViewById(R.id.car_service_card).setBackgroundResource(R.drawable.background_component);

        merkTextView = view.findViewById(R.id.merk_text);
        nopolTextView = view.findViewById(R.id.nopol_text);
        setCarSelected();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCarSelected();
    }

    private void setCarSelected() {
        car.setAction("detail");
        car.setNopol(sessionManager.getUserDetail().get("nopol"));
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<CarAPI> readDetailCall = apiInterface.carResponse(car);
        readDetailCall.enqueue(new Callback<CarAPI>() {
            @Override
            public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    merkTextView.setText(response.body().getCarData().getMerk());
                    String[] parts = response.body().getCarData().getNopol().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                    nopolTextView.setText(String.join(" ", parts));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                Log.e("HomeFragment", t.toString(), t);
            }
        });
    }
}