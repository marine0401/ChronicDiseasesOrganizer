package com.example.miqyasapp1.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.DialogFactory;
import com.example.miqyasapp1.OneButtonDialog;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.BloodPressureMeasurement;
import com.example.miqyasapp1.DatabaseTables.DiabetesMeasurement;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PatientMeasurements extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    Toolbar toolbar;
    public EditText date, SBP, DBP, Glucose;
    public Button saveMeasurement;
    private TextInputLayout sbpLayout, dbpLayout, glucoseLayout;
    public String userFileNumber = "";
    public boolean BP, Diabetes;
    public String dateInput;
    ConstraintLayout DiabetesAndBp;
    LinearLayout KidneyLL;
    TextView KidneyTV;

    //firbase
    FirebaseUser user;
    DatabaseReference referenceUser;

    static DatabaseReference bpRef = FirebaseDatabase.getInstance().getReference("BloodPressure");
    static DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Diabetes");
    static DatabaseReference kRef = FirebaseDatabase.getInstance().getReference("Kidney");
    static DatabaseReference bpMeasurementRef = FirebaseDatabase.getInstance().getReference("BloodPressureMeasurement");
    static DatabaseReference dMeasurementRef = FirebaseDatabase.getInstance().getReference("DiabetesMeasurement");

    public static String bpClassification;
    public static String dClassification;
    public static String kClassification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_measurements);//linking

        init();
        setToolbar();

        //get current date from Calender
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();

            }
        });

        saveMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeasurement();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        //go to patient to store current user file-number
        referenceUser = FirebaseDatabase.getInstance().getReference("patient");
        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("email").getValue().equals(user.getEmail())) {
                        userFileNumber = ds.child("fileNumber").getValue(String.class);
                        Log.d("", "userFileNumber:     " + userFileNumber);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //go to Blood-Pressure table to check if current user classification result
        bpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        bpClassification = ds.child("classification").getValue(String.class);
                        if (bpClassification.equals("1")) {
                            sbpLayout.setVisibility(View.VISIBLE);
                            dbpLayout.setVisibility(View.VISIBLE);
                            BP = true;
                        } else {
                            BP = false;
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

        //go to Diabetes table to check if current user classification result
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        dClassification = ds.child("classification").getValue(String.class);
                        if (dClassification.equals("1")) {
                            glucoseLayout.setVisibility(View.VISIBLE);
                            Diabetes = true;
                        } else {
                            Diabetes = false;
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

        //go to Kidney-Pressure table to check if current user classification result
        kRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        kClassification = ds.child("classification").getValue(String.class);
                        if (kClassification.equals("1")) {
                            DiabetesAndBp.setVisibility(View.GONE);
                            KidneyLL.setVisibility(View.VISIBLE);
                            //show message that user don't need to record daily measurement
                            KidneyTV.setText("حالتك الصحيّة لا تتطلب تسجيل دوري للقياسات" +
                                    "\n\n" +
                                    "دُمت بخير!");
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

        //if both classification result true(blood pressure&diabetes) then the patient diagnosed/have Diabetes an Blood pressure diseases
        //then show the fields of measurements attribute
        if (Diabetes && BP) {
            sbpLayout.setVisibility(View.VISIBLE);
            dbpLayout.setVisibility(View.VISIBLE);
            glucoseLayout.setVisibility(View.VISIBLE);
        }//end of id

    }//end oc OnCreate()

    //date Picker from calender
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PatientMeasurements.this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
        dateInput = month + "/" + dayofmonth + "/" + year;
        date.setText(dateInput);
    }

    //store measurements in required table in database
    public void saveMeasurement() {
        showProgressBar();
        if (BP) {
            BloodPressureMeasurement newMeasurement
                    = new BloodPressureMeasurement();
            newMeasurement.setId(bpMeasurementRef.child("id").push().getKey());
            newMeasurement.setFileNumber(userFileNumber);
            newMeasurement.setSbp(SBP.getText().toString());
            newMeasurement.setDbp(DBP.getText().toString());
            newMeasurement.setMeasurementDate(dateInput);
            bpMeasurementRef.push().setValue(newMeasurement);
        } else if (Diabetes) {
            DiabetesMeasurement newMeasurement
                    = new DiabetesMeasurement();
            newMeasurement.setId(dMeasurementRef.child("id").push().getKey());
            newMeasurement.setFileNumber(userFileNumber);
            newMeasurement.setGlucose(Glucose.getText().toString());
            newMeasurement.setMeasurementDate(dateInput);
            dMeasurementRef.push().setValue(newMeasurement);
        } else if (BP && Diabetes) {
            BloodPressureMeasurement newMeasurement
                    = new BloodPressureMeasurement(bpMeasurementRef.child("id").push().getKey(), userFileNumber,
                    SBP.getText().toString(), DBP.getText().toString(), dateInput);
            bpMeasurementRef.push().setValue(newMeasurement);

            DiabetesMeasurement newMeasurementD
                    = new DiabetesMeasurement(dMeasurementRef.child("id").push().getKey(),
                    userFileNumber, Glucose.getText().toString(), dateInput);
            dMeasurementRef.push().setValue(newMeasurementD);
        } else {
            hideProgressBar();
            return;
        }
        hideProgressBar();
        onBtnSavedClicked();
    }

    public void init(){
        date = (EditText) findViewById(R.id.date);
        SBP = (EditText) findViewById(R.id.SBP);
        DBP = (EditText) findViewById(R.id.DBP);
        Glucose = (EditText) findViewById(R.id.Glucose);
        sbpLayout = (TextInputLayout) findViewById(R.id.SBPtextInputLayout);
        dbpLayout = (TextInputLayout) findViewById(R.id.DBPtextInputLayout);
        glucoseLayout = (TextInputLayout) findViewById(R.id.glucosetextInputLayout);
        saveMeasurement = (Button) findViewById(R.id.save_measurementBtn);

        DiabetesAndBp = (ConstraintLayout) findViewById(R.id.DiabetesAndBpLayout);
        KidneyLL = (LinearLayout) findViewById(R.id.KidneyLayout);
        KidneyTV = (TextView) findViewById(R.id.KidneyLayoutTV);
    }

    public void setToolbar() {
        //support back button on tool bar
        toolbar = (Toolbar) findViewById(R.id.patient_measurement_toolbar);
        toolbar.setTitle("تسجيل القياسات");
        setSupportActionBar(toolbar);

        //setting back button
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//going automatically to the previous page
            }
        });
    }//end of Toolbar

    //Custom Dialog saved successfully!
    public void onBtnSavedClicked() {
        OneButtonDialog oneButtonDialog =
                DialogFactory.makeSuccessDialog(R.string.success_title_save,
                        R.string.success_message,
                        R.string.success_button_text,
                        new OneButtonDialog.ButtonDialogAction() {
                            @Override
                            public void onButtonClicked() {

                            }
                        });
        oneButtonDialog.show(getSupportFragmentManager(), OneButtonDialog.TAG);
    }
}//end of class