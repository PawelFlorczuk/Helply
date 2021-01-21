package com.example.helply.menu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.example.helply.MapActivity;
import com.example.helply.R;
import com.example.helply.login.LoginActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class AddTaskActivity extends Navigaction implements View.OnClickListener {


    private Spinner spinner;

    private TextView addressTV;
    private TextView yourAddressTV;
    private TextView helpKindTV;
    private TextView descTV;

    private EditText descET;
    private EditText helpKindET;

    private Button listBtn;
    private Button addressBtn;
    private Button addBtn;

    int PLACE_PICKER_REQUEST = 101;





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        spinner = findViewById(R.id.kindOfHelpSpinner);

        addressTV = findViewById(R.id.addressTV);
        yourAddressTV = findViewById(R.id.yourAddressTV);
        helpKindTV = findViewById(R.id.helpKindTV);
        descTV = findViewById(R.id.descTV);

        helpKindET = findViewById(R.id.helpKindET);
        descET = findViewById(R.id.descET);


        addBtn = findViewById(R.id.addBtn);
        addressBtn = findViewById(R.id.addressBtn);
        listBtn = findViewById(R.id.listBtn);

        addBtn.setOnClickListener(this);
        addressBtn.setOnClickListener(this);
        listBtn.setOnClickListener(this);

        navigationView = findViewById(R.id.nv_navView);
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Create announcements");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.bringToFront();

        View headerView = navigationView.inflateHeaderView(R.layout.header_deprecated);
        profileImage = (ImageView) headerView.findViewById(R.id.profileImage_deprecated);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("Bitmap");
        setProfileImage(bitmap);

        String[] spinnerString = {"Choose kind of help","Walking the dog", "Shopping", "Other"};
        spinner.setAdapter(new ArrayAdapter<>(AddTaskActivity.this,
                android.R.layout.simple_spinner_dropdown_item, spinnerString));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showForm(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                    case R.id.addressBtn: {



                        break;
                    }

                }
                return true;
            }
        });

    }




    public void showForm(int position) {
        switch (position) {

            case 0: {
                addBtn.setVisibility(View.INVISIBLE);
                listBtn.setVisibility(View.INVISIBLE);
                addressBtn.setVisibility(View.INVISIBLE);

                addressTV.setVisibility(View.INVISIBLE);
                yourAddressTV.setVisibility(View.INVISIBLE);
                helpKindTV.setVisibility(View.INVISIBLE);
                descTV.setVisibility(View.INVISIBLE);

                helpKindET.setVisibility(View.INVISIBLE);
                descET.setVisibility(View.INVISIBLE);

                break;
            }
            case 1: {
                addBtn.setVisibility(View.VISIBLE);
                addressBtn.setVisibility(View.VISIBLE);

                listBtn.setVisibility(View.INVISIBLE);

                addressTV.setVisibility(View.INVISIBLE);
                yourAddressTV.setVisibility(View.VISIBLE);

                helpKindTV.setVisibility(View.VISIBLE);
                helpKindET.setVisibility(View.VISIBLE);

                descTV.setVisibility(View.VISIBLE);
                descET.setVisibility(View.VISIBLE);

                helpKindTV.setText(R.string.dog_breed);
                helpKindET.setHint(R.string.dog_breed);





                break;
            }
            case 2: {
                addBtn.setVisibility(View.VISIBLE);
                addressBtn.setVisibility(View.VISIBLE);

                listBtn.setVisibility(View.VISIBLE);

                addressTV.setVisibility(View.INVISIBLE);
                yourAddressTV.setVisibility(View.VISIBLE);

                helpKindTV.setVisibility(View.VISIBLE);
                helpKindET.setVisibility(View.INVISIBLE);

                descTV.setVisibility(View.VISIBLE);
                descET.setVisibility(View.VISIBLE);

                helpKindTV.setText(R.string.shopping_list);

                break;
            }
            case 3: {
                addBtn.setVisibility(View.VISIBLE);
                addressBtn.setVisibility(View.VISIBLE);

                listBtn.setVisibility(View.INVISIBLE);

                addressTV.setVisibility(View.INVISIBLE);
                yourAddressTV.setVisibility(View.VISIBLE);

                helpKindTV.setVisibility(View.VISIBLE);
                helpKindET.setVisibility(View.VISIBLE);

                descTV.setVisibility(View.VISIBLE);
                descET.setVisibility(View.VISIBLE);

                helpKindTV.setText(R.string.kind_need);
                helpKindET.setHint(R.string.kind_need);
                break;
            }


        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addBtn)
        {
            user = FirebaseAuth.getInstance().getCurrentUser();
            String address,purch,mes,des;
            address = addressTV.getText().toString().trim();
            des = descTV.getText().toString().trim();
            db = FirebaseFirestore.getInstance();
            DocumentReference documentReference = db.collection("tasks").document(user.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put("address", address);
            user.put("description",des);
            user.put("helper"," ");
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddTaskActivity.this, "Udało się zarejestrować", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddTaskActivity.this, e.toString(), Toast.LENGTH_SHORT).show();


                }
            });
       }
        if(view.getId() == R.id.addressBtn) {
            startActivity(new Intent(this, MapActivity.class));



        }

    }


}
