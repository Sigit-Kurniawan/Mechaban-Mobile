package com.sigit.mechaban.dashboard.customer.garage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sigit.mechaban.R;

import java.util.Objects;

public class AddCarActivity extends AppCompatActivity {
    private EditText etAngka;
    private EditText etHuruf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tambah Mobil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText etKodeWilayah = findViewById(R.id.etKodeWilayah);
        etAngka = findViewById(R.id.etAngka);
        etHuruf = findViewById(R.id.etHuruf);

        // Pindah otomatis dari etKodeWilayah ke etAngka
        etKodeWilayah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    etAngka.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Pindah otomatis dari etAngka ke etHuruf
        etAngka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {  // Maksimal 4 digit
                    etHuruf.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Tidak perlu auto move untuk etHuruf karena ini adalah yang terakhir
        etHuruf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kamu bisa menambahkan aksi lain di sini jika diperlukan
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}