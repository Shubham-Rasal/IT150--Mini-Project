package com.example.attendanceapp;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    
    
    private Button login;
    private EditText email,password;
    private ProgressBar spinner;


    //initializing firebase auth
    //Auth
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private boolean GpsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //asking user to turn on gps
        buttonSwitchGPS_ON();


        //action bar

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //progress bar;
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);


        login =(Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.password);
        
        login.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login) {
            spinner.setVisibility(View.VISIBLE);
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            login.setVisibility(View.GONE);

            //sending credentials entered to validate to firebase
            mAuth.signInWithEmailAndPassword("q@nitk.edu.in", "abhishek").addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Intent itype = getIntent();
                    int type = itype.getIntExtra("type", 2);
//                        Toast.makeText(LoginActivity.this, ""+type, Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent i;
                    if (type == 1) {
                        i = new Intent(LoginActivity.this, TeacherActivity.class);

                    } else {
                        i = new Intent(LoginActivity.this, ActiveClassActivity.class);

                    }

                    spinner.setVisibility(View.GONE);
                    startActivity(i);
                    finish();


                } else {
                    Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                    email.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                    login.setVisibility(View.VISIBLE);
                }

            });





        }
            
    }
    //adding gps turning on function
    public void buttonSwitchGPS_ON() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();

        locationSettingsRequestBuilder.addLocationRequest(locationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Toast.makeText(getApplicationContext(), "Location settings (GPS) is ON.", Toast.LENGTH_SHORT).show();
                GpsStatus = true;
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            private static final int REQUEST_CHECK_SETTINGS = 1;

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "turned off", Toast.LENGTH_SHORT).show();

                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(LoginActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }
}