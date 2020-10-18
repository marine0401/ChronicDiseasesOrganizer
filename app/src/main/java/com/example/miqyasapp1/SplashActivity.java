package com.example.miqyasapp1;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.google.android.gms.measurement.AppMeasurement.getInstance;


public class SplashActivity extends Activity {
    //private final int SPLASH_DISPLAY_LENGTH=1000;
    ConstraintLayout splash;


    private static ViewPager viewPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3};

    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        splash = (ConstraintLayout) findViewById(R.id.splash);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d(TAG, "Token is!!!!!!!!    " + token);
                    }
                });

        init();
        goToUsersPage();

        splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUsersPage();
            }
        });
        // goToUsersPage();
    }

    private void init() {
        for (int i = 0; i < XMEN.length; i++) {
            XMENArray.add(XMEN[i]);
        }//end of for loop

        viewPager = (ViewPager) findViewById(R.id.imageSlider_viewPager);
        viewPager.setAdapter(new ImageSliderAdapter(SplashActivity.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;

                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);


            }
        }, 4000, 4000);
//

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_layout);//bind xml to java
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                goToUsersPage();
//                SplashActivity.this.finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);


//
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sleep(3000);
//                    //checkIsLogin();
//                    goToUsersPage();
//                    finish();
//
//
//                } catch (InterruptedException e) {
//                }//end of catch
//            }//end of run()
//        };
//        thread.start();//starting the thread timer

    }//end of init()

    public void goToUsersPage() {
        Intent intent = new Intent(this, UsersPage.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }//end of method

}//end of class
