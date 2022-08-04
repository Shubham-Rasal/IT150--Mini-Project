package com.example.attendanceapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherActivity extends AppCompatActivity {
    TextView classDate;
    ImageView cross;
    CardView cardView;
    Button StartNewClassButton;

    ActivityResultLauncher<Intent> activityLauncher =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==1){
                        Intent i=result.getData();
                        if(i != null){
                            StartNewClassButton.setVisibility(View.INVISIBLE);
                            Class c = (Class) i.getSerializableExtra(NewClassCreationActivity.EXTRA);
                            cardView.setVisibility(View.VISIBLE);
                            String date = c.getDate();
                            String className=c.getName();
                            classDate.setText(className+"   "+date);
                            Toast.makeText(TeacherActivity.this, "Students can now confirm their attendance", Toast.LENGTH_SHORT).show();
                            cross.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cardView.setVisibility(View.GONE);
                                    StartNewClassButton.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
//        Intent i=getIntent();
        StartNewClassButton=findViewById(R.id.StartNewClassButton);
        classDate = findViewById(R.id.ClassDate);
        cross = findViewById(R.id.cross);
        cardView=findViewById(R.id.cardView);
        cardView.setVisibility(View.INVISIBLE);

        StartNewClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_sent=new Intent(TeacherActivity.this,NewClassCreationActivity.class);
//                startActivity(intent_sent);
                activityLauncher.launch(intent_sent);

            }
        });



    }
}