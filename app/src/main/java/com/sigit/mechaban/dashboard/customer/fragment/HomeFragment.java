package com.sigit.mechaban.dashboard.customer.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sigit.mechaban.BuildConfig;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.car.CarAPI;
import com.sigit.mechaban.api.model.car.CarData;
import com.sigit.mechaban.api.model.status.StatusAPI;
import com.sigit.mechaban.dashboard.customer.consultation.ConsultationActivity;
import com.sigit.mechaban.dashboard.customer.garage.CarAdapter;
import com.sigit.mechaban.dashboard.customer.service.ServiceActivity;
import com.sigit.mechaban.object.Car;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CarAdapter.OnCarSelectedListener {
    private TextView merkTextView, nopolTextView, statusTextView;
    private final Car car = new Car();
    private SessionManager sessionManager;
    private RecyclerView carList;
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private BottomSheetDialog bottomSheetDialog;
    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences, key) -> {
        if (Objects.requireNonNull(key).equals("nopol")) {
            setCarSelected();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        statusTextView = view.findViewById(R.id.status_text);
        Call<StatusAPI> statusAPICall = apiInterface.statusResponse();
        statusAPICall.enqueue(new Callback<StatusAPI>() {
            @Override
            public void onResponse(@NonNull Call<StatusAPI> call, @NonNull Response<StatusAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    switch (response.body().getData()) {
                        case 0:
                            statusTextView.setText("Tutup");
                            break;
                        case 1:
                            statusTextView.setText("Buka");
                            break;
                    }
                } else {
                    Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<StatusAPI> call, @NonNull Throwable t) {
                Log.e("Status", t.toString(), t);
            }
        });

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

        carButton.setOnClickListener(v -> openCarList());

        view.findViewById(R.id.service_button).setOnClickListener(v -> {
            if (sessionManager.getPreferences().contains("nopol")) {
                startActivity(new Intent(getActivity(), ServiceActivity.class));
            } else {
                bottomSheetDialog = new BottomSheetDialog(requireActivity());
                @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.bottom_sheet_modal, null, false);
                ImageView imageView = view1.findViewById(R.id.photo);
                imageView.setImageResource(R.drawable.choose);

                TextView title = view1.findViewById(R.id.title);
                title.setText("Pilih Dulu Mobilnya");

                TextView desc = view1.findViewById(R.id.description);
                desc.setText("Kalau tidak ada mobil yang dipilih, kami servis apa?");

                Button close = view1.findViewById(R.id.button);
                close.setText("Tutup");
                close.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });

        view.findViewById(R.id.consultation_button).setOnClickListener(v -> startActivity(new Intent(getActivity(), ConsultationActivity.class)));

        view.findViewById(R.id.car_service_card).setBackgroundResource(R.drawable.background_component);

        merkTextView = view.findViewById(R.id.merk_text);
        nopolTextView = view.findViewById(R.id.nopol_text);
        setCarSelected();

        view.findViewById(R.id.consul_button).setOnClickListener(v -> startActivity(new Intent(requireActivity(), ConsultationActivity.class)));

        view.findViewById(R.id.contact_us_button).setOnClickListener(v -> {
            try {
                String phoneNumber = "+6283832566069";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/" + phoneNumber.replace("+", "").replace(" ", "")));
                startActivity(intent);
            } catch (Exception e) {
                Log.e("ContactUs", e.toString(), e);
            }
        });

        view.findViewById(R.id.about_us_button).setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + BuildConfig.ip + "/Mechaban-Web"))));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCarSelected();
        sessionManager.getPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        setCarSelected();
        sessionManager.getPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void setCarSelected() {
        car.setAction("detail");
        car.setNopol(sessionManager.getUserDetail().get("nopol"));
        Call<CarAPI> readDetailCall = apiInterface.carResponse(car);
        readDetailCall.enqueue(new Callback<CarAPI>() {
            @Override
            public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
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

    private void openCarList() {
        bottomSheetDialog = new BottomSheetDialog(requireActivity());
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_car_list, null, false);
        carList = view.findViewById(R.id.car_recycler);

        loadCarList();

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    @Override
    public void onCarSelected(String nopol, String merk) {
        merkTextView.setText(merk);
        String[] parts = nopol.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        nopolTextView.setText(String.join(" ", parts));
        bottomSheetDialog.dismiss();
    }

    private void loadCarList() {
        car.setAction("read");
        car.setEmail(sessionManager.getUserDetail().get("email"));
        Call<CarAPI> readCarCall = apiInterface.carResponse(car);
        readCarCall.enqueue(new Callback<CarAPI>() {
            @Override
            public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                    if (response.body().isExist()) {
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
                        carList.setAdapter(new CarAdapter(requireActivity().getApplicationContext(), carItemList, savedPosition, HomeFragment.this));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                Log.e("GarageFragment", t.toString(), t);
            }
        });
    }
}