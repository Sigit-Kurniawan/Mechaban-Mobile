package com.sigit.mechaban.api;

import com.sigit.mechaban.api.model.login.Login;
import com.sigit.mechaban.api.model.register.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("no_hp") String noHp,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<Register> registerResponse(
            @Field("no_hp") String noHp,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirm_password") String confirmPassword
    );
}
