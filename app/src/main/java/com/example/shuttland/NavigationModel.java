package com.example.shuttland;

import android.location.Location;

import java.util.List;
import java.util.Map;
import static java.lang.Math.pow;


public class NavigationModel {
    private List<Location> stations;
    // 15 km/h -> 250 metres per minute (15 * 1000 / 60)
    private final static int avarage_velocity = 250;

    private static final double SQUARED_RADIOS = 0.000000000673;


    public NavigationModel() {
        this.stations =  MapsDB.getInstance().getStations();
    }


    public Location findNearestStation(Location userLocation) {
        Location closestLocation = null;
        float smallestDistance = -1;

        for (Location location : stations) {
            float distance = userLocation.distanceTo(location);
            if (smallestDistance == -1 || distance < smallestDistance) {
                closestLocation = location;
                smallestDistance = distance;
            }
        }

        return closestLocation;
    }

    public float distance_shuttle(Location station_location, Location shuttle_location, int counter, int shuttleNum) {
        int numStation = Integer.parseInt(station_location.getProvider());
        Location nearest_station_location = findNearestStation(shuttle_location);
        Location lastStation = MapsDB.getInstance().getStations().get(counter);
        counter = updateCounter(shuttle_location.getLatitude(), shuttle_location.getLongitude(), lastStation.getLatitude(), lastStation.getLongitude(), shuttleNum);
        //int nearest_station_name = Integer.parseInt(nearest_station_location.getProvider());
        float distance = nearest_station_location.distanceTo(shuttle_location);
        if (counter > numStation)
            return Float.MAX_VALUE;
        for (int i = counter; i < numStation; i++) {
            distance += MapsDB.getInstance().getdistance_of_station().get(i);
        }
        return distance;
    }

    public float distance_shuttle(Location station_location, Location shuttle_location) {
        int numStation = Integer.parseInt(station_location.getProvider());
        Location nearest_station_location = findNearestStation(shuttle_location);
        int nearest_station_name = Integer.parseInt(nearest_station_location.getProvider());
        float distance = nearest_station_location.distanceTo(shuttle_location);
        if (nearest_station_name > numStation)
            return Float.MAX_VALUE;
        for (int i = nearest_station_name; i < numStation; i++) {
            distance += MapsDB.getInstance().getdistance_of_station().get(i);
        }
        return distance;
    }

    // - צריך להחזיר 10 דקות(לבדוק) + הזמן מתחנה 0 עד אליו אם מחזיר  MAX INT
    public int findNearestShuttle(Location station_location, Boolean isAccess) {
        float minDis = Float.MAX_VALUE;
        // not valid num shuttle
        int ans=Integer.MAX_VALUE;
        Map<Integer, Shuttle_Info> my_map;
        if (isAccess) {
            my_map = Shuttle_Map.getInstance().getAccessActiveShuttles();

        } else {
            my_map = Shuttle_Map.getInstance().getActiveShuttles();

        }
        for (Map.Entry<Integer, Shuttle_Info> entry : my_map.entrySet()) {
            Location shuttle_location = new Location("0");
            shuttle_location.setLongitude(entry.getValue().getLocation().longitude);
            shuttle_location.setLatitude(entry.getValue().getLocation().latitude);
            float temp_distance=distance_shuttle(station_location,shuttle_location, entry.getValue().getCounter(), entry.getKey());
            if (temp_distance < minDis) {
                minDis = temp_distance;
                ans=entry.getKey();
            }
        }
        return ans;
    }

    public int calcTime(Location station_location, Location shuttle_location) {
        float distance = distance_shuttle(station_location, shuttle_location);
        return (int) distance / avarage_velocity;
    }

    public int updateCounter(double shuttle_lat, double shuttle_lon, double station_lat, double station_lon, int shuttleNum) {
        // leaving first stop
        if (pow(shuttle_lat - station_lat, 2) + pow(shuttle_lon - station_lon, 2) <= SQUARED_RADIOS) {
            Shuttle_Map.getInstance().getShuttle_infoMap().get(shuttleNum).increaseCounter();
        }
        return  Shuttle_Map.getInstance().getShuttle_infoMap().get(shuttleNum).getCounter();
    }


}
