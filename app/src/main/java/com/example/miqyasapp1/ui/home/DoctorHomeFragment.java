package com.example.miqyasapp1.ui.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miqyasapp1.Doctor.PatientOfDoctorList;
import com.example.miqyasapp1.MyAdapter;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.Patient;
import com.example.miqyasapp1.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DoctorHomeFragment extends Fragment {
    private ProgressDialog mProgress;
    public TextView doctorName, doctorEmployeeNumber, openPatientfile;
    EditText addPatinetET;
    Button addPatinetBT;

    //firebase
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;

    RecyclerView recyclerView;
    static ArrayList<Patient> list;
    MyAdapter adapter;
    public String fileNumber, employeeNumber, id;
    AlertDialog.Builder builder;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_home, container, false);
        init(view);

        //show doctor's name and employee numb from database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (Objects.equals(ds.child("email").getValue(), user.getEmail())) {
                        id = ds.getKey();
                        doctorName.setText(ds.child("name").getValue(String.class));
                        doctorEmployeeNumber.setText(ds.child("employee_number").getValue(String.class));
                        //save it
                        employeeNumber = ds.child("employee_number").getValue(String.class);

                    }//end of if
                }//end of for loop

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addPatinetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<>();
                fileNumber = addPatinetET.getText().toString().trim();//get doctor's value
                reference = FirebaseDatabase.getInstance().getReference();//1-goto patient db to search
                reference.child("patient").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (Objects.equals(dataSnapshot1.child("fileNumber").getValue(String.class), fileNumber)) {
                                final Patient patient = dataSnapshot1.getValue(Patient.class);//2-saving the obiect of patient
                                reference.child("doctor").child(id).child("patient").push().setValue(patient);
                            }//end of if
                            else {
                                builder.setMessage("رقم المريض المُدخل خاطئ .. " +
                                        "\n" +
                                        "حاول مرة أخرى").setTitle("فشل في الإضافة")
                                        .setIcon(R.drawable.error)
                                        .setCancelable(true)
                                        .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }//end of for
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        openPatientfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorPatientList();
            }
        });

        return view;
    }//end of onCreateView()

    public void init(View view) {
        builder = new AlertDialog.Builder(getContext());

        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("جاري إضافة الملف...");
        mProgress.setMessage("فضلًا انتظر...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        doctorName = (TextView) view.findViewById(R.id.doctor_nameTV);
        doctorEmployeeNumber = (TextView) view.findViewById(R.id.employee_numberTV);
        openPatientfile = (TextView) view.findViewById(R.id.open_patient_fileTV);

        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.doctorPatient_recyclerView);
        addPatinetET = (EditText) view.findViewById(R.id.add_patinetET);
        addPatinetBT = (Button) view.findViewById(R.id.add_patinetBT);
    }

    public void goToDoctorPatientList() {
        Intent intent = new Intent(getContext(), PatientOfDoctorList.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("doctor_id", id);
        Log.d("", "IDDDDDDD:         " + id);
        editor.commit();
        startActivity(intent);
    }

}//end of fragment