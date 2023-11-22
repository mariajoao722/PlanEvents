package com.example.eventos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.eventos.databinding.ActivityMaps3Binding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MapsActivity3 extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity3";

    private AutocompleteSupportFragment mAutocomplete;
    private PlacesClient mPlaces;
    private String mOrigin;
    private LatLng mOriginLatLng;


    private Button btnConfirmar;


    private String newString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps3);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnConfirmar = findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity3.this);
        alertDialogBuilder.setMessage("Selecione a localização pretendida");

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String ok = "ok";
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        Bundle extras = getIntent().getExtras();
        newString = null;
        if (extras != null) {
            newString = extras.getString("nome"); // nome evento
        }

        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        mPlaces = Places.createClient(this);
        mAutocomplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeAutocompleteOrigin);

        mAutocomplete.setCountries("PT"); // limit search to Portugal

        mAutocomplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

        mAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, "onError: " + status.getStatusMessage());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mOrigin = place.getName();
                mOriginLatLng = place.getLatLng();
                Log.d(TAG, "onPlaceSelected: " + mOrigin);
                System.out.println(mOrigin);
                System.out.println(mOriginLatLng.latitude);
                System.out.println(mOriginLatLng.longitude);
                moveCamera(new LatLng(mOriginLatLng.latitude, mOriginLatLng.longitude), 17.5f, mOrigin);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(39.557191 , -7.8536599);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,7.5f));
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirmar:
                if (mOriginLatLng == null) {
                    Toast.makeText(this, "Por favor, selecione uma localização", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String add = Localizacao.getAddress(MapsActivity3.this, mOriginLatLng.latitude, mOriginLatLng.longitude);
                    mOrigin = add;
                    Intent intent = new Intent( MapsActivity3.this, Adicionar_lugares.class);
                    intent.putExtra("origem", mOrigin);
                    intent.putExtra("nome", newString);
                    startActivity(intent);
                }
                finish();

                break;
        }
    }

}