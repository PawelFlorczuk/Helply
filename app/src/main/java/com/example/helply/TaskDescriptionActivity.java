package com.example.helply;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TaskDescriptionActivity extends AppCompatActivity implements View.OnClickListener {
    protected Toolbar toolbar;
    private Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView addressTV;
    private TextView purchaseTV;
    private TextView messageTV;
    private TextView descTV;
    private Button addBtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_desc);
        addressTV = findViewById(R.id.adressET);
        purchaseTV = findViewById(R.id.purchaseTV);
        messageTV = findViewById(R.id.mesTV);
        descTV = findViewById(R.id.descET);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

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
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.tasksItem: {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    }
                    case R.id.lookForTaskItem: {
                        startActivity(new Intent(getApplicationContext(), AddTaskActivity.class));
                        break;
                    }
                    case R.id.myTasksItem: {
                        startActivity(new Intent(getApplicationContext(), MyTasksActivity.class));
                        break;
                    }
                    case R.id.settingsItem: {
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    }
                    case R.id.logOutItem: {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    }
                    case R.id.rankItem: {
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addBtn) {

            if(!mAuth.getUid().equals(Data.ID)) {

                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("tasks").document(Data.ID);
                Map<String, Object> user = new HashMap<>();

                user.put("helper",mAuth.getUid());

                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TaskDescriptionActivity.this, "Udało się zarejestrować", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TaskDescriptionActivity.this, e.toString(), Toast.LENGTH_SHORT).show();


                    }
                });
            } else{
                Toast.makeText(TaskDescriptionActivity.this, "e.toString()", Toast.LENGTH_SHORT).show();
            }
            }

    }

}
