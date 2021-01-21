package com.example.helply;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView emailET;
    private TextView passwordET;
    private TextView confirmPasswordET;
    private TextView phoneNumberET;
    private CheckBox termsOfUseCheckBox;
    private Button signInBtn;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.register_emailET);
        passwordET = findViewById(R.id.register_passwordET);
        confirmPasswordET = findViewById(R.id.register_confirmPasswordET);
        phoneNumberET = findViewById(R.id.register_phoneNumberET);
        termsOfUseCheckBox = findViewById(R.id.register_termsOfUseChckBox);
        signInBtn = findViewById(R.id.register_signInBtn);
        registerBtn = findViewById(R.id.register_registerBtn);
        progressBar = findViewById(R.id.progressBar);
        registerBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();

    }



    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.register_signInBtn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.register_registerBtn:
                register();
                break;
        }
    }

    public void register() {
        progressBar.setVisibility(View.VISIBLE);
        String email, password, phoneNumber, repeatPassword;
        email = emailET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        repeatPassword = confirmPasswordET.getText().toString().trim();
        phoneNumber = phoneNumberET.getText().toString().trim();

        if(!password.equals(repeatPassword)) {
            Toast.makeText(RegistrationActivity.this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
            return;
        }
        if(email.equals("")) {
            Toast.makeText(RegistrationActivity.this,"Email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.equals("")) {
            Toast.makeText(RegistrationActivity.this,"Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 6) {
            Toast.makeText(RegistrationActivity.this,"Pasword is less than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phoneNumber.length() != 9){
            Toast.makeText(RegistrationActivity.this,"Phone number have to have 9 numbers", Toast.LENGTH_SHORT).show();
            return;
        }



        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DocumentReference documentReference = db.collection("users").document(mAuth.getUid());
                    Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("phone", phoneNumber);
                        user.put("points",0);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegistrationActivity.this, "Authorization succeed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainAcivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                                user.reauthenticate(credential)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                user.delete();
                                            }
                                        });
                                Toast.makeText(RegistrationActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegistrationActivity.this, "Authorization failed" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}