package com.sigit.mechaban.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.object.Account;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {
    private final Account account = new Account();
    private final LoadingDialog loadingDialog = new LoadingDialog(this);
    private TextInputEditText code1, code2, code3, code4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        code1 = findViewById(R.id.kode1field);
        code2 = findViewById(R.id.kode2field);
        code3 = findViewById(R.id.kode3field);
        code4 = findViewById(R.id.kode4field);

        setUpEditTextNavigation(code1, code2);
        setUpEditTextNavigation(code2, code3);
        setUpEditTextNavigation(code3, code4);

        setUpBackspaceNavigation(code4, code3);
        setUpBackspaceNavigation(code3, code2);
        setUpBackspaceNavigation(code2, code1);

        setUpNextButtonNavigation(code1, code2);
        setUpNextButtonNavigation(code2, code3);
        setUpNextButtonNavigation(code3, code4);

        findViewById(R.id.send_otp_btn).setOnClickListener(v -> {
            Intent intent = getIntent();
            account.setAction("register");
            account.setName(intent.getStringExtra("name"));
            account.setEmail(intent.getStringExtra("email"));
            account.setNo_hp(intent.getStringExtra("noHP"));
            account.setPassword(intent.getStringExtra("password"));
            account.setOtp(String.join("", Objects.requireNonNull(code1.getText()).toString(), Objects.requireNonNull(code2.getText()).toString(), Objects.requireNonNull(code3.getText()).toString(), Objects.requireNonNull(code4.getText()).toString()));

            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<AccountAPI> verifyCall = apiInterface.accountResponse(account);
            verifyCall.enqueue(new Callback<AccountAPI>() {
                @Override
                public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        Toast.makeText(VerifyOtpActivity.this, "Register berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                    Log.e("VerifyOtpActivity", t.toString(), t);
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

    private void registerEvent() {
        loadingDialog.startLoadingDialog();

        Intent intent = getIntent();
        account.setAction("register");
        account.setName(intent.getStringExtra("name"));
        account.setEmail(intent.getStringExtra("email"));
        account.setNo_hp(intent.getStringExtra("noHP"));
        account.setPassword(intent.getStringExtra("password"));
//        if (new Connection(this).isNetworkAvailable()) {
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<AccountAPI> registerCall = apiInterface.accountResponse(account);
            registerCall.enqueue(new Callback<AccountAPI>() {
                @Override
                public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        Toast.makeText(VerifyOtpActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                        loadingDialog.dismissDialog();
                    }
//                    else {
//                        loadingDialog.dismissDialog();
//                        if (response.body().getMessage().equals("Email telah terdaftar")) {
//                            isValidateEmail = false;
//                            emailLayout.setError(response.body().getMessage());
//                            emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
//                        } else {
//                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                    Log.e("RegisterActivity", t.toString(), t);
                }
            });
//        } else {
//            loadingDialog.dismissDialog();
//            new ModalBottomSheet(R.drawable.no_internet,
//                    "Tidak Terhubung dengan Internet",
//                    "Periksa kembali koneksi internet Anda.",
//                    "Coba Lagi",
//                    this)
//                    .show(getSupportFragmentManager(), "ModalBottomSheet");
//        }
    }

    private void setUpEditTextNavigation(EditText currentEditText, EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1 && nextEditText != null) {
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
}