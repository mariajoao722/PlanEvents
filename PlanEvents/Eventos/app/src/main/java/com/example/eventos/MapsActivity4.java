package com.example.eventos;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.eventos.databinding.ActivityMaps5Binding;

import java.util.List;

public class MapsActivity4 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps5Binding binding;
    private String newString;
    private String newString2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps5Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        newString = null;
        newString2 = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome do evento
            newString2 = extras.getString("localizacao"); // localizacao do evento
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder geocoder = new Geocoder(MapsActivity4.this);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(newString2, 1);
            if (addressList != null){
                LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(newString));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}