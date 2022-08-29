package com.example.attendanceapp;

import static android.media.CamcorderProfile.get;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.ArrayList;

public class StudentViewAdapter extends ArrayAdapter {
//    private HashMap<String,Integer> presentStudentMap=new HashMap<>();
     ArrayList<String> Names;
     ArrayList<Integer> numberOfPresentClasses;
     int count;
    Context context;
    public StudentViewAdapter(@NonNull Context context, int resource, ArrayList<String> Names,ArrayList<Integer> numberOfPresentClasses,int count) {
        super(context,resource,Names);
        this.Names=Names;
        this.numberOfPresentClasses=numberOfPresentClasses;
        this.count=count;
        this.context=context;
    }
    @NonNull
    @Override
    public String getItem(int position){
        return Names.get(position);
    }

    public int getNumberOfPresentClasses(int position){
        return numberOfPresentClasses.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view= LayoutInflater.from(getContext()).inflate(R.layout.cardview2,parent,false);
        TextView studentName=view.findViewById(R.id.studentName);
        TextView attendancePercentage=view.findViewById(R.id.attendancePercentage);


//        attendancePercentage.setText(String.format("%0.2lf",String.valueOf(percentage)));
        studentName.setText(getItem(position));
        int percentage=getNumberOfPresentClasses(position)*100;
        attendancePercentage.setText(percentage/count+"%");
        return view;
    }
}
