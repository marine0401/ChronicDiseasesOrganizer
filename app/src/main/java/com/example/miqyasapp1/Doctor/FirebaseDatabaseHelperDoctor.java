package com.example.miqyasapp1.Doctor;

import androidx.annotation.NonNull;

import com.example.miqyasapp1.DatabaseTables.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelperDoctor {
    private DatabaseReference reference;
    private List<Doctor> doctorList = new ArrayList<>();

    public FirebaseDatabaseHelperDoctor() {
        reference =  FirebaseDatabase.getInstance().getReference("doctor");
    }

    public interface DataStatus {
        void DataIsLoaded(List<Doctor> doctors, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();

    }//end of interface


    public void readDoctor(final DataStatus dataStatus) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Doctor doctor = keyNode.getValue(Doctor.class);
                    doctorList.add(doctor);
                }
                dataStatus.DataIsLoaded(doctorList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addDoctor(Doctor doctor, final DataStatus dataStatus) {
        String key = reference.push().getKey();
        reference.child(key).setValue(doctor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public void updateDoctor(String key, Doctor doctor, final DataStatus dataStatus) {
        reference.child(key).setValue(doctor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });


    }

    public void deleteDoctor(String key, final DataStatus dataStatus) {
        reference.child(key).removeValue()
//        reference.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

}//end of class



