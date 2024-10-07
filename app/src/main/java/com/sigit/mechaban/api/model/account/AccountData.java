package com.sigit.mechaban.api.model.account;

import com.google.gson.annotations.SerializedName;

public class AccountData {

	@SerializedName("no_hp")
	private final String noHp;

	@SerializedName("name")
	private final String name;

	public AccountData(String noHp, String name) {
		this.noHp = noHp;
		this.name = name;
	}

	public String getNoHp(){
		return noHp;
	}

	public String getName(){
		return name;
	}
}