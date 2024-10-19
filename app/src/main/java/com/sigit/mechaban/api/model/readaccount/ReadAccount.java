package com.sigit.mechaban.api.model.readaccount;

import com.google.gson.annotations.SerializedName;

public class ReadAccount {

	@SerializedName("data")
	private final ReadAccountData readAccountData;

    @SerializedName("message")
	private final String message;

    public ReadAccount(ReadAccountData readAccountData, String message) {
        this.readAccountData = readAccountData;
        this.message = message;
    }

    public ReadAccountData getAccountData(){
		return readAccountData;
	}

    public String getMessage() {
        return message;
    }
}