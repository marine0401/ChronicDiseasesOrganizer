package com.example.miqyasapp1.Doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.DialogFactory;
import com.example.miqyasapp1.Patient.FirebaseDatabaseHelperPatient;
import com.example.miqyasapp1.OneButtonDialog;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.ButterKnife;

public class PatientOfDoctor extends BaseActivity {

    Toolbar toolbar;
    private TextView fileNumberTV, nameTV, ageTV, genderTV, bmiTV, classificationTV, lifestyleTV;
    private Button buttonLifestyle, buttonDeletePatient;

    private String key;
    public String fileNumber, name, age, gender, bmi, classification, lifestyle, classificationResult;

    DatabaseReference bpRef = FirebaseDatabase.getInstance().getReference("BloodPressure");
    DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Diabetes");
    DatabaseReference kRef = FirebaseDatabase.getInstance().getReference("Kidney");

    public String bpClassification;
    public String dClassification;
    public String kClassification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_of_doctor);
        ButterKnife.bind(this);

        init();
        setToolbar();

        //get the classification of patient added in doctor's list in(BloodPressure,Diabetes,Kidney)tables
        bpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(fileNumber)) {
                        bpClassification = ds.child("classification").getValue(String.class);
                        if (bpClassification.equals("1")) {
                            classificationResult = "مصاب بضغط الدم";
                            classificationTV.setText(classificationResult);
                        }
                        Log.d("", "bpClassification:     " + bpClassification);
                    } else {
                        Log.d("", "bpClassification:     null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(fileNumber)) {
                        dClassification = ds.child("classification").getValue(String.class);
                        if (dClassification.equals("1")) {
                            classificationResult = "مصاب بالسكر";
                            classificationTV.setText(classificationResult);
                            Log.d("Classification Result: ", classificationResult);
                        }
                        Log.d("", "dClassification:     " + dClassification);
                    } else {
                        Log.d("", "dClassification:     null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        kRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(fileNumber)) {
                        kClassification = ds.child("classification").getValue(String.class);
                        if (kClassification.equals("1")) {
                            classificationResult = "مصاب بالكلى";
                            classificationTV.setText(classificationResult);
                        }
                        Log.d("", "kClassification:     " + kClassification);
                    } else {
                        Log.d("", "kClassification:     null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get the result to the TextView
        fileNumberTV.setText(fileNumber);
        nameTV.setText(name);
        ageTV.setText(age);
        genderTV.setText(gender);
        bmiTV.setText(bmi);
        if (classificationTV.getText().toString().isEmpty()) {
            classificationResult = "غير معروف";
            classificationTV.setText(classificationResult);
        }

        buttonLifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientOfDoctor.this, PatientOfDoctorLifestyle.class);
                intent.putExtra("patient_file_number", fileNumberTV.getText().toString());
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String id = sharedPreferences.getString("doctor_id", "default");
        buttonDeletePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelperPatient().deletePatientFromList(id, key, new FirebaseDatabaseHelperPatient.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Patient> patients, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        onBtnSuccessClicked();
                    }
                });
            }
        });

    }//end of onCreate

    //    //Custom Dialog send successfully!
//    @OnClick(R.id.buttonDeletePatient)
    public void onBtnSuccessClicked() {
        OneButtonDialog oneButtonDialog =
                DialogFactory.makeSuccessDialog(R.string.success_title,
                        R.string.success_message,
                        R.string.success_button_text,
                        new OneButtonDialog.ButtonDialogAction() {
                            @Override
                            public void onButtonClicked() {

                            }
                        });
        oneButtonDialog.show(getSupportFragmentManager(), OneButtonDialog.TAG);
    }

    public void init() {
        key = getIntent().getStringExtra("key");
        fileNumber = getIntent().getStringExtra("fileNumber");
        name = getIntent().getStringExtra("name");
        age = getIntent().getStringExtra("age");
        gender = getIntent().getStringExtra("gender");
        bmi = getIntent().getStringExtra("bmi");
        //
        lifestyle = getIntent().getStringExtra("lifestyle");


        fileNumberTV = (TextView) findViewById(R.id.textView_fileNum);
        nameTV = (TextView) findViewById(R.id.textView_name);
        ageTV = (TextView) findViewById(R.id.textView_age);
        genderTV = (TextView) findViewById(R.id.textView_gender);
        bmiTV = (TextView) findViewById(R.id.textView_bmi);
        classificationTV = (TextView) findViewById(R.id.textView_classification);
        lifestyleTV = (TextView) findViewById(R.id.editText_phone);

        buttonLifestyle = (Button) findViewById(R.id.buttonLifestyle);
        buttonDeletePatient = (Button) findViewById(R.id.buttonDeletePatient);
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.patientOfDoctor_toolbar);
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
    }//end of toolbar()
}//end of class

