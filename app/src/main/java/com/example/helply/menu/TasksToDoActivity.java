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
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Vector;

public class TasksToDoActivity extends Navigaction {
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
        setContentView(R.layout.activity_tasks_to_do);
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Tasks to be performed");
        recyclerView = findViewById(R.id.recycledView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.inflateHeaderView(R.layout.header);
        profileImage = (ImageView) headerView.findViewById(R.id.profileImage);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("Bitmap");
        setProfileImage(bitmap);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.tasksItem: {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.lookForTaskItem: {
                        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.myTasksItem: {
                        Intent intent = new Intent(getApplicationContext(), MyTasksActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.settingsItem: {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.logOutItem: {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    }
                    case R.id.rankItem: {
                        Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.tasksToDoITem: {
                        Intent intent = new Intent(getApplicationContext(), TasksToDoActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
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
            com.google.android.gms.tasks.Task<QuerySnapshot> documentReference = db.collection("tasks").get();
            documentReference.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    List<DocumentSnapshot> list = task.getResult().getDocuments();
                    int i = 0 ;
                    for (DocumentSnapshot doc : list) {
                        if(doc.get("helper").toString().equals(mAuth.getUid())){
                            String[] dataString = new String[6];
                            dataString[0] = doc.get("address").toString();
                            dataString[1] = doc.get("description").toString();
                            dataString[2] = doc.get("helper").toString();
                            dataString[3] = doc.get("message").toString();
                            dataString[4] = doc.get("purchase").toString();
                            dataString[5] = doc.getId();
                            datalist.add(dataString);
                        }
                        i++;

                    }
                    adapter = new Adapter(TasksToDoActivity.this, datalist,1);
                    recyclerView.setAdapter(adapter);
                }




            });
        } else{
        }
    }
}
