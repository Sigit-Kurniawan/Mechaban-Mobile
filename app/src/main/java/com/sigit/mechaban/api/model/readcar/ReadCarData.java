package com.sigit.mechaban.api.model.readcar;

import com.google.gson.annotations.SerializedName;

public class ReadCarData {

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

    public ReadCarData(String nopol, String merk, String type, String transmition, String year) {
        this.nopol = nopol;
        this.merk = merk;
        this.type = type;
        this.transmition = transmition;
        this.year = year;
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
}
