package com.example.helply;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.helply.menu.AddTaskActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private SearchView searchView;
    private Button saveAddressBtn;
    private List<Address> addressList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        searchView = findViewById(R.id.searchView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);
        saveAddressBtn = findViewById(R.id.saveYourAddress);
        saveAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = addressList.get(0).toString();
                SharedPreferences preferences = getSharedPreferences("Address",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("address",address);
                editor.apply();
                setResult(1);
                finish();

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                    try {
                        String location = searchView.getQuery().toString();
                        addressList = null;
                        if (location != null || !location.equals("")) {
                            Geocoder geocoder = new Geocoder(MapActivity.this);
                            addressList = geocoder.getFromLocationName(location, 1);
                        }
                        myMap.clear();
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        saveAddressBtn.setVisibility(View.VISIBLE);


                    } catch (IOException | IndexOutOfBoundsException  e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Address should be written like this :\"Country City Street Street number \"", Toast.LENGTH_LONG);
                        toast.show();
                        saveAddressBtn.setVisibility(View.INVISIBLE);

                    }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng lat = new LatLng(52.229676,21.012229);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
        myMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
