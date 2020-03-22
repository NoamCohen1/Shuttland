package com.example.shuttland;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity {
    DatabaseReference myRef;
    private NavigationModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        this.model=new NavigationModel();
        writeToDB();
        readFromDB();
        Location loc=new Location("3");
        loc.setLatitude(32.0723118);
        loc.setLongitude(34.844318799999996);

        int ans=this.model.findNearestShuttle(loc);
        int o=9;
    }



    public void writeToDB() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        myRef = db.getReference("1");
        myRef.setValue("32.0734411, 34.8483981,true");
        myRef = db.getReference("2");
        myRef.setValue("32.0735301, 34.846368,true");
    }

    public void readFromDB() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.v("key", "Value is: " + value);
                System.out.println("********** " + value);
                String[] location_point = value.split(",");
                double lon = Double.parseDouble(location_point[0]);
                boolean b = Boolean.parseBoolean(location_point[2]);
                Shuttle_Info info = new Shuttle_Info(new LatLng(Double.parseDouble(location_point[0]), Double.parseDouble(location_point[1]))
                        , Boolean.parseBoolean(location_point[2]));
                Shuttle_Map.getInstance().setMap(Integer.parseInt(dataSnapshot.getKey()), info);
                int i = 0;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("error", "Failed to read value.", error.toException());
            }
        });
    }

}
