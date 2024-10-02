package com.sigit.mechaban.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.dashboard.customer.DashboardActivity;
import com.sigit.mechaban.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText noHPEditIext, passwordEditText;

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

            if (!(noHP.isEmpty() || password.isEmpty())){
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, new Database().getLogin() + "?no_hp=" + noHP + "&password=" + password, response -> {
                    if (response.equals("Login berhasil")) {
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(getApplicationContext(), "Login gagal", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", error.toString());
                });
                requestQueue.add(stringRequest);
            } else {
                Toast.makeText(getApplicationContext(), "Kolom wajib diisi", Toast.LENGTH_SHORT).show();
            }
        });

        TextView toRegister = findViewById(R.id.register_hyperlink);
        toRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}