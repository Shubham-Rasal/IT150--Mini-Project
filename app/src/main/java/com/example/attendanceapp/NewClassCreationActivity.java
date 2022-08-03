package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Date;


public class NewClassCreationActivity extends AppCompatActivity {
    public static final String EXTRA="com.example.attendanceapp.extra.ClassObj";
    private int hour_begin,minute_begin,hour_end,minute_end;
    private TextView textView1,textView2;
    private EditText className;
    private Button createClass;
    private String timeBegin,timeEnd,date;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class_creation);
        textView1 = findViewById(R.id.Start);
        textView2 = findViewById(R.id.End);
        className=findViewById(R.id.className);
        createClass=findViewById(R.id.createClass);

        date=String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy",new java.util.Date()));
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewClassCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour_begin = selectedHour;
                        minute_begin = selectedMinute;
                        textView1.setText(String.format("%02d:%02d",hour_begin,minute_begin));
                        timeBegin=String.format("%02d:%02d",hour_begin,minute_begin);

                    }
                }, hour_begin, minute_begin, false);
        timePickerDialog.show();
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewClassCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour_end = selectedHour;
                        minute_end = selectedMinute;
                        textView2.setText(String.format("%02d:%02d",hour_end,minute_end));
                        timeEnd=String.format("%02d:%02d",hour_end,minute_end);
                    }
                }, hour_end, minute_end, false);
                timePickerDialog.show();
            }
        });

        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String class_name=className.getText().toString();
                if(class_name.length()==0)
                    Toast.makeText(NewClassCreationActivity.this, "Please Enter the Class Name", Toast.LENGTH_SHORT).show();
                else{
                    Class c=new Class(class_name,timeBegin,timeEnd,date);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    databaseReference=firebaseDatabase.getReference("Classes");
                    databaseReference.push().setValue(c);
                    Toast.makeText(NewClassCreationActivity.this, "New Class has been Created", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(NewClassCreationActivity.this,TeacherActivity.class);
                    intent.putExtra(EXTRA,(Serializable) c);
                    startActivity(intent);

                }
            }
        });


    }
}