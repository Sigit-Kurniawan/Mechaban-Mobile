package com.sigit.mechaban.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.object.Account;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextInputEditText emailTextView = findViewById(R.id.email_field);

        findViewById(R.id.send_otp).setOnClickListener(v -> {
            String email = Objects.requireNonNull(emailTextView.getText()).toString().trim();
            Account account = new Account();
            account.setAction("verification_forget");
            account.setEmail(email);
            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<AccountAPI> sendOtpCall = apiInterface.accountResponse(account);
            sendOtpCall.enqueue(new Callback<AccountAPI>() {
                @Override
                public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                        Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                    Log.e("ForgetPassword", t.toString(), t);
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
}