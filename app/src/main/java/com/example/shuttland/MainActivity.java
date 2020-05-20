package com.example.shuttland;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private Button navButton;
    private Button stationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        stationBtn = (Button) findViewById(R.id.nearStation);
        stationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNearestStationActivity();
            }
        });

        navButton = (Button) findViewById(R.id.navigation);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityNavigation();
            }
        });
    }

    public void openNearestStationActivity() {
        Intent intent = new Intent(this,TryModel.class);
        startActivity(intent);
    }


    public void openActivityNavigation() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }


}