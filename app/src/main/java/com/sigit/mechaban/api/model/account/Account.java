package com.sigit.mechaban.api.model.account;

import com.google.gson.annotations.SerializedName;

public class Account{

	@SerializedName("data")
	private AccountData accountData;

	public AccountData getAccountData(){
		return accountData;
	}
}