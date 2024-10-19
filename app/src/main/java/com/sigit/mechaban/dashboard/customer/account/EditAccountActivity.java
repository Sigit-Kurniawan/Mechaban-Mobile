package com.sigit.mechaban.dashboard.customer.account;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ext.SdkExtensions;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresExtension;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.readaccount.ReadAccount;
import com.sigit.mechaban.api.model.updateaccount.UpdateAccount;
import com.sigit.mechaban.sessionmanager.SessionManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {
    private ShapeableImageView photoProfile;
    private TextInputEditText emailEditText, nameEditText, noHPEditText, passwordEditText;
    private ActivityResultLauncher<Intent> launcher;
    private Button saveButton;
    private String email, name, noHP, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emailEditText = findViewById(R.id.email_field);
        nameEditText = findViewById(R.id.name_field);
        noHPEditText = findViewById(R.id.nohp_field);
        passwordEditText = findViewById(R.id.pass_field);

        photoProfile = findViewById(R.id.photo_profile);
        registerResult();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2) {
            findViewById(R.id.edit_profile).setOnClickListener(v -> pickImage());
        }

        SessionManager sessionManager = new SessionManager(this);
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<ReadAccount> readAccountCall = apiInterface.readAccountResponse(sessionManager.getUserDetail().get("email"));
        readAccountCall.enqueue(new Callback<ReadAccount>() {
            @Override
            public void onResponse(@NonNull Call<ReadAccount> call, @NonNull Response<ReadAccount> response) {
                if (response.body() != null && response.isSuccessful()) {
                    emailEditText.setText(response.body().getAccountData().getEmail());
                    nameEditText.setText(response.body().getAccountData().getName());
                    noHPEditText.setText(response.body().getAccountData().getNoHp());
                    passwordEditText.setText(response.body().getAccountData().getPassword());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReadAccount> call, @NonNull Throwable t) {

            }
        });

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
            name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
            noHP = Objects.requireNonNull(noHPEditText.getText()).toString().trim();
            password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            Call<UpdateAccount> updateAccountCall = apiInterface.updateAccountResponse(
                    sessionManager.getUserDetail().get("email"),
                    email,
                    name,
                    noHP,
                    password
            );
            updateAccountCall.enqueue(new Callback<UpdateAccount>() {
                @Override
                public void onResponse(@NonNull Call<UpdateAccount> call, @NonNull Response<UpdateAccount> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        Toast.makeText(EditAccountActivity.this, "Edit berhasil", Toast.LENGTH_SHORT).show();
                        if (response.body().getMessage().equals("Update email")) {
                            sessionManager.updateEmail(email);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("updated_name", name);
                        intent.putExtra("updated_email", email);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(EditAccountActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UpdateAccount> call, @NonNull Throwable t) {
                    Log.e("EditAccountActivity", t.toString(), t);
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerResult() {
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        photoProfile.setImageURI(Objects.requireNonNull(result.getData()).getData());
                    } catch (Exception e) {
                        Toast.makeText(EditAccountActivity.this, "Tidak ada foto yang dipilih", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private void pickImage() {
        launcher.launch(new Intent(MediaStore.ACTION_PICK_IMAGES));
    }
}