package com.sigit.mechaban.api.model.booking;

import com.google.gson.annotations.SerializedName;
import com.sigit.mechaban.api.model.service.ServiceData;

import java.util.List;

public class BookingData {
    @SerializedName("id_booking")
    private final String id_booking;

    @SerializedName("tgl_booking")
    private final String tgl_booking;

    @SerializedName("latitude")
    private final double latitude;

    @SerializedName("longitude")
    private final double longitude;

    @SerializedName("name")
    private final String name;

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

    @SerializedName("metode_bayar")
    private final String metode_bayar;

    @SerializedName("status_bayar")
    private final String status_bayar;

    @SerializedName("status_pengerjaan")
    private final String status_pengerjaan;

    @SerializedName("total_biaya")
    private final double total_biaya;

    @SerializedName("services")
    private final List<ServiceData> serviceData;

    public BookingData(String id_booking, String tgl_booking, double latitude, double longitude, String name, String nopol, String merk, String type, String transmition, String year, String metode_bayar, String status_bayar, String status_pengerjaan, double total_biaya, List<ServiceData> serviceData) {
        this.id_booking = id_booking;
        this.tgl_booking = tgl_booking;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.nopol = nopol;
        this.merk = merk;
        this.type = type;
        this.transmition = transmition;
        this.year = year;
        this.metode_bayar = metode_bayar;
        this.status_bayar = status_bayar;
        this.status_pengerjaan = status_pengerjaan;
        this.total_biaya = total_biaya;
        this.serviceData = serviceData;
    }

    public String getId_booking() {
        return id_booking;
    }

    public String getTgl_booking() {
        return tgl_booking;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
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

    public String getMetode_bayar() {
        return metode_bayar;
    }

    public String getStatus_bayar() {
        return status_bayar;
    }

    public String getStatus_pengerjaan() {
        return status_pengerjaan;
    }

    public double getTotal_biaya() {
        return total_biaya;
    }

    public List<ServiceData> getServiceData() {
        return serviceData;
    }
}