package com.example.attendanceapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class TeacherActivity extends AppCompatActivity {
    private TextView classDate;
    private ImageView cross;
    private CardView cardView;
    private Button StartNewClassButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference classRef;
//    private int totalNumber=80;


    private ListView listView;
    private ListView presentStudents;
    private HashSet<String> pStudents;
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
                                    classRef.child(id).child("active").setValue("0");

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
        listView = findViewById(R.id.listView1);

        firebaseDatabase = FirebaseDatabase.getInstance();
        classRef = firebaseDatabase.getReference("Classes");
        className=new ArrayList<>();


        storeCorrespondingKeys=new ArrayList<>();
//        MyAdapter myAdapter = new MyAdapter(TeacherActivity.this, R.layout.cardview, className);

        classRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                    Class c = classSnapshot.getValue(Class.class);
                    assert c != null;
                    if ((c.getActive()).equals("1")) {
                        if(!storeCorrespondingKeys.contains(classSnapshot.getKey())) {
                            className.add(c.getName());
                        }
                    storeCorrespondingKeys.add(classSnapshot.getKey());
                    }
                }
                MyAdapter myAdapter = new MyAdapter(TeacherActivity.this, R.layout.cardview, className);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(TeacherActivity.this, ""+id, Toast.LENGTH_SHORT).show();
                        onButtonShowPopupWindowClick(view,snapshot);
                    }
                });
                listView.setAdapter(myAdapter);
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

    public void onButtonShowPopupWindowClick(View view,DataSnapshot snapshot) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);




        // create the popup window
        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 10, 0);
        TextView popupText = (TextView) popupView.findViewById(R.id.popup_title);
        presentStudents = (ListView) popupView.findViewById(R.id.present_list);

        //Array adapter for student list
        String temp[] = {"dhfkjd","Shubham","Abhishek"};




        popupText.setText("title");

        //Getting present students
        Query pQuery = classRef.orderByChild("active").equalTo("1");
        ValueEventListener studentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot studensSnap : snapshot.getChildren())
                {
                    Toast.makeText(TeacherActivity.this,"from here"+studensSnap.child("students_present"), Toast.LENGTH_LONG).show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        pQuery.addValueEventListener(studentListener);
        ArrayAdapter studentAdapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item,);
        presentStudents.setAdapter(studentAdapter);




        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}