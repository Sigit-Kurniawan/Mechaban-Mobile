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

public class ModalBottomSheetTwoButton extends BottomSheetDialogFragment {
    private final int image;
    private final String title, description, positiveButtonText, negativeButtonText;
    private final ModalBottomSheetTwoButton.ModalBottomSheetListener listener;

    public ModalBottomSheetTwoButton(int image, String title, String description, String positiveButtonText, String negativeButtonText, ModalBottomSheetListener listener) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
        this.listener = listener;
    }

    public interface ModalBottomSheetListener {
        void positiveButtonBottomSheet();
        void negativeButtonBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_modal_two_button, container, false);

        ImageView imageView = view.findViewById(R.id.photo);
        imageView.setImageResource(image);

        TextView titleText = view.findViewById(R.id.title);
        titleText.setText(title);

        TextView descText = view.findViewById(R.id.description);
        descText.setText(description);

        Button positiveButton = view.findViewById(R.id.positive_button);
        positiveButton.setText(positiveButtonText);
        positiveButton.setOnClickListener(v -> {
            listener.positiveButtonBottomSheet();
            dismiss();
        });

        Button negativeButton = view.findViewById(R.id.negative_button);
        negativeButton.setText(negativeButtonText);
        negativeButton.setOnClickListener(v -> {
            listener.negativeButtonBottomSheet();
            dismiss();
        });

        return view;
    }
}
