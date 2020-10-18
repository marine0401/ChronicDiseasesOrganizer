package com.example.miqyasapp1.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.DialogFactory;
import com.example.miqyasapp1.OneButtonDialog;
import com.example.miqyasapp1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;

public class PatientOfDoctorLifestyle extends BaseActivity {
    Toolbar toolbar;
    TextView lifestyleTV;
    EditText doctorNoteET;
    Button sendNote;
    public String id, userFileNumber, lifestyle;

    //firbase
    DatabaseReference referenceUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_of_doctor_lifestyle);
        ButterKnife.bind(this);

        setToolbar();
        lifestyleTV = (TextView) findViewById(R.id.lifestyle_resultTV);
        doctorNoteET = (EditText) findViewById(R.id.doctorNoteET);
        sendNote = (Button) findViewById(R.id.sendNote);

        //get the lifestyle stored in database of this specific patient
        userFileNumber = getIntent().getExtras().getString("patient_file_number");
        referenceUser = FirebaseDatabase.getInstance().getReference("patient");
        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        id = ds.getKey();
                        lifestyle = ds.child("lifestyle").getValue(String.class);
                        lifestyleTV.setText(lifestyle);
                        Log.d("", "userFileNumber:     " + userFileNumber);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //send doctor not to patient table
        sendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenceUser.child(id).child("doctorNote").setValue(doctorNoteET.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                onBtnSuccessClicked();

                            }
                        });
            }
        });
    }//end of onCreate()

    //Custom Dialog send successfully!
    public void onBtnSuccessClicked() {
        OneButtonDialog oneButtonDialog =
                DialogFactory.makeSuccessDialog(R.string.success_title,
                        R.string.success_message,
                        R.string.success_button_text,
                        new OneButtonDialog.ButtonDialogAction() {
                            @Override
                            public void onButtonClicked() {
                                doctorNoteET.setText("");//clean the field

                            }
                        });
        oneButtonDialog.show(getSupportFragmentManager(), OneButtonDialog.TAG);
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.patientOfDoctor_lifestyle_toolbar);
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
