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
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.components.Adapter;
import com.example.helply.R;
import com.example.helply.menu.MyAnnouncementsActivity;
import com.example.helply.menu.MenuNavigationTemplate;
import com.example.helply.popup.FinishTaskPopUpWindow;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAnnouncementDetailsActivity extends MenuNavigationTemplate implements View.OnClickListener {
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

    private Button addBtn;
    private FirebaseAuth mAuth;

    private String[] taskData;
    private long points;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_details);
        contactTV = findViewById(R.id.contactTV);
        takenTV = findViewById(R.id.takenTV);

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

        Intent intent = getIntent();
        taskData = intent.getStringArrayExtra("TaskData");
        kindOfHelpTV.setText(taskData[5]);
        descriptionTV.setText(taskData[2]);
        addressTV.setText(taskData[1]);
        emailPhoneNumberTV.setText(taskData[4]);
        dateTV.setText(taskData[0]);
        if (taskData[5].equals("Shopping")) {
            shoppingListTV.setText(taskData[6]);
            informactionBreedTV.setText("Shopping list");
            shoppingListTV.setVisibility(View.VISIBLE);
            needTV.setVisibility(View.GONE);
        } else if (taskData[5].equals("Walking the dog")) {
            needTV.setText(taskData[6]);
            informactionBreedTV.setText("Walking the dog");
            needTV.setVisibility(View.VISIBLE);
            shoppingListTV.setVisibility(View.GONE);
        } else if (taskData[5].equals("Other")) {
            needTV.setText(taskData[6]);
            informactionBreedTV.setText("Other");
            needTV.setVisibility(View.VISIBLE);
            shoppingListTV.setVisibility(View.GONE);
        }

        if (!taskData[8].equals(" ")) {
            contactTV.setText(taskData[8]);
            takenTV.setText("Accepted");
            addBtn.setText("Finish announcement");
            addBtn.setVisibility(View.VISIBLE);

        } else {
            addBtn.setVisibility(View.GONE);
        }


        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.nv_navView);
        navigationView.bringToFront();
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("My tasks");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


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
        if (view.getId() == R.id.takeBtn) {
            if (!mAuth.getUid().equals(taskData[3])) {
                startActivityForResult(new Intent(this, FinishTaskPopUpWindow.class), 1005);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1112) {
            SharedPreferences preferences = getSharedPreferences("PointAndDescription", MODE_PRIVATE);
            String point = preferences.getString("point", "0");
            String userOpinion = preferences.getString("opinion", " ");
            if (!mAuth.getUid().equals(taskData[3])) {
                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("tasks").document(taskData[7]);
                if (true) {//checkBox.isChecked()) {
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

                                        DocumentSnapshot doc = task.getResult();
                                        points = (long) doc.get("points");
                                        if (point.equals("1")) {
                                            points++;
                                        }
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("points", points);
                                        documentReference.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
//                                                        DocumentReference opinions = db.collection("users").document(taskData[7].split("-")[0]).collection("opinions").document(mAuth.getUid());
//                                                    DocumentReference opinions = db.collection("users").document(uid).collection("opinions").document(mAuth.getUid());
                                                    CollectionReference opinions = db.collection("users").document(uid).collection("opinions");
                                                    HashMap<String, Object> opinion = new HashMap<>();
                                                    opinion.put("opinion", userOpinion);
//                                                    opinions.set(opinion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    opinions.add(opinion).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentReference delete = db.collection("tasks").document(taskData[7]);
                                                                delete.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(MyAnnouncementDetailsActivity.this, "Successfully task is finished", Toast.LENGTH_SHORT).show();
                                                                            startActivity(new Intent(getApplicationContext(), MyAnnouncementsActivity.class));
                                                                        } else {
                                                                            Toast.makeText(MyAnnouncementDetailsActivity.this, "Finishing task is unsuccessful!", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });
                                                            } else {
                                                                Map<String, Object> unsuccesfulUser = new HashMap<>();
                                                                unsuccesfulUser.put("points", points);
                                                                documentReference.update(unsuccesfulUser);
                                                                Toast.makeText(MyAnnouncementDetailsActivity.this, "Finishing task is unsuccessful!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                    });
                                                } else {
                                                    Toast.makeText(MyAnnouncementDetailsActivity.this, "Finishing task is unsuccessful!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {

                                db.collection("tasks").document(taskData[7]).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MyAnnouncementDetailsActivity.this, "Finishing task is unsuccessful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MyAnnouncementsActivity.class));
                                    }
                                });
                            }


                        }
                    });
                } else {
                    db.collection("tasks").document(taskData[7]).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MyAnnouncementDetailsActivity.this, "Finishing task is unsuccessful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MyAnnouncementsActivity.class));
                        }
                    });
                }


            }
        }


    }

}