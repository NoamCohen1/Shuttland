package com.example.shuttland;
import android.location.Location;
import java.util.List;
import java.util.Map;


class NavigationModel {
    private List<Location> stations;
    // 15 km/h -> 250 metres per minute (15 * 1000 / 60)
    private final static int average_velocity = 250;

    NavigationModel() {
        this.stations = MapsDB.getInstance().getStations();
    }

    /**
     * finds the nearest station to the user location
     */
    Location findNearestStation(Location userLocation) {
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

    /**
     * calculate the distance from the user station to the shuttle location
     */
    private float distance_shuttle(Location station_location, Location shuttle_location) {
        int numStation = Integer.parseInt(station_location.getProvider());
        Location nearest_station_location = findNearestStation(shuttle_location);
        int nearest_station_name = Integer.parseInt(nearest_station_location.getProvider());
        float distance = nearest_station_location.distanceTo(shuttle_location);
        if (nearest_station_name > numStation)
            return Float.MAX_VALUE;
        for (int i = nearest_station_name; i < numStation; i++) {
            distance += MapsDB.getInstance().getDistanceOfStation().get(i);
        }
        return distance;
    }

    /**
     * find the nearest shuttle to the user station
     */
    int findNearestShuttle(Location station_location, Boolean isAccess) {
        float minDis = Float.MAX_VALUE;
        // not valid num shuttle
        int ans = Integer.MAX_VALUE;
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
            float temp_distance =  distance_shuttle(station_location, shuttle_location);
            if (temp_distance < minDis) {
                minDis = temp_distance;
                ans = entry.getKey();
            }
        }
        return ans;
    }

    /**
     * calculate the time until tha shuttle will arrive to the user station
     */
    int calcTime(Location station_location, Location shuttle_location) {
        float distance = distance_shuttle(station_location, shuttle_location);
        return (int) distance / average_velocity;
    }
}