package com.maryamkhadijah.mk;

public class ClockData {
    private String time;
    private double latitude;
    private double longitude;

    // Default constructor required for Firebase
    public ClockData() {}

    public ClockData(String time, double latitude, double longitude) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
