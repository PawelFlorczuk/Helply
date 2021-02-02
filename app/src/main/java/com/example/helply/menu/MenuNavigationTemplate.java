package com.example.helply.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.R;
import com.example.helply.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
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


    protected void setProfileImage(Bitmap imageBitmap) {
        this.bitmap = imageBitmap;
        if(bitmap == null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("profileImage")
                    .child(user.getUid() + ".jpg");

            try {
                final File image = File.createTempFile(user.getUid(), ".jpg");
                storageReference.getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap result = BitmapFactory.decodeFile(image.getAbsolutePath());
                        if(profileImage != null) {
                            bitmap = result;
                            profileImage.setImageBitmap(bitmap);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();
                        profileImage.setImageResource(R.drawable.user_default_logo);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            profileImage.setImageBitmap(bitmap);
        }
    }

    protected void initSideBarMenu() {
        View.OnClickListener customOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.announcements_card_view_menu: {
                        Intent intent = new Intent(getApplicationContext(), AnnouncementsMainActivity.class);
                      //  intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.my_announcements_card_view_menu: {
                        Intent intent = new Intent(getApplicationContext(), MyAnnouncementsActivity.class);
                     //   intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.create_announcement_card_view_menu: {
                        Intent intent = new Intent(getApplicationContext(), CreateAnnouncementActivity.class);
                      //  intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.tasks_to_be_performed_card_view_menu: {
                        Intent intent = new Intent(getApplicationContext(), TasksToDoActivity.class);
                      //  intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }

                    case R.id.settings_card_view_menu: {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                      //  intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }

                    case R.id.best_volunteers_card_view_menu: {
                        Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                        //intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }

                    case R.id.log_out_card_view_menu: {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        break;
                    }
                }
            }
        };

        CardView menu_element = findViewById(R.id.announcements_card_view_menu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.my_announcements_card_view_menu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.create_announcement_card_view_menu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.tasks_to_be_performed_card_view_menu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.settings_card_view_menu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.best_volunteers_card_view_menu);
        menu_element.setOnClickListener(customOnClickListener);

        menu_element = findViewById(R.id.log_out_card_view_menu);
        menu_element.setOnClickListener(customOnClickListener);

    }
}
