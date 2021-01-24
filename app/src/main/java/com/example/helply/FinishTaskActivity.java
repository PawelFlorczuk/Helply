package com.example.helply;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.menu.FinishTaskPopUpWindow;
import com.example.helply.menu.MyTasksActivity;
import com.example.helply.menu.Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FinishTaskActivity extends Navigation implements View.OnClickListener {
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

    private String [] data;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_desc);
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

        if(!data[8].equals(" ")) {
            contactTV.setText(data[8]);
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
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
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
        if (view.getId() == R.id.addBtn) {
            if (!mAuth.getUid().equals(Data.Helper)) {
                startActivityForResult(new Intent(this, FinishTaskPopUpWindow.class),1005);
            }


    }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1112) {
            SharedPreferences preferences = getSharedPreferences("PointAndDescription",MODE_PRIVATE);
            String point = preferences.getString("point","0");
            String description = preferences.getString("description"," ");

            if (!mAuth.getUid().equals(Data.Helper)) {
                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("tasks").document(Data.ID);
                if (true){//checkBox.isChecked()) {
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