package com.example.attendanceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button StudentLogin,FacultyLogin;
    private Button reg,new_class;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating Back Button on the action bar



        StudentLogin = (Button) findViewById(R.id.studentlog);
        FacultyLogin = (Button) findViewById(R.id.facultylog);
        reg = (Button) findViewById(R.id.button);
        new_class=findViewById(R.id.button3);

        StudentLogin.setOnClickListener(this);
        FacultyLogin.setOnClickListener(this);
        reg.setOnClickListener(this);
        new_class.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                startActivity(new Intent(this,RegistrationActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(this,NewClassCreationActivity.class));
                break;
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