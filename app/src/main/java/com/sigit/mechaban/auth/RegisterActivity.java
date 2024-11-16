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
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.components.ModalBottomSheet;
import com.sigit.mechaban.components.EditTextBehavior;
import com.sigit.mechaban.connection.Connection;
import com.sigit.mechaban.object.Account;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {
    private TextInputEditText nameEditText, emailEditText, noHPEditText, passwordEditText, confirmPasswordEditText;
    private TextInputLayout nameLayout, emailLayout, noHPLayout, passwordLayout, confirmPasswordLayout;
    private Button registerButton;
    private boolean isValidateName, isValidateEmail, isValidateNoHP, isValidatePassword, isValidateConfirmPassword;
    private String name, email, noHP, password;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);
    private final Account account = new Account();
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameLayout = findViewById(R.id.name);
        nameEditText = findViewById(R.id.name_field);
        emailLayout = findViewById(R.id.email);
        emailEditText = findViewById(R.id.email_field);
        noHPLayout = findViewById(R.id.no_hp);
        noHPEditText = findViewById(R.id.nohp_field);
        passwordLayout = findViewById(R.id.password);
        passwordEditText = findViewById(R.id.pass_field);
        confirmPasswordLayout = findViewById(R.id.confirm_password);
        confirmPasswordEditText = findViewById(R.id.confirm_pass_field);
        registerButton = findViewById(R.id.register_button);

        registerButton.setEnabled(false);
        isValidateName = true;
        isValidateEmail = true;
        isValidateNoHP = true;
        isValidatePassword = true;
        isValidateConfirmPassword = true;

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidateName = EditTextBehavior.validateName(getApplicationContext(), nameEditText, nameLayout);
                updateRegisterButtonState();
            }
        });

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
                updateRegisterButtonState();
            }
        });

        noHPEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidateNoHP = EditTextBehavior.validateNoHP(getApplicationContext(), noHPEditText, noHPLayout);
                updateRegisterButtonState();
            }
        });
        
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidatePassword = EditTextBehavior.validatePasswordRegister(getApplicationContext(), passwordEditText, passwordLayout);
                updateRegisterButtonState();
            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidateConfirmPassword = EditTextBehavior.validateConfirmPassword(getApplicationContext(), passwordEditText, confirmPasswordEditText, confirmPasswordLayout);
                updateRegisterButtonState();
            }
        });

        nameEditText.setOnFocusChangeListener(((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), nameLayout, hasFocus, isValidateName)));

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), emailLayout, hasFocus, isValidateEmail));

        noHPEditText.setOnFocusChangeListener(((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), noHPLayout, hasFocus, isValidateNoHP)));

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), passwordLayout, hasFocus, isValidatePassword));

        confirmPasswordEditText.setOnFocusChangeListener(((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), confirmPasswordLayout, hasFocus, isValidateConfirmPassword)));

        registerButton.setOnClickListener(v -> {
            name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
            email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
            noHP = Objects.requireNonNull(noHPEditText.getText()).toString().trim();
            password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            account.setAction("verification");
            account.setEmail(email);

            registerEvent();
        });

        findViewById(R.id.login_hyperlink).setOnClickListener(v -> finish());
    }

    private void updateRegisterButtonState() {
        name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
        email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        noHP = Objects.requireNonNull(noHPEditText.getText()).toString().trim();
        password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString().trim();
        if (!name.isEmpty() && !email.isEmpty() && !noHP.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            registerButton.setEnabled(isValidateName && isValidateEmail && isValidateNoHP && isValidatePassword && isValidateConfirmPassword);
        }
    }

    private void registerEvent() {
        loadingDialog.startLoadingDialog();
        if (new Connection(this).isNetworkAvailable()) {
            Call<AccountAPI> sendOtpCall = apiInterface.accountResponse(account);
            sendOtpCall.enqueue(new Callback<AccountAPI>() {
                @Override
                public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                        intent.putExtra("isForgetPassword", false);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("noHP", noHP);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                        loadingDialog.dismissDialog();
                    } else if (response.body() != null && response.body().getCode() == 400) {
                        loadingDialog.dismissDialog();
                        isValidateEmail = false;
                        emailLayout.setError("Email telah terdaftar!");
                        emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                    loadingDialog.dismissDialog();
                    Log.e("RegisterActivity", t.toString(), t);
                    new ModalBottomSheet(R.drawable.not_found,
                            "Tidak Ditemukan",
                            "Coba lagi lain kali.",
                            "Coba Lagi",
                            RegisterActivity.this)
                            .show(getSupportFragmentManager(), "ModalBottomSheet");
                }
            });
        } else {
            loadingDialog.dismissDialog();
            new ModalBottomSheet(R.drawable.no_internet,
                    "Tidak Terhubung dengan Internet",
                    "Periksa kembali koneksi internet Anda.",
                    "Coba Lagi",
                    this)
                    .show(getSupportFragmentManager(), "ModalBottomSheet");
        }
    }

    @Override
    public void buttonBottomSheetFirst() {
        registerEvent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}