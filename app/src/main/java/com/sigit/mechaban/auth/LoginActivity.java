package com.sigit.mechaban.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.api.model.account.AccountData;
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.components.ModalBottomSheet;
import com.sigit.mechaban.connection.Connection;
import com.sigit.mechaban.dashboard.customer.dashboard.DashboardActivity;
import com.sigit.mechaban.R;
import com.sigit.mechaban.object.Account;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton;
    private String email, password;
    private boolean isValidateEmail, isValidatePassword;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);
    private final Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLayout = findViewById(R.id.email);
        emailEditText = findViewById(R.id.email_field);
        passwordLayout = findViewById(R.id.password);
        passwordEditText = findViewById(R.id.pass_field);
        loginButton = findViewById(R.id.login_button);

        loginButton.setEnabled(false);
        isValidateEmail = true;
        isValidatePassword = true;

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
                if (email.isEmpty()) {
                    isValidateEmail = false;
                    emailLayout.setError("Kolom email-nya masih kosong!");
                    emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    isValidateEmail = false;
                    emailLayout.setError("Format email tidak valid!");
                    emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                } else {
                    isValidateEmail = true;
                    emailLayout.setError(null);
                    emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                }
                updateLoginButtonState();
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
                password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
                if (password.isEmpty()) {
                    isValidatePassword = false;
                    passwordLayout.setError("Kolom password tidak boleh kosong!");
                    passwordLayout.setErrorIconDrawable(null);
                    passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                    passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                } else {
                    isValidatePassword = true;
                    passwordLayout.setError(null);
                    passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                    passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                }
                updateLoginButtonState();
            }
        });

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (isValidateEmail) {
                    emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                } else {
                    emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            } else {
                if (isValidateEmail) {
                    emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_onSurfaceVariant));
                } else {
                    emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            }
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (isValidatePassword) {
                    passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                    passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                } else {
                    passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            } else {
                if (isValidatePassword) {
                    passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_onSurfaceVariant));
                    passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_onSurfaceVariant));
                } else {
                    passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            }
        });

        loginButton.setOnClickListener(v -> {
            email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
            password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            account.setAction("login");
            account.setEmail(email);
            account.setPassword(password);

            loginEvent();
        });

        findViewById(R.id.register_hyperlink).setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void updateLoginButtonState() {
        email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        if (!email.isEmpty() && !password.isEmpty()) {
            loginButton.setEnabled(isValidateEmail && isValidatePassword);
        }
    }

    private void loginEvent() {
        loadingDialog.startLoadingDialog();
        if (new Connection(this).isNetworkAvailable()) {
            SessionManager sessionManager = new SessionManager(this);
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<AccountAPI> loginCall = apiInterface.accountResponse(account);
            loginCall.enqueue(new Callback<AccountAPI>() {
                @Override
                public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        AccountData loginData = response.body().getAccountData();
                        sessionManager.createLoginSession(loginData);
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                        loadingDialog.dismissDialog();
                    } else {
                        loadingDialog.dismissDialog();
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                    Log.e("LoginActivity", t.toString(), t);
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
        loginEvent();
    }
}