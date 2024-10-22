package com.sigit.mechaban.api.model.readaccount;

import com.google.gson.annotations.SerializedName;

public class ReadAccount {

    @SerializedName("status")
    private final boolean status;

	@SerializedName("data")
	private final ReadAccountData readAccountData;

    @SerializedName("message")
	private final String message;

    public ReadAccount(ReadAccountData readAccountData, String message, boolean status) {
        this.readAccountData = readAccountData;
        this.message = message;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public ReadAccountData getAccountData(){
		return readAccountData;
	}

    public String getMessage() {
        return message;
    }
}