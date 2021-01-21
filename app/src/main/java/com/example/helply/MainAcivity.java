package com.example.helply;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class MainAcivity  extends AppCompatActivity {

    protected Toolbar toolbar;
    protected RecyclerView recyclerView;
    private Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseFirestore db;
    private Vector<String[]> datalist;
    private FirebaseAuth mAuth;

    private ImageView profileImage;
    private Bitmap mainBitmap;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser()== null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        datalist = new Vector<String[]>();
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();

        drawerLayout = findViewById(R.id.dl_drawer_layout);
        recyclerView = findViewById(R.id.recycledView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Tasks to take");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        View headerView = navigationView.inflateHeaderView(R.layout.header);
        profileImage = (ImageView) headerView.findViewById(R.id.profileImage);



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
            profileImage.setImageResource(R.drawable.logo);
        }




        if(mAuth.getUid()!= null){
        db = FirebaseFirestore.getInstance();
        com.google.android.gms.tasks.Task<QuerySnapshot> documentReference = db.collection("tasks").get();
        documentReference.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<DocumentSnapshot> list = task.getResult().getDocuments();
                int i = 0 ;
                for (DocumentSnapshot doc : list) {
                    if(doc.get("helper").toString().equals(" ")){
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
                adapter = new Adapter(MainAcivity.this, datalist,0);
                recyclerView.setAdapter(adapter);
            }




        });
        } else{
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.tasksItem: {
                        Intent intent = new Intent(getApplicationContext(), MainAcivity.class);
                        intent.putExtra("Bitmap", mainBitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.lookForTaskItem: {
                        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                        intent.putExtra("Bitmap", mainBitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.myTasksItem: {
                        Intent intent = new Intent(getApplicationContext(), MyTasksActivity.class);
                        intent.putExtra("Bitmap", mainBitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.settingsItem: {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.putExtra("Bitmap", mainBitmap);
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
                        intent.putExtra("Bitmap", mainBitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.tasksToDoITem: {
                        Intent intent = new Intent(getApplicationContext(), TasksToDoActivity.class);
                        intent.putExtra("Bitmap", mainBitmap);
                        startActivity(intent);
                        break;
                    }

                }
                return true;
            }
        });
    }
}
