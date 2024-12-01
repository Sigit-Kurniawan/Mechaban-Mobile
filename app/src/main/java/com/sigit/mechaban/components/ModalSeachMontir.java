package com.sigit.mechaban.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.sigit.mechaban.R;
import com.sigit.mechaban.api.ApiClient;
import com.sigit.mechaban.api.ApiInterface;
import com.sigit.mechaban.api.model.montir.MontirAPI;
import com.sigit.mechaban.api.model.montir.MontirData;
import com.sigit.mechaban.dashboard.montir.listmontir.MontirAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModalSeachMontir extends BottomSheetDialogFragment {
    private final Context context;
    private final ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private final List<MontirAdapter.MontirItem> montirItems = new ArrayList<>();
    private MontirAdapter montirAdapter;

    public ModalSeachMontir(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search_montir, container, false);

        RecyclerView montirList = view.findViewById(R.id.montir_list);
        montirList.setLayoutManager(new LinearLayoutManager(context));

        TextInputEditText searchInput = view.findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                montirAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Call<MontirAPI> readMontir = apiInterface.montirResponse();
        readMontir.enqueue(new Callback<MontirAPI>() {
            @Override
            public void onResponse(@NonNull Call<MontirAPI> call, @NonNull Response<MontirAPI> response) {
                if (response.body() != null && response.isSuccessful() && response.body().getCode() == 200) {
                    for (MontirData montirData : response.body().getMontirDataList()) {
                        montirItems.add(new MontirAdapter.MontirItem(montirData.getName()));
                    }
                    montirAdapter = new MontirAdapter(montirItems);
                    montirList.setAdapter(montirAdapter);
                } else {
                    Toast.makeText(context, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MontirAPI> call, @NonNull Throwable t) {
                Log.e("ModalSearchMontir", t.toString(), t);
            }
        });

        return view;
    }
}
