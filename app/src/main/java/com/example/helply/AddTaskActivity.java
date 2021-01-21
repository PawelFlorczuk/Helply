package com.example.helply;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {
    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView addressTV;
    private TextView purchaseTV;
    private TextView messageTV;
    private TextView descTV;
    private Button addBtn;
    private FirebaseFirestore db;
    private FirebaseUser mAuth;

    private Bitmap mainBitmap;
    private ImageView profileImage;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        addressTV = findViewById(R.id.adressET);
        purchaseTV = findViewById(R.id.purchaseTV);
        messageTV = findViewById(R.id.mesTV);
        descTV = findViewById(R.id.descET);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Add tasks");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();



        Intent intent = getIntent();
        mainBitmap = intent.getParcelableExtra("Bitmap");

        if(mainBitmap == null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("profileImage")
                    .child(mAuth.getUid() + ".jpg");

            try {
                final File image = File.createTempFile(mAuth.getUid(), ".jpg");
                storageReference.getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                        if(profileImage != null) {
                            profileImage.setImageBitmap(bitmap);
                            mainBitmap = bitmap;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profileImage.setImageResource(R.drawable.logo);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            profileImage.setImageBitmap(mainBitmap);
        }


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
                    case R.id.rankItem: {
                        finish();
                        startActivity(new Intent(getApplicationContext(), RankingActivity.class));
                        break;
                    }
                    case R.id.tasksToDoITem: {

                        startActivity(new Intent(getApplicationContext(), TasksToDoActivity.class));
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

                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addBtn)
        {
            mAuth = FirebaseAuth.getInstance().getCurrentUser();
            String address,purch,mes,des;
            address = addressTV.getText().toString().trim();
            purch = purchaseTV.getText().toString().trim();
            mes = messageTV.getText().toString().trim();
            des = descTV.getText().toString().trim();
            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("tasks").document(mAuth.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put("address", address);
            user.put("purchase",purch);
            user.put("message",mes);
            user.put("description",des);
            user.put("helper"," ");

            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddTaskActivity.this, "Udało się zarejestrować", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainAcivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddTaskActivity.this, e.toString(), Toast.LENGTH_SHORT).show();


                }
            });
       }

    }
}
