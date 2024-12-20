package com.sigit.mechaban.object;

import java.util.List;

public class Booking {
    private String action, id_booking, tgl_booking, nopol, email, status, review;
    private double latitude, longitude;
    private int rating;
    private List<BookingService> services;
    private List<String> emails;

    public void setAction(String action) {
        this.action = action;
    }

    public String getId_booking() {
        return id_booking;
    }

    public void setId_booking(String id_booking) {
        this.id_booking = id_booking;
    }

    public void setTgl_booking(String tgl_booking) {
        this.tgl_booking = tgl_booking;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setServices(List<BookingService> services) {
        this.services = services;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public static class BookingService {
        private final String id_data_servis;

        public BookingService(String id_data_servis) {
            this.id_data_servis = id_data_servis;
        }

        public String getId_data_servis() {
            return id_data_servis;
        }
    }
}
