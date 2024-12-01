package com.sigit.mechaban.api;

import com.sigit.mechaban.api.model.booking.BookingAPI;
import com.sigit.mechaban.api.model.car.CarAPI;
import com.sigit.mechaban.api.model.account.AccountAPI;
import com.sigit.mechaban.api.model.montir.MontirAPI;
import com.sigit.mechaban.api.model.service.ServiceAPI;
import com.sigit.mechaban.object.Account;
import com.sigit.mechaban.object.Booking;
import com.sigit.mechaban.object.Car;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("account.php")
    Call<AccountAPI> accountResponse(@Body Account account);

    @POST("car.php")
    Call<CarAPI> carResponse(@Body Car car);

    @GET("service.php")
    Call<ServiceAPI> serviceResponse();

    @POST("booking.php")
    Call<BookingAPI> bookingResponse(@Body Booking booking);

    @GET("montir.php")
    Call<MontirAPI> montirResponse();
}
