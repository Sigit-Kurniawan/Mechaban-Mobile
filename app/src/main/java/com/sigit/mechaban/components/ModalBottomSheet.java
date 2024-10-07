package com.sigit.mechaban.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sigit.mechaban.R;

public class ModalBottomSheet extends BottomSheetDialogFragment {
    private final ModalBottomSheetListener listener;

    public ModalBottomSheet(ModalBottomSheetListener listener) {
        this.listener = listener;
    }

    public interface ModalBottomSheetListener {
        void buttonTryAgain();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_bottom_sheet, container, false);

        view.findViewById(R.id.try_again_btn).setOnClickListener(v -> {
            listener.buttonTryAgain();
            dismiss();
        });

        return view;
    }
}
