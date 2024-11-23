package com.sigit.mechaban.api.model.booking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingAPI {
    @SerializedName("code")
    private final int code;

    @SerializedName("data")
    private final BookingData bookingData;

    @SerializedName("list")
    private final List<BookingData> bookingDataList;

    @SerializedName("message")
    private final String message;

    public BookingAPI(int code, BookingData bookingData, List<BookingData> bookingDataList, String message) {
        this.code = code;
        this.bookingData = bookingData;
        this.bookingDataList = bookingDataList;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public BookingData getBookingData() {
        return bookingData;
    }

    public List<BookingData> getBookingDataList() {
        return bookingDataList;
    }

    public String getMessage() {
        return message;
    }
}
