package com.example.attendanceapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class ActiveClassActivity extends AppCompatActivity {


    Button authenticate ;
    TextView classLabel;
    TextView noClass;
    FirebaseDatabase db  = FirebaseDatabase.getInstance();
    DatabaseReference classRef = db.getReference("Classes");
    DatabaseReference testRef = db.getReference("Students");
    DatabaseReference activeclassRef;
    DataSnapshot activeClass;




    //auth
    FirebaseAuth userAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = userAuth.getCurrentUser();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_class);
        authenticate = (Button) findViewById(R.id.authenticate);
        classLabel = (TextView)findViewById(R.id.class_label);
        noClass = (TextView) findViewById(R.id.no_class);
        authenticate.setVisibility(View.GONE);
        classLabel.setVisibility(View.GONE);


        //getting active classes
        Query query = classRef.orderByChild("active").equalTo("1");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(String.valueOf(dataSnapshot.getValue())=="null") {
                    Toast.makeText(ActiveClassActivity.this, "no active class available"+dataSnapshot.getChildren(), Toast.LENGTH_SHORT).show();

                }
                else {
                    authenticate.setVisibility(View.VISIBLE);
                    classLabel.setVisibility(View.VISIBLE);
                    noClass.setVisibility(View.GONE);


                    Log.d("Active classes", String.valueOf(dataSnapshot));
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        activeclassRef = postSnapshot.getRef();
                        activeClass = postSnapshot;
                        Toast.makeText(ActiveClassActivity.this, "" + String.valueOf(postSnapshot.child("name").getValue()), Toast.LENGTH_SHORT).show();
                        classLabel.setText(String.valueOf(postSnapshot.child("name").getValue()));

                        break;
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase error",String.valueOf(databaseError));

            }
        };

        query.addValueEventListener(valueEventListener);


        //Adding biometrics auth
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(ActiveClassActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,

                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();

                String email = currentUser.getEmail();
                String id = currentUser.getUid();

                testRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());


                        } else {
                            Log.d("firebase", String.valueOf(task.getResult().child("name").getValue()));
                            //adding student to class object
                            String name =  String.valueOf(task.getResult().child("name").getValue());
                            activeclassRef.child("students_present").child(currentUser.getUid()).setValue(name)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                                Toast.makeText(ActiveClassActivity.this, "Added student successfully", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(ActiveClassActivity.this, "Error in adding the student", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });










            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                authenticate.setVisibility(View.GONE);


            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();


            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();
        authenticate.setOnClickListener(v -> {

            //calling the authenticate method
            biometricPrompt.authenticate(promptInfo);

        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = userAuth.getCurrentUser();
        if(currentUser == null){
            Toast.makeText(this, "No current user", Toast.LENGTH_SHORT).show();

        }
    }
}