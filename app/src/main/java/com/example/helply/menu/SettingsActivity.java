package com.example.helply.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.helply.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends MenuNavigationTemplate implements View.OnClickListener {

    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth mAuth;

    private Button nickBtn;
    private Button passwordBtn;
    private Button chooseAvatarBtn;

    private TextView passwordTV;
    private TextView repeatPasswordTV;
    private TextView oldPasswordTV;
    private TextView nickTV;

    private CircleImageView avatar;

    private Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        navigationView = findViewById(R.id.nv_navView);
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        nickTV = findViewById(R.id.settingsNewNickET);
        passwordTV = findViewById(R.id.settingsNewPasswordET);
        repeatPasswordTV = findViewById(R.id.settingsConfirmPasswordET);
        oldPasswordTV = findViewById(R.id.settingsCurrentPasswordET);
        avatar = findViewById(R.id.settingsAvatarIV);
        toolbar.setTitleTextColor(Color.DKGRAY);

        repeatPasswordTV = findViewById(R.id.settingsConfirmPasswordET);
        passwordBtn = findViewById(R.id.settingsSavePasswordBtn);
        nickBtn = findViewById(R.id.settingsChangeNickBtn);
        chooseAvatarBtn = findViewById(R.id.settingsChooseAvatarBtn);

        passwordBtn.setOnClickListener(this);
        chooseAvatarBtn.setOnClickListener(this);
        nickBtn.setOnClickListener(this);

        navigationView.bringToFront();
        toolbar.setTitle("Settings");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, (R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        View headerView = navigationView.inflateHeaderView(R.layout.sidebar_header);
        profileImage = headerView.findViewById(R.id.profileImage);

        setProfileImage();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("profileImage")
                .child(user.getUid() + ".jpg");

        try {
            final File image = File.createTempFile(user.getUid(), ".jpg");
            storageReference.getFile(image).addOnSuccessListener(taskSnapshot -> {
                Bitmap result = BitmapFactory.decodeFile(image.getAbsolutePath());
                if (avatar != null) {
                    bitmap = result;
                    avatar.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(e -> avatar.setImageResource(R.drawable.user_default_logo));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.initSideBarMenu();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settingsChooseAvatarBtn: {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("progileImage")
                        .child(mAuth.getUid() + ".jpg");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Load your picture"), 10001);
                break;
            }
            case R.id.settingsChangeNickBtn: {
                String newLogin = nickTV.getText().toString().trim();
                DocumentReference loginGet = db.collection("users").document(Objects.requireNonNull(mAuth.getUid()));
                loginGet.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String oldLogin = (String) task.getResult().get("login");
                        if (oldLogin.equals(newLogin)) {
                            Toast.makeText(getApplicationContext(), "New nickname cannot equal to the old one", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DocumentReference checkExistsLogin = db.collection("logins").document(newLogin);
                        checkExistsLogin.get().addOnCompleteListener(existingLogin -> {
                            if (existingLogin.getResult().exists()) {
                                Toast.makeText(SettingsActivity.this, "This nickname already exists", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            DocumentReference createNewLogin = db.collection("logins").document(newLogin);
                            createNewLogin.set(new HashMap<>()).addOnCompleteListener(addingNewLoginToUsersCollection -> {
                                if (addingNewLoginToUsersCollection.isSuccessful()) {

                                    DocumentReference documentReference = db.collection("users").document(mAuth.getUid());
                                    Map<String, Object> user = Map.of("login", newLogin);
                                    documentReference.update(user).addOnSuccessListener(aVoid -> {

                                        DocumentReference deleteOldLogin = db.collection("logins").document(oldLogin);
                                        deleteOldLogin.delete().addOnCompleteListener(deletingOldLoginFromLoginsCollection -> {
                                            if (deletingOldLoginFromLoginsCollection.isSuccessful()) {
                                                Toast.makeText(SettingsActivity.this, "You changed your nickname successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), AnnouncementsMainActivity.class));
                                                finish();

                                            } else {
                                                createNewLogin.delete();
                                                DocumentReference changingNewPasswordToOld = db.collection("users").document(mAuth.getUid());
                                                Map<String, Object> userLogin = Map.of("login", oldLogin);
                                                changingNewPasswordToOld.update(userLogin);
                                                Toast.makeText(SettingsActivity.this, "Your old nickname does not exist", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }).addOnFailureListener(e -> {
                                        createNewLogin.delete();
                                        Toast.makeText(SettingsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "This login already exists", Toast.LENGTH_SHORT).show();

                                }
                            });
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.settingsSavePasswordBtn: {
                mAuth.getCurrentUser();
                String email = user.getEmail();
                String repeatPassword = repeatPasswordTV.getText().toString().trim();
                String oldPassword = oldPasswordTV.getText().toString().trim();
                String newPassword = passwordTV.getText().toString().trim();
                if (oldPassword.equals(newPassword)) {
                    Toast.makeText(SettingsActivity.this, "New and old password can't be equal", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!repeatPassword.equals(newPassword)) {
                    Toast.makeText(SettingsActivity.this, "Password and confirm password are not equal", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (repeatPassword.length() < 6) {
                    Toast.makeText(SettingsActivity.this, "Password has to have at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, oldPassword);
                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(changePassword -> {
                            if (!changePassword.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, changePassword.getException().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingsActivity.this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), AnnouncementsMainActivity.class));
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(SettingsActivity.this, "Authorization failed!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int k = height / avatar.getHeight();
            width = (int) (width / (k * 1.5));
            height = (int) (avatar.getHeight() / 1.5);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            avatar.setImageBitmap(bitmap);
            handleUpload(bitmap);
        }
    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImage")
                .child(uid + ".jpg");
        reference.putBytes(byteArrayOutputStream.toByteArray()).addOnSuccessListener(taskSnapshot -> getDownUrl(reference)).addOnFailureListener(e ->
                Toast.makeText(SettingsActivity.this, "Adding picture failed", Toast.LENGTH_LONG).show());
    }

    private void getDownUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(uri -> setUserProfileUrl(uri));
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        user.updateProfile(request).addOnSuccessListener(aVoid -> Toast.makeText(SettingsActivity.this,
                "Picture added successfully", Toast.LENGTH_LONG).show());
    }
}