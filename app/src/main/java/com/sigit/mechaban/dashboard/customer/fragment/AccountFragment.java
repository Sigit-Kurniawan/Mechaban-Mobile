package com.sigit.mechaban.dashboard.customer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.Account;
import com.sigit.mechaban.auth.LoginActivity;
import com.sigit.mechaban.sessionmanager.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private TextView tvName, tvEmail;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);

        SessionManager sessionManager = new SessionManager(requireActivity());
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<Account> accountCall = apiInterface.accountResponse(sessionManager.getUserDetail().get("email"));
        accountCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(@NonNull Call<Account> call, @NonNull Response<Account> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Account account = response.body();
                    tvName.setText(account.getAccountData().getName());
                    tvEmail.setText(account.getAccountData().getEmail());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Account> call, @NonNull Throwable t) {
                Log.e("AccountFragment", t.toString(), t);
            }
        });

        view.findViewById(R.id.logout_button).setOnClickListener(v -> {
            sessionManager.logoutSession();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}