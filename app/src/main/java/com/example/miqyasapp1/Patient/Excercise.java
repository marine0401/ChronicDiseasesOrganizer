package com.example.miqyasapp1.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.miqyasapp1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Excercise extends AppCompatActivity {
    public TextView resultExercise;
    public Button prev, next;
    ViewFlipper viewFlipperMela_60;
    Toolbar toolbar;
    public String gender;
    public int age;

    //firbase
    FirebaseUser user;
    DatabaseReference referenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise);
        init();
        setToolbar();

        referenceUser = FirebaseDatabase.getInstance().getReference("patient");
        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //for loop to all database columns then check if any email match logged in user email
                    if (ds.child("email").getValue().equals(user.getEmail())) {
                        gender = ds.child("gender").getValue(String.class);//male or female
                        age = Integer.parseInt(ds.child("age").getValue(String.class));
                        if (gender.equals("Female")) {
                            checkFemaleAge(age);
                        } else {
                            checkMaleAge(age);
                        }
                        Log.d("", "gender:     " + gender);
                        Log.d("", "age:     " + age);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }//end of onCreate()

    public void checkFemaleAge(int age) {
        if (age >= 20 && age <= 29) {
            resultExercise.setText(R.string.female_20s);
        } else if (age >= 30 && age <= 39) {
            resultExercise.setText(R.string.female_30s);
        } else if (age >= 40 && age <= 49) {
            resultExercise.setText(R.string.female_40s);
        } else if (age >= 50 && age <= 59) {
            resultExercise.setText(R.string.female_50s);
        } else if (age >= 60 && age <= 69) {
            resultExercise.setText(R.string.female_60s);
        }
    }//end of checkFemaleAge()

    public void checkMaleAge(int age) {
        if (age >= 20 && age <= 29) {
            resultExercise.setText(R.string.male_20s);
        } else if (age >= 30 && age <= 39) {
            resultExercise.setText(R.string.male_30s);
        } else if (age >= 40 && age <= 49) {
            resultExercise.setText(R.string.male_40s);
        } else if (age >= 50 && age <= 59) {
            resultExercise.setText(R.string.male_50s);
        } else if (age >= 60 && age <= 69) {
            viewFlipperMela_60.setVisibility(View.VISIBLE);
            resultExercise.setText(R.string.male_60s);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prev.setVisibility(View.VISIBLE);
                    viewFlipperMela_60.showNext();
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewFlipperMela_60.showPrevious();
                }
            });
        }
    }//end of checkMaleAge()

    public void init(){
        resultExercise = (TextView) findViewById(R.id.exercise_result1);
        viewFlipperMela_60 = (ViewFlipper) findViewById(R.id.viewFlipperMela_60);
        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.prev);
        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.exercise_toolbar);
        setSupportActionBar(toolbar);

        //setting back button
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //setting back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//setting the tool bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);//making it showing

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//going automatically to the previous page
            }
        });
    }
}//end of class
