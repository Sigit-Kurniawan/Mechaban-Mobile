package com.sigit.mechaban.dashboard.customer.service;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sigit.mechaban.R;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);



//        TextView nameText = findViewById(R.id.name_text);
//        TextView addressText = findViewById(R.id.address_text);
//        TextView dateText = findViewById(R.id.date_text);
//
//        Intent intent = getIntent();
//        nameText.setText(intent.getStringExtra("name"));
//        addressText.setText(intent.getStringExtra("address"));
//        dateText.setText(intent.getStringExtra("date"));

        findViewById(R.id.close_button).setOnClickListener(v -> finish());
    }
}