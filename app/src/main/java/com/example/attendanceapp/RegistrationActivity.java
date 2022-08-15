package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText name,password,confirmPassword,email;
    Button registerButton;
    FirebaseDatabase Firebase;
    DatabaseReference referenceToAddUser;
    FirebaseAuth mAuth;
    Boolean isTeacher = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email=findViewById(R.id.Email);
        name=findViewById(R.id.Name);
        password=findViewById(R.id.Password);
        confirmPassword=findViewById(R.id.ConfirmPassword);
        registerButton=findViewById(R.id.Register);
        isTeacher = false;

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);





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

                                            Student s = new Student(NAME, EMAIL, PASSWORD);
                                            Firebase = FirebaseDatabase.getInstance();
                                            if(isTeacher)
                                            referenceToAddUser = Firebase.getReference("Teachers");
                                            else
                                                referenceToAddUser = Firebase.getReference("Students");


                                        referenceToAddUser.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(s)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                                Toast.makeText(RegistrationActivity.this, "Registered Successfully!!", Toast.LENGTH_SHORT).show();
                                                            else
                                                                Toast.makeText(RegistrationActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });



                                    }
                                    else{
                                            if (PASSWORD.length() < 6) {
                                                Toast.makeText(RegistrationActivity.this, "The password should have at least 6 characters", Toast.LENGTH_SHORT).show();
                                            } else {
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




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(String.valueOf(parent.getItemAtPosition(position)).equals("Teacher")) isTeacher = true;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}