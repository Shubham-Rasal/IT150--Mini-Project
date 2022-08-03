package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
=======
import android.content.Intent;
>>>>>>> dev
import android.os.Bundle;

public class TeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
<<<<<<< HEAD
=======
        Intent intent=getIntent();
        Class c=intent.getParcelableExtra(NewClassCreationActivity.EXTRA);
        String date=c.getDate();

>>>>>>> dev
    }
}