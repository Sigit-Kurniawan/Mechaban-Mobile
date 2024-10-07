package com.sigit.mechaban.api.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginData {

	@SerializedName("no_hp")
	private final String noHp;

    public LoginData(String noHp) {
        this.noHp = noHp;
    }

    public String getNoHp(){
		return noHp;
	}
}