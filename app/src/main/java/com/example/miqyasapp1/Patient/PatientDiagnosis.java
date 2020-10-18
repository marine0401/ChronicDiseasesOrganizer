package com.example.miqyasapp1.Patient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.miqyasapp1.R;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class PatientDiagnosis extends AppCompatActivity {
    Toolbar toolbar;
    Button lifestyleBtn;

    //firebase
    FirebaseUser user;
    DatabaseReference referenceUser;

    public static DatabaseReference bpRef = FirebaseDatabase.getInstance().getReference("BloodPressure");
    public static DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Diabetes");
    public static DatabaseReference kRef = FirebaseDatabase.getInstance().getReference("Kidney");

    public static DatabaseReference bpMRef = FirebaseDatabase.getInstance().getReference("BloodPressureMeasurement");
    public static DatabaseReference dMRef = FirebaseDatabase.getInstance().getReference("DiabetesMeasurement");
    public static String bpClassification, dClassification, kClassification, measurementDate, glucose, sbp, dbp;

    public String userFileNumber = "";
    private ProgressBar DiabetesProgressBar, KidneyProgressBar, HypertensionProgressBar;
    private TextView sugerResult, hypertensionResult, kidneyResult;

    BarChart chart;
    ArrayList<String> glucoseList;
    ArrayList<String> sbpList;
    ArrayList<String> dbpList;
    ArrayList<String> dateList;

    BarDataSet Bardataset;
    BarData BARDATA;
    ArrayList<BarEntry> BARENTRY;// ArrayList barEntries;
    ArrayList<String> BarEntryLabels;

    BarDataSet BardatasetSBP;
    BarDataSet BardatasetDBP;
    BarData BARDATAbp;
    ArrayList<BarEntry> BARENTRY2;
    ArrayList<BarEntry> BARENTRYbp;// ArrayList barEntries;
    ArrayList<String> BarEntryLabelsBp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_diagnosis);
        init();
        setToolbar();

        //get current user id and fileNumber
        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUser = FirebaseDatabase.getInstance().getReference("patient");
        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (Objects.equals(ds.child("email").getValue(), user.getEmail())) {
                        userFileNumber = ds.child("fileNumber").getValue(String.class);
                        Log.d("", "userFileNumber:     " + userFileNumber);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //check if current user diagnosis with BP, then get (classification Result, SBP, DBP) to check it in generating suitable diet.
        bpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        bpClassification = ds.child("classification").getValue(String.class);
                        checkBPClassification(bpClassification);
                        BPDailyMeasurements();
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

        //check if current user diagnosis with Diabetes, then get (classification Result, glucose) to check it in generating suitable diet.
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        dClassification = ds.child("classification").getValue(String.class);
                        checkDiabetesClassification(dClassification);
                        DiabetesDailyMeasurements();
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

        //check if current user diagnosis with Kidney, then get (classification Result, potassium) to check it in generating suitable diet.
        kRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        kClassification = ds.child("classification").getValue(String.class);
                        checkKidneyClassification(kClassification);
                        chart.setVisibility(View.GONE);
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

        lifestyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientDiagnosis.this, PatientLifestyle.class));
            }
        });

    }//end of onCreate()

    public void init() {
        BARENTRY = new ArrayList<>();
        BARENTRY2 = new ArrayList<>();
        BARENTRYbp = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        BarEntryLabelsBp = new ArrayList<String>();

        glucoseList = new ArrayList<String>();
        sbpList = new ArrayList<String>();
        dbpList = new ArrayList<String>();
        dateList = new ArrayList<>();

        chart = (BarChart) findViewById(R.id.chart);
        DiabetesProgressBar = (ProgressBar) findViewById(R.id.DiabetesBar);
        HypertensionProgressBar = (ProgressBar) findViewById(R.id.HypertensionBar);
        KidneyProgressBar = (ProgressBar) findViewById(R.id.KidneyBar);
        sugerResult = (TextView) findViewById(R.id.suger_result);
        hypertensionResult = (TextView) findViewById(R.id.hypertension_result);
        kidneyResult = (TextView) findViewById(R.id.kidney_result);
        lifestyleBtn = (Button) findViewById(R.id.goToLifestyleBT);
    }

    public void checkBPClassification(String hypertensionClassificationResult) {
        // Hypertension Bar
        if (hypertensionClassificationResult.equals("0")) {
            HypertensionProgressBar.setProgress(1);
            HypertensionProgressBar.getProgressDrawable().setColorFilter(
                    Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
            hypertensionResult.setText("انت غير مصاب بارتفاع ضغط الدم");
        } else if (hypertensionClassificationResult.equals("1")) {
            HypertensionProgressBar.setProgress(100);
            HypertensionProgressBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            hypertensionResult.setText("انت مصاب بارتفاع ضغط الدم");

        } else if (hypertensionClassificationResult.equals("2")) {
            HypertensionProgressBar.setProgress(50);
            HypertensionProgressBar.getProgressDrawable().setColorFilter(
                    Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
            hypertensionResult.setText("انت مصاب بارتفاع متوسط في ضغط الدم");
        }
    }//end of checkBPClassification()

    public void checkDiabetesClassification(String diabetesClassificationResult) {
        // Diabetes bar
        if (diabetesClassificationResult.equals("0")) {
            DiabetesProgressBar.setProgress(1);
            DiabetesProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            sugerResult.setText("انت غير مصاب بالسكري");
        } else if (diabetesClassificationResult.equals("1")) {
            DiabetesProgressBar.setProgress(100);
            DiabetesProgressBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            sugerResult.setText("انت مصاب بالسكري");
        }
    }//end of checkDiabetesClassification()

    public void checkKidneyClassification(String kidneyClassificationResult) {
        // Kidney Bar
        if (kidneyClassificationResult.equals("0")) {
            KidneyProgressBar.setProgress(100);
            KidneyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            kidneyResult.setText("انت غير مصاب بأمراض الكلى");
        } else if (kidneyClassificationResult.equals("1")) {
            KidneyProgressBar.setProgress(100);
            KidneyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            kidneyResult.setText("انت مصاب بأمراض الكلى");
        }
    }//end of checkKidneyClassification()

    public void BPDailyMeasurements() {
        bpMRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dateList.clear();
                sbpList.clear();
                dbpList.clear();
                BARENTRYbp.clear();
                BarEntryLabelsBp.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        measurementDate = ds.child("measurementDate").getValue(String.class);
                        dateList.add(measurementDate);
                        sbp = ds.child("sbp").getValue(String.class);
                        sbpList.add(sbp);
                        dbp = ds.child("dbp").getValue(String.class);
                        dbpList.add(dbp);
                        Log.d("", "date:     " + measurementDate + ", glucose: " + glucose);

                    }
                }
                for (int i = 0; i < sbpList.size(); i++) {
                    BARENTRYbp.add(new BarEntry(Float.parseFloat(sbpList.get(i)), i));//y is glucose value
                }
                BardatasetSBP = new BarDataSet(BARENTRYbp, "SBP: red");
                for (int i = 0; i < dbpList.size(); i++) {
                    BARENTRY2.add(new BarEntry(Float.parseFloat(dbpList.get(i)), i));//y is glucose value
                }
                BardatasetDBP = new BarDataSet(BARENTRY2, "DBP: Orange");
                for (int i = 0; i < dateList.size(); i++) {
                    BarEntryLabelsBp.add(dateList.get(i));
                    Log.d("", "dateOrder:     " + dateList.get(i));
                }
                BARDATAbp = new BarData(BarEntryLabelsBp, BardatasetSBP);
                BARDATAbp.addDataSet(BardatasetDBP);
                chart.setData(BARDATAbp); // set the data and list of labels into chart
                chart.setDescription("تمثيل بياني للقياسات اليومية ( ضغط الدم)");
                chart.setScaleEnabled(true);
                chart.setDescriptionTextSize(18);
                BardatasetSBP.setColors(Collections.singletonList(getColor(R.color.red)));
                BardatasetDBP.setColors(Collections.singletonList(getColor(R.color.orange)));
                chart.animateY(4000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//end of DiabetesDailyMeasurement()

    public void DiabetesDailyMeasurements() {
        dMRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dateList.clear();
                glucoseList.clear();
                BARENTRY.clear();
                BarEntryLabels.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        measurementDate = ds.child("measurementDate").getValue(String.class);
                        dateList.add(measurementDate);
                        glucose = ds.child("glucose").getValue(String.class);
                        glucoseList.add(glucose);
                        Log.d("", "date:     " + measurementDate + ", glucose: " + glucose);
                    }
                }
                Log.d("", "date:     " + measurementDate + ", glucose: " + glucose);
                for (int i = 0; i < glucoseList.size(); i++) {
                    BARENTRY.add(new BarEntry(Float.parseFloat(glucoseList.get(i)), i));//y is glucose value
                }
                Bardataset = new BarDataSet(BARENTRY, "Glucose");
                for (int i = 0; i < dateList.size(); i++) {
                    BarEntryLabels.add(dateList.get(i));
                    Log.d("", "dateOrder:     " + dateList.get(i));
                }
                BARDATA = new BarData(BarEntryLabels, Bardataset);
                chart.setData(BARDATA); // set the data and list of labels into chart
                chart.setDescription("تمثيل بياني للقياسات اليومية (السكري)");
                Bardataset.setColors(ColorTemplate.LIBERTY_COLORS);
                chart.setDescriptionTextSize(18);
                chart.animateY(4000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//end of DiabetesDailyMeasurement()

    public void setToolbar() {
        toolbar = findViewById(R.id.patient_diagnosis_toolbar);
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