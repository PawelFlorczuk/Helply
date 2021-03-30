package com.example.helply.popup;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helply.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

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

        saveAddressBtn.setOnClickListener(v -> {
            String address = addressList.get(0).toString();
            addressList.get(0).getCountryName();
            if(!addressList.get(0).getCountryName().equals("Poland") && !addressList.get(0).getCountryName().equals("Polska")) {
                Toast.makeText(getApplicationContext(), "You can only choose a place in Poland or you have mistyped your address", Toast.LENGTH_LONG).show();
                return;
            }
            if(addressList.get(0).getCountryName() == null || address.split(",")[7].split("=")[1] == null ||
                    address.split(",")[6].split("=")[1] == null ||  address.split(",")[3].split("=")[1] == null ||
                    address.split(",")[4].split("=")[1] == null) {
                Toast.makeText(getApplicationContext(), "Address should be written like this :\"Country City Street Street number \"", Toast.LENGTH_LONG).show();
                return;
            }
            try {
               Integer.valueOf(address.split(",")[3].split("=")[1]);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Address should be written like this :\"Country City Street Street number \"", Toast.LENGTH_LONG).show();
                return;
            }

            SharedPreferences preferences = getSharedPreferences("Address",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("address",address);
            editor.apply();
            setResult(1);
            finish();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    try {
                        String location = searchView.getQuery().toString();
                        addressList = null;
                        if (!location.equals("")) {
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
                        Toast.makeText(getApplicationContext(), "Address should be written like this :\"Country City Street Street number \"", Toast.LENGTH_LONG).show();
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
        LatLng lat = new LatLng(52.112795,19.211946);
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,5));
        myMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
