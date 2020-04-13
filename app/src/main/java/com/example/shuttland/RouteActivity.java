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

        Location ans_near=model.findNearestStation(userLocation);
        if(ans_near.getProvider().equals("0"))
            ans_near=MapsDB.getInstance().getStations().get(1);
        final Location userStation = ans_near;
        int final_station = MapsDB.getInstance().getBuilding_to_near_station().get(selectedBuilding);
        final Location final_station_location = MapsDB.getInstance().getStations().get(final_station);

        TextView src_station = (TextView) findViewById(R.id.srcStation);
        src_station.setText("לך אל תחנה " + userStation.getProvider());

        TextView dst_building = (TextView) findViewById(R.id.targetBuilding);
        dst_building.setText("לך אל בניין " + selectedBuilding);



        Button button = (Button) findViewById(R.id.navigateBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityNearStation(userLocation, userStation);
            }
        });

        Button BuildingBtn = (Button) findViewById(R.id.navigateBuilding);
        BuildingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityNearStation(final_station_location, MapsDB.getInstance().getSpecificBuildingLocation(selectedBuilding));
            }
        });

        final int nearsetShuttle = model.findNearestShuttle(userStation);
        final Location userStationTemp = userStation;

        Thread th = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int time=0;
                            boolean is_find_shuttle=true;
                            int nearesetShuttleTemp=nearsetShuttle;
                            if(nearesetShuttleTemp==Integer.MAX_VALUE){
                                is_find_shuttle=false;
                                time=10;
                                nearesetShuttleTemp=model.findNearestShuttle(userStation);
                                if(nearesetShuttleTemp!=Integer.MAX_VALUE)
                                    is_find_shuttle=true;

                            }
                           if(is_find_shuttle){
                                Location shuttleLoc = new Location("user");
                                shuttleLoc.setLatitude(Shuttle_Map.getInstance().getShuttle_infoMap().get(nearesetShuttleTemp).getLocation().latitude);
                                shuttleLoc.setLongitude(Shuttle_Map.getInstance().getShuttle_infoMap().get(nearesetShuttleTemp).getLocation().longitude);
                                time = model.calcTime(userStationTemp, shuttleLoc);
                            }
                            TextView timeText = (TextView) findViewById(R.id.timeShuttle);
                            if (time == 0) {
                                timeText.setText("השאטל מגיע כעת");
                            } else {
                                timeText.setText("השאטל יגיע בעוד  " + time + " דקות");
                            }
                        }
                    });
                    try {
                        Thread.sleep(20000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();

        TextView destText = (TextView) findViewById(R.id.targetStation);
        destText.setText("סע עד תחנה "+ final_station);

    }

    public void openActivityNearStation(Location src, Location dest) {
        // move parameters - nearest station, user location
        // move back
        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("srcLat",src.getLatitude());
        bundle.putDouble("srcLon",src.getLongitude());
        bundle.putDouble("destLat",dest.getLatitude());
        bundle.putDouble("destLon",dest.getLongitude());
        intent.putExtras(bundle);
        startActivity(intent);
    }



}
