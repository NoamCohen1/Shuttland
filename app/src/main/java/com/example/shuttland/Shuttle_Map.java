package com.example.shuttland;

import android.annotation.SuppressLint;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shuttle_Map {

    private Map<Integer, Shuttle_Info> shuttle_infoMap;
    final static int num_shuttle = 7;
    private static Shuttle_Map my_instance = null;

    @SuppressLint("UseSparseArrays")
    private Shuttle_Map() {
        this.shuttle_infoMap = new HashMap<>();
        for (int i = 0; i < num_shuttle; i++) {
            shuttle_infoMap.put(i, new Shuttle_Info());
        }
    }

    public static Shuttle_Map getInstance() {
        if (my_instance == null) {
            my_instance = new Shuttle_Map();
        }

        return my_instance;
    }

    public void setMap(int shuttle_id, Shuttle_Info info) {
        this.shuttle_infoMap.put(shuttle_id, info);
    }
}
