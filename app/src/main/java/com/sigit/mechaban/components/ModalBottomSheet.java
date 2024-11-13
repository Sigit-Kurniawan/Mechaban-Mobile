package com.sigit.mechaban.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sigit.mechaban.R;

public class ModalBottomSheet extends BottomSheetDialogFragment {
    private final int image;
    private final String title, description, buttonText;
    private final ModalBottomSheetListener listener;

    public ModalBottomSheet(int image, String title, String description, String buttonText, ModalBottomSheetListener listener) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.buttonText = buttonText;
        this.listener = listener;
    }

    public interface ModalBottomSheetListener {
        void buttonBottomSheetFirst();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_modal, container, false);

        ImageView imageView = view.findViewById(R.id.photo);
        imageView.setImageResource(image);

        TextView titleText = view.findViewById(R.id.title);
        titleText.setText(title);

        TextView descText = view.findViewById(R.id.description);
        descText.setText(description);

        Button button = view.findViewById(R.id.button);
        button.setText(buttonText);
        button.setOnClickListener(v -> {
            listener.buttonBottomSheetFirst();
            dismiss();
        });

        return view;
    }
}
