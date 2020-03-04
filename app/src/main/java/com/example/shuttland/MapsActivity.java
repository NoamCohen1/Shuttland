package com.example.shuttland;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import com.example.shuttland.MapsDB;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location userLocation;
    DatabaseReference myRef;
    private List<Location> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        stations = MapsDB.getInstance().getStations();
        writeToDB();
        readFromDB();

    }


    public void writeToDB(){
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        myRef=db.getReference("1");
        myRef.setValue("32.545342,34.02832,true");
    }

    public void readFromDB(){
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.v("key", "Value is: " + value);
                System.out.println("********** "+value);
                String[] location_point=value.split(",");
                double lon=Double.parseDouble(location_point[0]);
                boolean b=Boolean.parseBoolean(location_point[2]);
                Shuttle_Info info=new Shuttle_Info(new LatLng(Double.parseDouble(location_point[0]),Double.parseDouble(location_point[1]))
                        ,Boolean.parseBoolean(location_point[2]));
                Shuttle_Map.getInstance().setMap(Integer.parseInt(dataSnapshot.getKey()),info);
                int i=0;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("error", "Failed to read value.", error.toException());
            }
        });
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



        //Location userLocation = new Location("user");
        //userLocation.setLatitude(userLocation.getLatitude());
        //userLocation.setLongitude(userLocation.getLongitude());
        Location nearestStation = findNearestStation(location);


        LatLng latLng = new LatLng(nearestStation.getLatitude(), nearestStation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("Marker in station"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

//        System.out.println("---------------------------------");
//        System.out.println(nearestStation.getProvider());
//        System.out.println("---------------------------------");


        String strUri = "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude()
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

    private void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

}