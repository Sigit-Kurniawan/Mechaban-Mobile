package com.sigit.mechaban.api.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginData {

	@SerializedName("no_hp")
	private String noHp;

	public void setNoHp(String noHp){
		this.noHp = noHp;
	}

	public String getNoHp(){
		return noHp;
	}
}