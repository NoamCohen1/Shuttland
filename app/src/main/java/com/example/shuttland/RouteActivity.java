package com.example.shuttland;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RouteActivity extends AppCompatActivity {
     Location userLocation= new Location("user");
     int selectedBuilding;
    private NavigationModel model = new NavigationModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Bundle bundle = getIntent().getExtras();
        userLocation.setLongitude(bundle.getDouble("lon"));
        userLocation.setLatitude(bundle.getDouble("lat"));
        selectedBuilding=bundle.getInt("numBuilding");

        Location userStation = model.findNearestStation(userLocation);
        int final_station = MapsDB.getInstance().getBuilding_to_near_station().get(selectedBuilding);
        Location final_station_location = MapsDB.getInstance().getStations().get(final_station);

        TextView src_station = (TextView) findViewById(R.id.srcStation);
        src_station.setText("לך אל תחנה " + userStation.getProvider());

        Button button = (Button) findViewById(R.id.navigateBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityNearStation();
            }
        });


        Location shuttleLoc = new Location("user");
        int nearsetShuttle = model.findNearestShuttle(userStation);

        //thread
        shuttleLoc.setLatitude(Shuttle_Map.getInstance().getShuttle_infoMap().get(nearsetShuttle).getLocation().latitude);
        shuttleLoc.setLongitude(Shuttle_Map.getInstance().getShuttle_infoMap().get(nearsetShuttle).getLocation().longitude);
        int time = model.calcTime(userStation, shuttleLoc);

        TextView timeText = (TextView) findViewById(R.id.time);
        src_station.setText("השאטל יגיע בעוד  " + time + " דקות");

        int i=0;

    }

    public void openActivityNearStation() {
        // move parameters - nearest station, user location
        // move back
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
