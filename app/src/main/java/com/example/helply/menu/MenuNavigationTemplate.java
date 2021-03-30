package com.example.helply.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.R;
import com.example.helply.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuNavigationTemplate extends AppCompatActivity {
    protected Bitmap bitmap;
    protected NavigationView navigationView;
    protected CircleImageView profileImage;
    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    protected FirebaseFirestore db;
    protected FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setProfileImage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("profileImage")
                .child(user.getUid() + ".jpg");

        try {
            final File image = File.createTempFile(user.getUid(), ".jpg");
            storageReference.getFile(image).addOnSuccessListener(taskSnapshot -> {
                Bitmap result = BitmapFactory.decodeFile(image.getAbsolutePath());
                if(profileImage != null) {
                    bitmap = result;
                    profileImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(e -> profileImage.setImageResource(R.drawable.user_default_logo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initSideBarMenu() {
        View.OnClickListener customOnClickListener = v -> {
            switch (v.getId()) {
                case R.id.announcementsCardViewMenu: {
                    startActivity(new Intent(getApplicationContext(), AnnouncementsMainActivity.class));
                    break;
                }
                case R.id.myAnnouncementsCardViewMenu: {
                    startActivity(new Intent(getApplicationContext(), MyAnnouncementsActivity.class));
                    break;
                }
                case R.id.createAnnouncementCardViewMenu: {
                    startActivity(new Intent(getApplicationContext(), CreateAnnouncementActivity.class));
                    break;
                }
                case R.id.tasksToBePerformedCardViewMenu: {
                    startActivity(new Intent(getApplicationContext(), TasksToDoActivity.class));
                    break;
                }

                case R.id.settingsCardViewMenu: {
                    startActivity( new Intent(getApplicationContext(), SettingsActivity.class));
                    break;
                }

                case R.id.bestVolunteersCardViewMenu: {
                    startActivity(new Intent(getApplicationContext(), RankingActivity.class));
                    break;
                }

                case R.id.logOutCardViewMenu: {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                    break;
                }
            }
        };

        CardView menu_element = findViewById(R.id.announcementsCardViewMenu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.myAnnouncementsCardViewMenu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.createAnnouncementCardViewMenu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.tasksToBePerformedCardViewMenu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.settingsCardViewMenu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.bestVolunteersCardViewMenu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.logOutCardViewMenu);
        menu_element.setOnClickListener(customOnClickListener);

    }
}
