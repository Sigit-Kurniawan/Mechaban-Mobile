package com.sigit.mechaban.dashboard.customer.garage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.car.CarAPI;
import com.sigit.mechaban.object.Car;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.lang.reflect.Field;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarActivity extends AppCompatActivity {
    private TextInputEditText kodeWilayahEditText, angkaEditText, hurufEditText, merkEditText, typeEditText, transmitionEditText, yearEditText;
    private String nopol, merk, type, transmition, year, email;
    private int transmitionOption;
    private Button saveButton;
    private final Car car = new Car();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kodeWilayahEditText = findViewById(R.id.etKodeWilayah);
        angkaEditText = findViewById(R.id.etAngka);
        hurufEditText = findViewById(R.id.etHuruf);
        merkEditText = findViewById(R.id.merk_field);
        typeEditText = findViewById(R.id.type_field);
        transmitionEditText = findViewById(R.id.transmition_field);
        yearEditText = findViewById(R.id.year_field);
        saveButton = findViewById(R.id.save_button);

        saveButton.setEnabled(false);

        setUpEditTextNavigation(kodeWilayahEditText, angkaEditText, 2);
        setUpEditTextNavigation(angkaEditText, hurufEditText, 4);
        setUpEditTextNavigation(hurufEditText, null, 3);

        setUpBackspaceNavigation(kodeWilayahEditText, null);
        setUpBackspaceNavigation(angkaEditText, kodeWilayahEditText);
        setUpBackspaceNavigation(hurufEditText, angkaEditText);

        setUpNextButtonNavigation(kodeWilayahEditText, angkaEditText);
        setUpNextButtonNavigation(angkaEditText, hurufEditText);
        setUpNextButtonNavigation(hurufEditText, merkEditText);

        setUpNoSpaceFilter(kodeWilayahEditText);
        setUpNoSpaceFilter(angkaEditText);
        setUpNoSpaceFilter(hurufEditText);

        transmitionEditText.setOnClickListener(v -> showDropdownTransmitionBottomSheet());

        yearEditText.setOnClickListener(v -> showYearPickerBottomSheet());

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setEnabled(!Objects.requireNonNull(kodeWilayahEditText.getText()).toString().isEmpty() &&
                        !Objects.requireNonNull(angkaEditText.getText()).toString().isEmpty() &&
                        !Objects.requireNonNull(hurufEditText.getText()).toString().isEmpty() &&
                        !Objects.requireNonNull(merkEditText.getText()).toString().isEmpty() &&
                        !Objects.requireNonNull(typeEditText.getText()).toString().isEmpty() &&
                        !Objects.requireNonNull(transmitionEditText.getText()).toString().isEmpty() &&
                        !Objects.requireNonNull(yearEditText.getText()).toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        kodeWilayahEditText.addTextChangedListener(watcher);
        angkaEditText.addTextChangedListener(watcher);
        hurufEditText.addTextChangedListener(watcher);
        merkEditText.addTextChangedListener(watcher);
        typeEditText.addTextChangedListener(watcher);
        transmitionEditText.addTextChangedListener(watcher);
        yearEditText.addTextChangedListener(watcher);

        TextView title = findViewById(R.id.toolbar_title);
        Button deleteButton = findViewById(R.id.delete_button);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("nopol")) {
            title.setText("Detail Mobil");
            deleteButton.setVisibility(View.VISIBLE);
            car.setAction("detail");
            car.setNopol(intent.getStringExtra("nopol"));
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<CarAPI> readCarCall = apiInterface.carResponse(car);
            readCarCall.enqueue(new Callback<CarAPI>() {
                @Override
                public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        String[] parts = response.body().getCarData().getNopol().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                        if (parts.length == 3) {
                            kodeWilayahEditText.setText(parts[0]);
                            angkaEditText.setText(parts[1]);
                            hurufEditText.setText(parts[2]);
                        }
                        merkEditText.setText(response.body().getCarData().getMerk());
                        typeEditText.setText(response.body().getCarData().getType());
                        transmitionEditText.setText(response.body().getCarData().getTransmition());
                        yearEditText.setText(response.body().getCarData().getYear());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                    Log.e("AddCarActivity", t.toString(), t);
                }
            });

            saveButton.setOnClickListener(v -> {
                nopol = String.join("", Objects.requireNonNull(kodeWilayahEditText.getText()).toString(), Objects.requireNonNull(angkaEditText.getText()).toString(), Objects.requireNonNull(hurufEditText.getText()).toString());
                merk = Objects.requireNonNull(merkEditText.getText()).toString();
                type = Objects.requireNonNull(typeEditText.getText()).toString();
                transmition = Objects.requireNonNull(transmitionEditText.getText()).toString().toLowerCase();
                year = Objects.requireNonNull(yearEditText.getText()).toString();
                email = new SessionManager(this).getUserDetail().get("email");

                car.setAction("update");
                car.setNopol(intent.getStringExtra("nopol"));
                car.setNopolUpdate(nopol);
                car.setMerk(merk);
                car.setType(type);
                car.setTransmition(transmition);
                car.setYear(year);

                Call<CarAPI> createCarCall = apiInterface.carResponse(car);
                createCarCall.enqueue(new Callback<CarAPI>() {
                    @Override
                    public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                        if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                            Toast.makeText(AddCarActivity.this, "Berhasil update mobil", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddCarActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                        Log.e("AddCarActivity", t.toString(), t);
                    }
                });
            });

            deleteButton.setOnClickListener(v -> {
                car.setAction("delete");
                car.setNopol(intent.getStringExtra("nopol"));

                Call<CarAPI> deleteCarCall = apiInterface.carResponse(car);
                deleteCarCall.enqueue(new Callback<CarAPI>() {
                    @Override
                    public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                        if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                            Toast.makeText(AddCarActivity.this, "Berhasil hapus mobil", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddCarActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                        Log.e("AddCarActivity", t.toString(), t);
                    }
                });
            });
        }

        saveButton.setOnClickListener(v -> {
            nopol = String.join("", Objects.requireNonNull(kodeWilayahEditText.getText()).toString(), Objects.requireNonNull(angkaEditText.getText()).toString(), Objects.requireNonNull(hurufEditText.getText()).toString());
            merk = Objects.requireNonNull(merkEditText.getText()).toString();
            type = Objects.requireNonNull(typeEditText.getText()).toString();
            transmition = Objects.requireNonNull(transmitionEditText.getText()).toString().toLowerCase();
            year = Objects.requireNonNull(yearEditText.getText()).toString();
            email = new SessionManager(this).getUserDetail().get("email");

            car.setAction("create");
            car.setNopol(nopol);
            car.setMerk(merk);
            car.setType(type);
            car.setTransmition(transmition);
            car.setYear(year);
            car.setEmail(email);

            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<CarAPI> createCarCall = apiInterface.carResponse(car);
            createCarCall.enqueue(new Callback<CarAPI>() {
                @Override
                public void onResponse(@NonNull Call<CarAPI> call, @NonNull Response<CarAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        Toast.makeText(AddCarActivity.this, "Berhasil menambahkan mobil", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddCarActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CarAPI> call, @NonNull Throwable t) {
                    Log.e("AddCarActivity", t.toString(), t);
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpEditTextNavigation(EditText currentEditText, EditText nextEditText, int maxLength) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= maxLength && nextEditText != null) {
                    nextEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setUpBackspaceNavigation(EditText currentEditText, EditText previousEditText) {
        currentEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (currentEditText.getText().toString().isEmpty() && previousEditText != null) {
                    previousEditText.requestFocus();
                    previousEditText.setSelection(previousEditText.getText().length());
                }
            }
            return false;
        });
    }

    private void setUpNextButtonNavigation(EditText currentEditText, EditText nextEditText) {
        currentEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT && nextEditText != null) {
                nextEditText.requestFocus();
                return true;
            }
            return false;
        });
    }

    private void setUpNoSpaceFilter(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    editText.setText(s.toString().replace(" ", ""));
                    editText.setSelection(editText.getText().length());  // Set cursor to the end
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @SuppressLint("SetTextI18n")
    private void showDropdownTransmitionBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_transmition, null, false);

        if (transmitionOption == 1) {
            view.findViewById(R.id.check_auto).setVisibility(View.VISIBLE);
        } else if (transmitionOption == 2) {
            view.findViewById(R.id.check_manual).setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.auto_button).setOnClickListener(v -> {
            transmitionEditText.setText("Auto");
            transmitionOption = 1;
            bottomSheetDialog.dismiss();
        });

        view.findViewById(R.id.manual_button).setOnClickListener(v -> {
            transmitionEditText.setText("Manual");
            transmitionOption = 2;
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showYearPickerBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_year_picker, null, false);

        NumberPicker yearPicker = bottomSheetView.findViewById(R.id.year_picker);
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(currentYear);
        yearPicker.setValue(currentYear);
        yearPicker.setWrapSelectorWheel(false);
//        setNumberPickerTextSize(yearPicker, 24f);

        Button btnConfirm = bottomSheetView.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            int selectedYear = yearPicker.getValue();
            yearEditText.setText(String.valueOf(selectedYear));
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void setNumberPickerTextSize(NumberPicker numberPicker, float textSize) {
        try {
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field field : pickerFields) {
                if (field.getName().equals("mSelectorWheelPaint")) {
                    field.setAccessible(true);
                    ((android.graphics.Paint) field.get(numberPicker)).setTextSize(textSize);
                    numberPicker.invalidate();
                    break;
                }
            }

            // Cari dan ubah semua TextView di dalam NumberPicker
            for (int i = 0; i < numberPicker.getChildCount(); i++) {
                View child = numberPicker.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTextSize(textSize);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}