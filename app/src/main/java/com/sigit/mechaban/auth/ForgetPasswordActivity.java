package com.sigit.mechaban.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.components.EditTextBehavior;
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.components.ModalBottomSheet;
import com.sigit.mechaban.object.Account;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {
    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;
    private Button sendOtpButton;
    private String email;
    private boolean isValidateEmail;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);
    private final Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailLayout = findViewById(R.id.email);
        emailEditText = findViewById(R.id.email_field);
        sendOtpButton = findViewById(R.id.send_otp);
        
        sendOtpButton.setEnabled(false);
        isValidateEmail = true;

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidateEmail = EditTextBehavior.validateEmail(getApplicationContext(), emailEditText, emailLayout);
                updateSendOtpButtonState();
            }
        });

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), emailLayout, hasFocus, isValidateEmail));

        sendOtpButton.setOnClickListener(v -> {
            loadingDialog.startLoadingDialog();
            email = Objects.requireNonNull(emailEditText.getText()).toString().trim();

            account.setAction("verification_forget");
            account.setEmail(email);

            sendOtpEvent();
        });

        emailEditText.requestFocus();
    }

    private void sendOtpEvent() {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AccountAPI> sendOtpCall = apiInterface.accountResponse(account);
        sendOtpCall.enqueue(new Callback<AccountAPI>() {
            @Override
            public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("isForgetPassword", true);
                    startActivity(intent);
                    finish();
                    loadingDialog.dismissDialog();
                } else if (response.body() != null && response.body().getCode() == 404) {
                    loadingDialog.dismissDialog();
                    isValidateEmail = false;
                    emailLayout.setError("Email belum terdaftar!");
                    emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                loadingDialog.dismissDialog();
                Log.e("ForgetPassword", t.toString(), t);
                new ModalBottomSheet(R.drawable.not_found,
                        "Tidak Ditemukan",
                        "Coba lagi lain kali.",
                        "Coba Lagi",
                        ForgetPasswordActivity.this)
                        .show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
    }

    private void updateSendOtpButtonState() {
        email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        if (!email.isEmpty()) {
            sendOtpButton.setEnabled(isValidateEmail);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void buttonBottomSheet() {
        sendOtpEvent();
    }
}