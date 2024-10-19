package com.sigit.mechaban.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.register.Register;
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.components.ModalBottomSheet;
import com.sigit.mechaban.connection.Connection;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {
    private TextInputEditText nameEditText, emailEditText, noHPEditText, passwordEditText, confirmPasswordEditText;
    private TextInputLayout nameLayout, emailLayout, noHPLayout, passwordLayout, confirmPasswordLayout;
    private Button registerButton;
    private boolean isValidateName, isValidateEmail, isValidateNoHP, isValidatePassword, isValidateConfirmPassword;
    private String name, email, noHP, password, confirmPassword;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);

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
                name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
                if (!name.isEmpty()) {
                    isValidateName = true;
                    nameLayout.setError(null);
                    nameLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                } else {
                    isValidateName = false;
                    nameLayout.setError("Kolom nama tidak boleh kosong!");
                    nameLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
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
                noHP = Objects.requireNonNull(noHPEditText.getText()).toString().trim();
                if (noHP.isEmpty()) {
                    isValidateNoHP = false;
                    noHPLayout.setError("Kolom no. HP-nya tidak boleh kosong!");
                    noHPLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                } else if (noHP.length() < 10) {
                    isValidateNoHP = false;
                    noHPLayout.setError("No. HP tidak valid!");
                    noHPLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                } else {
                    isValidateNoHP = true;
                    noHPLayout.setError(null);
                    noHPLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                }
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
                password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
                if (password.isEmpty()) {
                    isValidatePassword = false;
                    passwordLayout.setError("Kolom password tidak boleh kosong!");
                    passwordLayout.setErrorIconDrawable(null);
                    passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                    passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                } else if (!(password.length() >= 8)) {
                    unvalidPassword();
                } else if (!password.matches("(.*[A-Z].*)")) {
                    unvalidPassword();
                } else if (!password.matches("(.*[a-z].*)")) {
                    unvalidPassword();
                } else if (!password.matches(".*[0-9].*")) {
                    unvalidPassword();
                } else if (!password.matches(".*[@$!%*?&].*")) {
                    unvalidPassword();
                } else {
                    isValidatePassword = true;
                    passwordLayout.setError(null);
                    passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                    passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                }
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
                password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
                confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString().trim();
                if (confirmPassword.isEmpty()) {
                    isValidateConfirmPassword = false;
                    confirmPasswordLayout.setError("Kolom konfirmasi password-nya tidak boleh kosong!");
                    confirmPasswordLayout.setErrorIconDrawable(null);
                    confirmPasswordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                    confirmPasswordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                } else if (!confirmPassword.equals(password)) {
                    isValidateConfirmPassword = false;
                    confirmPasswordLayout.setError("Password tidak sama!");
                    confirmPasswordLayout.setErrorIconDrawable(null);
                    confirmPasswordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                    confirmPasswordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                } else {
                    isValidateConfirmPassword = true;
                    confirmPasswordLayout.setError(null);
                    confirmPasswordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                    confirmPasswordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                }
                updateRegisterButtonState();
            }
        });

        nameEditText.setOnFocusChangeListener(((v, hasFocus) -> {
            if (hasFocus) {
                if (isValidateName) {
                    nameLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                } else {
                    nameLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            } else {
                if (isValidateName) {
                    nameLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_onSurfaceVariant));
                } else {
                    nameLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            }
        }));

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

        noHPEditText.setOnFocusChangeListener(((v, hasFocus) -> {
            if (hasFocus) {
                if (isValidateNoHP) {
                    noHPLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                } else {
                    noHPLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            } else {
                if (isValidateNoHP) {
                    noHPLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_onSurfaceVariant));
                } else {
                    noHPLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            }
        }));

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

        confirmPasswordEditText.setOnFocusChangeListener(((v, hasFocus) -> {
            if (hasFocus) {
                if (isValidateConfirmPassword) {
                    confirmPasswordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                    confirmPasswordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_primary));
                } else {
                    confirmPasswordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            } else {
                if (isValidateConfirmPassword) {
                    confirmPasswordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_onSurfaceVariant));
                    confirmPasswordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_onSurfaceVariant));
                } else {
                    confirmPasswordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                }
            }
        }));

        registerButton.setOnClickListener(v -> {
            name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
            email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
            noHP = Objects.requireNonNull(noHPEditText.getText()).toString().trim();
            password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
            confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString().trim();

            registerEvent();
        });

        findViewById(R.id.login_hyperlink).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void updateRegisterButtonState() {
        name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
        email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        noHP = Objects.requireNonNull(noHPEditText.getText()).toString().trim();
        password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString().trim();
        if (!name.isEmpty() && !email.isEmpty() && !noHP.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            registerButton.setEnabled(isValidateName && isValidateEmail && isValidateNoHP && isValidatePassword && isValidateConfirmPassword);
        }
    }
    
    private void unvalidPassword() {
        isValidatePassword = false;
        passwordLayout.setError("Password minimal 8 karakter, termasuk huruf kapital, huruf kecil, angka, dan simbol (@$!%*?&)!");
        passwordLayout.setErrorIconDrawable(null);
        passwordLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
        passwordLayout.setEndIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
    }

    private void registerEvent() {
        loadingDialog.startLoadingDialog();
        if (new Connection(this).isNetworkAvailable()) {
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<Register> registerCall = apiInterface.registerResponse(email, name, noHP, password, confirmPassword);
            registerCall.enqueue(new Callback<Register>() {
                @Override
                public void onResponse(@NonNull Call<Register> call, @NonNull Response<Register> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        getOnBackPressedDispatcher().onBackPressed();
                        loadingDialog.dismissDialog();
                    } else {
                        loadingDialog.dismissDialog();
                        if (response.body().getMessage().equals("Email telah terdaftar")) {
                            isValidateEmail = false;
                            emailLayout.setError(response.body().getMessage());
                            emailLayout.setStartIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.md_theme_error));
                        } else {
                            Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Register> call, @NonNull Throwable t) {
                    Log.e("RegisterActivity", t.toString(), t);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}