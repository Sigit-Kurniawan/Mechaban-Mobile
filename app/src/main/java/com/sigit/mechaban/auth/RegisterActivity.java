package com.sigit.mechaban.auth;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.register.Register;
import com.sigit.mechaban.components.ModalBottomSheet;
import com.sigit.mechaban.connection.Connection;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {
    private TextInputEditText nameEditText, emailEditText, noHPEditText, passwordEditText, confirmPasswordEditText;
    private String name, email, noHP, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.name_field);
        emailEditText = findViewById(R.id.email_field);
        TextInputLayout emailInputLayout = findViewById(R.id.email);
        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                email = Objects.requireNonNull(emailEditText.getText()).toString().trim();

                if (email.isEmpty()) {
                    emailInputLayout.setError("Email tidak boleh kosong");
                } else if (!isValidEmail(email)) {
                    emailInputLayout.setError("Email tidak valid");
                } else {
                    emailInputLayout.setError(null);
                }
            }
        });

        noHPEditText = findViewById(R.id.nohp_field);
        passwordEditText = findViewById(R.id.pass_field);
        confirmPasswordEditText = findViewById(R.id.confirm_pass_field);

        Button registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(v -> {
            name = Objects.requireNonNull(nameEditText.getText()).toString();
            email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
            noHP = Objects.requireNonNull(noHPEditText.getText()).toString();
            password = Objects.requireNonNull(passwordEditText.getText()).toString();
            confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString();

            registerEvent();
        });

        ImageButton backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        TextView toLogin = findViewById(R.id.login_hyperlink);
        toLogin.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void registerEvent() {
        ModalBottomSheet bottomSheet = new ModalBottomSheet(this);

        if (new Connection(this).isNetworkAvailable()) {
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<Register> registerCall = apiInterface.registerResponse(noHP, name, email, password, confirmPassword);
            registerCall.enqueue(new Callback<Register>() {
                @Override
                public void onResponse(@NonNull Call<Register> call, @NonNull Response<Register> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        getOnBackPressedDispatcher().onBackPressed();
                    } else {
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Register> call, @NonNull Throwable t) {
                    Log.e("RegisterActivity", t.toString(), t);
                }
            });
        } else {
            bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
        }
    }

    @Override
    public void buttonTryAgain() {
        registerEvent();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}