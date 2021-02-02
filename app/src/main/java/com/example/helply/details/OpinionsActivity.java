package com.example.helply.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.R;
import com.example.helply.components.Adapter;
import com.example.helply.components.OpinionsAdapter;
import com.example.helply.login.LoginActivity;
import com.example.helply.menu.AnnouncementsMainActivity;
import com.example.helply.menu.MenuNavigationTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpinionsActivity extends MenuNavigationTemplate {
    protected Toolbar toolbar;
    protected RecyclerView recyclerView;
    private OpinionsAdapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseFirestore db;
    private Vector<String[]> datalist;
    private FirebaseAuth mAuth;
    private String id;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.activity_volunteer_opinions);
            super.onCreate(savedInstanceState);
            user = FirebaseAuth.getInstance().getCurrentUser();
            mAuth = FirebaseAuth.getInstance();
            progressBar = findViewById(R.id.opinionsProgressBar);
            progressBar.setVisibility(View.VISIBLE);
            datalist = new Vector<String[]>();
            navigationView = findViewById(R.id.nv_navView);
            navigationView.bringToFront();

            drawerLayout = findViewById(R.id.dl_drawer_layout);
            recyclerView = findViewById(R.id.recycledView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            toolbar = findViewById(R.id.toolBar);
            toolbar.setTitle("Opinions");
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
            profileImage = (CircleImageView) headerView.findViewById(R.id.profileImage);

            Intent intent = getIntent();
            bitmap = intent.getParcelableExtra("Bitmap");
            id = intent.getStringExtra("Id");
            setProfileImage(bitmap);

            initSideBarMenu();
        toolbar.setTitleTextColor(Color.DKGRAY);

            if (mAuth.getUid() != null) {
                db = FirebaseFirestore.getInstance();
                try {
                    com.google.android.gms.tasks.Task<QuerySnapshot> documentReference = db.collection("users").document(id).collection("opinions").orderBy("date", Query.Direction.DESCENDING).get();
                    documentReference.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            List<DocumentSnapshot> list = task.getResult().getDocuments();
                            int i = 0;
                            for (DocumentSnapshot doc : list) {

                                String[] dataString = new String[8];
                                dataString[0] = doc.get("opinion").toString();
                                dataString[1] = doc.get("date").toString();
                                datalist.add(dataString);


                                i++;
                            }
                            if(i == 0) {
                                Toast.makeText(getApplicationContext(),"This volunteer hasn't got any opinions",Toast.LENGTH_SHORT).show();
                            }
                            adapter = new OpinionsAdapter(OpinionsActivity.this, datalist);
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"This volunteer hasn't got any opinions",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                }
            } else {
            }

        }
    }


