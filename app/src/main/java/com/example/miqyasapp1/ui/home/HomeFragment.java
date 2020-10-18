package com.example.miqyasapp1.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.miqyasapp1.Patient.PatientDiagnosis;
import com.example.miqyasapp1.Patient.PatientEditInfo;
import com.example.miqyasapp1.Patient.PatientLifestyle;
import com.example.miqyasapp1.Patient.PatientMeasurements;
import com.example.miqyasapp1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private ImageView editInfoBT, recordMeasuremnetsBT, diagnosisBT, lifestyleBT;
    public TextView patientName, patientFileNumber;

    //firebase
    FirebaseUser user;
    DatabaseReference reference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        patientName = (TextView) view.findViewById(R.id.patient_nameTV);
        patientFileNumber = (TextView) view.findViewById(R.id.file_numberTV);

        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("patient");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //for loop to all database coloumns then check if any email match loged in user email
                    if (Objects.equals(ds.child("email").getValue(), user.getEmail())) {
                        patientName.setText(ds.child("name").getValue(String.class));
                        patientFileNumber.setText(ds.child("fileNumber").getValue(String.class));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editInfoBT = (ImageView) view.findViewById(R.id.editInfoBT);
        recordMeasuremnetsBT = (ImageView) view.findViewById(R.id.diabetes_icon);
        diagnosisBT = (ImageView) view.findViewById(R.id.diagnosisBT);
        lifestyleBT = (ImageView) view.findViewById(R.id.lifestyle_icon);

        editInfoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditInfo();
            }
        });
        recordMeasuremnetsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goToRecordMeasurments();
            }
        });
        diagnosisBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDiagnosis();
            }
        });
        lifestyleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLifestyle();
            }
        });

        return view;

    }//end of onCreate()

    public void goToEditInfo() {
        startActivity(new Intent(getContext(), PatientEditInfo.class));
    }//end of goToPatientHome()


    public void goToDiagnosis() {
        startActivity(new Intent(getContext(), PatientDiagnosis.class));
    }//end of goToPatientHome()

    public void goToRecordMeasurments() {
        startActivity(new Intent(getContext(), PatientMeasurements.class));
    }//end of goToPatientHome()

    public void goToLifestyle() {
        startActivity(new Intent(getContext(), PatientLifestyle.class));
    }//end of goToPatientHome()

}//end of class