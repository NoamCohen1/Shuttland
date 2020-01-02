package com.example.shuttland;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Location> stations;
    Location userLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        stations = new ArrayList<>();
        SetStationsLocations(stations);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        checkPermission();

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                if (userLocation == null) {
                    userLocation = new Location("user");
                    userLocation.setLatitude(location.getLatitude());
                    userLocation.setLongitude(location.getLongitude());
                }
                double latitude=location.getLatitude();
                double longitude=location.getLongitude();
                String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
                System.out.println(msg);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        for ( String provider : lm.getProviders(true) ) {
            location =  lm.getLastKnownLocation(provider);
            lm.requestLocationUpdates(provider, 3000, 0, locationListener);
        }
        //3 seconds and 10 meters
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, locationListener);



        Location userLocation = new Location("user");
        userLocation.setLatitude(32.069267);
        userLocation.setLongitude(34.843128);

        Location nearestStation = findNearestStation(userLocation);


        LatLng latLng = new LatLng(nearestStation.getLatitude(), nearestStation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("Marker in station"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

//        System.out.println("---------------------------------");
//        System.out.println(nearestStation.getProvider());
//        System.out.println("---------------------------------");


        String strUri = "http://maps.google.com/maps?saddr=" + userLocation.getLatitude() + "," + userLocation.getLongitude()
                + "&daddr=" + nearestStation.getLatitude() + "," + nearestStation.getLongitude();

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(strUri));
        startActivity(intent);

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

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

    public void SetStationsLocations(List stations) {
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

    private void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

}