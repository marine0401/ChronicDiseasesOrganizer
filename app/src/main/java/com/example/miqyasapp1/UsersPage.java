package com.example.miqyasapp1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.miqyasapp1.Admin.AdminLogin;
import com.example.miqyasapp1.Doctor.DoctorLogin;
import com.example.miqyasapp1.Patient.PatientLogin;

public class UsersPage extends BaseActivity {
    private ImageView patient, doctor, admin, aboutApp, close;
    LinearLayout usersPageLL;
    RelativeLayout moreDetailLL;
    private TextView desc;
    private Animation scale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_page);
        init();

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // patient.startAnimation(scale); //gives me error to scale//
                goToPatientLogin();
            }
        });

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorLogin();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdminLogin();
            }
        });

        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDetailLL.setVisibility(View.VISIBLE);
                desc.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moreDetailLL.setVisibility(View.GONE);
                        desc.setVisibility(View.GONE);
                        close.setVisibility(View.GONE);
                    }
                });
            }
        });
    }//end of onCreate()

    public void init() {
        //scale = AnimationUtils.loadAnimation(this, R.anim.scale);
        usersPageLL = (LinearLayout) findViewById(R.id.users_page_LL);
        moreDetailLL = (RelativeLayout) findViewById(R.id.more_detail);
        desc = (TextView) findViewById(R.id.description);
        close = (ImageView) findViewById(R.id.close);
        patient = (ImageView) findViewById(R.id.patient);
        doctor = (ImageView) findViewById(R.id.doctor);
        admin = (ImageView) findViewById(R.id.admin);
        aboutApp = (ImageView) findViewById(R.id.about_app);
        patient.setClickable(true);
        doctor.setClickable(true);
        admin.setClickable(true);
        aboutApp.setClickable(true);

    }

    public void goToPatientLogin() {
        startActivity(new Intent(this, PatientLogin.class));
    }//end of goToPatientLogin()

    public void goToDoctorLogin() {
        startActivity(new Intent(this, DoctorLogin.class));
    }//end of goToPatientLogin()

    public void goToAdminLogin() {
        startActivity(new Intent(this, AdminLogin.class));
    }//end of goToPatientLogin()

}//end of class