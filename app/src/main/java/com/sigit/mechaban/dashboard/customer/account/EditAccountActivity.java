package com.sigit.mechaban.dashboard.customer.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sigit.mechaban.BuildConfig;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.auth.LoginActivity;
import com.sigit.mechaban.components.EditTextBehavior;
import com.sigit.mechaban.object.Account;
import com.sigit.mechaban.sessionmanager.SessionManager;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {
    private ShapeableImageView photoProfile;
    private TextInputLayout nameLayout, emailLayout, noHPLayout, passwordLayout;
    private TextInputEditText emailEditText, nameEditText, noHPEditText, passwordEditText;
    private ActivityResultLauncher<Intent> launcher;
    private Button saveButton;
    private String email, name, noHP, password;
    private boolean isValidateName, isValidateEmail, isValidateNoHP, isValidatePassword;
    private final Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

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
        photoProfile = findViewById(R.id.photo_profile);
        saveButton = findViewById(R.id.save_button);

        saveButton.setEnabled(false);
        isValidateName = true;
        isValidateEmail = true;
        isValidateNoHP = true;
        isValidatePassword = true;

        findViewById(R.id.edit_profile).setOnClickListener(v -> pickImage());
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        startCrop(Objects.requireNonNull(imageUri));
                    } else {
                        Toast.makeText(EditAccountActivity.this, "Tidak ada foto yang dipilih", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidateName = EditTextBehavior.validateName(getApplicationContext(), nameEditText, nameLayout);
                updateEditButtonState();
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
                isValidateEmail = EditTextBehavior.validateEmail(getApplicationContext(), emailEditText, emailLayout);
                updateEditButtonState();
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
                isValidateNoHP = EditTextBehavior.validateNoHP(getApplicationContext(), noHPEditText, noHPLayout);
                updateEditButtonState();
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
                isValidatePassword = EditTextBehavior.validatePasswordRegister(getApplicationContext(), passwordEditText, passwordLayout);
                updateEditButtonState();
            }
        });

        nameEditText.setOnFocusChangeListener(((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), nameLayout, hasFocus, isValidateName)));

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), emailLayout, hasFocus, isValidateEmail));

        noHPEditText.setOnFocusChangeListener(((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), noHPLayout, hasFocus, isValidateNoHP)));

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> EditTextBehavior.setIconTintOnFocus(getApplicationContext(), passwordLayout, hasFocus, isValidatePassword));

        SessionManager sessionManager = new SessionManager(this);
        account.setAction("read");
        account.setEmail(sessionManager.getUserDetail().get("email"));
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AccountAPI> readAccountCall = apiInterface.accountResponse(account);
        readAccountCall.enqueue(new Callback<AccountAPI>() {
            @Override
            public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                if (response.body() != null && response.isSuccessful()) {
                    emailEditText.setText(response.body().getAccountData().getEmail());
                    nameEditText.setText(response.body().getAccountData().getName());
                    noHPEditText.setText(response.body().getAccountData().getNoHp());
                    passwordEditText.setText(response.body().getAccountData().getPassword());
                    String photoBase64 = response.body().getAccountData().getPhoto();
                    if (photoBase64 != null && !photoBase64.isEmpty()) {
                        Glide.with(EditAccountActivity.this)
                                .load("http://" + BuildConfig.ip + "/api/src/" + photoBase64)
                                .into(photoProfile);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                Log.e("EditAccountActivity", t.toString(), t);
            }
        });

        saveButton.setOnClickListener(v -> {
            email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
            name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
            noHP = Objects.requireNonNull(noHPEditText.getText()).toString().trim();
            password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            account.setAction("update");
            account.setEmail(sessionManager.getUserDetail().get("email"));
            account.setEmailUpdate(email);
            account.setName(name);
            account.setNo_hp(noHP);
            account.setPassword(password);

            Call<AccountAPI> updateAccountCall = apiInterface.accountResponse(account);
            updateAccountCall.enqueue(new Callback<AccountAPI>() {
                @Override
                public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        Toast.makeText(EditAccountActivity.this, "Edit berhasil", Toast.LENGTH_SHORT).show();
                        if (response.body().getMessage().equals("Update email")) {
                            sessionManager.updateEmail(email);
                        }
                        finish();
                    } else {
                        Toast.makeText(EditAccountActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                    Log.e("EditAccountActivity", t.toString(), t);
                }
            });
        });

        findViewById(R.id.delete_button).setOnClickListener(v -> {
            account.setAction("delete");
            account.setEmail(sessionManager.getUserDetail().get("email"));

            Call<AccountAPI> deleteAccountCall = apiInterface.accountResponse(account);
            deleteAccountCall.enqueue(new Callback<AccountAPI>() {
                @Override
                public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                    if (response.body() != null && response.isSuccessful() && response.body().isStatus()) {
                        sessionManager.logoutSession();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
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

    @SuppressLint("IntentReset")
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    private void startCrop(@NonNull Uri uri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image.jpg"));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(EditAccountActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = UCrop.getOutput(data);
                photoProfile.setImageURI(resultUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    String imageBase64 = convertImageToBase64(bitmap);
                    account.setPhoto(imageBase64);
                } catch (IOException e) {
                    Toast.makeText(this, "Gagal mengonversi gambar", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                Throwable cropError = UCrop.getError(data);
                Toast.makeText(this, "Gagal crop: " + cropError, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String convertImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void updateEditButtonState() {
        name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
        email = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        noHP = Objects.requireNonNull(noHPEditText.getText()).toString().trim();
        password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        if (!name.isEmpty() && !email.isEmpty() && !noHP.isEmpty() && !password.isEmpty()) {
            saveButton.setEnabled(isValidateName && isValidateEmail && isValidateNoHP && isValidatePassword);
        }
    }
}