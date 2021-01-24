package com.example.helply.login;

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

import com.example.helply.menu.MainActivity;
import com.example.helply.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView emailET;
    private TextView loginET;
    private TextView passwordET;
    private TextView repeatedPasswordET;
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
        emailET = findViewById(R.id.emailET);
        loginET = findViewById(R.id.loginET);
        passwordET = findViewById(R.id.passwordET);
        repeatedPasswordET = findViewById(R.id.repeatedPasswordET);
        termsOfUseCheckBox = findViewById(R.id.termsOfUseChckBox);
        signInBtn = findViewById(R.id.signInBtn);
        registerBtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.registerProgressBar);
        registerBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();



    }



    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.signInBtn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.registerBtn:
                register();
                break;
        }
    }

    public void register() {
        progressBar.setVisibility(View.VISIBLE);
        String email, password, login, repeatPassword;
        email = emailET.getText().toString().trim();
        login = loginET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        repeatPassword = repeatedPasswordET.getText().toString().trim();

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
            Toast.makeText(RegistrationActivity.this,"Password is less than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!termsOfUseCheckBox.isChecked()){
            Toast.makeText(RegistrationActivity.this,"You have to accept the rules", Toast.LENGTH_SHORT).show();
            return;
        }

      
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DocumentReference documentReference = db.collection("users").document(mAuth.getUid());
                    Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("login", login);
                        user.put("points",0);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DocumentReference documentReference2 = db.collection("logins").document(login);
                                documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();
                                        if(doc.exists()) {
                                            documentReference.delete();
                                            Toast.makeText(getApplicationContext(),"This login is already taken", Toast.LENGTH_LONG);

                                        } else {
                                            documentReference2.set(new HashMap<>()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                        }
                                    }
                                });
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegistrationActivity.this, "Authorization succeed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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