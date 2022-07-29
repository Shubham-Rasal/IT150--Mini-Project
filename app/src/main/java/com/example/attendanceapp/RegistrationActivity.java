package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    EditText name,password,confirmPassword,email;
    Button registerButton;
    FirebaseDatabase Firebase;
    DatabaseReference student_databaseReference;
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
                if(PASSWORD.equals(CONFIRM_PASSWORD)) {
                    Student s = new Student(NAME, EMAIL, PASSWORD);
                    Firebase = FirebaseDatabase.getInstance();
                    student_databaseReference = Firebase.getReference("Students");
                    student_databaseReference.push().setValue(s);
                    Toast.makeText(RegistrationActivity.this, "Registered!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Passwords don't match!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}