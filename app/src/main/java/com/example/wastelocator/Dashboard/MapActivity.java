package com.example.wastelocator.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastelocator.BinActivity.NewBinActivity;
import com.example.wastelocator.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button findBinBtn, cancelBtn;
    private double latitude, longitude;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng userCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        bindingViews();
        initialisingGoogleMap();
        gettingBinLocation();
        onClickListener();


    }

    private void bindingViews() {
        findBinBtn = findViewById(R.id.map_find_bin_btn);
        cancelBtn = findViewById(R.id.map_cancel_btn);

    }

    private void initialisingGoogleMap() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    private void onClickListener() {
        findBinBtn.setOnClickListener(view -> {
            findBinBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.VISIBLE);
            navigateToBin(new LatLng(latitude, longitude));

        });

        cancelBtn.setOnClickListener(view -> {
            findBinBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);

            if (mMap != null) {
                mMap.clear();
                addBinMarker();// add the bin locations back

                // Reset zoom view
                if (userCurrentLocation != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userCurrentLocation, 13);
                    mMap.animateCamera(cameraUpdate, 1000, null); // 1000 ms duration
                }
            }


        });
    }


    // get the route from the Google Directions API
    private void navigateToBin(LatLng destination) {
        if (mMap == null || userCurrentLocation == null) {
            Toast.makeText(this, "No bin available.", Toast.LENGTH_SHORT).show();
            return;
        }

        String apiKey = "AIzaSyA96EjtC0kzDW2WVSCrzOPqd6U5QxHtOqE";
        LatLng origin = userCurrentLocation;

        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.latitude + "," + origin.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&mode=walking" + // walking mode
                "&key=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray routesArray = response.getJSONArray("routes");
                    JSONObject route = routesArray.getJSONObject(0);
                    JSONObject polyline = route.getJSONObject("overview_polyline");
                    String point = polyline.getString("points");
                    List<LatLng> decodedPath = PolyUtil.decode(point);
                    mMap.addPolyline(new PolylineOptions().addAll(decodedPath));

                    // Adjusting the camera position to be centered between the user location and the destination
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(userCurrentLocation);
                    builder.include(destination);
                    LatLngBounds bounds = builder.build();
                    int padding = 250;
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cameraUpdate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }

    private void gettingBinLocation() {
        latitude = getIntent().getDoubleExtra("lat", 0);
        longitude = getIntent().getDoubleExtra("long", 0);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        zoomedCurrentLocation();
        addBinMarker();
    }

    private void zoomedCurrentLocation() {
        // read current location of the user device
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // blue point
            mMap.setMyLocationEnabled(true);

            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location != null) {
                        // zoom to the current location
                        double currentLatitude = location.getLatitude();
                        double currentLongitude = location.getLongitude();
                        LatLng currentLatLng = new LatLng(currentLatitude, currentLongitude);
                        userCurrentLocation = currentLatLng; // save current location
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
                    }
                }
            });

        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

    private void addBinMarker() {
        if (latitude != 0 && longitude != 0 && mMap != null) {
            LatLng binLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(binLocation).title("Bin Location"));
        }
    }







}

