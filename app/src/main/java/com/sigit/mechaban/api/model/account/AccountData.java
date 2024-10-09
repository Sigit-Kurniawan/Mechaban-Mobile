package com.sigit.mechaban.api.model.account;

import com.google.gson.annotations.SerializedName;

public class AccountData {

	@SerializedName("email")
	private final String email;

	@SerializedName("name")
	private final String name;

	public AccountData(String email, String name) {
		this.email = email;
		this.name = name;
	}

	public String getEmail(){
		return email;
	}

	public String getName(){
		return name;
	}
}