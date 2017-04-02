package com.example.keniel.test;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class gmsFragment extends Fragment implements OnMapReadyCallback{


    Map<String,String> myMap = new HashMap<>();
    public gmsFragment() {
        // Required empty public constructor
    }

    private RequestQueue requesQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requesQueue = Volley.newRequestQueue(getActivity());
        fireStationNearYou();
        Log.i("mess","a yah so nice");
        return inflater.inflate(R.layout.fragment_gms, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);


        fragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        LatLng marker = new LatLng(MainActivity.getX(), MainActivity.getY());
       // LatLng m = new LatLng(getX(), getY());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,13));
        googleMap.addMarker(new MarkerOptions().title("Hello Google Maps").position(marker));
        createMarkers(googleMap);
    }

    public void createMarkers(GoogleMap map){
        Log.i("ys","yessss goodeeee");
        for (Map.Entry<String, String> entry : myMap.entrySet()) {
            Log.i("ys","yessss pamputae");
            String key = entry.getKey();
            String value = entry.getValue();
            String[] splited = value.split("\\s+");
            double latitude = Double.parseDouble(splited[0]);
            double longitude = Double.parseDouble(splited[1]);
            Log.i("test", key + " " + latitude + " " + longitude);
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(key));
        }

    }

    public void fireStationNearYou(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                "https://maps.googleapis.com/maps/api/place/textsearch/json?query=firestations+in+Jamaica&key=AIzaSyAUmdMmaPoWk6W5oKKc7slpHdwHXyrRELs",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject station = jsonArray.getJSONObject(i);

                                String name = station.getString("name");
                                JSONObject location = station.getJSONObject("geometry").getJSONObject("location");
                                String lat = location.getString("lat");
                                String lng = location.getString("lng");
                                myMap.put(name, lat + " " + lng);
                            }


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
        Log.i("yes","Yes good gyal");
        requesQueue.add(request);


    }


}
