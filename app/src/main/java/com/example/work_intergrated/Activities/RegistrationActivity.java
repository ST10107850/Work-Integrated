package com.example.work_intergrated.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.work_intergrated.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, password;
    ProgressBar progress;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

       // getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progress = findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);
    }
    public void signup(View view){
        progress.setVisibility(View.VISIBLE);
        String username = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPass = password.getText().toString();
        if(TextUtils.isEmpty(username)){
            progress.setVisibility(View.GONE);
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(userEmail)){
            progress.setVisibility(View.GONE);
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPass)){
            progress.setVisibility(View.GONE);
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validate email using Patterns.EMAIL_ADDRESS
        if(TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            progress.setVisibility(View.GONE);
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPass.length() < 6){
            progress.setVisibility(View.GONE);
            Toast.makeText(this, "Password too short, enter minimum of 6 characters!!", Toast.LENGTH_SHORT).show();
        }
        auth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progress.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, "Successfully registers..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        }else{
                            progress.setVisibility(View.GONE);
                            Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
       // return;

    }
    public void signin(View view){
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }
}