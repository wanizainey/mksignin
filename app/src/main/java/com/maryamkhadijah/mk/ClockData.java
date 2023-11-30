package com.maryamkhadijah.mk;

public class ClockData {
    private String time;
    private double latitude;
    private double longitude;

    private String address; // New field for the address

    // Default constructor required for Firebase
    public ClockData() {}

    public ClockData(String time, double latitude, double longitude, String address) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }
}
