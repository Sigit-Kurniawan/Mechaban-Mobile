package com.sigit.mechaban.components;

import android.app.Activity;
import android.app.AlertDialog;

import com.sigit.mechaban.R;

public class LoadingDialog {
    private final Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(activity.getLayoutInflater().inflate(R.layout.loading_dialog, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog() {
        alertDialog.dismiss();
    }
}
