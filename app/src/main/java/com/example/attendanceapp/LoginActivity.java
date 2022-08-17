package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    
    
    private Button login;
    private EditText email,password;
    private ProgressBar spinner;


    //initializing firebase auth
    //Auth
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        //action bar

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //progress bar;
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);


        login =(Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.password);
        
        login.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login)
        {
            spinner.setVisibility(View.VISIBLE);
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            login.setVisibility(View.GONE);

            //sending credentials entered to validate to firebase
            mAuth.signInWithEmailAndPassword("q@nitk.edu.in","abhishek").addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    Intent itype = getIntent();
                    int type = itype.getIntExtra("type",2);
//                        Toast.makeText(LoginActivity.this, ""+type, Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent i;
                    if(type==1)
                    {
                        i = new Intent(LoginActivity.this, TeacherActivity.class);

                    }
                    else{
                        i = new Intent(LoginActivity.this, ActiveClassActivity.class);

                    }
                    spinner.setVisibility(View.GONE);
                    startActivity(i);
                    finish();


                }
                else {
                    Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                    email.setVisibility(View.VISIBLE);
                    password.setVisibility(View.VISIBLE);
                    login.setVisibility(View.VISIBLE);
                }

            });
           




        }
            
    }
}