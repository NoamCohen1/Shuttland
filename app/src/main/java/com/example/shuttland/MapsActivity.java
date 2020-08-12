package com.example.shuttland;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    Location src = new Location("src");
    Location dest = new Location("dest");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        src.setLatitude(bundle.getDouble("srcLat"));
        src.setLongitude(bundle.getDouble("srcLon"));
        dest.setLatitude(bundle.getDouble("destLat"));
        dest.setLongitude(bundle.getDouble("destLon"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        checkPermission();
        LatLng latLng = new LatLng(dest.getLatitude(), dest.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("Marker in station"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        String strUri = "http://maps.google.com/maps?saddr=" + src.getLatitude() + "," + src.getLongitude()
                + "&daddr=" + dest.getLatitude() + "," + dest.getLongitude();


        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(strUri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        // call to google maps activity to navigate from the src to dest.
        startActivity(intent);
        finish();
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

    }

    private void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

}