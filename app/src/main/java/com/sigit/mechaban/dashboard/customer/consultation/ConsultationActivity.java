package com.sigit.mechaban.dashboard.customer.consultation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.api.model.account.AccountData;
import com.sigit.mechaban.object.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultationActivity extends AppCompatActivity {
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private final List<MontirAdapter.MontirItem> montirItems = new ArrayList<>();
    private final Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.montir_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MontirAdapter montirAdapter = new MontirAdapter(this, montirItems);
        recyclerView.setAdapter(montirAdapter);

        account.setAction("read_montir");
        Call<AccountAPI> readMontir = apiInterface.accountResponse(account);
        readMontir.enqueue(new Callback<AccountAPI>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    for (AccountData accountData : response.body().getAccountDataList()) {
                        montirItems.add(new MontirAdapter.MontirItem(accountData.getPhoto(), accountData.getName(), accountData.getEmail(), accountData.getNoHp()));
                    }
                    montirAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ConsultationActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                Log.e("ConsultationActivity", t.toString(), t);
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