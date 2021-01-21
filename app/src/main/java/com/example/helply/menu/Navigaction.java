package com.example.helply.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Navigaction extends AppCompatActivity {
    protected Bitmap bitmap;
    protected NavigationView navigationView;
    protected ImageView profileImage;
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
                        profileImage.setImageResource(R.drawable.logo);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            profileImage.setImageBitmap(bitmap);
        }
    }
}
