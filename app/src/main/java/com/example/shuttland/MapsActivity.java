package com.example.shuttland;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location src = new Location("src");
    Location dest = new Location("dest");
    private NavigationModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        src.setLatitude(bundle.getDouble("srcLat"));
        src.setLongitude(bundle.getDouble("srcLon"));
        dest.setLatitude(bundle.getDouble("destLat"));
        dest.setLongitude(bundle.getDouble("destLon"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.model=new NavigationModel();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        checkPermission();

//        final LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(android.location.Location location) {
//                if (userLocation == null) {
//                    userLocation = new Location("user");
//                    userLocation.setLatitude(location.getLatitude());
//                    userLocation.setLongitude(location.getLongitude());
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//
//        };
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Location location = null;
//        for (String provider : lm.getProviders(true)) {
//            location = lm.getLastKnownLocation(provider);
//            lm.requestLocationUpdates(provider, 3000, 0, locationListener);
//        }
//        //3 seconds and 10 meters
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, locationListener);
//
//
//        Location nearestStation = model.findNearestStation(location);


        LatLng latLng = new LatLng(dest.getLatitude(), dest.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("Marker in station"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

//        System.out.println("---------------------------------");
//        System.out.println(nearestStation.getProvider());
//        System.out.println("---------------------------------");


        String strUri = "http://maps.google.com/maps?saddr=" + src.getLatitude() + "," + src.getLongitude()
                + "&daddr=" + dest.getLatitude() + "," + dest.getLongitude();



        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(strUri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
        finish();
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

    }


    private void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

}