package com.sigit.mechaban.api;

import com.sigit.mechaban.api.model.readaccount.ReadAccount;
import com.sigit.mechaban.api.model.login.Login;
import com.sigit.mechaban.api.model.register.Register;
import com.sigit.mechaban.api.model.updateaccount.UpdateAccount;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<Register> registerResponse(
            @Field("email") String email,
            @Field("name") String name,
            @Field("no_hp") String noHP,
            @Field("password") String password,
            @Field("confirm_password") String confirmPassword
    );

    @FormUrlEncoded
    @POST("read_account.php")
    Call<ReadAccount> readAccountResponse(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("update_account.php")
    Call<UpdateAccount> updateAccountResponse(
            @Field("email_default") String emailDefault,
            @Field("email_update") String emailUpdate,
            @Field("name") String name,
            @Field("no_hp") String noHP,
            @Field("password") String password
    );
}
