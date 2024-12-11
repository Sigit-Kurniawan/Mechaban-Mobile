package com.sigit.mechaban.api.model.account;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountAPI {
    @SerializedName("data")
    private final AccountData AccountData;

    @SerializedName("message")
    private final String message;

    @SerializedName("code")
    private final int code;

    @SerializedName("list")
    private final List<AccountData> accountDataList;

    public AccountAPI(AccountData accountData, String message, int code, List<AccountData> accountDataList) {
        this.AccountData = accountData;
        this.message = message;
        this.code = code;
        this.accountDataList = accountDataList;
    }

    public AccountData getAccountData() {
        return AccountData;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public List<AccountData> getAccountDataList() {
        return accountDataList;
    }
}
