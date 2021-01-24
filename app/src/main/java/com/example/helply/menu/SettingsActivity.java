package com.example.helply.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helply.Adapter;
import com.example.helply.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends Navigation implements View.OnClickListener {

    protected Toolbar toolbar;
    private Adapter adapter;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth mAuth;

    private TextView nickTV;
    private Button nickBtn;
    private TextView passwordTV;
    private TextView repeatPasswordTV;
    private TextView oldPasswordTV;
    private Button passwordBtn;
    private Button chooseAvatarBtn;
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
        nickTV = findViewById(R.id.settings_newNickET);
        passwordTV = findViewById(R.id.settings_newPasswordET);
        repeatPasswordTV = findViewById(R.id.settings_confirmPasswordET);
        oldPasswordTV = findViewById(R.id.settings_currentPasswordET);
        avatar = findViewById(R.id.settings_avatarIV);


        repeatPasswordTV = findViewById(R.id.settings_confirmPasswordET);
        passwordBtn = findViewById(R.id.settings_savePasswordBtn);
        nickBtn = findViewById(R.id.settings_changeNickBtn);
        chooseAvatarBtn = findViewById(R.id.settings_chooseAvatarBtn);

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
        profileImage = (CircleImageView) headerView.findViewById(R.id.profileImage);


        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("Bitmap");
        setProfileImage(bitmap);


        this.initSideBarMenu();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.settings_chooseAvatarBtn: {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("progileImage")
                        .child(mAuth.getUid() + ".jpg");


                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,  "Pobierz zdjecie"), 10001);

                break;
            }
            case R.id.settings_changeNickBtn: // todo handle this
            {
                String newLogin = nickTV.getText().toString().trim();
                String oldLogin;
                DocumentReference loginGet = db.collection("users").document(mAuth.getUid());
                loginGet.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            String oldLogin = (String) task.getResult().get("login");
                            if(oldLogin.equals(newLogin)) {
                                Toast.makeText(getApplicationContext(), "New nick can't equals to the old one",Toast.LENGTH_SHORT).show();
                            } else {
                                DocumentReference checkExistsLogin = db.collection("logins").document(newLogin);
                                checkExistsLogin.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.getResult().exists()) {
                                            Toast.makeText(SettingsActivity.this, "This nick already exists", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Map<String, Object> log = new HashMap<>();
                                            DocumentReference newLoginCreate = db.collection("logins").document(newLogin);
                                            newLoginCreate.set(log).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()) {
                                                        Map<String, Object> user = new HashMap<>();
                                                        DocumentReference documentReference = db.collection("users").document(mAuth.getUid());
                                                        user.put("login",newLogin);
                                                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                DocumentReference deleteOldLogin = db.collection("logins").document(oldLogin);
                                                                deleteOldLogin.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()) {
                                                                            Toast.makeText(SettingsActivity.this, "Udało się zmienic numer", Toast.LENGTH_SHORT).show();
                                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                            finish();
                                                                        } else {
                                                                            newLoginCreate.delete();
                                                                            Map<String, Object> user = new HashMap<>();
                                                                            DocumentReference documentReference = db.collection("users").document(mAuth.getUid());
                                                                            user.put("login",oldLogin);
                                                                            documentReference.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                }
                                                                            });
                                                                            Toast.makeText(SettingsActivity.this, "Your old login doesn't exist", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                newLoginCreate.delete();
                                                                Toast.makeText(SettingsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();


                                                            }
                                                        });
                                                    } else {

                                                        Toast.makeText(getApplicationContext(), "This login already exists",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Check your internet connection",Toast.LENGTH_SHORT).show();
                        }
                    }
                });







                break;
            }
            case R.id.settings_savePasswordBtn: {
                mAuth.getCurrentUser();
                String email = user.getEmail();
                String repeatPassword = repeatPasswordTV.getText().toString().trim();
                String oldPassword = oldPasswordTV.getText().toString().trim();
                String newPassword = passwordTV.getText().toString().trim();
                if(oldPassword.equals(newPassword)) {
                    Toast.makeText(SettingsActivity.this, "New and old password can't be equal", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!repeatPassword.equals(newPassword)) {
                    Toast.makeText(SettingsActivity.this, "Password and confirm password are not equal", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (repeatPassword.length() < 6 ) {
                    Toast.makeText(SettingsActivity.this, "Password has to have at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length() < 6 ) {
                    Toast.makeText(SettingsActivity.this, "Password has to have at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, oldPassword);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Authorization failed!", Toast.LENGTH_SHORT).show();
                        }
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

            bitmap = Bitmap.createScaledBitmap(bitmap, avatar.getWidth(), avatar.getHeight(), true);
            avatar.setImageBitmap(bitmap);
            Toast.makeText(SettingsActivity.this, "1", Toast.LENGTH_LONG).show();
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
        reference.putBytes(byteArrayOutputStream.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SettingsActivity.this, "2", Toast.LENGTH_LONG).show();
                getDownUrl(reference);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, "Nie udało", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDownUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(SettingsActivity.this, "3", Toast.LENGTH_LONG).show();
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SettingsActivity.this, "4", Toast.LENGTH_LONG).show();
            }
        });
    }
}