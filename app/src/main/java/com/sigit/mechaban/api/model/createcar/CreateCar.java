package com.sigit.mechaban.api.model.createcar;

import com.google.gson.annotations.SerializedName;

public class CreateCar {
    @SerializedName("status")
    private final boolean status;

    @SerializedName("message")
    private final String message;

    public CreateCar(boolean status, String message) {
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
