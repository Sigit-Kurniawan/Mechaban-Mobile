package com.sigit.mechaban.api.model.status;

import com.google.gson.annotations.SerializedName;

public class StatusAPI {
    @SerializedName("code")
    private final int code;

    @SerializedName("data")
    private final int data;

    @SerializedName("message")
    private final String message;

    public StatusAPI(int code, int data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public int getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
