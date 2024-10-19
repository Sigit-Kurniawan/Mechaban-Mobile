package com.sigit.mechaban.api.model.updateaccount;

import com.google.gson.annotations.SerializedName;

public class UpdateAccount {

    @SerializedName("status")
    private final boolean status;

    @SerializedName("message")
    private final String message;

    public UpdateAccount(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
