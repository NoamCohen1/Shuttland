package com.example.shuttland;

import android.location.Location;

import java.util.List;
import java.util.Map;

public class NavigationModel {
    private List<Location> stations;

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
    public int findNearestShuttle(Location station_location) {
        float minDis = Float.MAX_VALUE;
        // not valid num shuttle
        int ans=Integer.MAX_VALUE;
        for (Map.Entry<Integer, Shuttle_Info> entry : Shuttle_Map.getInstance().getActiveShuttles().entrySet()) {
            Location shuttle_location = new Location("0");
            shuttle_location.setLongitude(entry.getValue().getLocation().longitude);
            shuttle_location.setLatitude(entry.getValue().getLocation().latitude);
            float temp_distance=distance_shuttle(station_location,shuttle_location);
            if (temp_distance < minDis) {
                minDis = temp_distance;
                ans=entry.getKey();
            }
        }
        return ans;
    }
}
