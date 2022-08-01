package com.example.attendanceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button StudentLogin,FacultyLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating Back Button on the action bar



        StudentLogin = (Button) findViewById(R.id.studentlog);
        FacultyLogin = (Button) findViewById(R.id.facultylog);


        StudentLogin.setOnClickListener(this);
        FacultyLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.facultylog:
                Intent i = new Intent(this,LoginActivity.class);
                
                startActivity(i);
                break;
            case R.id.studentlog:
                startActivity(new Intent(this,LoginActivity.class));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}