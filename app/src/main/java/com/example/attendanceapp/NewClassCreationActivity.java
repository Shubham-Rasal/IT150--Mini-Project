package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NewClassCreationActivity extends AppCompatActivity {
    private TimePicker timePicker_begin;
    private int hour,minute;
    private TextView textView1,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_class_creation);
        textView1 = findViewById(R.id.Start);
//        begin = findViewById(R.id.button4);
        textView2 = findViewById(R.id.End);
        Date date=new java.util.Date();
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewClassCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        textView1.setText(String.format("%02d:%02d",hour,minute));
                    }
                }, hour, minute, false);
        timePickerDialog.show();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewClassCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        textView2.setText(String.format("%02d:%02d",hour,minute));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

    }
}