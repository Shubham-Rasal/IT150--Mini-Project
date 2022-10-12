//package com.example.attendanceapp;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.content.IntentSender;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.google.android.gms.common.api.ResolvableApiException;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResponse;
//import com.google.android.gms.location.Priority;
//import com.google.android.gms.location.SettingsClient;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private Button StudentLogin,FacultyLogin;
//    private Button reg,new_class,teacherdashboard;
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //asking user to turn on gps
//        buttonSwitchGPS_ON();
//
//        StudentLogin = (Button) findViewById(R.id.studentlog);
//        FacultyLogin = (Button) findViewById(R.id.facultylog);
//        reg = (Button) findViewById(R.id.button);
//
//
//        StudentLogin.setOnClickListener(this);
//        FacultyLogin.setOnClickListener(this);
//        reg.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//
//            case R.id.button:
//                startActivity(new Intent(this,RegistrationActivity.class));
//                break;
//
//            case R.id.facultylog:
//                Intent i = new Intent(this,LoginActivity.class);
//                i.putExtra("type",1);
//                startActivity(i);
//                break;
//            case R.id.studentlog:
//                Intent intent = new Intent(this,LoginActivity.class);
//                intent.putExtra("type",2);
//                startActivity(intent);
//
//                break;
//
//            default:
//                throw new IllegalStateException("Unexpected value: " + v.getId());
//        }
//    }
//    //adding gps turning on function
//    public void buttonSwitchGPS_ON() {
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(10000 / 2);
//
//        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();
//
//        locationSettingsRequestBuilder.addLocationRequest(locationRequest);
//        locationSettingsRequestBuilder.setAlwaysShow(true);
//
//        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
//        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());
//        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//            @Override
//            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                Toast.makeText(getApplicationContext(), "Location settings (GPS) is ON.", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        task.addOnFailureListener(this, new OnFailureListener() {
//            private static final int REQUEST_CHECK_SETTINGS = 1;
//
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Location is turned off!!", Toast.LENGTH_SHORT).show();
//
//                if (e instanceof ResolvableApiException) {
//                    try {
//                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                        resolvableApiException.startResolutionForResult(MainActivity.this,
//                                REQUEST_CHECK_SETTINGS);
//                    } catch (IntentSender.SendIntentException sendIntentException) {
//                        sendIntentException.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//
//}