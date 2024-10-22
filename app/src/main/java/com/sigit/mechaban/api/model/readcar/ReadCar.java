package com.sigit.mechaban.api.model.readcar;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReadCar {

    @SerializedName("data")
    private final List<ReadCarData> readCarData;

    @SerializedName("message")
    private final String message;

    @SerializedName("status")
    private final boolean status;

    @SerializedName("exist")
    private final boolean exist;

    public ReadCar(List<ReadCarData> readCarData, String message, boolean status, boolean exist) {
        this.readCarData = readCarData;
        this.message = message;
        this.status = status;
        this.exist = exist;
    }

    public List<ReadCarData> getReadCarData() {
        return readCarData;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isExist() {
        return exist;
    }
}
