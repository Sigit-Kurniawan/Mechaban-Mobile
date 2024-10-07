package com.sigit.mechaban.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.login.Login;
import com.sigit.mechaban.api.model.login.LoginData;
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.components.ModalBottomSheet;
import com.sigit.mechaban.connection.Connection;
import com.sigit.mechaban.dashboard.customer.DashboardActivity;
import com.sigit.mechaban.R;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements ModalBottomSheet.ModalBottomSheetListener {
    private TextInputEditText noHPEditIext, passwordEditText;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);
    private String noHP, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        noHPEditIext = findViewById(R.id.nohp_field);
        passwordEditText = findViewById(R.id.pass_field);

        Button loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(v -> {
            noHP = Objects.requireNonNull(noHPEditIext.getText()).toString();
            password = Objects.requireNonNull(passwordEditText.getText()).toString();

            loginEvent();
        });

        TextView toRegister = findViewById(R.id.register_hyperlink);
        toRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void loginEvent() {
        ModalBottomSheet bottomSheet = new ModalBottomSheet(this);

        if (new Connection(this).isNetworkAvailable()) {
            SessionManager sessionManager = new SessionManager(this);
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<Login> loginCall = apiInterface.loginResponse(noHP, password);
            loginCall.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        LoginData loginData = response.body().getLoginData();
                        sessionManager.createLoginSession(loginData);
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                    Log.e("LoginActivity", t.toString(), t);
                }
            });
        } else {
            bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
        }
    }

    @Override
    public void buttonTryAgain() {
        loginEvent();
    }
}