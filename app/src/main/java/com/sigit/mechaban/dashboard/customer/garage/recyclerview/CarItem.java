package com.sigit.mechaban.dashboard.customer.garage.recyclerview;

public class CarItem {
    private final String merk, type, year;

    public CarItem(String merk, String type, String year) {
        this.merk = merk;
        this.type = type;
        this.year = year;
    }

    public String getMerk() {
        return merk;
    }

    public String getType() {
        return type;
    }

    public String getYear() {
        return year;
    }
}
