package com.example.shuttland;

public class Shuttle_string {
    private String lat;
    private String lon;
    private String isActive;

    public Shuttle_string(String lat, String lon, String isActive) {
        this.lat = lat;
        this.lon = lon;
        this.isActive = isActive;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getIsActive() {
        return isActive;
    }
}
