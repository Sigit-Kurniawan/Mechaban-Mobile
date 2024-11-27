package com.sigit.mechaban.api.model.car;

import com.google.gson.annotations.SerializedName;

public class CarData {

    @SerializedName("nopol")
    private final String nopol;

    @SerializedName("merk")
    private final String merk;

    @SerializedName("type")
    private final String type;

    @SerializedName("transmition")
    private final String transmition;

    @SerializedName("year")
    private final String year;

    @SerializedName("status")
    private final int status;

    public CarData(String nopol, String merk, String type, String transmition, String year, int status) {
        this.nopol = nopol;
        this.merk = merk;
        this.type = type;
        this.transmition = transmition;
        this.year = year;
        this.status = status;
    }

    public String getNopol() {
        return nopol;
    }

    public String getMerk() {
        return merk;
    }

    public String getType() {
        return type;
    }

    public String getTransmition() {
        return transmition;
    }

    public String getYear() {
        return year;
    }

    public int getStatus() {
        return status;
    }
}
