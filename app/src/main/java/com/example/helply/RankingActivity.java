package com.example.helply;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Vector;


public class RankingActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    private RankingAdapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Vector<String[]> listData = new Vector<String[]>();
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        this.recyclerView = findViewById(R.id.rankingRecycledView);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("My tasks");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        db = FirebaseFirestore.getInstance();

        Task<QuerySnapshot> documentReference = db.collection("users").get();
        documentReference.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<DocumentSnapshot> list = task.getResult().getDocuments();
                int i = 0 ;
                for (DocumentSnapshot doc : list) {
                    String[] dataString = new String[3];
                    dataString[0] = doc.get("email").toString();
                    dataString[1] = doc.get("phone").toString();
                    dataString[2] = doc.get("points").toString();

                    listData.add(dataString);

                    i++;

                }
                adapter = new RankingAdapter(RankingActivity.this, listData);
                recyclerView.setAdapter(adapter);
            }




        });




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.tasksItem: {
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainAcivity.class));
                        break;
                    }
                    case R.id.lookForTaskItem: {
                        finish();
                        startActivity(new Intent(getApplicationContext(), AddTaskActivity.class));
                        break;
                    }
                    case R.id.myTasksItem: {
                        finish();
                        startActivity(new Intent(getApplicationContext(), MyTasksActivity.class));
                        break;
                    }
                    case R.id.settingsItem: {
                        finish();
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    }
                    case R.id.logOutItem: {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    }
                    case R.id.rankItem: {
                        finish();
                        startActivity(new Intent(getApplicationContext(), RankingActivity.class));
                        break;
                    }
                    case R.id.tasksToDoITem: {
                        startActivity(new Intent(getApplicationContext(), TasksToDoActivity.class));
                        break;
                    }

                }
                return true;
            }
        });
    }
}
