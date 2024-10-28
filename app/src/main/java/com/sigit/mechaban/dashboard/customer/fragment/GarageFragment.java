package com.sigit.mechaban.dashboard.customer.fragment;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GarageFragment extends Fragment {
    private SessionManager sessionManager;
    private RecyclerView carList;
    private final CallbackInterface callback;
    private final Car car = new Car();

    public interface CallbackInterface {
        void updateFabVisibility(boolean isCarListEmpty);
    }

    public GarageFragment(CallbackInterface callback) {
        this.callback = callback;
    }

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
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                    if (response.body().isExist()) {
                        requireView().findViewById(R.id.empty_view).setVisibility(View.GONE);
                        requireView().findViewById(R.id.car_list_view).setVisibility(View.VISIBLE);
                        List<CarAdapter.CarItem> carItemList = new ArrayList<>();
                        for (CarData carData : response.body().getCarData()) {
                            carItemList.add(new CarAdapter.CarItem(carData.getMerk(), carData.getType(), carData.getYear()));
                        }
                        carList.setLayoutManager(new LinearLayoutManager(getContext()));
                        carList.setAdapter(new CarAdapter(requireActivity().getApplicationContext(), carItemList));
                    } else {
                        callback.updateFabVisibility(false);
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
    }
}