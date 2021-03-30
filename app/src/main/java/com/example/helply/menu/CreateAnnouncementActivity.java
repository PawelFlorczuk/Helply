package com.example.helply.menu;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.example.helply.R;
import com.example.helply.popup.MapActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class CreateAnnouncementActivity extends MenuNavigationTemplate implements View.OnClickListener {

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

    private String address;
    private String kindOfHelp;
    private String need;
    private String shoppingList;

    private TextView emailPhoneNumberTV;
    private EditText emailPhoneNumberET;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        spinner = findViewById(R.id.kindOfHelpSpinner);

        addressTV = findViewById(R.id.addressTV);
        yourAddressTV = findViewById(R.id.yourAddressTV);
        helpKindTV = findViewById(R.id.helpKindTV);
        descTV = findViewById(R.id.descTV);

        helpKindET = findViewById(R.id.helpKindET);
        descET = findViewById(R.id.descET);

        addBtn = findViewById(R.id.takeBtn);
        addressBtn = findViewById(R.id.addressBtn);
        listBtn = findViewById(R.id.createListBtn);

        emailPhoneNumberET = findViewById(R.id.emailPhoneNumberET);
        emailPhoneNumberTV = findViewById(R.id.emailPhoneNumberTV);

        navigationView = findViewById(R.id.nv_navView);
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);

        addBtn.setOnClickListener(this);
        addressBtn.setOnClickListener(this);
        listBtn.setOnClickListener(this);

        toolbar.setTitle("Create announcements");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.bringToFront();
        toolbar.setTitleTextColor(Color.DKGRAY);

        profileImage = navigationView.inflateHeaderView(R.layout.sidebar_header).findViewById(R.id.profileImage);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setProfileImage();

        String[] spinnerString = {"Choose type of help","Walking the dog", "Shopping", "Other"};
        spinner.setAdapter(new ArrayAdapter<>(CreateAnnouncementActivity.this,
                android.R.layout.simple_spinner_dropdown_item, spinnerString));

        this.initSideBarMenu();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showForm(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showForm(int position) {
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

                emailPhoneNumberTV.setVisibility(View.INVISIBLE);
                emailPhoneNumberET.setVisibility(View.INVISIBLE);
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

                emailPhoneNumberTV.setVisibility(View.VISIBLE);
                emailPhoneNumberET.setVisibility(View.VISIBLE);

                helpKindTV.setText(R.string.dog_breed);
                helpKindET.setHint(R.string.dog_breed);

                kindOfHelp = "Walking the dog";
                break;
            }
            case 2: {
                addBtn.setVisibility(View.VISIBLE);
                addressBtn.setVisibility(View.VISIBLE);

                listBtn.setVisibility(View.VISIBLE);

                emailPhoneNumberTV.setVisibility(View.VISIBLE);
                emailPhoneNumberET.setVisibility(View.VISIBLE);

                addressTV.setVisibility(View.INVISIBLE);
                yourAddressTV.setVisibility(View.VISIBLE);

                helpKindTV.setVisibility(View.VISIBLE);
                helpKindET.setVisibility(View.INVISIBLE);

                descTV.setVisibility(View.VISIBLE);
                descET.setVisibility(View.VISIBLE);

                helpKindTV.setText(R.string.shopping_list);

                kindOfHelp = "Shopping";
                break;
            }
            case 3: {
                addBtn.setVisibility(View.VISIBLE);
                addressBtn.setVisibility(View.VISIBLE);

                emailPhoneNumberTV.setVisibility(View.VISIBLE);
                emailPhoneNumberET.setVisibility(View.VISIBLE);

                listBtn.setVisibility(View.INVISIBLE);

                addressTV.setVisibility(View.INVISIBLE);
                yourAddressTV.setVisibility(View.VISIBLE);

                helpKindTV.setVisibility(View.VISIBLE);
                helpKindET.setVisibility(View.VISIBLE);

                descTV.setVisibility(View.VISIBLE);
                descET.setVisibility(View.VISIBLE);

                helpKindTV.setText(R.string.kind_need);
                helpKindET.setHint(R.string.kind_need);

                kindOfHelp = "Other";
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Map<String, Object> createMap(String description, String emailPhoneNumber) {
        Map<String, Object> user = new HashMap<>();
        user.put("date", LocalDateTime.now().toString());
        user.put("address", address);
        user.put("description",description);
        user.put("helper"," ");
        user.put("emailPhoneNumber", emailPhoneNumber);
        user.put("kindOfHelp", kindOfHelp);
        user.put("nameOfHelp", need);
        user.put("volunteerContact", " ");
        return user;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.takeBtn)
        {
            String description, emailPhoneNumber;

            emailPhoneNumber = emailPhoneNumberET.getText().toString().trim();
            description = descET.getText().toString();

            if (address == null) {
                Toast.makeText(getApplicationContext(), "You have to fill all the fields" ,Toast.LENGTH_SHORT).show();
                return;
            }

            if (address.equals("") || address.equals(" ")) {
                Toast.makeText(getApplicationContext(), "You have to set your address" ,Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.equals("") || description.equals(" ")) {
                Toast.makeText(getApplicationContext(), "The description cannot be empty" ,Toast.LENGTH_SHORT).show();
                return;
            }
            if (emailPhoneNumber.equals("") || emailPhoneNumber.equals(" ")) {
                Toast.makeText(getApplicationContext(), "The phone number or email field cannot be empty" ,Toast.LENGTH_SHORT).show();
                return;
            }

            switch(this.kindOfHelp) {
                case "Walking the dog":
                {
                    this.need = helpKindET.getText().toString();
                    if (need.equals("") || need.equals(" ")) {
                        Toast.makeText(getApplicationContext(), "The breed of the dog field cannot be empty" ,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                }
                case "Shopping":
                {
                    this.need = shoppingList;
                    if (need.equals("") || need.equals(" ") ) {
                        Toast.makeText(getApplicationContext(), "The list field cannot be empty" ,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                }
                case "Other":
                {
                    this.need = helpKindET.getText().toString();
                    if (need.equals("") || need.equals(" ")) {
                        Toast.makeText(getApplicationContext(), "The type of the help field cannot be empty" ,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                }
            }
            user = FirebaseAuth.getInstance().getCurrentUser();
            db = FirebaseFirestore.getInstance();

            DocumentReference document = (db.collection("tasks").document(user.getUid() + "-1"));
            document.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(!doc.exists()) {
                        Map<String, Object> user = createMap(description, emailPhoneNumber);
                        document.set(user).addOnSuccessListener(aVoid -> {
                            Toast.makeText(CreateAnnouncementActivity.this, "Announcement created!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AnnouncementsMainActivity.class));
                            finish();
                        }).addOnFailureListener(e -> Toast.makeText(CreateAnnouncementActivity.this, e.toString(), Toast.LENGTH_SHORT).show());
                    } else {
                        DocumentReference document1 = (db.collection("tasks").document(user.getUid() + "-2"));
                        document1.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                DocumentSnapshot doc1 = task1.getResult();
                                if(!doc1.exists()) {
                                    Map<String, Object> user = createMap(description, emailPhoneNumber);
                                    document1.set(user).addOnSuccessListener(aVoid -> {
                                        Toast.makeText(CreateAnnouncementActivity.this, "Announcement created!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), AnnouncementsMainActivity.class));
                                        finish();
                                    }).addOnFailureListener(e -> Toast.makeText(CreateAnnouncementActivity.this, e.toString(), Toast.LENGTH_SHORT).show());
                                } else {
                                    Toast.makeText(CreateAnnouncementActivity.this, "You can create only two announcements", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
       }
        if(view.getId() == R.id.addressBtn) {
            startActivityForResult(new Intent(this, MapActivity.class),1001);

        }
        if (view.getId() == R.id.createListBtn) {
            Dialog myDialog;
            myDialog = new Dialog(this);
            myDialog.setContentView(R.layout.custom_pop_up);
            TextView txtClose = myDialog.findViewById(R.id.txtClose);
            txtClose.setText("X");

            TextView shoppingListTV = myDialog.findViewById(R.id.shoppingListEditText);
            txtClose.setOnClickListener(v -> myDialog.dismiss());

            myDialog.findViewById(R.id.saveShoppingList).setOnClickListener(v -> {
                helpKindTV.setText(R.string.change_shopping_list);
                listBtn.setText(R.string.change_list);
                shoppingList = shoppingListTV.getText().toString();
                myDialog.dismiss();
            });

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            SharedPreferences preferences = getSharedPreferences("Address",MODE_PRIVATE);
            String address = preferences.getString("address","Test");
            String[] split = address.split(",");
            String country = split[10].split("=")[1];
            String street =  split[7].split("=")[1];
            String city =  split[6].split("=")[1];
            String number = split[3].split("=")[1];
            String partOfCountry = split[4].split("=")[1];
            String result = country + ", " + partOfCountry + ", " + city + ", "
                    + street  + ", "+  number;

            addressTV.setVisibility(View.VISIBLE);
            addressBtn.setText(R.string.change_address);
            yourAddressTV.setText(result);
            yourAddressTV.setTypeface(null, Typeface.NORMAL);

            this.address = country + "-" + partOfCountry + "-" + city + "-"
                    + street  + "-"+  number;
        }
    }
}
