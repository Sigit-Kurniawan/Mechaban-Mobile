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
import com.sigit.mechaban.components.LoadingDialog;
import com.sigit.mechaban.connection.Connection;
import com.sigit.mechaban.dashboard.customer.DashboardActivity;
import com.sigit.mechaban.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText noHPEditIext, passwordEditText;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        noHPEditIext = findViewById(R.id.nohp_field);
        passwordEditText = findViewById(R.id.pass_field);

        Button loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(v -> {
            String noHP = Objects.requireNonNull(noHPEditIext.getText()).toString();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();

            if (new Connection(this).isNetworkAvailable()) {
                apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
                Call<Login> loginCall = apiInterface.loginResponse(noHP, password);
                loginCall.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                        if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                            getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isLoggedIn", true)
                                    .apply();
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                        Log.e("LoginActivity", t.toString());
                    }
                });
            }

//            if (new Connection(this).isNetworkAvailable()) {
//                if (!(noHP.isEmpty() || password.isEmpty())){
//                    loadingDialog.startLoadingDialog();
//
//                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, new Database().getLogin() + "?no_hp=" + noHP + "&password=" + password, response -> {
//                        if (response.equals("Login berhasil")) {
//                            getSharedPreferences("LoginPrefs", MODE_PRIVATE)
//                                    .edit()
//                                    .putBoolean("isLoggedIn", true)
//                                    .apply();
//                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
//                            finish();
//                        } else {
//                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//                        }
//                    }, error -> {
//                        Toast.makeText(getApplicationContext(), "Login gagal", Toast.LENGTH_SHORT).show();
//                        Log.e("LoginActivity", error.toString());
//                    });
//                    requestQueue.add(stringRequest);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Kolom wajib diisi", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
//            }
        });

        TextView toRegister = findViewById(R.id.register_hyperlink);
        toRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}