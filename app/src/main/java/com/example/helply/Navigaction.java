package com.example.helply;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

public class Navigaction extends AppCompatActivity {
    protected Bitmap bitmap;
    protected NavigationView navigationView;
    protected ImageView profileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();


        View headerView = navigationView.inflateHeaderView(R.layout.header);
        profileImage = (ImageView) headerView.findViewById(R.id.profileImage);



    }
}
