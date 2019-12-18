package com.example.shuttland;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> listPoints= new ArrayList<>();
    private static final int LOCATION_REQUEST = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (listPoints.size() == 2) {
                    listPoints.clear();
                    mMap.clear();
                }
                // save first point
                listPoints.add(latLng);
                //create marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                if (listPoints.size() == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    //add second marker to the map
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                mMap.addMarker(markerOptions);

                // get distance between the markers
                if (listPoints.size() == 2) {
                    LatLng origin = (LatLng) listPoints.get(0);
                    LatLng dest = (LatLng) listPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
            }
        });

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsParser parser = new DirectionsParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                System.out.println("-------------------------Direction not found--------------------");
                Toast.makeText(getApplicationContext(), "Direction not found", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyDv2j2BgxO2PSwGcX_bA9sobewoSyd7-N0";


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        System.out.println(data);
        return data;
    }
}

//package com.example.shuttland;
//
//import android.location.Location;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private List<Location> stations;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        stations = new ArrayList<>();
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Bar-Ilan, Australia, and move the camera.
//        LatLng Bar_Ilan = new LatLng(32.0693838, 34.8433342);
//        mMap.addMarker(new MarkerOptions().position(Bar_Ilan).title("Marker in Bar-Ilan"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Bar_Ilan));
//
//        Location userLocation = new Location("user");
//        userLocation.setLatitude(32.066038);
//        userLocation.setLongitude(34.841434);
//        Location nearstStation = findNearestStation(userLocation);
//        System.out.println("---------------------------------");
//        System.out.println(nearstStation.getProvider());
//        System.out.println("---------------------------------");
//    }
//
//
//    public Location findNearestStation(Location userLocation) {
//        Location closestLocation = null;
//        float smallestDistance = -1;
//
//        for(Location location: stations){
//            float distance  = userLocation.distanceTo(location);
//            if(smallestDistance == -1 || distance < smallestDistance) {
//                closestLocation = location;
//                smallestDistance = distance;
//            }
//        }
//
//        return closestLocation;
//    }
//
//
//    public void SetStationsLocations(List stations) {
//        createLocation("0", 32.0727493, 34.849301);
//        createLocation("1",32.0734411, 34.8483981);
//        createLocation("2", 32.0735301, 34.846368);
//        createLocation("3", 32.0723118, 34.844318799999996);
//        createLocation("4", 32.071165199999996, 34.8429532);
//        createLocation("5", 32.069904, 34.8420564);
//        createLocation("6", 32.0681564, 34.840918099999996);
//        createLocation("7", 32.0671975, 34.840333699999995);
//        createLocation("8",32.0655436, 34.8424443 );
//        createLocation("9",32.0659431, 34.844034199999996 );
//        createLocation("10", 32.0673506, 34.8446212);
//        createLocation("11", 32.069542, 34.8438793);
//        createLocation("12", 32.0695559, 34.8423678);
//        createLocation("13", 32.0711751, 34.8430622);
//        createLocation("14", 32.0722382, 34.8445136);
//        createLocation("15", 32.073458699999996, 34.8459821);
//        createLocation("16", 32.072267499999995, 34.8480397);
//
//    }
//
//
//    public Location createLocation(String name, double lat, double lon) {
//        Location location = new Location(name);
//        location.setLatitude(lat);
//        location.setLongitude(lon);
//        return location;
//
//    }
//
//}