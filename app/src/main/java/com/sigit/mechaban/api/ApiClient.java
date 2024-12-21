package com.sigit.mechaban.api;

import com.sigit.mechaban.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String baseUrl = "https://" + BuildConfig.ip + "/Api/api/";
    private static final String photoUrl = "https://" + BuildConfig.ip + "/uploads/customers/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getPhotoUrl() {
        return photoUrl;
    }
}
