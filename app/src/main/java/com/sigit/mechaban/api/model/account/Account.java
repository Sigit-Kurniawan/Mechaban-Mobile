package com.sigit.mechaban.api.model.account;

import com.google.gson.annotations.SerializedName;

public class Account{

	@SerializedName("data")
	private final AccountData accountData;

    public Account(AccountData accountData) {
        this.accountData = accountData;
    }

    public AccountData getAccountData(){
		return accountData;
	}
}