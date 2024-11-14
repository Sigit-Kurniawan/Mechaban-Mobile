package com.sigit.mechaban.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextInputEditText passwordTextView = findViewById(R.id.pass_field);
        TextInputEditText confirmPasswordTextView = findViewById(R.id.confirm_pass_field);

        findViewById(R.id.change_password).setOnClickListener(v -> {
            String password = Objects.requireNonNull(passwordTextView.getText()).toString().trim();
            String confirmPassword = Objects.requireNonNull(confirmPasswordTextView.getText()).toString().trim();

            if (confirmPassword.equals(password)) {
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
                        if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                            Toast.makeText(ChangePasswordActivity.this, "Ganti Password Berhasil", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                        Log.e("ChangePassword", t.toString(), t);
                    }
                });
            }
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