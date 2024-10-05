package com.sigit.mechaban.components;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.sigit.mechaban.R;

public class LoadingDialog {
    private final Activity activity;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
