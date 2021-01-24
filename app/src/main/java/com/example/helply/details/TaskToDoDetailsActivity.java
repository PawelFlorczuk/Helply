package com.example.helply.details;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.model.Data;
import com.example.helply.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskToDoDetailsActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView addressTV;
    private TextView purchaseTV;
    private TextView messageTV;
    private TextView descTV;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_details);

//        addressTV = findViewById(R.id.kindOfHelpTV);
//        purchaseTV = findViewById(R.id.purchaseTV);
//        messageTV = findViewById(R.id.descriptionTV);
//        descTV = findViewById(R.id.descET);
//
//        addressTV.setText(Data.Address);
//        purchaseTV.setText(Data.Purchase);
//        messageTV.setText(Data.Message);
//        descTV.setText(Data.Description);
//
//
//        mAuth = FirebaseAuth.getInstance();
//        navigationView = findViewById(R.id.nv_navView);
//        navigationView.bringToFront();
//        drawerLayout = findViewById(R.id.dl_drawer_layout);
//        toolbar = findViewById(R.id.toolBar);
//        toolbar.setTitle("My tasks");
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();

    }

}