package com.example.wastelocator.Dashboard;

public class Bin {
    private int id;
    private double latitude;
    private double longitude;
    private int fillLevel;

    public Bin(int id, double latitude, double longitude, int fillLevel) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fillLevel = fillLevel;
    }

    public int getId() {
        return id;
    }

    public int getFillLevel() {
        return fillLevel;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFillLevel(int fillLevel) {
        this.fillLevel = fillLevel;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
