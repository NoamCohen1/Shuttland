package com.example.shuttland;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather extends AppCompatActivity {
    Client client = new Client();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        client.Connect();
        findWeather();
    }

    public void findWeather(){
        final TextView t1 = (TextView)findViewById(R.id.gal);
        String key = "7bbeff127d7275e0bdab8ad3a6220fb1";
        //String url = "http://api.openweathermap.org/data/2.5/weather?q=London&appid=" + key;
        String url = "http://api.openweathermap.org/data/2.5/weather?q=London&appid=7bbeff127d7275e0bdab8ad3a6220fb1";
        //CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>(){

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_obj = response.getJSONObject("main");
                    JSONArray arr = response.getJSONArray("weather");
                    JSONObject obj = arr.getJSONObject(0);
                    String temp = String.valueOf(main_obj.getDouble("temp"));
                    String desc = obj.getString("description");
                    double temp_int = Double.parseDouble(temp);
                    double centi = (temp_int - 32) / 1.8000;
                    centi = Math.round(centi);
                    int i = (int)centi;
                    t1.setText(i);
                    client.sendMessage(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                t1.setText("erorrrrrrrrrrrrrrrrrrrrrrrr");

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }
}
