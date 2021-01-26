package com.example.helply.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.components.Adapter;
import com.example.helply.menu.MenuNavigationTemplate;
import com.example.helply.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskToDoDetailsActivity extends MenuNavigationTemplate implements View.OnClickListener {
    protected Toolbar toolbar;
    private Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView kindOfHelpTV;
    private TextView addressTV;
    private TextView descriptionTV;
    private TextView emailPhoneNumberTV;
    private TextView needTV;
    private TextView shoppingListTV;
    private TextView dateTV;
    private TextView informactionBreedTV;

    private TextView contactTV;
    private TextView takenTV;

    private CardView contactCV;

    private Button addBtn;
    private FirebaseAuth mAuth;

    private String [] data;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_details);

        contactCV = findViewById(R.id.cv_7);
        contactCV.setVisibility(View.GONE);

        contactTV = findViewById(R.id.contactTV);
        takenTV = findViewById(R.id.takenTV);
        contactTV.setVisibility(View.GONE);
        takenTV.setVisibility(View.GONE);

        kindOfHelpTV = findViewById(R.id.kindHelpTV);
        descriptionTV = findViewById(R.id.desTV);
        addressTV = findViewById(R.id.addressTaskTV);
        emailPhoneNumberTV = findViewById(R.id.emailPhoneTV);
        needTV = findViewById(R.id.dogBreedTV);
        shoppingListTV = findViewById(R.id.shoppingListTV);
        dateTV = findViewById(R.id.dateTV);
        informactionBreedTV = findViewById(R.id.informactionBreadTV);

        addBtn = findViewById(R.id.takeBtn);
        addBtn.setOnClickListener(this);
        addBtn.setVisibility(View.GONE);

        Intent intent = getIntent();
        data = intent.getStringArrayExtra("TaskData");
        kindOfHelpTV.setText(data[5]);
        descriptionTV.setText(data[2]);
        addressTV.setText(data[1]);
        emailPhoneNumberTV.setText(data[4]);
        dateTV.setText(data[0]);
        if(data[5].equals("Shopping")) {
            shoppingListTV.setText(data[6]);
            informactionBreedTV.setText("Shopping list");
            shoppingListTV.setVisibility(View.VISIBLE);
            needTV.setVisibility(View.GONE);
        } else if(data[5].equals("Walking the dog")){
            needTV.setText(data[6]);
            informactionBreedTV.setText("Walking the dog");
            needTV.setVisibility(View.VISIBLE);
            shoppingListTV.setVisibility(View.GONE);
        } else if(data[5].equals("Other")) {
            needTV.setText(data[6]);
            informactionBreedTV.setText("Other");
            needTV.setVisibility(View.VISIBLE);
            shoppingListTV.setVisibility(View.GONE);
        }

        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("My announcements");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(mAuth.getUid().equals(this.data[7].split("-")[0])) {
            addBtn.setVisibility(View.GONE);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();

        this.initSideBarMenu();
        toolbar.setTitleTextColor(Color.BLACK);

        View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
        profileImage = (CircleImageView) headerView.findViewById(R.id.profileImage);

        Intent intent2 = getIntent();
        bitmap = intent2.getParcelableExtra("Bitmap");
        setProfileImage(bitmap);


    }

    @Override
    public void onClick(View v) {

    }
}