package com.example.shuttland;

import com.google.android.gms.maps.model.LatLng;

public class Shuttle_Info {
    private LatLng location;
    private boolean isActive;
    private int counter;

    public Shuttle_Info() {
        this.location = new LatLng(0,0);
        this.isActive = false;
        this.counter = 0;
    }


    public Shuttle_Info(LatLng location, boolean isActive) {
        this.location = location;
        this.isActive = isActive;
        if (isActive) {
            counter = 1;
        } else {
            counter = 0;
        }
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

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
    public void increaseCounter(){
       this.counter++;
    }
}
