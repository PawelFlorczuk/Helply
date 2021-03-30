package com.example.helply.menu;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.components.Adapter;
import com.example.helply.R;
import com.example.helply.login.LoginActivity;
import com.example.helply.popup.ChoosePartPopUpWindow;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class AnnouncementsMainActivity extends MenuNavigationTemplate {

    protected Toolbar toolbar;
    protected RecyclerView recyclerView;
    private Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseFirestore db;
    private Vector<String[]> dataList;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private ImageView refreshView;
    private ImageView searchView;

    private String partOfCountry;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            super.onCreate(savedInstanceState);
            user = FirebaseAuth.getInstance().getCurrentUser();
            mAuth = FirebaseAuth.getInstance();
            setContentView(R.layout.activity_announcements_main);
            dataList = new Vector<>();
            navigationView = findViewById(R.id.nv_navView);
            navigationView.bringToFront();
            progressBar = findViewById(R.id.mainProgressBar);
            progressBar.setVisibility(View.VISIBLE);
            drawerLayout = findViewById(R.id.dl_drawer_layout);
            recyclerView = findViewById(R.id.recycledView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            toolbar = findViewById(R.id.toolBar);
            partOfCountry = " ";
            refreshView = findViewById(R.id.refreshView);
            searchView = findViewById(R.id.searchPartOfCountryView);

            refreshView.setOnClickListener(v -> {
                partOfCountry = " ";
                refresh();
            });
            searchView.setOnClickListener(v -> startActivityForResult(
                    new Intent(AnnouncementsMainActivity.this, ChoosePartPopUpWindow.class),1010));

            toolbar.setTitleTextColor(Color.DKGRAY);
            toolbar.setTitle("Announcements");
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            refreshView.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);

            View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
            profileImage = headerView.findViewById(R.id.profileImage);

            setProfileImage();
            initSideBarMenu();
            refresh();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1113){
            SharedPreferences preferences = getSharedPreferences("Province",MODE_PRIVATE);
            this.partOfCountry = preferences.getString("province"," ");
            refresh();
        }
    }

    public void refresh() {
        if (mAuth.getUid() != null) {
            db = FirebaseFirestore.getInstance();
            com.google.android.gms.tasks.Task<QuerySnapshot> documentReference = db.collection("tasks").orderBy("date", Query.Direction.DESCENDING).get();
            documentReference.addOnCompleteListener(task -> {
                dataList = new Vector<>();
                List<DocumentSnapshot> list = task.getResult().getDocuments();
                if (partOfCountry.equals(" ")) {
                    for (DocumentSnapshot doc : list) {
                        if (Objects.requireNonNull(doc.get("helper")).toString().equals(" ")) {
                            dataList.add(extractData(doc));
                        }
                    }
                } else {
                    for (DocumentSnapshot doc : list) {
                        if (Objects.requireNonNull(doc.get("helper")).toString().equals(" ") &&
                                Objects.requireNonNull(doc.get("address")).toString().split("-")[1].equals(partOfCountry)) {
                            dataList.add(extractData(doc));
                        }
                    }
                }
                adapter = new Adapter(AnnouncementsMainActivity.this, dataList, "AnnouncementDetails");
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            });
        }
    }
    private String[] extractData (DocumentSnapshot doc) {
        String[] dataString = new String[8];
        dataString[0] = Objects.requireNonNull(doc.get("date")).toString();
        dataString[1] = Objects.requireNonNull(doc.get("address")).toString();
        dataString[2] = Objects.requireNonNull(doc.get("description")).toString();
        dataString[3] = Objects.requireNonNull(doc.get("helper")).toString();
        dataString[4] = Objects.requireNonNull(doc.get("emailPhoneNumber")).toString();
        dataString[5] = Objects.requireNonNull(doc.get("kindOfHelp")).toString();
        dataString[6] = Objects.requireNonNull(doc.get("nameOfHelp")).toString();
        dataString[7] = doc.getId();
        return dataString;
    }


}
