package com.example.shuttland;

import com.google.android.gms.maps.model.LatLng;

public class Shuttle_Info {
    private LatLng location;
    private boolean isActive;

    public Shuttle_Info() {
        this.location = new LatLng(0,0);
        this.isActive = false;
    }


    public Shuttle_Info(LatLng location, boolean isActive) {
        this.location = location;
        this.isActive = isActive;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
