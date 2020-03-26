package com.example.shuttland;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NavigationActivity extends AppCompatActivity {
    DatabaseReference myRef;
    private NavigationModel model;
    Location userLocation = new Location("user");
    int selectedBuilding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Button goButton = (Button) findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRootActivity();
            }
        });
        Spinner staticSpinner = (Spinner) findViewById(R.id.target);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.building_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //String buildingSelected = ;
                if (!parent.getItemAtPosition(position).equals("")) {

                    selectedBuilding=Integer.parseInt((String)parent.getItemAtPosition(position));
                    getUserLocation();
                    Log.v("itembbbbbbbbbbb", (String) parent.getItemAtPosition(position));
                    int y=0;

                }
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        this.model=new NavigationModel();
        writeToDB();
        readFromDB();
        Location loc=new Location("3");
        loc.setLatitude(32.0723118);
        loc.setLongitude(34.844318799999996);
        userLocation=getUserLocation();
        int ans=this.model.findNearestShuttle(loc);

        int o=9;
    }


    public Location getUserLocation() {
        checkPermission();

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                if (userLocation == null) {
                    userLocation = new Location("user");
                    userLocation.setLatitude(location.getLatitude());
                    userLocation.setLongitude(location.getLongitude());
                }
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
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        for (String provider : lm.getProviders(true)) {
            location = lm.getLastKnownLocation(provider);
            lm.requestLocationUpdates(provider, 3000, 0, locationListener);
        }
        //3 seconds and 10 meters
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, locationListener);

        return location;
    }


    private void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                Shuttle_Info info = new Shuttle_Info(new LatLng(Double.parseDouble(location_point[0]), Double.parseDouble(location_point[1]))
                        , Boolean.parseBoolean(location_point[2]));
                Shuttle_Map.getInstance().setMap(Integer.parseInt(dataSnapshot.getKey()), info);



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("error", "Failed to read value.", error.toException());
            }
        });
    }
    public void openRootActivity() {
        Intent intent = new Intent(this, RouteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",userLocation.getLatitude());
        bundle.putDouble("lon",userLocation.getLongitude());
        bundle.putInt("numBuilding",selectedBuilding);
        intent.putExtras(bundle);
        startActivity(intent);
    }



}
