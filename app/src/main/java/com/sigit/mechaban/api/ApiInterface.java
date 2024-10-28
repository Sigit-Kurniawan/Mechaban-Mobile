package com.sigit.mechaban.api;

import com.sigit.mechaban.api.model.createcar.CreateCar;
import com.sigit.mechaban.api.model.readaccount.ReadAccount;
import com.sigit.mechaban.api.model.login.Login;
import com.sigit.mechaban.api.model.readcar.ReadCar;
import com.sigit.mechaban.api.model.register.Register;
import com.sigit.mechaban.api.model.updateaccount.UpdateAccount;
import com.sigit.mechaban.object.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("login.php")
    Call<Login> loginResponse(@Body Account account);

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

    @FormUrlEncoded
    @POST("create_car.php")
    Call<CreateCar> createCarResponse(
            @Field("nopol") String nopol,
            @Field("merk") String merk,
            @Field("type") String type,
            @Field("transmition") String transmition,
            @Field("year") String year,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("read_car.php")
    Call<ReadCar> readCarResponse(
            @Field("email") String email
    );
}
