package com.example.helply.menu;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.example.helply.components.RankingAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;
import java.util.Vector;


public class RankingActivity extends MenuNavigationTemplate {
    protected Toolbar toolbar;
    private RankingAdapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Vector<String[]> listData = new Vector<>();
        progressBar = findViewById(R.id.rankingProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        this.recyclerView = findViewById(R.id.rankingRecycledView);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Best volunteers");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        toolbar.setTitleTextColor(Color.DKGRAY);
        db = FirebaseFirestore.getInstance();

        View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
        profileImage = headerView.findViewById(R.id.profileImage);
        user = FirebaseAuth.getInstance().getCurrentUser();

        setProfileImage();
        this.initSideBarMenu();

        Task<QuerySnapshot> documentReference = db.collection("users").get();
        documentReference.addOnCompleteListener(task -> {

            List<DocumentSnapshot> list = task.getResult().getDocuments();
            for (DocumentSnapshot doc : list) {
                String[] dataString = new String[3];
                dataString[0] = Objects.requireNonNull(doc.get("login")).toString();
                dataString[1] = Objects.requireNonNull(doc.get("points")).toString();
                dataString[2] = doc.getId();
                listData.add(dataString);
            }
            adapter = new RankingAdapter(RankingActivity.this, listData, bitmap);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
        });
    }
}
