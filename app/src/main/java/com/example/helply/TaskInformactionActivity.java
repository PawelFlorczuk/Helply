package com.example.helply;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.login.LoginActivity;
import com.example.helply.menu.AddTaskActivity;
import com.example.helply.menu.MainActivity;
import com.example.helply.menu.MyTasksActivity;
import com.example.helply.menu.RankingActivity;
import com.example.helply.menu.SettingsActivity;
import com.example.helply.menu.TasksToDoActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskInformactionActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_task_informaction);
        addressTV = findViewById(R.id.adressET);
        purchaseTV = findViewById(R.id.purchaseTV);
        messageTV = findViewById(R.id.mesTV);
        descTV = findViewById(R.id.descET);

        addressTV.setText(Data.Address);
        purchaseTV.setText(Data.Purchase);
        messageTV.setText(Data.Message);
        descTV.setText(Data.Description);


        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("My tasks");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

}