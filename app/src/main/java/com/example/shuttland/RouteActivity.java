package com.example.shuttland;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static androidx.core.view.ViewCompat.getLayoutDirection;

public class RouteActivity extends AppCompatActivity {

    private static final int BEGIN_HOUR = 730; //7:30
    private static final int END_HOUR = 2000;
    private static final int END_HOUR_FRIDAY = 1300;


     Location userLocation= new Location("user");
     int selectedBuilding;
     boolean isAccess = false;
     Map<Integer, String> accessTime = MapsDB.getInstance().getAccessTime();
    private NavigationModel model = new NavigationModel();
    private boolean accessMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // shouldUseLayoutRtl();
        setContentView(R.layout.activity_route);

        Bundle bundle = getIntent().getExtras();
        userLocation.setLongitude(bundle.getDouble("lon"));
        userLocation.setLatitude(bundle.getDouble("lat"));
        selectedBuilding=bundle.getInt("numBuilding");

        TextView dest_text = (TextView) findViewById(R.id.dest);
       dest_text.setText("       בניין " + selectedBuilding);


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


        final ImageView accessBtn = (ImageView) findViewById(R.id.accessBtn);
        final TextView accessTime=(TextView) findViewById(R.id.accessShuttle);
        final TextView regularTime=(TextView) findViewById(R.id.timeShuttle);
        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!accessMode) {
                    isAccess = true;
                    accessMode=true;
                    accessBtn.setImageResource(R.drawable.shuttle_icon);
                    accessTime.setVisibility(View.VISIBLE);
                    regularTime.setVisibility(View.INVISIBLE);
                    final int nearsetShuttle = model.findNearestShuttle(userStation, isAccess);
                    final Location userStationTemp = userStation;
                    Thread th = new Thread(new Runnable() {
                        public void run() {
                            while (true) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int time = updateTime(isAccess, nearsetShuttle, userStationTemp, userStation);
                                        updateMsgForAccess(time);
                                    }
                                });
                                try {
                                    Thread.sleep(20000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    th.start();
                }else{
                    accessMode=false;
                    accessTime.setVisibility(View.INVISIBLE);
                    regularTime.setVisibility(View.VISIBLE);
                    accessBtn.setImageResource(R.drawable.wheelchair);
                }
            }
        });




        final int nearsetShuttle = model.findNearestShuttle(userStation, isAccess);
        final Location userStationTemp = userStation;

        Thread th = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int time = updateTime(isAccess,nearsetShuttle,userStationTemp, userStation);
                            updateMsg(time);

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

    private boolean shouldUseLayoutRtl() {
        Configuration config = getResources().getConfiguration();
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

        } else {
            return false;
        }
    }

    public int updateTime(boolean isAccess,int nearsetShuttle, Location userStationTemp, Location userStation) {
        int time=0;
        boolean is_find_shuttle=true;
        int nearesetShuttleTemp=nearsetShuttle;
        if(nearesetShuttleTemp==Integer.MAX_VALUE){
            is_find_shuttle=false;
            time=10;
            if (isAccess) {
                time = Integer.MAX_VALUE;
            }
            nearesetShuttleTemp=model.findNearestShuttle(userStation, isAccess);
            if(nearesetShuttleTemp!=Integer.MAX_VALUE)
                is_find_shuttle=true;

        }
        if(is_find_shuttle){
            Location shuttleLoc = new Location("user");
            shuttleLoc.setLatitude(Shuttle_Map.getInstance().getShuttle_infoMap().get(nearesetShuttleTemp).getLocation().latitude);
            shuttleLoc.setLongitude(Shuttle_Map.getInstance().getShuttle_infoMap().get(nearesetShuttleTemp).getLocation().longitude);
            time = model.calcTime(userStationTemp, shuttleLoc);
        }
        return time;
    }

    public void updateMsg(int time ){
        TextView timeText = (TextView) findViewById(R.id.timeShuttle);

        Date date=new Date();
//                            String day = String.format("%E", date ).toLowerCase();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);

        if((day==7)||(day==6 && !validTime(BEGIN_HOUR,END_HOUR_FRIDAY,c))){
            timeText.setText("השאטל הבא יצא ביום ראשון ב-7:30 בבוקר");
            return;
        }

        if(!validTime(BEGIN_HOUR,END_HOUR,c)){
            timeText.setText("השאטל הבא יצא ב-7:30 בבוקר");
            return;
        }

        if (time == 0) {
            timeText.setText("השאטל מגיע כעת");
        } else {
            timeText.setText("השאטל יגיע בעוד  " + time + " דקות");
        }
    }

    public void updateMsgForAccess(int time){
        TextView timeText = (TextView) findViewById(R.id.accessShuttle);
       // timeText.setVisibility(View.VISIBLE);


        Date date=new Date();
//                            String day = String.format("%E", date ).toLowerCase();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_WEEK);

        if((day==7)||(day==6 && !validTime(BEGIN_HOUR,END_HOUR_FRIDAY,c))){
            timeText.setText("השאטל הנגיש הבא יצא ביום ראשון ב-7:30 בבוקר");
            return;
        }

        if(!validTime(BEGIN_HOUR,END_HOUR,c)){
            timeText.setText("השאטל הנגיש הבא יצא ב-7:30 בבוקר");
            return;
        }

        if (time == Integer.MAX_VALUE) {
            int current_time = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
            int temp;
           // int current_time = 1400;
            String ans = "";
            int min = Integer.MAX_VALUE;
            List<Integer> keys = new ArrayList(accessTime.keySet());
            for (int i = 0 ; i < keys.size(); i++){
                 temp = keys.get(i) - current_time;
                 if (temp > 0 && temp < min) {
                     min = temp;
                     ans = accessTime.get(keys.get(i));
                 }
            }
            timeText.setText("השאטל הנגיש יוצא ב - " + ans);

        }
        else if (time == 0) {
            timeText.setText("השאטל מגיע כעת");
        } else {
            timeText.setText("השאטל הנגיש יגיע בעוד  " + time + " דקות");
        }

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

    public  Boolean validTime(int from, int to, Calendar c){
        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        return (to > from && t >= from && t <= to || to < from && (t >= from || t <= to));
    }

}
