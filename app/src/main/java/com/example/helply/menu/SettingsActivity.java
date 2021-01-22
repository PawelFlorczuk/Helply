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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helply.Adapter;
import com.example.helply.R;
import com.example.helply.login.LoginActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    private TextView phoneTV;
    private Button phoneBtn;
    private TextView passwordTV;
    private TextView repeatPasswordTV;
    private TextView oldPasswordTV;
    private Button passwordBtn;
    private Button chooseAvatarBtn;
    private ImageView avatar;

    private Bitmap bitmap;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        navigationView = findViewById(R.id.nv_navView);
        drawerLayout = findViewById(R.id.dl_drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        phoneTV = findViewById(R.id.settings_newPhoneNumET);
        passwordTV = findViewById(R.id.settings_newPasswordET);
        repeatPasswordTV = findViewById(R.id.settings_confirmPasswordET);
        oldPasswordTV = findViewById(R.id.settings_currentPasswordET);
        avatar = findViewById(R.id.settings_avatarIV);


        passwordBtn = findViewById(R.id.settings_savePasswordBtn);
        phoneBtn = findViewById(R.id.settings_changePhoneNumBtn);
        chooseAvatarBtn = findViewById(R.id.settings_chooseAvatarBtn);

        passwordBtn.setOnClickListener(this);
        chooseAvatarBtn.setOnClickListener(this);
        phoneBtn.setOnClickListener(this);

        navigationView.bringToFront();
        toolbar.setTitle("Settings");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,(R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        View headerView = navigationView.inflateHeaderView(R.layout.header_deprecated);
        profileImage = (ImageView) headerView.findViewById(R.id.profileImage_deprecated);



        Intent intent = getIntent();
        bitmap = intent.getParcelableExtra("Bitmap");
        setProfileImage(bitmap);


        this.initSideBarMenu();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.tasksItem: {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.lookForTaskItem: {
                        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.myTasksItem: {
                        Intent intent = new Intent(getApplicationContext(), MyTasksActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.settingsItem: {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.logOutItem: {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    }
                    case R.id.rankItem: {
                        Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }
                    case R.id.tasksToDoITem: {
                        Intent intent = new Intent(getApplicationContext(), TasksToDoActivity.class);
                        intent.putExtra("Bitmap", bitmap);
                        startActivity(intent);
                        break;
                    }


                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settings_chooseAvatarBtn:
            {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("progileImage")
                        .child(mAuth.getUid() + ".jpg");


                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Pobierz zdjecie"), 10001);





                break;
            }
            case R.id.settings_changePhoneNumBtn:
            {
                String phoneNumber = phoneTV.getText().toString().trim();
                Map<String, Object> user = new HashMap<>();
                DocumentReference documentReference = db.collection("users").document(mAuth.getUid());
                user.put("phone",phoneNumber);


                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SettingsActivity.this, "Udało się zmienic numer", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();


                    }
                });
                break;
            }
            case R.id.settings_savePasswordBtn:
            {
                mAuth.getCurrentUser();
                String email = user.getEmail();
                String oldPassword = oldPasswordTV.getText().toString().trim();
                String newPassword = passwordTV.getText().toString().trim();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, oldPassword);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(SettingsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(SettingsActivity.this,"Password successfully changed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
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
        if(requestCode == 10001 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, avatar.getWidth(), avatar.getHeight() , true);
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