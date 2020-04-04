package com.example.shuttland;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsDB {


    private List<Float> distance_of_station = new ArrayList<>();
    private List<Location> stations = new ArrayList<>();

    public Map<Integer, Integer> getBuilding_to_near_station() {
        return building_to_near_station;
    }

    private Map<Integer, Integer> building_to_near_station;
    private Map<Integer, Location> location_buildings;
    private static MapsDB single_instance = null;

    private MapsDB() {
        SetStationsLocations();
        setStationsDistances();
        building_to_near_station = new HashMap<>();
        location_buildings = new HashMap<>();
        setBuildingToNearStation();
    }

    // static method to create instance of Singleton class
    public static MapsDB getInstance() {
        if (single_instance == null) {
            single_instance = new MapsDB();
        }

        return single_instance;
    }


    public void setStationsDistances() {
        // 0->1
        distance_of_station.add(Float.valueOf("127"));
        //1->2
        distance_of_station.add( Float.valueOf("200"));
        // 2->3
        distance_of_station.add(Float.valueOf("279"));
        // 3->4
        distance_of_station.add(Float.valueOf("190"));
        // 4->5
        distance_of_station.add(Float.valueOf("160"));
        // 5->6
        distance_of_station.add(Float.valueOf("230"));
        // 6->7
        distance_of_station.add(Float.valueOf("120"));
        // 7->8
        distance_of_station.add(Float.valueOf("325"));
        // 8->9
        distance_of_station.add(Float.valueOf("160"));
        // 9->10
        distance_of_station.add(Float.valueOf("190"));
        // 10->11
        distance_of_station.add(Float.valueOf("300"));
        // 11->12
        distance_of_station.add(Float.valueOf("140"));
        // 12->13
        distance_of_station.add(Float.valueOf("247"));
        // 13->14
        distance_of_station.add(Float.valueOf("190"));
        // 14->15
        distance_of_station.add(Float.valueOf("99"));
        // 15->16
        distance_of_station.add(Float.valueOf("265"));
        // 16->0
        distance_of_station.add(Float.valueOf("168"));
    }

    public void setLocation_buildings(){
        //location_buildings.put(numBuilding,createLocation());

        location_buildings.put(501, createLocation("501", 32.069910, 34.842495));
        location_buildings.put(502, createLocation("502", 32.070693, 34.843264));
        //location_buildings.put(503, createLocation());
        location_buildings.put(504, createLocation("504", 32.069888, 34.844179));
        location_buildings.put(505, createLocation("505", 32.070525, 34.844453));
        location_buildings.put(506, createLocation("506", 32.070866, 34.844837));
        location_buildings.put(507, createLocation("507", 32.071138, 34.844493));
        location_buildings.put(508, createLocation("508", 32.071356, 34.843863));
        //location_buildings.put(509, createLocation());
        location_buildings.put(604,createLocation("604", 32.070314, 34.843767));
        location_buildings.put(605,createLocation("605", 32.070399, 34.843492));
        location_buildings.put(801,createLocation("801",32.071479, 34.844267));
        location_buildings.put(802,createLocation("802",32.072109, 34.844672));
        location_buildings.put(901,createLocation("901", 32.073178, 34.845940));
        location_buildings.put(902,createLocation("902", 32.073069, 34.846144));
        location_buildings.put(905,createLocation("905", 32.073379, 34.845569));
        //location_buildings.put(906,createLocation());
        location_buildings.put(1002,createLocation("1002", 32.073830, 34.846781));
        location_buildings.put(1004,createLocation("1004", 32.074129, 34.847459));
        location_buildings.put(1005,createLocation("1005", 32.074205, 34.847726));
        location_buildings.put(1102,createLocation("1102", 32.073480, 34.848892));
        location_buildings.put(1103,createLocation("1103", 32.073345, 34.849095));
        location_buildings.put(1104,createLocation("1104", 32.073186, 34.849227));
        location_buildings.put(1105,createLocation("1105", 32.073000, 34.849446));
        location_buildings.put(1401,createLocation("1401", 32.071568, 34.846642));
        location_buildings.put(1501,createLocation("1501", 32.072930, 34.848366));
        //location_buildings.put(1502,createLocation());


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

    public void setBuildingToNearStation() {
        building_to_near_station.put(100, 8);
        building_to_near_station.put(101, 7);
        building_to_near_station.put(102, 7);
        building_to_near_station.put(103, 7);
        building_to_near_station.put(104, 7);
        building_to_near_station.put(105, 8);
        building_to_near_station.put(106, 8);
        building_to_near_station.put(107, 8);
        building_to_near_station.put(108, 8);
        building_to_near_station.put(109, 8);
        building_to_near_station.put(201, 7);
        building_to_near_station.put(202, 7);
        building_to_near_station.put(203, 7);
        building_to_near_station.put(204, 7);
        building_to_near_station.put(205, 7);
        building_to_near_station.put(206, 7);
        building_to_near_station.put(207, 7);
        building_to_near_station.put(208, 7);
        building_to_near_station.put(209, 7);
        building_to_near_station.put(211, 6);
        building_to_near_station.put(212, 6);
        building_to_near_station.put(213, 6);
        building_to_near_station.put(214, 6);
        building_to_near_station.put(215, 6);
        building_to_near_station.put(216, 6);
        building_to_near_station.put(217, 12);
        building_to_near_station.put(300, 9);
        building_to_near_station.put(301, 9);
        building_to_near_station.put(302, 9);
        building_to_near_station.put(303, 9);
        building_to_near_station.put(304, 9);
        building_to_near_station.put(305, 10);
        building_to_near_station.put(306, 10);
        building_to_near_station.put(307, 9);
        building_to_near_station.put(401, 10);
        building_to_near_station.put(402, 10);
        building_to_near_station.put(403, 10);
        building_to_near_station.put(404, 10);
        building_to_near_station.put(405, 10);
        building_to_near_station.put(406, 10);
        building_to_near_station.put(407, 10);
        building_to_near_station.put(408, 12);
        building_to_near_station.put(409, 12);
        building_to_near_station.put(410, 11);
        building_to_near_station.put(411, 11);
        building_to_near_station.put(501, 12);
        building_to_near_station.put(502, 13);
        building_to_near_station.put(503, 11);
        building_to_near_station.put(504, 11);
        building_to_near_station.put(505, 14);
        building_to_near_station.put(506, 14);
        building_to_near_station.put(507, 14);
        building_to_near_station.put(508, 13);
        building_to_near_station.put(509, 14);
        building_to_near_station.put(604, 11);
        building_to_near_station.put(605, 11);
        building_to_near_station.put(801, 14); //3 - depend from where he come from? *
        building_to_near_station.put(802, 14); //*3
        building_to_near_station.put(901, 15); //*2
        building_to_near_station.put(902, 15); //*2
        building_to_near_station.put(905, 15); //*2
        building_to_near_station.put(906, 15); //*2
        building_to_near_station.put(1002, 15); //*2
        building_to_near_station.put(1004, 15); //*2
        building_to_near_station.put(1005, 15); //*2
        building_to_near_station.put(1102, 0);
        building_to_near_station.put(1103, 0);
        building_to_near_station.put(1104, 0);
        building_to_near_station.put(1105, 0);
        building_to_near_station.put(1401, 16);
        building_to_near_station.put(1501, 16);
        building_to_near_station.put(1502, 15); //*2



    }


    public void addLocation(String name, double lat, double lon) {
        Location location = new Location(name);
        location.setLatitude(lat);
        location.setLongitude(lon);
        stations.add(location);
    }
    public Location createLocation(String name, double lat, double lon) {
        Location location = new Location(name);
        location.setLatitude(lat);
        location.setLongitude(lon);
        return location;
}
    public List<Location> getStations() {
        return stations;
    }

    public List<Float> getdistance_of_station() {
        return distance_of_station;
    }
}
