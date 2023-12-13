package com.example.pureflow_group_project;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pureflow_group_project.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final private int REQUEST_COARSE_ACCESS = 123;
    boolean permissionGranted = false;
    LocationManager lm;
    LocationListener locationListener;
    private ActivityMapsBinding binding;

    public static double MaplatitudeRes, MaplongitudeRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION}, REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }

        if (permissionGranted) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        //coordinates for the location of the pointer. A
        //Add coordinates in line 79 for your location
        LatLng locationPointer = new LatLng(54.10, -5.58);
        mMap.addMarker(new MarkerOptions().position(locationPointer).title("set location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationPointer, 12.0f));

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);  //give a satellite view of the map, rather than 2d
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    String placeName = "Titanic";
                    List<Address> addresses = geocoder.getFromLocationName(placeName, 1);
                    Address address = addresses.get(0);
                    String add = "";
                    if (addresses.size() > 0) {
                        add += address.getAddressLine(0) + "/n";
                        LatLng p = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(p).title(add));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 12.0f));
                    }
                    Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
                    Inputs.latitudeRes = address.getLatitude();
                    Inputs.longitudeRes = address.getLongitude();
                    MaplatitudeRes = address.getLatitude();
                    MaplongitudeRes = address.getLongitude();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                Toast.makeText(getBaseContext(),
                        "Current location : Lat: " + location.getLatitude() +
                                " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                LatLng p = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(p).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 12.0f));
                Inputs.latitudeRes = location.getLatitude();
                Inputs.longitudeRes = location.getLongitude();
                MaplatitudeRes = location.getLatitude();
                MaplongitudeRes = location.getLongitude();
            }
        }

        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                } else {
                    permissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }

        if (permissionGranted) {
            if (lm != null && locationListener != null) {
                lm.removeUpdates(locationListener);
            }
        }
    }
}