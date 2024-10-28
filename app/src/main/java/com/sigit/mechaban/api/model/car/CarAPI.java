package com.sigit.mechaban.api.model.car;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarAPI {
    @SerializedName("data")
    private final List<CarData> carData;

    @SerializedName("message")
    private final String message;

    @SerializedName("status")
    private final boolean status;

    @SerializedName("exist")
    private final boolean exist;

    public CarAPI(List<CarData> carData, String message, boolean status, boolean exist) {
        this.carData = carData;
        this.message = message;
        this.status = status;
        this.exist = exist;
    }

    public List<CarData> getCarData() {
        return carData;
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
