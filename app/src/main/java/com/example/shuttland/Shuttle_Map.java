package com.example.shuttland;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

class Shuttle_Map {

    private Map<Integer, Shuttle_Info> shuttle_infoMap;
    private final static int NUM_SHUTTLE = 7;
    private final static int NUM_ACCESS_SHUTTLE=2;
    private static Shuttle_Map my_instance = null;

    @SuppressLint("UseSparseArrays")
    private Shuttle_Map() {
        this.shuttle_infoMap = new HashMap<>();
        // initialize the shuttles
        for (int i = 0; i < NUM_SHUTTLE+NUM_ACCESS_SHUTTLE; i++) {
            shuttle_infoMap.put(i, new Shuttle_Info());
        }

    }


    static Shuttle_Map getInstance() {
        if (my_instance == null) {
            my_instance = new Shuttle_Map();
        }
        return my_instance;
    }

    void setMap(int shuttle_id, Shuttle_Info info) {
        Shuttle_Map.getInstance().shuttle_infoMap.put(shuttle_id, info);
    }

    Map<Integer, Shuttle_Info> getShuttle_infoMap() {
        return shuttle_infoMap;
    }

    /**
     * @return Map with all the active shuttles.
     */
    Map<Integer, Shuttle_Info> getActiveShuttles() {
        @SuppressLint("UseSparseArrays") Map<Integer, Shuttle_Info> active=new HashMap<>();
        for (Map.Entry<Integer, Shuttle_Info> entry : Shuttle_Map.getInstance().getShuttle_infoMap().entrySet()) {
            if(entry.getValue().isActive()) {
                active.put(entry.getKey(), entry.getValue());
            }
        }
        return active;
    }

    /**
     * @return Map with all the access active shuttles.
     */
    Map<Integer, Shuttle_Info> getAccessActiveShuttles() {
        @SuppressLint("UseSparseArrays") Map<Integer, Shuttle_Info> access_active=new HashMap<>();
        for (Map.Entry<Integer, Shuttle_Info> entry : Shuttle_Map.getInstance().getShuttle_infoMap().entrySet()) {
            if(entry.getValue().isActive()&& entry.getKey()>NUM_SHUTTLE) {
                access_active.put(entry.getKey(), entry.getValue());
            }
        }
        return access_active;
    }

    int getCounter(Integer key) {
        return this.shuttle_infoMap.get(key).getCounter();
    }

}
