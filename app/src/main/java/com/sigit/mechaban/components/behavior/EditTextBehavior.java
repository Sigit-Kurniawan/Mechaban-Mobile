package com.sigit.mechaban.components.behavior;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.sigit.mechaban.R;

public class EditTextBehavior {
    public static boolean validateName(Context context, EditText editText, TextInputLayout textInputLayout) {
        String name = editText.getText().toString().trim();
        if (name.isEmpty()) {
            textInputLayout.setError("Kolom nama tidak boleh kosong!");
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            return true;
        }
    }

    public static boolean validateEmail(Context context, EditText editText, TextInputLayout textInputLayout) {
        String email = editText.getText().toString().trim();
        if (email.isEmpty()) {
            textInputLayout.setError("Kolom email-nya masih kosong!");
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayout.setError("Format email tidak valid!");
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            return true;
        }
    }

    public static boolean validateNoHP(Context context, EditText editText, TextInputLayout textInputLayout) {
        String noHP = editText.getText().toString().trim();
        if (noHP.isEmpty()) {
            textInputLayout.setError("Kolom no. HP-nya tidak boleh kosong!");
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else if (noHP.length() < 10) {
            textInputLayout.setError("No. HP tidak valid!");
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            return true;
        }
    }

    public static boolean validatePassword(Context context, EditText editText, TextInputLayout textInputLayout) {
        String password = editText.getText().toString().trim();
        if (password.isEmpty()) {
            textInputLayout.setError("Kolom password tidak boleh kosong!");
            textInputLayout.setErrorIconDrawable(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            return true;
        }
    }

    public static boolean validatePasswordRegister(Context context, EditText editText, TextInputLayout textInputLayout) {
        String password = editText.getText().toString().trim();
        if (password.isEmpty()) {
            textInputLayout.setError("Kolom password tidak boleh kosong!");
            textInputLayout.setErrorIconDrawable(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else if (!(password.length() >= 8)) {
            unvalidPassword(textInputLayout, context);
            return false;
        } else if (!password.matches("(.*[A-Z].*)")) {
            unvalidPassword(textInputLayout, context);
            return false;
        } else if (!password.matches("(.*[a-z].*)")) {
            unvalidPassword(textInputLayout, context);
            return false;
        } else if (!password.matches(".*[0-9].*")) {
            unvalidPassword(textInputLayout, context);
            return false;
        } else if (!password.matches(".*[@$!%*?&].*")) {
            unvalidPassword(textInputLayout, context);
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            return true;
        }
    }

    public static boolean validateConfirmPassword(Context context, EditText passwordEditText, EditText editText, TextInputLayout textInputLayout) {
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = editText.getText().toString().trim();
        if (confirmPassword.isEmpty()) {
            textInputLayout.setError("Kolom konfirmasi password tidak boleh kosong!");
            textInputLayout.setErrorIconDrawable(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else if (!confirmPassword.equals(password)) {
            textInputLayout.setError("Password tidak sama!");
            textInputLayout.setErrorIconDrawable(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
            return true;
        }
    }

    public static void setIconTintOnFocus(Context context, TextInputLayout textInputLayout, boolean hasFocus, boolean isValid) {
        if (hasFocus) {
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, isValid ? R.color.md_theme_primary : R.color.md_theme_error));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_primary));
        } else {
            textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, isValid ? R.color.md_theme_onSurfaceVariant : R.color.md_theme_error));
            textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_onSurfaceVariant));
        }
    }

    private static void unvalidPassword(TextInputLayout textInputLayout, Context context) {
        textInputLayout.setError("Password minimal 8 karakter, termasuk huruf kapital, huruf kecil, angka, dan simbol (@$!%*?&)!");
        textInputLayout.setErrorIconDrawable(null);
        textInputLayout.setStartIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
        textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(context, R.color.md_theme_error));
    }
}
