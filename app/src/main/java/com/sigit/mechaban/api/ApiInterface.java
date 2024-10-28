package com.sigit.mechaban.api;

import com.sigit.mechaban.api.model.car.CarAPI;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.object.Account;
import com.sigit.mechaban.object.Car;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("account.php")
    Call<AccountAPI> accountResponse(@Body Account account);

    @POST("car.php")
    Call<CarAPI> carResponse(@Body Car car);
}
