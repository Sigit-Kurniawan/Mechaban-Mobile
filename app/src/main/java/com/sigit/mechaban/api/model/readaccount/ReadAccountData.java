package com.sigit.mechaban.api.model.readaccount;

import com.google.gson.annotations.SerializedName;

public class ReadAccountData {

	@SerializedName("email")
	private final String email;

	@SerializedName("name")
	private final String name;

	@SerializedName("no_hp")
	private final String noHp;

	@SerializedName("password")
	private final String password;

	public ReadAccountData(String email, String name, String noHp, String password) {
		this.email = email;
		this.name = name;
		this.noHp = noHp;
		this.password = password;
	}

	public String getEmail(){
		return email;
	}

	public String getName(){
		return name;
	}

	public String getNoHp() {
		return noHp;
	}

	public String getPassword() {
		return password;
	}
}