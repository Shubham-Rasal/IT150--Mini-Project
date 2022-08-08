package com.example.attendanceapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherActivity extends AppCompatActivity {
    private TextView classDate;
    private ImageView cross;
    private CardView cardView;
    private Button StartNewClassButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
//    private int totalNumber=80;


    private ListView listView;
    private ArrayList<String> className;
    private ArrayList<String> storeCorrespondingKeys;


    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 1) {
                        Intent i = result.getData();
                        if (i != null) {
                            StartNewClassButton.setVisibility(View.INVISIBLE);
                            Class c = (Class) i.getSerializableExtra(NewClassCreationActivity.EXTRA);
                            String id = i.getStringExtra("ID");
                            cardView.setVisibility(View.VISIBLE);
                            String date = c.getDate();
                            String className = c.getName();
                            classDate.setText(className + "   " + date);
                            Toast.makeText(TeacherActivity.this, "Students can now confirm their attendance", Toast.LENGTH_SHORT).show();
                            cross.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cardView.setVisibility(View.GONE);
                                    StartNewClassButton.setVisibility(View.VISIBLE);
                                    databaseReference.child(id).child("active").setValue("0");

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
        StartNewClassButton = findViewById(R.id.StartNewClassButton);
        classDate = findViewById(R.id.ClassDate);
        cross = findViewById(R.id.cross);
        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.INVISIBLE);
        listView = findViewById(R.id.listView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Classes");
        className=new ArrayList<>();
        storeCorrespondingKeys=new ArrayList<>();
        MyAdapter myAdapter = new MyAdapter(TeacherActivity.this, R.layout.cardview, className);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                    Class c = classSnapshot.getValue(Class.class);
                    if ((c.getActive()).equals("0")) {
//                        Toast.makeText(TeacherActivity.this,classSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        if(!storeCorrespondingKeys.contains(classSnapshot.getKey()))
                            className.add(c.getName());

                    }
                    storeCorrespondingKeys.add(classSnapshot.getKey());
                }
                listView.setAdapter(myAdapter);

//                Toast.makeText(TeacherActivity.this,"Err!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TeacherActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
            }
        });


        StartNewClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_sent = new Intent(TeacherActivity.this, NewClassCreationActivity.class);
                activityLauncher.launch(intent_sent);

            }
        });


    }
}