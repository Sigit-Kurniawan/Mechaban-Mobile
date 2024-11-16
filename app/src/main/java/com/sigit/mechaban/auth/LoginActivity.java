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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

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
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class LoginActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {
    private Toolbar toolbar;
    private ImageView loginIllustration;
    private RelativeLayout bottomSheet;
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
        bottomSheet = findViewById(R.id.bottom_sheet);
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

        findViewById(R.id.forget_password).setOnClickListener(v -> startActivity(new Intent(this, ForgetPasswordActivity.class)));

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
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bottomSheet.getLayoutParams();

            if (keypadHeight > screenHeight * 0.15) {
                loginIllustration.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                params.matchConstraintPercentHeight = 0.85f;
                bottomSheet.setLayoutParams(params);
            } else {
                loginIllustration.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.GONE);
                params.matchConstraintPercentHeight = 0.5f;
                bottomSheet.setLayoutParams(params);
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
                    } else if (response.body() != null && response.body().getCode() == 400) {
                        loadingDialog.dismissDialog();
                        isValidateEmail = false;
                        isValidatePassword = false;
                        emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                        passwordLayout.setErrorIconDrawable(null);
                        passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                        passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                        MotionToast.Companion.createColorToast(LoginActivity.this,
                                "Email atau Password Salah!",
                                "Periksa kembali hasil",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(LoginActivity.this,R.font.montserrat_semibold));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                    loadingDialog.dismissDialog();
                    Log.e("LoginActivity", t.toString(), t);
                    new ModalBottomSheet(R.drawable.not_found,
                            "Tidak Ditemukan",
                            "Coba lagi lain kali.",
                            "Coba Lagi",
                            LoginActivity.this)
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
        loginEvent();
    }
}