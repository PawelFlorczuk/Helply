package com.example.helply.menu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.Adapter;
import com.example.helply.R;
import com.example.helply.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTasksActivity extends Navigation {
    protected Toolbar toolbar;
    private Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        recyclerView = findViewById(R.id.recycledView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar.setTitle("My announcements");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
        profileImage = (CircleImageView) headerView.findViewById(R.id.profileImage);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("Bitmap");
        setProfileImage(bitmap);

        this.initSideBarMenu();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Vector<String[]> datalist = new Vector<>();
        if (mAuth.getUid() != null) {
            db = FirebaseFirestore.getInstance();
            com.google.android.gms.tasks.Task<QuerySnapshot> documentReference = db.collection("tasks").get();
            documentReference.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    List<DocumentSnapshot> list = task.getResult().getDocuments();
                    int i = 0;
                    for (DocumentSnapshot doc : list) {
                        if (doc.getId().split("-")[0].equals(mAuth.getUid())) {
                            if (doc.get("helper").toString().equals(" ")) {
                                String[] dataString = new String[8];
                                dataString[0] = doc.get("date").toString();
                                dataString[1] = doc.get("address").toString();
                                dataString[2] = doc.get("description").toString();
                                dataString[3] = doc.get("helper").toString();
                                dataString[4] = doc.get("emailPhoneNumber").toString();
                                dataString[5] = doc.get("kindOfHelp").toString();
                                dataString[6] = doc.get("nameOfHelp").toString();
                                dataString[7] = doc.getId();
                                datalist.add(dataString);

                              }
                            }
                        i++;
                    }

                    adapter = new Adapter(MyTasksActivity.this, datalist, 0,bitmap);
                    recyclerView.setAdapter(adapter);
                }

            });
        } else {
        }
    }


}
