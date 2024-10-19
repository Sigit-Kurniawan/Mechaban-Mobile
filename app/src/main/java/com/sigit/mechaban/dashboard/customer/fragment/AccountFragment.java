package com.sigit.mechaban.dashboard.customer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.readaccount.ReadAccount;
import com.sigit.mechaban.auth.LoginActivity;
import com.sigit.mechaban.dashboard.customer.account.EditAccountActivity;
import com.sigit.mechaban.sessionmanager.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private TextView tvName, tvEmail;
    private ActivityResultLauncher<Intent> editAccountLauncher;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);

        SessionManager sessionManager = new SessionManager(requireActivity());
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

        editAccountLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                        String updatedName = result.getData().getStringExtra("updated_name");
                        String updatedEmail = result.getData().getStringExtra("updated_email");

                        tvName.setText(updatedName);
                        tvEmail.setText(updatedEmail);
                    }
        });

        Call<ReadAccount> accountCall = apiInterface.readAccountResponse(sessionManager.getUserDetail().get("email"));
        accountCall.enqueue(new Callback<ReadAccount>() {
            @Override
            public void onResponse(@NonNull Call<ReadAccount> call, @NonNull Response<ReadAccount> response) {
                if (response.body() != null && response.isSuccessful()) {
                    tvName.setText(response.body().getAccountData().getName());
                    tvEmail.setText(response.body().getAccountData().getEmail());
                } else {
                    Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReadAccount> call, @NonNull Throwable t) {
                Log.e("AccountFragment", t.toString(), t);
            }
        });

        view.findViewById(R.id.edit_button).setOnClickListener(v -> editAccountLauncher.launch(new Intent(getActivity(), EditAccountActivity.class)));

        view.findViewById(R.id.logout_button).setOnClickListener(v -> {
            sessionManager.logoutSession();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}