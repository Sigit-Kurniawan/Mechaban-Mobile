package com.sigit.mechaban.api.model.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ServiceAPI {
    @SerializedName("data")
    private final Map<String, List<ServiceData>> component;

    @SerializedName("message")
    private final String message;

    public ServiceAPI(Map<String, List<ServiceData>> component, String message) {
        this.component = component;
        this.message = message;
    }

    public Map<String, List<ServiceData>> getComponent() {
        return component;
    }

    public String getMessage() {
        return message;
    }
}
