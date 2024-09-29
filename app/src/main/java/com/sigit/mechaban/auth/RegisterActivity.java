package com.sigit.mechaban.auth;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sigit.mechaban.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageButton backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        TextView toLogin = findViewById(R.id.login_hyperlink);
        toLogin.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }
}