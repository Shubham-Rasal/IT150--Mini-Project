package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;

public class RegistrationActivity extends AppCompatActivity {
    EditText name,password,confirmPassword,email;
    Button registerButton;
    FirebaseDatabase Firebase;
    DatabaseReference studentRef;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email=findViewById(R.id.Email);
        name=findViewById(R.id.Name);
        password=findViewById(R.id.Password);
        confirmPassword=findViewById(R.id.ConfirmPassword);
        registerButton=findViewById(R.id.Register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL=email.getText().toString();
                String NAME=name.getText().toString();
                String PASSWORD=password.getText().toString();
                String CONFIRM_PASSWORD=confirmPassword.getText().toString();
                if(PASSWORD.equals(CONFIRM_PASSWORD) && EMAIL.contains("nitk.edu.in")) {
                    mAuth =FirebaseAuth.getInstance();

                    mAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
//                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        updateUI(user);
                                        Student s = new Student(NAME, EMAIL, PASSWORD);
                                        Firebase = FirebaseDatabase.getInstance();
                                        studentRef = Firebase.getReference("Students");
                                        studentRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(s)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                    Toast.makeText(RegistrationActivity.this, "Registered Successfully!!", Toast.LENGTH_SHORT).show();
                                                                else
                                                                    Toast.makeText(RegistrationActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });

//                                        student_databaseReference.push().setValue(s);

                                    } else {
                                        if(PASSWORD.length()<6){
                                            Toast.makeText(RegistrationActivity.this, "The password should have at least 6 characters", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(RegistrationActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });


                }
                else {
                    if(!EMAIL.contains("nitk.edu.in")){
                        Toast.makeText(RegistrationActivity.this, "Please enter the NITK Email ID", Toast.LENGTH_SHORT).show();
                    }
                    else if(!PASSWORD.equals(CONFIRM_PASSWORD)){
                        Toast.makeText(RegistrationActivity.this, "Passwords don't match!!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
}