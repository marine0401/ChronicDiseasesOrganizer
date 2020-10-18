package com.example.miqyasapp1.Patient;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Objects;

public class PatientEditInfo extends BaseActivity {
    private TextView gender, bmiResult;
    private EditText age, height, weight;
    private Button saveEditBT, calcuateBmiBT;
    private Toolbar toolbar;

    //firebase
    FirebaseUser user;
    DatabaseReference reference;
    public String id;
    private static DecimalFormat df2 = new DecimalFormat("#.###");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_edit_info);

        init();
        setToolbar();

        reference = FirebaseDatabase.getInstance().getReference("patient");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (Objects.equals(ds.child("email").getValue(), user.getEmail())) {
                        id = ds.getKey();
                        age.setText(ds.child("age").getValue(String.class));
                        gender.setText(ds.child("gender").getValue(String.class));
                        if (ds.child("weight").exists()) {
                            weight.setText(ds.child("weight").getValue(String.class));
                        }
                        if (ds.child("height").exists()) {
                            height.setText(ds.child("height").getValue(String.class));
                        }
                    }//end of big if
                }//end of for loop
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        calcuateBmiBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weightInput = Double.parseDouble(weight.getText().toString());
                double heightInput = Double.parseDouble(height.getText().toString());
                double bmi = (weightInput / Math.pow(heightInput, 2))*10000;
                bmiResult.setText(df2.format(bmi));

            }
        });

        saveEditBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("user-id:      ", id);
                reference.child(id).child("age").setValue(age.getText().toString());
                if (!weight.getText().toString().isEmpty())
                    reference.child(id).child("weight").setValue(weight.getText().toString());
                if (!height.getText().toString().isEmpty())
                    reference.child(id).child("height").setValue(height.getText().toString());
                reference.child(id).child("bmi").setValue(bmiResult.getText().toString());

                onBtnSavedClicked();
            }
        });


    }//end of onCreate()

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

    public void init() {
        age = (EditText) findViewById(R.id.patient_age);
        height = (EditText) findViewById(R.id.patient_height);
        weight = (EditText) findViewById(R.id.patient_weight);

        gender = (TextView) findViewById(R.id.patient_gender);
        bmiResult = (TextView) findViewById(R.id.bmi_result);

        saveEditBT = (Button) findViewById(R.id.patient_saveEditBT);
        calcuateBmiBT = (Button) findViewById(R.id.calculate_bmi);
        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.editInfo_toolbar);
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
