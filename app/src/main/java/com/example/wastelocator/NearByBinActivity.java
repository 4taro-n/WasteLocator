package com.example.wastelocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class NearByBinActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button addBinBtn, reportWastBtn, findBinBtn;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_bin);

        bindingViews();
        initialisingGoogleMap();
        onClickListener();

    }

    private void bindingViews() {
        addBinBtn = findViewById(R.id.add_bin_btn);
        findBinBtn = findViewById(R.id.find_nearby_bin_btn);
        reportWastBtn = findViewById(R.id.report_waste_btn);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void onClickListener() {
        addBinBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewBinActivity.class);
            startActivity(intent);
        });

        findBinBtn.setOnClickListener(view -> {
            // Intent
        });

        reportWastBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        zoomedCurrentLocation();
    }

    private void initialisingGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void zoomedCurrentLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }


}