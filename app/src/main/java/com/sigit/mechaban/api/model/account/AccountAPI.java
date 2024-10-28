package com.sigit.mechaban.api.model.account;

import com.google.gson.annotations.SerializedName;

public class AccountAPI {
    @SerializedName("data")
    private final AccountData AccountData;

    @SerializedName("message")
    private final String message;

    @SerializedName("status")
    private final boolean status;

    public AccountAPI(AccountData accountData, String message, boolean status) {
        this.AccountData = accountData;
        this.message = message;
        this.status = status;
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
}
