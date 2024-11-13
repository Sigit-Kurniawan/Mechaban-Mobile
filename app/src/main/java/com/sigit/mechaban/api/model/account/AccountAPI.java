package com.sigit.mechaban.api.model.account;

import com.google.gson.annotations.SerializedName;

public class AccountAPI {
    @SerializedName("data")
    private final AccountData AccountData;

    @SerializedName("message")
    private final String message;

    @SerializedName("status")
    private final boolean status;

    @SerializedName("code")
    private final int code;

    public AccountAPI(AccountData accountData, String message, boolean status, int code) {
        this.AccountData = accountData;
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public AccountData getAccountData() {
        return AccountData;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }
}
