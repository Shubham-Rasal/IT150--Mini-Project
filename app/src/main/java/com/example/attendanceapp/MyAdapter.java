package com.example.attendanceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter {
    ArrayList<String> arr;
    ArrayList<String> Date;
    ArrayList<String> presentStudentCount;
    Context context;
    public MyAdapter(@NonNull Context context, int resource, ArrayList<String> arr,ArrayList<String> Date,ArrayList<String> presentStudentCount) {
        super(context,resource,arr);
        this.arr= arr;
        this.Date=Date;
        this.presentStudentCount=presentStudentCount;
        this.context=context;
    }
    @NonNull
    @Override
    public String getItem(int position){
        return arr.get(position)+"       "+Date.get(position);
    }
    @NonNull
    public String getNumberOfPresentStudents(int position){
        return presentStudentCount.get(position);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view= LayoutInflater.from(getContext()).inflate(R.layout.cardview,parent,false);
        TextView classDate=view.findViewById(R.id.classDate);
        TextView fractionOfStudents=view.findViewById(R.id.fractionOfStudents);
        classDate.setText(getItem(position));
        fractionOfStudents.setText(getNumberOfPresentStudents(position)+"/"+80);


        return view;
    }
}
