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
    private ImageView studentList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference classRef;
    public int totalNumber=80;


    private ListView listView;
    private ListView presentStudents;
    private ArrayList<String> className;
    private HashSet<String> pStudents;
    private ArrayList<String> date=new ArrayList<>();
    private ArrayList<String> presentStudentsCount=new ArrayList<>();
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
                            studentList.setVisibility(View.INVISIBLE);
                            Class c = (Class) i.getSerializableExtra(NewClassCreationActivity.EXTRA);
                            String id = i.getStringExtra("ID");
                            cardView.setVisibility(View.VISIBLE);
                            String date = c.getDate();
                            String className = c.getName();
                            classDate.setText(className + "         " + date);
                            Toast.makeText(TeacherActivity.this, "Students can now confirm their attendance", Toast.LENGTH_SHORT).show();
                            cross.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cardView.setVisibility(View.GONE);
                                    StartNewClassButton.setVisibility(View.VISIBLE);
                                    studentList.setVisibility(View.VISIBLE);
                                    classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            Toast.makeText(TeacherActivity.this, "hmm", Toast.LENGTH_SHORT).show();
                                            for(DataSnapshot ds:snapshot.getChildren()){
//                                                Toast.makeText(TeacherActivity.this,"yeah: "+String.valueOf(ds.child("active").getValue()), Toast.LENGTH_SHORT).show();
                                                if(String.valueOf(ds.child("active").getValue()).equals("1")){
//                                                    Toast.makeText(TeacherActivity.this, "step2", Toast.LENGTH_SHORT).show();
                                                    int c=0;
//                                                    (ds.child("PresentStudents").getChildren()).size();
//                                                    Toast.makeText(TeacherActivity.this, ds.child("PresentStudents"), Toast.LENGTH_SHORT).show();
                                                    for(DataSnapshot i:ds.child("PresentStudents").getChildren()){
//                                                        Toast.makeText(TeacherActivity.this, "c=: "+c, Toast.LENGTH_SHORT).show();
                                                        c++;
//                                                        Toast.makeText(TeacherActivity.this, String.valueOf(i.getValue()), Toast.LENGTH_SHORT).show();

                                                    }
//                                                    Toast.makeText(TeacherActivity.this, "Count: "+c, Toast.LENGTH_SHORT).show();
                                                    ds.getRef().child("presentStudent").setValue(String.valueOf(c));
                                                }
                                            }
                                            classRef.child(id).child("active").setValue("0");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(TeacherActivity.this, "Error!!", Toast.LENGTH_SHORT).show();

                                        }
                                    });


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
        studentList=findViewById(R.id.studentList);
        studentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeacherActivity.this,StudentListActivity.class));
            }
        });

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
                    if ((c.getActive()).equals("0")) {
                        if(!storeCorrespondingKeys.contains(classSnapshot.getKey())) {
                            className.add(0,c.getName());
                            date.add(0,c.getDate());
                            presentStudentsCount.add(0,c.getPresentStudent());
                        }
                        storeCorrespondingKeys.add(0,classSnapshot.getKey());
                    }
                }
                if(storeCorrespondingKeys.size()!=0) {
                    MyAdapter myAdapter = new MyAdapter(TeacherActivity.this, R.layout.cardview, className, date, presentStudentsCount);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                                Class c = classSnapshot.getValue(Class.class);
                                assert c != null;
                                if(c.getName().equals(parent.getItemAtPosition(position))){
                                    onButtonShowPopupWindowClick(view, c);


                                }



                            }
                        }
                    });
                    listView.setAdapter(myAdapter);
                }
                else
                    Toast.makeText(TeacherActivity.this, "No classes", Toast.LENGTH_SHORT).show();
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

    public void onButtonShowPopupWindowClick(View view, Class item) {

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
        ArrayList<String> pStudents = new ArrayList<>();




        popupText.setText(item.getBegin() +"-"+item.getEnd());



        //Getting present students
        DatabaseReference pClassRef = classRef.child("");
        Query pQuery = pClassRef.orderByChild("name").equalTo(String.valueOf(item.getName()));
        ValueEventListener ps = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot PresentStudents = null;
                for(DataSnapshot ps: snapshot.getChildren()){

                    Log.d("pstude",String.valueOf(ps));
                    PresentStudents =ps.child("PresentStudents");
                    break;

                }

                Log.d("Reprent",String.valueOf(PresentStudents.getValue()));
                for (DataSnapshot d : PresentStudents.getChildren()){
                    pStudents.add(String.valueOf(d.getValue()));

                }

                ArrayAdapter studentAdapter = new ArrayAdapter(TeacherActivity.this, android.R.layout.simple_selectable_list_item,pStudents);
                presentStudents.setAdapter(studentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error",String.valueOf(error));

            }
        };
        pQuery.addListenerForSingleValueEvent(ps);








        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}