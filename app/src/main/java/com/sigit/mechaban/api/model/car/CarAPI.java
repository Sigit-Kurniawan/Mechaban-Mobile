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

    public CarAPI(int code, CarData carData, List<CarData> listCarData, String message) {
        this.code = code;
        this.carData = carData;
        this.listCarData = listCarData;
        this.message = message;
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
}
