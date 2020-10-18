package com.example.miqyasapp1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.miqyasapp1.ui.home.AdminHome;
import com.example.miqyasapp1.ui.setting.SettingFragment;
import com.example.miqyasapp1.ui.home.DoctorHomeFragment;
import com.example.miqyasapp1.ui.home.HomeFragment;
import com.example.miqyasapp1.ui.profile.ProfileFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userType = sharedPreferences.getString("user_type", "default");

        //showing the default fragment once the user(doctor/patient) logged in
        if (userType.equals("patient")) {
            toolbar.setTitle("الرئيسية");
            // load the store fragment by default
            loadFragment(new HomeFragment());
            setSupportActionBar(toolbar);
        } else if (userType.equals("doctor")) {
            toolbar.setTitle("الرئيسية");
            // load the store fragment by default
            loadFragment(new DoctorHomeFragment());
            setSupportActionBar(toolbar);
        } else if (userType.equals("admin")) {
            toolbar.setTitle("الرئيسية");
            // load the store fragment by default
            loadFragment(new AdminHome());
            setSupportActionBar(toolbar);

        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }//end of onCreate()

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (userType.equals("patient")) {
                        toolbar.setTitle("الرئيسية");
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        return true;
                    } else if (userType.equals("doctor")) {
                        toolbar.setTitle("الرئيسية");
                        fragment = new DoctorHomeFragment();
                        loadFragment(fragment);
                        return true;
                    } else if (userType.equals("admin")) {
                        toolbar.setTitle("الرئيسية");
                        fragment = new AdminHome();
                        loadFragment(fragment);
                        return true;
                    } else {
                        return true;
                    }

                case R.id.navigation_setting:
                    toolbar.setTitle("الإعدادات");
                    fragment = new SettingFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_profile:
                    toolbar.setTitle("الملف الشخصي");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.mainactivity_toolbar);
        //setting back button
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

}//end of class 