package com.sigit.mechaban.dashboard.customer.fragment;

import android.content.SharedPreferences;
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
import com.sigit.mechaban.api.model.car.CarAPI;
import com.sigit.mechaban.api.model.car.CarData;
import com.sigit.mechaban.dashboard.customer.garage.CarAdapter;
import com.sigit.mechaban.object.Car;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GarageFragment extends Fragment {
    private SessionManager sessionManager;
    private RecyclerView carList;
    private final Car car = new Car();
    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences, key) -> {
        if (Objects.requireNonNull(key).equals("nopol")) {
            setCar();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garage, container, false);

        sessionManager = new SessionManager(requireActivity());
        carList = view.findViewById(R.id.car_recycler_view);

        setCar();

        return view;
    }

    private void setCar() {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        car.setAction("read");
        car.setEmail(sessionManager.getUserDetail().get("email"));
        Call<CarAPI> readCarCall = apiInterface.carResponse(car);
        readCarCall.enqueue(new Callback<CarAPI>() {
            @Override
            public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getCode() == 201) {
                        requireView().findViewById(R.id.empty_view).setVisibility(View.GONE);
                        requireView().findViewById(R.id.car_list_view).setVisibility(View.VISIBLE);
                        List<CarAdapter.CarItem> carItemList = new ArrayList<>();
                        for (CarData carData : response.body().getListCarData()) {
                            carItemList.add(new CarAdapter.CarItem(carData.getNopol(), carData.getMerk(), carData.getType(), carData.getYear(), carData.getStatus()));
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
                    } else if (response.body() != null && response.body().getCode() == 200) {
                        requireView().findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                        requireView().findViewById(R.id.car_list_view).setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                Log.e("GarageFragment", t.toString(), t);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setCar();
        sessionManager.getPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        sessionManager.getPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}