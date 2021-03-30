package com.example.helply.menu;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.example.helply.components.Adapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class MyAnnouncementsActivity extends MenuNavigationTemplate {
    protected Toolbar toolbar;
    private Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_announcements);
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recycledView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar.setTitle("My announcements");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        progressBar = findViewById(R.id.myProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        toolbar.setTitleTextColor(Color.DKGRAY);
        View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
        profileImage = headerView.findViewById(R.id.profileImage);

        user = FirebaseAuth.getInstance().getCurrentUser();
        setProfileImage();
        this.initSideBarMenu();

        ImageView refresh = findViewById(R.id.refreshView);
        refresh.setVisibility(View.VISIBLE);
        refresh.setOnClickListener(v -> refreshMyAnnouncement());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Vector<String[]> dataList = new Vector<>();
        if (mAuth.getUid() != null) {
            db = FirebaseFirestore.getInstance();
            com.google.android.gms.tasks.Task<QuerySnapshot> documentReference = db.collection("tasks").get();
            documentReference.addOnCompleteListener(task -> {
                List<DocumentSnapshot> list = task.getResult().getDocuments();
                for (DocumentSnapshot doc : list) {
                    if (doc.getId().split("-")[0].equals(mAuth.getUid())) {
                        dataList.add(extractData(doc));
                    }
                }
                adapter = new Adapter(MyAnnouncementsActivity.this, dataList, "MyAnnouncementDetails");
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            });
        }
    }

    public void refreshMyAnnouncement() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Vector<String[]> dataList = new Vector<>();
        if (mAuth.getUid() != null) {
            db = FirebaseFirestore.getInstance();
            com.google.android.gms.tasks.Task<QuerySnapshot> documentReference = db.collection("tasks").get();
            documentReference.addOnCompleteListener(task -> {
                List<DocumentSnapshot> list = task.getResult().getDocuments();
                for (DocumentSnapshot doc : list) {
                    if (doc.getId().split("-")[0].equals(mAuth.getUid())) {
                        dataList.add(extractData(doc));
                    }
                }
                adapter = new Adapter(MyAnnouncementsActivity.this, dataList, "MyAnnouncementDetails");
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            });
        }
    }

    private String[] extractData (DocumentSnapshot doc) {
        String[] dataString = new String[9];
        dataString[0] = Objects.requireNonNull(doc.get("date")).toString();
        dataString[1] = Objects.requireNonNull(doc.get("address")).toString();
        dataString[2] = Objects.requireNonNull(doc.get("description")).toString();
        dataString[3] = Objects.requireNonNull(doc.get("helper")).toString();
        dataString[4] = Objects.requireNonNull(doc.get("emailPhoneNumber")).toString();
        dataString[5] = Objects.requireNonNull(doc.get("kindOfHelp")).toString();
        dataString[6] = Objects.requireNonNull(doc.get("nameOfHelp")).toString();
        dataString[7] = doc.getId();
        dataString[8] = Objects.requireNonNull(doc.get("volunteerContact")).toString();
        return dataString;
    }

}
