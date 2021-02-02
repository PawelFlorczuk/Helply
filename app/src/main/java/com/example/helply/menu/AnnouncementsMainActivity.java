package com.example.helply.menu;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helply.components.Adapter;
import com.example.helply.R;
import com.example.helply.login.LoginActivity;
import com.example.helply.popup.ChoosePartPopUpWindow;
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

public class AnnouncementsMainActivity extends MenuNavigationTemplate {

    protected Toolbar toolbar;
    protected RecyclerView recyclerView;
    private Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseFirestore db;
    private Vector<String[]> datalist;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private ImageView refreshView;
    private ImageView searchView;

    private String partOfCountry;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            super.onCreate(savedInstanceState);
            user = FirebaseAuth.getInstance().getCurrentUser();
            mAuth = FirebaseAuth.getInstance();
            setContentView(R.layout.activity_announcements_main);
            datalist = new Vector<String[]>();
            navigationView = findViewById(R.id.nv_navView);
            navigationView.bringToFront();
            progressBar = findViewById(R.id.mainProgressBar);
            progressBar.setVisibility(View.VISIBLE);
            drawerLayout = findViewById(R.id.dl_drawer_layout);
            recyclerView = findViewById(R.id.recycledView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            toolbar = findViewById(R.id.toolBar);
            partOfCountry = " ";
            refreshView = findViewById(R.id.refreshView);
            searchView = findViewById(R.id.searchPartOfCountryView);


            refreshView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    partOfCountry = " ";
                    refresh();
                }
            });
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(AnnouncementsMainActivity.this, ChoosePartPopUpWindow.class),1010);
//                    Dialog myDialog = new Dialog(getApplicationContext());
//                    myDialog.setContentView(R.layout.activity_choose_part_pop_up_window);
//                     Spinner chooseSpinner;
//                     Button saveBtn;
//
//                    chooseSpinner = findViewById(R.id.choosePopUpWindowSpinner);
//
//                    String[] spinnerString = {"Choose your province","Dolnośląskie","Kujawsko-Pomorskie","Lubelskie",
//                            "Lubuskie","Łódzkie", "Małopolskie", "Mazowieckie","Opolskie","Podkarpackie","Podlaskie",
//                            "Pomorskie","Śląskie","Świętokrzyskie","Warmińsko-Mazurskie","Wielkopolskie","Zachodniopomorskie"};
//
//                    chooseSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),
//                            android.R.layout.simple_spinner_dropdown_item, spinnerString));
//                    saveBtn = findViewById(R.id.savePartOfTheCountryBtn);
//
//                    Button finalSaveBtn = saveBtn;
//                    chooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            if(position != 0) {
//                                partOfCountry = spinnerString[position];
//                                finalSaveBtn.setEnabled(true);
//                            } else {
//                                finalSaveBtn.setEnabled(false);
//                            }
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//
//                    saveBtn = findViewById(R.id.savePartOfTheCountryBtn);
//
//                    saveBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            refresh();
//                            myDialog.dismiss();
//                        }
//                    });
//
//
//                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    myDialog.show();


                }
            });

            toolbar.setTitleTextColor(Color.DKGRAY);



            toolbar.setTitle("Announcements");
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            ImageView searchView = findViewById(R.id.searchPartOfCountryView);
            ImageView refreshView = findViewById(R.id.refreshView);
            refreshView.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);

            View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
            profileImage = (CircleImageView) headerView.findViewById(R.id.profileImage);

            Intent intent = getIntent();
            bitmap = intent.getParcelableExtra("Bitmap");
            setProfileImage(bitmap);

            initSideBarMenu();
            refresh();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1113){
            SharedPreferences preferences = getSharedPreferences("Province",MODE_PRIVATE);
            this.partOfCountry = preferences.getString("province"," ");
            refresh();
        }


    }

    public void refresh() {
        if (mAuth.getUid() != null) {

            db = FirebaseFirestore.getInstance();
            com.google.android.gms.tasks.Task<QuerySnapshot> documentReference = db.collection("tasks").orderBy("date", Query.Direction.DESCENDING).get();
            documentReference.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    datalist = new Vector<String[]>();
                    List<DocumentSnapshot> list = task.getResult().getDocuments();
                    int i = 0;
                    if (partOfCountry.equals(" ")) {
                        for (DocumentSnapshot doc : list) {
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
                            i++;
                        }
                    } else {
                        for (DocumentSnapshot doc : list) {
                            if (doc.get("helper").toString().equals(" ") && doc.get("address").toString().split("-")[1].equals(partOfCountry)) {
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
                            i++;
                        }
                    }


                    adapter = new Adapter(AnnouncementsMainActivity.this, datalist, 0,bitmap);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }

            });
        }
    }


}
