package com.example.keniel.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import android.location.LocationListener;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Keniel on 11/27/2016.
 */
public class GPS_Service extends Service  {


    private LocationListener locationListener;
    private LocationManager locationManager;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //super.onCreate();
        requestQueue = Volley.newRequestQueue(this);  // rq != null
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,"https://maps.googleapis.com/maps/api/geocode/json?latlng="
                        + location.getLatitude() + "," + location.getLongitude() +
                        "&key=AIzaSyAUmdMmaPoWk6W5oKKc7slpHdwHXyrRELs",null,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String address = response.getJSONArray("results").getJSONObject(0)
                                    .getString("formatted_address");
                            Intent i = new Intent("location_update");
                            i.putExtra("address",address);
                            sendBroadcast(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ,new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Log.i("error","error");
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(
                        10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
                requestQueue.add(request);
                Intent a = new Intent("latlng");
                a.putExtra("lat",location.getLatitude());
                a.putExtra("lng",location.getLongitude());
                sendBroadcast(a);
            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,locationListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(locationListener);
        }
    }
}
