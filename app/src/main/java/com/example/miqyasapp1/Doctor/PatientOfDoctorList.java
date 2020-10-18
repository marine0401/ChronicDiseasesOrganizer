package com.example.miqyasapp1.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.SearchView;

import com.example.miqyasapp1.Patient.FirebaseDatabaseHelperPatient;
import com.example.miqyasapp1.MyAdapter;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.RecyclerViewConfig.RecyclerViewConfigPatientList;
import com.example.miqyasapp1.DatabaseTables.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PatientOfDoctorList extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView mRecyclerView;
    SearchView doctorSearchView;
    RecyclerView recyclerView;

    //firebase
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_patient_list);

        setToolbar();
        mRecyclerView = (RecyclerView) findViewById(R.id.doctorPatient_recyclerView);
        recyclerView = (RecyclerView) findViewById(R.id.doctorPatient_recyclerView);

        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        //read id
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String id = sharedPreferences.getString("doctor_id", "default");

        //saving doctor id to get all his patient stored in database
        new FirebaseDatabaseHelperPatient().readPatientWithId(id, new FirebaseDatabaseHelperPatient.DataStatus() {
            @Override
            public void DataIsLoaded(final List<Patient> patients, List<String> keys) {
                new RecyclerViewConfigPatientList().setCofig(mRecyclerView, PatientOfDoctorList.this,
                        patients, keys);
                //search in patient list
                doctorSearchView = (SearchView) findViewById(R.id.doctor_patient_searchView);
                doctorSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Search(newText, patients);
                        return true;
                    }
                });
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }

        });

    }//end of onCreate()

    public void Search(String str, List<Patient> p) {
        ArrayList<Patient> myList = new ArrayList<>();
        for (Patient obj : p) {
            if (obj.getFileNumber().contains(str)) {
                myList.add(obj);
            }//end of if
        }//end of for loop
        MyAdapter myAdapter = new MyAdapter(PatientOfDoctorList.this, myList,null);
        recyclerView.setAdapter(myAdapter);
    }//end of Search()

    public void setToolbar() {
        toolbar = findViewById(R.id.doctor_patient_list_toolbar);
        setSupportActionBar(toolbar);

        //setting back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//setting the tool bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);//making it showing

        //setting back button
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//going automatically to the previous page
            }
        });

    }

}//end of class
