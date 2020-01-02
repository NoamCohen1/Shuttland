package com.example.shuttland;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapsDB {


    private Map<String, Float> distances;
    private List<Location> stations = new ArrayList<>();
    private static MapsDB single_instance = null;

    private MapsDB(){
        SetStationsLocations();
    }
    // static method to create instance of Singleton class
    public static MapsDB getInstance()
    {
        if (single_instance == null) {
            single_instance = new MapsDB();
        }

        return single_instance;
    }


    public void setStationsDistances() {
        distances.put("0->1", Float.valueOf("127"));
        distances.put("1->2", Float.valueOf("200"));
        distances.put("2->3", Float.valueOf("279"));
        distances.put("3->4", Float.valueOf("190"));
        distances.put("4->5", Float.valueOf("160"));
        distances.put("5->6", Float.valueOf("230"));
        distances.put("6->7", Float.valueOf("120"));
        distances.put("7->8", Float.valueOf("325"));
        distances.put("8->9", Float.valueOf("160"));
        distances.put("9->10", Float.valueOf("190"));
        distances.put("10->11", Float.valueOf("300"));
        distances.put("11->12", Float.valueOf("140"));
        distances.put("12->13", Float.valueOf("247"));
        distances.put("13->14", Float.valueOf("190"));
        distances.put("14->15", Float.valueOf("99"));
        distances.put("15->16", Float.valueOf("265"));
        distances.put("16->0", Float.valueOf("168"));
    }

    public void SetStationsLocations() {
        addLocation("0", 32.0727493, 34.849301);
        addLocation("1", 32.0734411, 34.8483981);
        addLocation("2", 32.0735301, 34.846368);
        addLocation("3", 32.0723118, 34.844318799999996);
        addLocation("4", 32.071165199999996, 34.8429532);
        addLocation("5", 32.069904, 34.8420564);
        addLocation("6", 32.0681564, 34.840918099999996);
        addLocation("7", 32.0671975, 34.840333699999995);
        addLocation("8", 32.0655436, 34.8424443);
        addLocation("9", 32.0659431, 34.844034199999996);
        addLocation("10", 32.0673506, 34.8446212);
        addLocation("11", 32.069542, 34.8438793);
        addLocation("12", 32.0695559, 34.8423678);
        addLocation("13", 32.0711751, 34.8430622);
        addLocation("14", 32.0722382, 34.8445136);
        addLocation("15", 32.073458699999996, 34.8459821);
        addLocation("16", 32.072267499999995, 34.8480397);
    }


    public void addLocation(String name, double lat, double lon) {
        Location location = new Location(name);
        location.setLatitude(lat);
        location.setLongitude(lon);
        stations.add(location);
    }

    public List<Location> getStations() {
        return stations;
    }
}
