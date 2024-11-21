package com.sigit.mechaban.api.model.booking;

import com.google.gson.annotations.SerializedName;

public class BookingData {
    @SerializedName("id_booking")
    private final String id;

    @SerializedName("tgl_booking")
    private final String date;

    @SerializedName("nopol")
    private final String nopol;

    @SerializedName("nama_customer")
    private final String name;

    @SerializedName("id_data_servis")
    private final String services;

    @SerializedName("total_harga")
    private final double total;

    @SerializedName("latitude")
    private final double latitude;

    @SerializedName("longitude")
    private final double longitude;

    public BookingData(String id, String date, String nopol, String name, String services, double total, double latitude, double longitude) {
        this.id = id;
        this.date = date;
        this.nopol = nopol;
        this.name = name;
        this.services = services;
        this.total = total;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getNopol() {
        return nopol;
    }

    public String getName() {
        return name;
    }

    public String getServices() {
        return services;
    }

    public double getTotal() {
        return total;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
