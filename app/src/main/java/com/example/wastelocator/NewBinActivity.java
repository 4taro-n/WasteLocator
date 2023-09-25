package com.example.wastelocator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class NewBinActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText latitudeET, longitudeET;
    private Button submitBtn;
    private GoogleMap mMap;
    private Marker currentMarker = null;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bin);

        bindingViews();
        initialisingGoogleMap();

        onClickListener();
    }

    private void onClickListener() {
        submitBtn.setOnClickListener(view -> {
            // Do something (e.g create Bin object with lat & long)
            finish();
        });

    }
    private void bindingViews() {
        latitudeET = findViewById(R.id.latitude_editText);
        longitudeET = findViewById(R.id.longitude_editText);
        submitBtn = findViewById(R.id.submit_lat_long_btn);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initialisingGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_2);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        zoomedCurrentLocation();

        // Marker
        mMap.setOnMapClickListener(latLng -> {
            latitudeET.setText(String.valueOf(latLng.latitude));
            longitudeET.setText(String.valueOf(latLng.longitude));

            // Clear current marker if exists
            if (currentMarker != null) {
                currentMarker.remove();
            }

            // Place new marker
            currentMarker = mMap.addMarker((new MarkerOptions().position(latLng).title("Selected Location")));
        });

    }

    private void zoomedCurrentLocation() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
                    }
                }
            });

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }


}
