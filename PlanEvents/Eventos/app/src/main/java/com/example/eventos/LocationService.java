package com.example.eventos;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LocationService extends Service implements LocationListener {


    // Tempo mínimo entre atualizações em milissegundos
    private static final long LOCATION_INTERVAL = 1000 * 30; // A cada 30 segundos atualiza a localização

    private static final long LOCATION_DISTANCE = 0; // Distâcia mínima entre atualizações de localização

    private LocationManager locationManager;

    private FirebaseUser user;
    private String userID;
    private String newString;


    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize necessary components or variables here
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check for location permissions and request if needed
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            // Handle permission not granted
        }

        if (intent != null) {
            newString = intent.getStringExtra("nome");
        }

        return START_STICKY;
    }

    private void startLocationUpdates() {
        // Check if the location provider is enabled
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Request location updates
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, (float) LOCATION_DISTANCE, this);
        } else {
            // Handle location provider not enabled
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String coordenadas = latitude + " " + longitude;
        System.out.println("Latitude: " + latitude + " Longitude: " + longitude);

        FirebaseDatabase.getInstance().getReference("Eventos").child(newString).child("Inscritos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.child("id").getValue().toString().equals(userID)) {
                            FirebaseDatabase.getInstance().getReference("Eventos").child(newString).child("Inscritos").child(ds.getKey()).child("currentLoc").setValue(coordenadas);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
        });

        // Handle the updated location
    }


    public void onProviderEnabled(String provider) {
        // Handle provider enabled
    }


    public void onProviderDisabled(String provider) {
        // Handle provider disabled
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status changed
    }



    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Handle the updated location here
        }

        // Implement other location listener methods if needed
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    private void stopLocationUpdates() {
        if (locationManager != null) {
            // Stop receiving location updates
            locationManager.removeUpdates((android.location.LocationListener) locationListener);
        }
    }


}
