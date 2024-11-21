package com.sigit.mechaban.object;

import java.util.List;

public class Booking {
    private String action, nopol;
    private double latitude, longitude;
    private List<BookingService> services;

    public void setAction(String action) {
        this.action = action;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setServices(List<BookingService> services) {
        this.services = services;
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
