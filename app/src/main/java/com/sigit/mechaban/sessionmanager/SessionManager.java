package com.sigit.mechaban.sessionmanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.sigit.mechaban.api.model.account.AccountData;

import java.util.HashMap;

public class SessionManager {
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void createLoginSession(AccountData accountData) {
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", accountData.getEmail());
        editor.commit();
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put("email", preferences.getString("email", null));
        user.put("nopol", preferences.getString("nopol", null));
        return user;
    }

    public void logoutSession() {
        editor.putBoolean("isLoggedIn", false);
        editor.putString("email", null);
        editor.putString("nopol", null);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("isLoggedIn", false);
    }

    public void updateEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public void updateCar(String nopol) {
        editor.putString("nopol", nopol);
        editor.commit();
    }

    public void deleteCar() {
        editor.putString("nopol", null);
        editor.commit();
    }
}
