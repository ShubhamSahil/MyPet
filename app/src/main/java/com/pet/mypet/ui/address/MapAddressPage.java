package com.pet.mypet.ui.address;

import android.content.Context;
import android.location.Geocoder;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pet.mypet.R;
import com.pet.mypet.appointment.GPSTracker;

import java.io.IOException;
import java.util.Locale;

public class MapAddressPage extends AppCompatActivity implements OnMapReadyCallback {
    private Context context;
    GoogleMap map;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_address);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context=MapAddressPage.this;
         gpsTracker=new GPSTracker(context);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latlng = new LatLng(gpsTracker.latitude,gpsTracker.longitude);
        map.addMarker(new MarkerOptions().position(latlng).draggable(true).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
//        map.animateCamera(CameraUpdateFactory.newLatLng(sydney,12.0f))      ;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.latitude, gpsTracker.longitude), 12.0f));
        // Enable the zoom controls for the map
        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12.0f));

                LatLng latLng = marker.getPosition();
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                    Log.e("address",address.getLatitude()+ " "+address.getLongitude());
                } catch (IOException e) {
                    Log.e("address",e+"");

                    e.printStackTrace();
                }
            }
        });



    }

}
