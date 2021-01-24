package com.example.helply;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import com.example.helply.menu.MainActivity;
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

    private TextView kindOfHelpTV;
    private TextView addressTV;
    private TextView descriptionTV;
    private TextView emailPhoneNumberTV;
    private TextView needTV;
    private TextView shoppingListTV;
    private TextView dateTV;
    private TextView informactionBreedTV;

    private Button addBtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private String [] data;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_desc);

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
        } else if(data[5].equals("Walking the dog")){
            needTV.setText(data[6]);
            informactionBreedTV.setText("Walking the dog");
        } else if(data[5].equals("Other")) {
            needTV.setText(data[6]);
            informactionBreedTV.setText("Other");
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
        if(view.getId() == R.id.addBtn) {

            if(!mAuth.getUid().equals(data[7].split("-")[0])) {

                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("tasks").document(data[7]);
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
