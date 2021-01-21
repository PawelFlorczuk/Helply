package com.example.helply;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Places.initialize(getApplicationContext(), "AIzaSyCzrOHdM_GIXoHnik1FBi62j7cpxy1w2UU");
        PlacesClient placesClient = Places.createClient(this);
        searchView = findViewById(R.id.searchView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);
//        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.mySearchMap);
//
//
//        autocompleteSupportFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
//        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
//                new LatLng(-33.880490, 151.1843363),
//                new LatLng(-33.858754, 151.229596)));
//        autocompleteSupportFragment.setCountries("IN");
//        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                Toast.makeText(getApplicationContext(), place.getName()+  " " + place.getId(),Toast.LENGTH_SHORT).show();
//                place.getLatLng();
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();
//            }
//        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
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
        myMap.addMarker(new MarkerOptions().position(lat));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                myMap.addMarker(new MarkerOptions().position(lat));
            }
        });
    }
}
