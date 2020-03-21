package com.example.shuttland;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
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
        Shuttle_Info info = new Shuttle_Info(new LatLng(32.0734411, 34.8483981), true);
        shuttle_infoMap.put(1, info);
        info = new Shuttle_Info(new LatLng(32.0735301, 34.846368),true);
        shuttle_infoMap.put(2, info);
    }


    public static Shuttle_Map getInstance() {
        if (my_instance == null) {
            my_instance = new Shuttle_Map();
        }

        return my_instance;
    }

    public void setMap(int shuttle_id, Shuttle_Info info) {
        Shuttle_Map.getInstance().shuttle_infoMap.put(shuttle_id, info);
    }

    public Map<Integer, Shuttle_Info> getShuttle_infoMap() {
        return shuttle_infoMap;
    }

    public Map<Integer, Shuttle_Info> getActiveShuttles() {
        @SuppressLint("UseSparseArrays") Map<Integer, Shuttle_Info> active=new HashMap<>();
        for (Map.Entry<Integer, Shuttle_Info> entry : Shuttle_Map.getInstance().getShuttle_infoMap().entrySet()) {
            if(entry.getValue().isActive()) {
                active.put(entry.getKey(), entry.getValue());
            }
        }
        return active;
    }
}
