package com.example.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference studentsReference,classReference;
    private int count=0;
    private ArrayList<String> Names;
    private ArrayList<Integer> numberOfPresentClasses;
    private ListView studentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        firebaseDatabase=FirebaseDatabase.getInstance();
        studentListView=findViewById(R.id.studentListView);

        studentsReference=firebaseDatabase.getReference("Students");
        classReference=firebaseDatabase.getReference("Classes");

        Names=new ArrayList<>();
        numberOfPresentClasses=new ArrayList<>();

        getClassCount();
        fillArrayList();




//        Toast.makeText(this, Names.get(0), Toast.LENGTH_SHORT).show();
//        if(Names==null)
//            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
//        else{
//            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
//        }

//        StudentViewAdapter studentViewAdapter=new StudentViewAdapter(this,R.layout.cardview2,Names,numberOfPresentClasses,count);
//        ArrayAdapter<String> av=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,Names);
//        studentListView.setAdapter(av);



//        ArrayList<String> i=new ArrayList<>();
//        i.add("1");
//        i.add("1");
//        i.add("1");
//        Names.add("Abhishek Satpathy");
//        Toast.makeText(this, "A"+Names.size(), Toast.LENGTH_SHORT).show();
//        ArrayAdapter<String> av=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,Names);
//        studentListView.setAdapter(av);

//        ListView lv=findViewById(R.id.listView1);
//        lv.setAdapter(studentViewAdapter);







    }
    public void getClassCount(){
        classReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Toast.makeText(StudentListActivity.this, "in", Toast.LENGTH_SHORT).show();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Toast.makeText(StudentListActivity.this, String.valueOf(dataSnapshot.child("name").getValue()), Toast.LENGTH_SHORT).show();
                    count+=1;
                }
//                Toast.makeText(StudentListActivity.this, ""+count, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentListActivity.this, "Unable to count total classes", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void fillArrayList(){
        studentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Toast.makeText(StudentListActivity.this,String.valueOf(dataSnapshot.child("name").getValue()), Toast.LENGTH_SHORT).show();
//                    Student studentObject=dataSnapshot.getValue(Student.class);
//                    String name=;
//                    Names.add(studentObject.getName());
//                    numberOfPresentClasses.add(Integer.parseInt(studentObject.getNumberOfClasses()));
//                    Toast.makeText(StudentListActivity.this, name, Toast.LENGTH_SHORT).show();
                    Names.add(String.valueOf(dataSnapshot.child("name").getValue()));
                    numberOfPresentClasses.add(Integer.parseInt(String.valueOf(dataSnapshot.child("numberOfClasses").getValue())));
//                    Toast.makeText(StudentListActivity.this,(String.valueOf(dataSnapshot.child("numberOfClasses").getValue())), Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(StudentListActivity.this,""+ Names.size(), Toast.LENGTH_SHORT).show();
//                for(String s:Names){
//                    Toast.makeText(StudentListActivity.this, s, Toast.LENGTH_SHORT).show();
////                    Toast.makeText(this, "hmm", Toast.LENGTH_SHORT).show();
//                }
//                ArrayAdapter<String> av=new ArrayAdapter<String>(StudentListActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,Names);
                if(count!=0) {
                    StudentViewAdapter studentViewAdapter = new StudentViewAdapter(StudentListActivity.this, R.layout.cardview2, Names, numberOfPresentClasses, count);
                    studentListView.setAdapter(studentViewAdapter);
//                    Toast.makeText(StudentListActivity.this, "A" + Names.size(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(StudentListActivity.this, "No classes conducted yet!!", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentListActivity.this, "Unable to get a access the Students branch", Toast.LENGTH_SHORT).show();
            }
        });
    }
}