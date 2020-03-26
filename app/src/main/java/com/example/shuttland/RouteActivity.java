package com.example.shuttland;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;

public class RouteActivity extends AppCompatActivity {
     Location userLocation= new Location("user");
     int selectedBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Bundle bundle = getIntent().getExtras();
        userLocation.setLongitude(bundle.getDouble("lon"));
        userLocation.setLatitude(bundle.getDouble("lat"));
        selectedBuilding=bundle.getInt("numBuilding");
        int i=0;

    }
}
