package com.example.helply.details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.components.Adapter;
import com.example.helply.R;
import com.example.helply.menu.AnnouncementsMainActivity;
import com.example.helply.menu.MenuNavigationTemplate;
import com.example.helply.popup.TaskPopUpWindow;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class AnnouncementDetailsActivity extends MenuNavigationTemplate implements View.OnClickListener {
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

        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

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
        toolbar.setTitle("My tasks");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(mAuth.getUid().equals(this.data[7].split("-")[0])) {
            addBtn.setVisibility(View.GONE);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();

        this.initSideBarMenu();


        View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
        profileImage = (CircleImageView) headerView.findViewById(R.id.profileImage);

        Intent intent2 = getIntent();
        bitmap = intent2.getParcelableExtra("Bitmap");
        setProfileImage(bitmap);



    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addBtn) {
            if(!mAuth.getUid().equals(this.data[7].split("-")[0])) {
                startActivityForResult(new Intent(this, TaskPopUpWindow.class), 1003);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1111){

            SharedPreferences preferences = getSharedPreferences("VolunteerContact",MODE_PRIVATE);
            String contact = preferences.getString("volunteer_contact","Contact");
            if(contact == null || contact.equals("") || contact.equals(" ") || contact.equals("Contact")) {
                Toast.makeText(AnnouncementDetailsActivity.this, "The contact can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!mAuth.getUid().equals(this.data[7].split("-")[0])) {

                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("tasks").document(this.data[7]);
                Map<String, Object> user = new HashMap<>();

                user.put("helper",mAuth.getUid());
                user.put("volunteerContact",contact);

                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AnnouncementDetailsActivity.this, "Udało się zarejestrować", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AnnouncementsMainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AnnouncementDetailsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();


                    }
                });
            } else{
                Toast.makeText(AnnouncementDetailsActivity.this, "e.toString()", Toast.LENGTH_SHORT).show();
            }
        }

        }


    }


