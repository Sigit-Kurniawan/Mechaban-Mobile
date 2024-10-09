package com.sigit.mechaban.api.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginData {

	@SerializedName("email")
	private final String email;

    public LoginData(String email) {
        this.email = email;
    }

    public String getEmail(){
		return email;
	}
}