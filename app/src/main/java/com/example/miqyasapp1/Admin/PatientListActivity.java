package com.example.miqyasapp1.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miqyasapp1.Doctor.PatientOfDoctorList;
import com.example.miqyasapp1.MyAdapter;
import com.example.miqyasapp1.Patient.FirebaseDatabaseHelperPatient;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.RecyclerViewConfig.RecyclerViewConfigPatient;
import com.example.miqyasapp1.DatabaseTables.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    Button addNew;
    Toolbar toolbar;
    SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_list);
        setToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.doctor_list_recyclerView);
        addNew = (Button) findViewById(R.id.addNew);
        new FirebaseDatabaseHelperPatient().readPatient(new FirebaseDatabaseHelperPatient.DataStatus() {
            @Override
            public void DataIsLoaded(final List<Patient> patients, List<String> keys) {
                new RecyclerViewConfigPatient().setCofig(mRecyclerView, PatientListActivity.this,
                        patients, keys);
                //search in patient list
                searchView = (SearchView) findViewById(R.id.doctor_patient_searchView);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


        //to add new doctor go to new page to enter its information
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientListActivity.this, newPatientActivity.class));
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
        MyAdapter myAdapter = new MyAdapter(PatientListActivity.this, myList,null);
        mRecyclerView.setAdapter(myAdapter);
    }//end of Search()


    public void setToolbar() {
        toolbar = findViewById(R.id.doctor_list_toolbar);
        setSupportActionBar(toolbar);

        //setting back button
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

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
