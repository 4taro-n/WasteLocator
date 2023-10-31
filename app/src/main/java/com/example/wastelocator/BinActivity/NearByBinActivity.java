package com.example.wastelocator.BinActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastelocator.FeedbackActivity;
import com.example.wastelocator.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearByBinActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button addBinBtn, reportWastBtn, findBinBtn, cancelBtn;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<LatLng> markerLocations = new ArrayList<>();
    private static final double RADIUS = 0.03; // 3 km
    private LatLng userCurrentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_bin);

        bindingViews();
        initialisingGoogleMap();
        onClickListener();

    }


    private void bindingViews() {
        //addBinBtn = findViewById(R.id.add_bin_btn);
        findBinBtn = findViewById(R.id.find_nearby_bin_btn);
        reportWastBtn = findViewById(R.id.report_waste_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
    }

    private void initialisingGoogleMap() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void onClickListener() {
//        addBinBtn.setOnClickListener(view -> {
//            Intent intent = new Intent(this, NewBinActivity.class);
//            startActivity(intent);
//        });

        findBinBtn.setOnClickListener(view -> {
            findBinBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.VISIBLE);
            navigateToNearestBin();
        });

        reportWastBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        });

        cancelBtn.setOnClickListener(view -> {
            findBinBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.GONE);

            // Clear the map of all polylines and then add markers again
            if (mMap != null) {
                mMap.clear();
                fetchBinLocations(); // add the bin locations back

                // Reset zoom view
                if (userCurrentLocation != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userCurrentLocation, 13);
                    mMap.animateCamera(cameraUpdate, 1000, null); // 1000 ms duration
                }
            }


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

                       // add random markers around current location
                       /*
                       for (int i = 0; i < 10; i++) {
                           double randomLatitude = currentLatitude + (Math.random() * 2 * RADIUS) - RADIUS;
                           double randomLongitude = currentLongitude + (Math.random() * 2 * RADIUS) - RADIUS;

                           markerLocations.add(new LatLng(randomLatitude, randomLongitude));
                       }
                       addMarkers();*/

                       fetchBinLocations(); // add bins on the map

                   }
               }
            });

        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }
    }

//    private void addMarkers() {
//        for (LatLng location : markerLocations) {
//            mMap.addMarker(new MarkerOptions().position(location));
//        }
//    }

    private void navigateToNearestBin() {
        if (mMap == null || userCurrentLocation == null || markerLocations.isEmpty()) {
            Toast.makeText(this, "No bins available nearby.", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng nearestLocation = null;
        double shortestDistance = Double.MAX_VALUE;

        // Check nearest distance of bins
        for (LatLng markerLocation : markerLocations) {
            double distance = SphericalUtil.computeDistanceBetween(userCurrentLocation, markerLocation);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestLocation = markerLocation;
            }
        }

        if (nearestLocation != null) {
            getRouteToMarker(nearestLocation);
        } else {
            Toast.makeText(this, "No bins found within the vicinity.", Toast.LENGTH_SHORT).show();
        }
    }

    // get the route from the Google Directions API
    private void getRouteToMarker(LatLng destination) {
        String apiKey = "YOUR_GOOGLE_KRY";
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

    private void fetchBinLocations() {
        String url = "http://192.168.0.142:4567/bins";

        // Creating a JSON Array request to fetch bin locations
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Clear the existing markers
                            markerLocations.clear();
                            mMap.clear();

                            // Parsing the JSON response and adding markers
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject binLocation = response.getJSONObject(i);
                                double latitude = binLocation.getDouble("lat");
                                double longitude = binLocation.getDouble("long");
                                LatLng latLng = new LatLng(latitude, longitude);

                                markerLocations.add(latLng); // Adding location to the list
                                mMap.addMarker(new MarkerOptions().position(latLng));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NearByBinActivity.this, "Failed to get bin locations", Toast.LENGTH_SHORT).show();
            }
        });

        // Adding the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

}