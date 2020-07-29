package com.example.shuttland;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public class Shuttle_Map {

    private Map<Integer, Shuttle_Info> shuttle_infoMap;
    private final static int NUM_SHUTTLE = 7;
    private final static int NUM_ACCESS_SHUTTLE=2;
    private static Shuttle_Map my_instance = null;

    @SuppressLint("UseSparseArrays")
    private Shuttle_Map() {
        this.shuttle_infoMap = new HashMap<>();
        for (int i = 0; i < NUM_SHUTTLE+NUM_ACCESS_SHUTTLE; i++) {
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

    public Map<Integer, Shuttle_Info> getAccessActiveShuttles() {
        @SuppressLint("UseSparseArrays") Map<Integer, Shuttle_Info> access_active=new HashMap<>();
        for (Map.Entry<Integer, Shuttle_Info> entry : Shuttle_Map.getInstance().getShuttle_infoMap().entrySet()) {
            if(entry.getValue().isActive()&& entry.getKey()>NUM_SHUTTLE) {
                access_active.put(entry.getKey(), entry.getValue());
            }
        }
        return access_active;
    }

    public int getCounter(Integer key) {
        return this.shuttle_infoMap.get(key).getCounter();
    }

}
