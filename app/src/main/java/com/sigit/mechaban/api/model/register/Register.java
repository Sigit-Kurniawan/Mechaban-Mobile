package com.sigit.mechaban.api.model.register;

import com.google.gson.annotations.SerializedName;

public class Register{

	@SerializedName("message")
	private final String message;

	@SerializedName("status")
	private final boolean status;

    public Register(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage(){
		return message;
	}

	public boolean isStatus(){
		return status;
	}
}