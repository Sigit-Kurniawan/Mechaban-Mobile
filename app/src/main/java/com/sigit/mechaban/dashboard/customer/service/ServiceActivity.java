package com.sigit.mechaban.dashboard.customer.service;

import android.os.Bundle;
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
import com.sigit.mechaban.components.DetailBottomSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceActivity extends AppCompatActivity implements ServiceAdapter.OnItemSelectedListener {
    private TextView priceTextView;
    private int totalPrice = 0, currentStep = 0;
    private final List<ServiceAdapter.ServiceItem> selectedServices = new ArrayList<>();
    private StepView stepView;
    private ViewFlipper viewFlipper;

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

        List<AccordionAdapter.AccordionItem> itemList = new ArrayList<>();
        List<ServiceAdapter.ServiceItem> oilServices = new ArrayList<>();
        oilServices.add(new ServiceAdapter.ServiceItem("Ganti Oli", 20000));
        oilServices.add(new ServiceAdapter.ServiceItem("Tambah Oli", 3000));

        List<ServiceAdapter.ServiceItem> tireServices = new ArrayList<>();
        tireServices.add(new ServiceAdapter.ServiceItem("Tambah angin", 900000));
        tireServices.add(new ServiceAdapter.ServiceItem("Ganti ban", 1000000));
        tireServices.add(new ServiceAdapter.ServiceItem("Tambah ban", 23000));

        List<ServiceAdapter.ServiceItem> engineServices = new ArrayList<>();
        engineServices.add(new ServiceAdapter.ServiceItem("Ganti kalbulator", 2000000));

        itemList.add(new AccordionAdapter.AccordionItem(R.drawable.oil, "Oli", oilServices));
        itemList.add(new AccordionAdapter.AccordionItem(R.drawable.oil, "Ban", tireServices));
        itemList.add(new AccordionAdapter.AccordionItem(R.drawable.oil, "Mesin", engineServices));

        AccordionAdapter adapter = new AccordionAdapter(itemList, this);
        serviceListRecyclerView.setAdapter(adapter);

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