package com.sigit.mechaban.dashboard.customer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.sigit.mechaban.BuildConfig;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.auth.LoginActivity;
import com.sigit.mechaban.dashboard.customer.account.EditAccountActivity;
import com.sigit.mechaban.object.Account;
import com.sigit.mechaban.sessionmanager.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private ShapeableImageView photoProfile;
    private TextView tvName, tvEmail;
    private SessionManager sessionManager;
    private final Account account = new Account();

    public AccountFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        sessionManager = new SessionManager(requireActivity());

        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        photoProfile = view.findViewById(R.id.profile_icon);
        setDataAccount();

        view.findViewById(R.id.edit_button).setOnClickListener(v -> startActivity(new Intent(getActivity(), EditAccountActivity.class)));

        view.findViewById(R.id.contact_button).setOnClickListener(v -> {
            try {
                String phoneNumber = "+6283832566069";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/" + phoneNumber.replace("+", "").replace(" ", "")));
                startActivity(intent);
            } catch (Exception e) {
                Log.e("ContactUs", e.toString(), e);
            }
        });

        view.findViewById(R.id.info_button).setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + BuildConfig.ip + "/Mechaban-Web"))));

        view.findViewById(R.id.logout_button).setOnClickListener(v -> {
            sessionManager.logoutSession();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }

    private void setDataAccount() {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        account.setAction("read");
        account.setEmail(sessionManager.getUserDetail().get("email"));
        Call<AccountAPI> accountCall = apiInterface.accountResponse(account);
        accountCall.enqueue(new Callback<AccountAPI>() {
            @Override
            public void onResponse(@NonNull Call<AccountAPI> call, @NonNull Response<AccountAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    String photo = response.body().getAccountData().getPhoto();
                    if (photo != null && !photo.isEmpty()) {
                        Glide.with(requireActivity())
                                .load("http://" + BuildConfig.ip + "/api/src/" + photo)
                                .placeholder(R.drawable.baseline_account_circle_24)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(photoProfile);
                    }
                    tvName.setText(response.body().getAccountData().getName());
                    tvEmail.setText(response.body().getAccountData().getEmail());
                } else {
                    Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountAPI> call, @NonNull Throwable t) {
                Log.e("AccountFragment", t.toString(), t);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataAccount();
    }
}