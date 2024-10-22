package com.sigit.mechaban.dashboard.customer.garage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.createcar.CreateCar;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarActivity extends AppCompatActivity {
    private TextInputEditText kodeWilayahEditText, angkaEditText, hurufEditText, merkEditText, typeEditText, variantEditText, yearEditText;
    private String nopol, merk, type, variant, year, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tambah Mobil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kodeWilayahEditText = findViewById(R.id.etKodeWilayah);
        angkaEditText = findViewById(R.id.etAngka);
        hurufEditText = findViewById(R.id.etHuruf);
        merkEditText = findViewById(R.id.merk_field);
        typeEditText = findViewById(R.id.type_field);
        variantEditText = findViewById(R.id.variant_field);
        yearEditText = findViewById(R.id.year_field);

        kodeWilayahEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    angkaEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        angkaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    hurufEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.save_button).setOnClickListener(v -> {
            nopol = String.join("", Objects.requireNonNull(kodeWilayahEditText.getText()).toString(), Objects.requireNonNull(angkaEditText.getText()).toString(), Objects.requireNonNull(hurufEditText.getText()).toString());
            merk = Objects.requireNonNull(merkEditText.getText()).toString();
            type = Objects.requireNonNull(typeEditText.getText()).toString();
            variant = Objects.requireNonNull(variantEditText.getText()).toString();
            year = Objects.requireNonNull(yearEditText.getText()).toString();
            email = new SessionManager(this).getUserDetail().get("email");

            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<CreateCar> createCarCall = apiInterface.createCarResponse(nopol, merk, type, variant, year, email);
            createCarCall.enqueue(new Callback<CreateCar>() {
                @Override
                public void onResponse(@NonNull Call<CreateCar> call, @NonNull Response<CreateCar> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        Toast.makeText(AddCarActivity.this, "Berhasil menambahkan mobil", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddCarActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CreateCar> call, @NonNull Throwable t) {
                    Log.e("AddCarActivity", t.toString(), t);
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}