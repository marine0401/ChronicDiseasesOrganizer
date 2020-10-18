package com.example.miqyasapp1.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.miqyasapp1.R;

public class PatientLifestyle extends AppCompatActivity {
    Toolbar toolbar;
    ImageView food, exercise, generalTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_lifestyle);
        setToolbar();

        //linking
        food = (ImageView) findViewById(R.id.food_card);
        exercise = (ImageView) findViewById(R.id.exercise_card);
        //generalTips = (ImageView) findViewById(R.id.generaltips_card);

        //onclick cards
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientLifestyle.this, Food.class));
            }
        });

        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientLifestyle.this, Excercise.class));
            }
        });
//        generalTips.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //go to general tips
//                //startActivity(new Intent(this,GeneralTips.class));
//            }
//        });


    }//end of onCreate()

    public void setToolbar() {
        toolbar = findViewById(R.id.patient_lifestyle_toolbar);
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
    }//end of setToolbar

}//end of class
