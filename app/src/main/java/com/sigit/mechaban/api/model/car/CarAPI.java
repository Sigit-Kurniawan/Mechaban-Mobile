package com.sigit.mechaban.api.model.car;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarAPI {
    @SerializedName("code")
    private final int code;

    @SerializedName("data")
    private final CarData carData;

    @SerializedName("cars")
    private final List<CarData> listCarData;

    @SerializedName("message")
    private final String message;

    @SerializedName("status")
    private final boolean status;

    @SerializedName("exist")
    private final boolean exist;

    public CarAPI(int code, CarData carData, List<CarData> listCarData, String message, boolean status, boolean exist) {
        this.code = code;
        this.carData = carData;
        this.listCarData = listCarData;
        this.message = message;
        this.status = status;
        this.exist = exist;
    }

    public int getCode() {
        return code;
    }

    public CarData getCarData() {
        return carData;
    }

    public List<CarData> getListCarData() {
        return listCarData;
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
