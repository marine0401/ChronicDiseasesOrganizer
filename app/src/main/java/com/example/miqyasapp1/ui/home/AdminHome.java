package com.example.miqyasapp1.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.miqyasapp1.Admin.DoctorListActivity;
import com.example.miqyasapp1.Admin.PatientListActivity;
import com.example.miqyasapp1.R;

public class AdminHome extends Fragment {

    private Button adminDoctor, adminPatient;
    //Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_home, container, false);
        //setToolbar();

        adminPatient = (Button) view.findViewById(R.id.admin_patientsBT);
        adminDoctor = (Button) view.findViewById(R.id.admin_doctorsBT);

        adminPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPatientsPage();
            }
        });
        adminDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorsPage();
            }
        });

        return view;

    }//end of onCreate()

//    public void setToolbar() {
//        toolbar = findViewById(R.id.admin_home_toolbar);
//        toolbar.setTitle("الرئيسية");
//        setSupportActionBar(toolbar);
//
//        //setting back button
//        Window window = this.getWindow();
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
//
//        //setting back button
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//setting the tool bar
//        getSupportActionBar().setDisplayShowHomeEnabled(true);//making it showing
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();//going automatically to the previous page
//            }
//        });
//
//    }//end of setToolbar()


    public void goToPatientsPage() {
        startActivity(new Intent(getContext(), PatientListActivity.class));
    }

    public void goToDoctorsPage() {
        startActivity(new Intent(getContext(), DoctorListActivity.class));
    }

}//end of class
