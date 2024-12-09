package com.sigit.mechaban.api.model.booking;

import com.google.gson.annotations.SerializedName;
import com.sigit.mechaban.api.model.montir.MontirData;
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

    @SerializedName("email")
    private final String email;

    @SerializedName("no_hp")
    private final String no_hp;

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
    private final String status;

    @SerializedName("ketua_montir")
    private final String ketua_montir;

    @SerializedName("total_biaya")
    private final double total_biaya;

    @SerializedName("services")
    private final List<ServiceData> serviceData;

    @SerializedName("anggota_montir")
    private final List<MontirData> montirData;

    @SerializedName("rating")
    private final String rating;

    @SerializedName("teks_review")
    private final String teks_review;

    @SerializedName("role")
    private final String role;

    public BookingData(String id_booking, String tgl_booking, double latitude, double longitude, String name, String email, String noHp, String nopol, String merk, String type, String transmition, String year, String status, String ketuaMontir, double total_biaya, List<ServiceData> serviceData, List<MontirData> montirData, String rating, String teksReview, String role) {
        this.id_booking = id_booking;
        this.tgl_booking = tgl_booking;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.email = email;
        this.no_hp = noHp;
        this.nopol = nopol;
        this.merk = merk;
        this.type = type;
        this.transmition = transmition;
        this.year = year;
        this.status = status;
        this.ketua_montir = ketuaMontir;
        this.total_biaya = total_biaya;
        this.serviceData = serviceData;
        this.montirData = montirData;
        this.rating = rating;
        this.teks_review = teksReview;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public String getNo_hp() {
        return no_hp;
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

    public String getStatus() {
        return status;
    }

    public String getKetua_montir() {
        return ketua_montir;
    }

    public double getTotal_biaya() {
        return total_biaya;
    }

    public List<ServiceData> getServiceData() {
        return serviceData;
    }

    public List<MontirData> getMontirData() {
        return montirData;
    }

    public String getRating() {
        return rating;
    }

    public String getTeks_review() {
        return teks_review;
    }

    public String getRole() {
        return role;
    }
}
