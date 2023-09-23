package com.example.wastelocator;

public class Bin {
    private String id;
    private String volume;
    private String latitude;
    private String longitude;

    public Bin(String id, String volume, String latitude, String longitude) {
        this.id = id;
        this.volume = volume;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }
    public String getVolume() {
        return volume;
    }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }
    public void setId(String id) { this.id = id;}
    public void setVolume(String volume) { this.volume = volume;}
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
