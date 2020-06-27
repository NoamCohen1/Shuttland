//package com.example.shuttland;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class Weather extends AppCompatActivity {
//    Client client = new Client();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_weather);
//        client.Connect();
//        findWeather();
//    }
//
//    public void findWeather(){
//        final TextView t1 = (TextView)findViewById(R.id.gal);
//        String key = "7bbeff127d7275e0bdab8ad3a6220fb1";
//  //      String location="lat="+lat+"&lon="+lon;
//        //String url = "https://api.openweathermap.org/data/2.5/weather?"+location+"&appid="+key;
//        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject main_obj = response.getJSONObject("main");
//                    String temp = String.valueOf(main_obj.getDouble("feels_like"));
//                    convart_celsius(temp);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//
//            }
//        });
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(jor);
//    }
//
//    public int convart_celsius(String weather){
//        double temp_int = Double.parseDouble(weather);
//        double centi = temp_int-273.15;
//        centi = Math.round(centi);
//        return (int)centi;
//    }
//}
//
//
////import android.os.Bundle;
////import android.widget.TextView;
////
////import java.io.BufferedReader;
////import java.io.InputStream;
////import java.io.InputStreamReader;
////import java.net.HttpURLConnection;
////import java.net.URL;
////import androidx.appcompat.app.AppCompatActivity;
////
////
////public class Weather extends AppCompatActivity {
////    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
////    private static String IMG_URL = "http://openweathermap.org/img/w/";
////    private static String APPID = "{your_key}";
////
////
////    @Override
////        protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_weather);
////       getWeatherData();
////    }
////
////    public String getWeatherData() {
////        final TextView t1 = (TextView)findViewById(R.id.gal);
////        HttpURLConnection con = null;
////        InputStream is = null;
////
////
////        try {
////            con = (HttpURLConnection) (new URL("https://api.openweathermap.org/data/2.5/weather?q=London&appid=7bbeff127d7275e0bdab8ad3a6220fb1")
////            ).openConnection();
////            con.setRequestMethod("GET");
////            con.setDoInput(true);
////            con.setDoOutput(true);
////            con.connect();
////
////// Let’s read the response
////            StringBuffer buffer = new StringBuffer();
////            is = con.getInputStream();
////            BufferedReader br = new BufferedReader(new InputStreamReader(is));
////            String line = null;
////            while ((line = br.readLine()) != null)
////                buffer.append(line + "rn");
////
////            is.close();
////            con.disconnect();
////            t1.setText(buffer.toString());
////            return buffer.toString();
////        } catch (Throwable t) {
////            t.printStackTrace();
////        } finally {
////            try {
////                is.close();
////            } catch (Throwable t) {
////            }
////            try {
////                con.disconnect();
////            } catch (Throwable t) {
////            }
////        }
////
////        return null;
////
////    }
////}
////
