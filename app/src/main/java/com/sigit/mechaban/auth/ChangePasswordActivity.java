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
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.components.EditTextBehavior;
import com.sigit.mechaban.object.Account;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputEditText passwordEditText, confirmPasswordEditText;
    private TextInputLayout passwordLayout, confirmPasswordLayout;
    private Button changePassword;
    private boolean isValidatePassword, isValidateConfirmPassword;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        passwordEditText = findViewById(R.id.pass_field);
        passwordLayout = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_pass_field);
        confirmPasswordLayout = findViewById(R.id.confirm_password);
        changePassword = findViewById(R.id.change_password);

        changePassword.setEnabled(false);
        isValidatePassword = true;
        isValidateConfirmPassword = true;

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

        changePassword.setOnClickListener(v -> {
            password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            Account account = new Account();
            Intent intent = getIntent();
            account.setAction("change_password");
            account.setEmail(intent.getStringExtra("email"));
            account.setPassword(password);

            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<AccountAPI> changePasswordCall = apiInterface.accountResponse(account);
            changePasswordCall.enqueue(new Callback<AccountAPI>() {
                @Override
                public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                        MotionToast.Companion.createColorToast(ChangePasswordActivity.this,
                                "password berhasil diganti",
                                "jangan lupa lagi",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_TOP,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(ChangePasswordActivity.this,R.font.montserrat_semibold));
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                    Log.e("ChangePassword", t.toString(), t);
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

    private void updateRegisterButtonState() {
        password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString().trim();
        if (!password.isEmpty() && !confirmPassword.isEmpty()) {
            changePassword.setEnabled(isValidatePassword && isValidateConfirmPassword);
        }
    }
}