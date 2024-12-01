package com.sigit.mechaban.api.model.montir;

import com.google.gson.annotations.SerializedName;

public class MontirData {
    @SerializedName("name")
    private final String name;

    @SerializedName("email")
    private final String email;

    @SerializedName("no_hp")
    private final String no_hp;

    @SerializedName("photo")
    private final int photo;

    @SerializedName("nama_anggota")
    private final String namaAnggota;

    public MontirData(String name, String email, String noHp, int photo, String namaAnggota) {
        this.name = name;
        this.email = email;
        this.no_hp = noHp;
        this.photo = photo;
        this.namaAnggota = namaAnggota;
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

    public int getPhoto() {
        return photo;
    }

    public String getNamaAnggota() {
        return namaAnggota;
    }
}
