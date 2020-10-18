package com.example.miqyasapp1.Patient;

import androidx.annotation.NonNull;

import com.example.miqyasapp1.DatabaseTables.Patient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelperPatient {
    private DatabaseReference reference;
    private List<Patient> patientList = new ArrayList<>();

    public FirebaseDatabaseHelperPatient() {
        reference = FirebaseDatabase.getInstance().getReference();
    }


    public interface DataStatus {
        void DataIsLoaded(List<Patient> patients, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();

    }//end of interface


    public void readPatient(final DataStatus dataStatus) {
        reference.child("patient").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Patient patient = keyNode.getValue(Patient.class);
                    patientList.add(patient);
                }
                dataStatus.DataIsLoaded(patientList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readPatientWithId(final String id, final FirebaseDatabaseHelperPatient.DataStatus dataStatus) {
        reference.child("doctor").child(id).child("patient").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {

                    keys.add(keyNode.getKey());
                    Patient patient = keyNode.getValue(Patient.class);
                    patientList.add(patient);
                }
                dataStatus.DataIsLoaded(patientList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addPatient(Patient patient, final DataStatus dataStatus) {
        String key = reference.push().getKey();
        reference.child("patient").child(key).setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public void updatePatient(String key, Patient patient, final DataStatus dataStatus) {
        reference.child("patient").child(key).setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });


    }

    public void deletePatient(String key, final DataStatus dataStatus) {
        reference.child("patient").child(key).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

    public void deletePatientFromList(final String doctorKey,final String patientkey, final DataStatus dataStatus) {
        reference.child("doctor").child(doctorKey).child("patient").child(patientkey).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //patientList.remove(patientkey);
                        dataStatus.DataIsDeleted();
                    }
                });
    }



}//end of class Helper



