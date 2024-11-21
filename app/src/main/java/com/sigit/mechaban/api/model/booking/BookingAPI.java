package com.sigit.mechaban.api.model.booking;

import com.google.gson.annotations.SerializedName;

public class BookingAPI {
    @SerializedName("code")
    private final int code;

    @SerializedName("data")
    private final BookingData bookingData;

    public BookingAPI(int code, BookingData bookingData) {
        this.code = code;
        this.bookingData = bookingData;
    }

    public int getCode() {
        return code;
    }

    public BookingData getBookingData() {
        return bookingData;
    }
}
