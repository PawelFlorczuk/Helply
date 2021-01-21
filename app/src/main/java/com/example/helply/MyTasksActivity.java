package com.example.helply;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Vector;

public class MyTasksActivity extends AppCompatActivity {
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Vector<String[]> datalist = new Vector<>();
        if(mAuth.getUid()!= null){


            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("tasks").document(mAuth.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists() && !
                            documentSnapshot.equals(null)){
                        String[] dataString = new String[6];
                        dataString[0] = documentSnapshot.get("address").toString();
                        dataString[1] = documentSnapshot.get("description").toString();
                        dataString[2] = documentSnapshot.get("helper").toString();
                        dataString[3] = documentSnapshot.get("message").toString();
                        dataString[4] = documentSnapshot.get("purchase").toString();
                        dataString[5] = documentSnapshot.getId();
                        datalist.add(dataString);
                        adapter = new Adapter(MyTasksActivity.this, datalist,2);
                        recyclerView.setAdapter(adapter);
                    }

                }

            });


        } else{
        }
    }
}
