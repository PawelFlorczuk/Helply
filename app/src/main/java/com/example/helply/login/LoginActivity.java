package com.example.helply.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helply.menu.AnnouncementsMainActivity;
import com.example.helply.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView emailET;
    private TextView passwordET;
    private Button loginBtn;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.loginEmailET);
        passwordET = findViewById(R.id.loginPasswordET);
        loginBtn = findViewById(R.id.loginLoginBtn);
        registerBtn = findViewById(R.id.loginSignUpBtn);
        progressBar = findViewById(R.id.progressBar);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.loginLoginBtn:
                login();
                break;
            case R.id.loginSignUpBtn:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
        }
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        String email,password;
        email = emailET.getText().toString().trim();
        password = passwordET.getText().toString().trim();

        if(email.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,"Email field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.equals("")) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,"Password field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 6) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,"Password is less than 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this,"Login successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AnnouncementsMainActivity.class));

                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}