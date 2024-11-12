package com.sigit.mechaban.auth;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.api.model.account.AccountData;
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.components.ModalBottomSheet;
import com.sigit.mechaban.components.EditTextBehavior;
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
    private Toolbar toolbar;
    private ImageView loginIllustration;
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

        toolbar = findViewById(R.id.toolbar);
        loginIllustration = findViewById(R.id.illustration);
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
                isValidateEmail = EditTextBehavior.validateEmail(getApplicationContext(), emailEditText, emailLayout);
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
                isValidatePassword = EditTextBehavior.validatePassword(getApplicationContext(), passwordEditText, passwordLayout);
                updateLoginButtonState();
            }
        });

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), emailLayout, hasFocus, isValidateEmail));

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), passwordLayout, hasFocus, isValidatePassword));

        loginButton.setOnClickListener(v -> {
            email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
            password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            account.setAction("login");
            account.setEmail(email);
            account.setPassword(password);

            loginEvent();
        });

        findViewById(R.id.register_hyperlink).setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            rootView.getWindowVisibleDisplayFrame(rect);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - rect.bottom;

            if (keypadHeight > screenHeight * 0.15) {
                loginIllustration.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
            } else {
                loginIllustration.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
            }
        });
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
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        AccountData loginData = response.body().getAccountData();
                        sessionManager.createLoginSession(loginData);
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                        loadingDialog.dismissDialog();
                    } else if (Objects.requireNonNull(response.body()).getCode() == 400) {
                        loadingDialog.dismissDialog();
                        isValidatePassword = false;
                        passwordLayout.setError("Password salah!");
                        passwordLayout.setErrorIconDrawable(null);
                        passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                        passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                    } else if (response.body().getCode() == 401) {
                        loadingDialog.dismissDialog();
                        isValidateEmail = false;
                        emailLayout.setError("Email belum terdaftar!");
                        emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
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