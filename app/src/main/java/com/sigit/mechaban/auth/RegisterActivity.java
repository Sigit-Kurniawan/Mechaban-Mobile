package com.sigit.mechaban.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.R;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText nameEditText, emailEditText, noHPEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.name_field);
        emailEditText = findViewById(R.id.email_field);
        noHPEditText = findViewById(R.id.nohp_field);
        passwordEditText = findViewById(R.id.pass_field);
        confirmPasswordEditText = findViewById(R.id.confirm_pass_field);

        Button registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(v -> {
            String name = Objects.requireNonNull(nameEditText.getText()).toString();
            String email = Objects.requireNonNull(emailEditText.getText()).toString();
            String noHP = Objects.requireNonNull(noHPEditText.getText()).toString();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();
            String confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString();

            if (!(name.isEmpty() || email.isEmpty() || noHP.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, new Database().getRegister(), response -> {
                    if (response.equals("Registrasi berhasil. Silakan login.")) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(getApplicationContext(), "Registrasi gagal", Toast.LENGTH_SHORT).show();
                    Log.e("RegisterActivity", error.toString());
                })
                {
                    @Override
                    protected HashMap<String, String> getParams(){
                        HashMap<String, String> params = new HashMap<>();
                        params.put("name", name);
                        params.put("email", email);
                        params.put("no_hp", noHP);
                        params.put("password", password);
                        params.put("confirm_password", confirmPassword);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            } else {
                Toast.makeText(this, "Kolom wajib diisi", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        TextView toLogin = findViewById(R.id.login_hyperlink);
        toLogin.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}