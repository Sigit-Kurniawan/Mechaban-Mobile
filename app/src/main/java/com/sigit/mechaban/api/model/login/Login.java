package com.sigit.mechaban.api.model.login;

import com.google.gson.annotations.SerializedName;

public class Login {

	@SerializedName("data")
	private final LoginData loginData;

	@SerializedName("message")
	private final String message;

	@SerializedName("status")
	private final boolean status;

    public Login (LoginData loginData, String message, boolean status) {
        this.loginData = loginData;
        this.message = message;
        this.status = status;
    }

    public LoginData getLoginData(){
		return loginData;
	}

	public String getMessage(){
		return message;
	}

	public boolean isStatus(){
		return status;
	}
}