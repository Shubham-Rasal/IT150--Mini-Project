package com.example.attendanceapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class ActiveClassActivity extends AppCompatActivity implements LocationListener {


//    Button authenticate;
    TextView classLabel;
    TextView noClass;
    TextView disText;
    SwipeButton enableButton;
    LocationManager lm;

    boolean GpsStatus =false;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = db.getReference();
    DatabaseReference classRef = db.getReference("Classes");
    DatabaseReference testRef = db.getReference("Students");
    DatabaseReference activeclassRef;
    DataSnapshot activeClass;

    ArrayList<String> pushedStudents = new ArrayList<String>();


    //auth
    FirebaseAuth userAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = userAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_class);
//        authenticate = (Button) findViewById(R.id.authenticate);
        enableButton = (SwipeButton) findViewById(R.id.swipeButton);
        classLabel = (TextView) findViewById(R.id.class_label);
        noClass = (TextView) findViewById(R.id.no_class);
        disText = findViewById(R.id.distance);
//        authenticate.setVisibility(View.GONE);
        enableButton.setVisibility(View.GONE);

        classLabel.setVisibility(View.GONE);


        //adding location manager
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(ActiveClassActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);

            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //getting active classes

        Query query = classRef.orderByChild("active").equalTo("1");



        ValueEventListener valueEventListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(String.valueOf(dataSnapshot.getValue()).equals("null")) {
                    Toast.makeText(ActiveClassActivity.this, "no active class available"+dataSnapshot.getChildren(), Toast.LENGTH_SHORT).show();

                }
                else {
                    noClass.setText("Not within the radius of active class");


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

            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

//                authenticate.setVisibility(View.GONE);
                enableButton.setVisibility(View.GONE);
                String email = currentUser.getEmail();
                String id = currentUser.getUid();

                if(!pushedStudents.contains(email)) {
                    DatabaseReference s = classRef.child(activeclassRef.getKey()).child("PresentStudents");
                    s.child(id).setValue(email);
                    pushedStudents.add(email);
                }




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

        enableButton.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                Toast.makeText(ActiveClassActivity.this, "Active!", Toast.LENGTH_SHORT).show();
                biometricPrompt.authenticate(promptInfo);

            }
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double Lat= location.getLatitude();
        double Long = location.getLongitude();
//        13.007862, 74.795968
        //13.007859, 74.796000
        //aravali
        //13.008011, 74.797227
        //my room 13.008064, 74.795947
        double dis = distance(13.008064, 74.795947,Lat,Long);
        disText.setText(String.valueOf(dis));
        Log.i("distance",String.valueOf(dis));
        if(dis<25){
//            authenticate.setVisibility(View.VISIBLE);
            enableButton.setVisibility(View.VISIBLE);

            classLabel.setVisibility(View.VISIBLE);
            noClass.setVisibility(View.GONE);
            lm.removeUpdates(this);

        }


    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist*1000-3);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }




}