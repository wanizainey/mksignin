package com.maryamkhadijah.mk;

public class ClockData {
    private String time;
    private double latitude;
    private double longitude;

    private String address; // New field for the address
    private String clockType;  // new field for clock type

    // Default constructor required for Firebase
    public ClockData() {}

    public ClockData(String time, double latitude, double longitude, String address, String clockType) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.clockType = clockType;
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

    public String getClockType() {
        return clockType;
    }
}
