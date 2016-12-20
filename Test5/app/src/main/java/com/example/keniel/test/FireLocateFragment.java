package com.example.keniel.test;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;




/**
 * A simple {@link Fragment} subclass.
 */
public class FireLocateFragment extends Fragment implements OnMapReadyCallback {

    Double lat;
    Double lng;
    String title;

    public FireLocateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_fire_locate, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng marker = new LatLng(this.lat, this.lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,10));
        googleMap.addMarker(new MarkerOptions().title(this.title).position(marker));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.locatemap);
        try {
            this.title = getArguments().getString("title");
            this.lat = getArguments().getDouble("Latitude");
            this.lng = getArguments().getDouble("Longitude");
            //setStateX(Double.parseDouble(lat));
            //setStateY(Double.parseDouble(lng));
        }catch (Exception e){
            e.printStackTrace();
        }


        fragment.getMapAsync(this);

    }
}
