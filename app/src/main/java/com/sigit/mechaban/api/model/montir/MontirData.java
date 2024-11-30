package com.sigit.mechaban.api.model.montir;

import com.google.gson.annotations.SerializedName;

public class MontirData {
    @SerializedName("nama_anggota")
    private final String namaAnggota;

    public MontirData(String namaAnggota) {
        this.namaAnggota = namaAnggota;
    }

    public String getNamaAnggota() {
        return namaAnggota;
    }
}
