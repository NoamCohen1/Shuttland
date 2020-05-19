package com.example.shuttland;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NavigationActivity extends AppCompatActivity {
    FirebaseDatabase mFireBase;
    DatabaseReference myRef;
    private NavigationModel model;
    Location userLocation = new Location("user");
    int selectedBuilding;
    ArrayAdapter<CharSequence> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouldUseLayoutRtl();
        setContentView(R.layout.activity_navigation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFireBase = FirebaseDatabase.getInstance();
        myRef = mFireBase.getReference("shuttles");


        //userLocation = getUserLocation();
        userLocation = new Location("user");
        userLocation.setLatitude(32.074879);
        userLocation.setLongitude(34.868378);

//        final ImageView bus = (ImageView) findViewById(R.id.bus);
//
//       final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.move);
//       MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
//        myAnim.setInterpolator(interpolator);
//        bus.startAnimation(myAnim);
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        userLocation.setLatitude(32.0671975);
//        userLocation.setLongitude(34.840333699999995);

        final Button goButton = (Button) findViewById(R.id.goButton);
//        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.grow);
//        //AnimationSet set=new AnimationSet(false);
//        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
//        myAnim.setInterpolator(interpolator);
//        goButton.startAnimation(myAnim);


        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedBuilding==0) {  // the user not choose building
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!isFinishing()){
                                new AlertDialog.Builder(NavigationActivity.this)
                                        .setTitle("יש לבחור בניין")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).setIcon(R.drawable.warning)
                                        .show();
                            }
                        }
                    });

                }else {
                    openRootActivity();
                }
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner
        arrayAdapter = ArrayAdapter
                .createFromResource(this, R.array.building_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final AutoCompleteTextView autoComplete = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        autoComplete.setAdapter(arrayAdapter);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                InputMethodManager imm = (InputMethodManager) getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                if (!parent.getItemAtPosition(position).toString().equals("")) {
                    String selected = parent.getItemAtPosition(position).toString();
                    String[] parts = selected.split(" -");
                    selectedBuilding = Integer.parseInt(parts[0]);
                }
            }
        });

        autoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        ImageView dropDown = (ImageView) findViewById(R.id.drop_down_img);
        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoComplete.showDropDown();
            }
        });

        this.model=new NavigationModel();
        //writeToDB();
        readFromDB();
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


    public void readFromDB() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    String value = keyNode.getValue(String.class);
                    String[] location_point = value.split(",");
                   Shuttle_Info info = new Shuttle_Info(new LatLng(Double.parseDouble(location_point[0]), Double.parseDouble(location_point[1]))
                       , Boolean.parseBoolean(location_point[2]));
                   Shuttle_Map.getInstance().setMap(Integer.parseInt(keyNode.getKey()), info);

                }
               // Shuttle_string value = dataSnapshot.getValue(Shuttle_string.class);
//                Log.v("key", "Value is: " + value);
//                System.out.println("********** " + value);
//                String[] location_point = value.split(",");
//                Shuttle_Info info = new Shuttle_Info(new LatLng(Double.parseDouble(location_point[0]), Double.parseDouble(location_point[1]))
//                        , Boolean.parseBoolean(location_point[2]));
//                Shuttle_Map.getInstance().setMap(Integer.parseInt(dataSnapshot.getKey()), info);

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
    private boolean shouldUseLayoutRtl() {
        Configuration config = getResources().getConfiguration();
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

        } else {
            return false;
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
