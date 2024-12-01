package com.sigit.mechaban.api.model.montir;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MontirAPI {
    @SerializedName("code")
    private final int code;

    @SerializedName("list")
    private final List<MontirData> montirDataList;

    @SerializedName("message")
    private final String message;

    public MontirAPI(int code, List<MontirData> montirDataList, String message) {
        this.code = code;
        this.montirDataList = montirDataList;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public List<MontirData> getMontirDataList() {
        return montirDataList;
    }

    public String getMessage() {
        return message;
    }
}
