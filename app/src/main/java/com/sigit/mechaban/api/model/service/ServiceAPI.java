package com.sigit.mechaban.api.model.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ServiceAPI {
    @SerializedName("data")
    private final Map<String, List<ServiceData>> component;

    public ServiceAPI(Map<String, List<ServiceData>> component) {
        this.component = component;
    }

    public Map<String, List<ServiceData>> getComponent() {
        return component;
    }
}
