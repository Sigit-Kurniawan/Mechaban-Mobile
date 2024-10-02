package com.sigit.mechaban.auth;

import com.sigit.mechaban.BuildConfig;

public class Database {
    private static final String ip = BuildConfig.ip;

    public String getRegister() {
        return "http://" + ip + "/api/register.php";
    }

    public String getLogin() {
        return "http://" + ip + "/api/login.php";
    }
}
