package com.sigit.mechaban.dashboard.customer.service;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shuhart.stepview.StepView;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.service.ServiceAPI;
import com.sigit.mechaban.api.model.service.ServiceData;
import com.sigit.mechaban.components.DetailBottomSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity implements ServiceAdapter.OnItemSelectedListener {
    private TextView priceTextView;
    private int totalPrice = 0, currentStep = 0;
    private final List<ServiceAdapter.ServiceItem> selectedServices = new ArrayList<>();
    private StepView stepView;
    private ViewFlipper viewFlipper;
    private final List<AccordionAdapter.AccordionItem> itemList = new ArrayList<>();
    private AccordionAdapter accordionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        stepView = findViewById(R.id.step_view);
        stepView.getState()
                .steps(new ArrayList<String>() {{
                    add("Pilih Servis");
                    add("Pilih Lokasi");
                    add("Konfirmasi");
                }})
                .commit();

        viewFlipper = findViewById(R.id.view_flipper);

        RecyclerView serviceListRecyclerView = findViewById(R.id.service_list);
        serviceListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        accordionAdapter = new AccordionAdapter(itemList, this);
        serviceListRecyclerView.setAdapter(accordionAdapter);

        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ServiceAPI> serviceCall = apiInterface.serviceResponse();
        serviceCall.enqueue(new Callback<ServiceAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ServiceAPI> call, @NonNull Response<ServiceAPI> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Map<String, List<ServiceData>> serviceMap = response.body().getComponent();

                    for (String key : serviceMap.keySet()) {
                        List<ServiceAdapter.ServiceItem> serviceItems = new ArrayList<>();
                        for (ServiceData serviceData : Objects.requireNonNull(serviceMap.get(key))) {
                            serviceItems.add(new ServiceAdapter.ServiceItem(
                                    serviceData.getService(),
                                    serviceData.getPriceService()
                            ));
                        }

                        itemList.add(new AccordionAdapter.AccordionItem(
                                R.drawable.oil,
                                key,
                                serviceItems
                        ));
                    }

                    accordionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceAPI> call, @NonNull Throwable t) {
                Log.e("ServiceActivity", t.toString(), t);
            }
        });

        priceTextView = findViewById(R.id.price);

        findViewById(R.id.detail_service).setOnClickListener(v -> new DetailBottomSheet(totalPrice, selectedServices).show(getSupportFragmentManager(), "ModalBottomSheet"));

        findViewById(R.id.next_button).setOnClickListener(v -> onNextClicked());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backPressed();
            }
        });
    }

    private void onNextClicked() {
        if (currentStep < stepView.getStepCount() - 1) {
            currentStep++;
            stepView.go(currentStep, true);
            viewFlipper.showNext();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String service, int price, boolean isSelected) {
        if (isSelected) {
            totalPrice += price;
            selectedServices.add(new ServiceAdapter.ServiceItem(service, price));
        } else {
            totalPrice -= price;
            selectedServices.removeIf(item -> item.getService().equals(service));
        }
        priceTextView.setText(String.valueOf(totalPrice));
    }

    private void backPressed() {
        if (currentStep == 1) {
            currentStep--;
            stepView.go(currentStep, true);
            viewFlipper.showPrevious();
        } else if (currentStep == 0) {
            finish();
        }
    }
}