package com.example.helply;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FinishTaskActivity extends AppCompatActivity implements View.OnClickListener {
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
    private TextView textView;
    private CheckBox checkBox;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_task);
        addressTV = findViewById(R.id.adressET);
        purchaseTV = findViewById(R.id.purchaseTV);
        messageTV = findViewById(R.id.mesTV);
        descTV = findViewById(R.id.descET);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);
        textView = findViewById(R.id.textView);
        checkBox = findViewById(R.id.finishTaskCheckBox);

        addressTV.setText(Data.Address);
        purchaseTV.setText(Data.Purchase);
        messageTV.setText(Data.Message);
        descTV.setText(Data.Description);
        if(!Data.Helper.equals(" ")){
            textView.setText("Taken");
        } else  {
            textView.setText("Not taken");
        }

        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("My tasks");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addBtn) {

            if (!mAuth.getUid().equals(Data.Helper)) {
                db = FirebaseFirestore.getInstance();

                DocumentReference documentReference = db.collection("tasks").document(Data.ID);
                if (checkBox.isChecked()) {
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.get("helper") != " " && doc.exists() && doc != null) {
                                String uid = (String) doc.get("helper");
                                DocumentReference documentReference = db.collection("users").document(uid);
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        long points;
                                        DocumentSnapshot doc = task.getResult();

                                        points = (long) doc.get("points");
                                        points++;
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("points", points);
                                        documentReference.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(FinishTaskActivity.this, "Dodano punkty!", Toast.LENGTH_SHORT).show();
                                                db.collection("tasks").document(Data.ID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(FinishTaskActivity.this, "Udało się zakończyc task!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), MyTasksActivity.class));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                            } else {

                                db.collection("tasks").document(Data.ID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(FinishTaskActivity.this, "Udało się zakończyc task!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MyTasksActivity.class));
                                    }
                                });
                            }


                        }
                    });
                } else {
                    Toast.makeText(FinishTaskActivity.this, "2", Toast.LENGTH_SHORT).show();
                    db.collection("tasks").document(Data.ID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(FinishTaskActivity.this, "Udało się zakończyc task!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MyTasksActivity.class));
                        }
                    });
                }



            }
    }
    }

}