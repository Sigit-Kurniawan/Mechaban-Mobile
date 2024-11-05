package com.sigit.mechaban.api.model.service;

import com.google.gson.annotations.SerializedName;

public class ServiceData {
    @SerializedName("id_data_servis")
    private final String idService;

    @SerializedName("nama_servis")
    private final String service;

    @SerializedName("harga_servis")
    private final int priceService;

    public ServiceData(String idService, String service, int priceService) {
        this.idService = idService;
        this.service = service;
        this.priceService = priceService;
    }

    public String getIdService() {
        return idService;
    }

    public String getService() {
        return service;
    }

    public int getPriceService() {
        return priceService;
    }
}
