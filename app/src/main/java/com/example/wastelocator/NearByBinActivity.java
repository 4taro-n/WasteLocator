package com.example.wastelocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class NearByBinActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button addBinBtn, reportWastBtn, findBinBtn;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<LatLng> markerLocations = new ArrayList<>();
    private static final double RADIUS = 0.03; // 5 km

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
    }

    private void initialisingGoogleMap() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void onClickListener() {
        addBinBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewBinActivity.class);
            startActivity(intent);
        });

        findBinBtn.setOnClickListener(view ->
            navigateToNearestBin()
        );

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

    // Update the camera position as the user move
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
            }
        }
    };

    private void requestLocationUpdate() {
        LocationRequest locationRequest = new LocationRequest();


    }

    private void zoomedCurrentLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, task -> {
               if (task.isSuccessful()) {
                   Location location = task.getResult();
                   if (location != null) {
                       // zoom to the current location
                       double currentLatitude = location.getLatitude();
                       double currentLongitude = location.getLongitude();
                       LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));

                       // add random markers around current location
                       for (int i = 0; i < 10; i++) {
                           double randomLatitude = currentLatitude + (Math.random() * 2 * RADIUS) - RADIUS;
                           double randomLongitude = currentLongitude + (Math.random() * 2 * RADIUS) - RADIUS;

                           markerLocations.add(new LatLng(randomLatitude, randomLongitude));
                       }
                       addMarkers();
                   }
               }
            });

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    private void navigateToNearestBin() {
        if (mMap == null) return;
        LatLng currentLocation = mMap.getCameraPosition().target;
        LatLng nearestLocation = null;
        double shortestDistance = Double.MIN_VALUE;

        // Check nearest distance of bins
        for (LatLng markerLocation : markerLocations) {
            double distance = SphericalUtil.computeDistanceBetween(currentLocation, markerLocation);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestLocation = markerLocation;
            }
        }
        if (nearestLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nearestLocation, 20));
        }
    }


    private void addMarkers() {
        for (LatLng location : markerLocations) {
            mMap.addMarker(new MarkerOptions().position(location));
        }
    }


}