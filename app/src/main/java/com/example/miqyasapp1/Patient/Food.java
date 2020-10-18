package com.example.miqyasapp1.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.miqyasapp1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Food extends AppCompatActivity {
    public ImageView dietFood, Notification;
    public TextView resultFood;
    public Button prev, next;
    Toolbar toolbar;

    public ViewFlipper viewFlipperD_low, viewFlipperD_normal, viewFlipperD_high;
    public ViewFlipper viewFlipperBP_low, viewFlipperBP_normal, viewFlipperBP_upNormal, viewFlipperBP_high;

    public String userFileNumber = "";
    public double SBP, DBP, glucose, pot;
    public String id;

    //firebase
    FirebaseUser user;
    DatabaseReference referenceUser;

    static DatabaseReference bpRef = FirebaseDatabase.getInstance().getReference("BloodPressure");
    static DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Diabetes");
    static DatabaseReference kRef = FirebaseDatabase.getInstance().getReference("Kidney");

    public static String bpClassification;
    public static String dClassification;
    public static String kClassification;

    MenuItem menuItem;
    // badge text view
    TextView badgeCounter;
    // change the number to see badge in action
    int pendingNotifications = 0;
    public static String doctorNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lifestyle);

        init();
        setToolbar();

        //firebase, getting the doctor noted sent to patient through database
        user = FirebaseAuth.getInstance().getCurrentUser();
        //getting current user id and fileNumber
        referenceUser = FirebaseDatabase.getInstance().getReference("patient");
        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //for loop to all database columns then check if any email match logged in user email
                    if (ds.child("email").getValue().equals(user.getEmail())) {
                        id = ds.getKey();
                        userFileNumber = ds.child("fileNumber").getValue(String.class);
                        Log.d("", "userFileNumber:     " + userFileNumber);
                    }//end of if
                }//end of for loop
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (Objects.equals(ds.child("email").getValue(), user.getEmail())) {
                        if (ds.child("doctorNote").exists()) {
                            if (!ds.child("doctorNote").getValue(String.class).isEmpty()) {
                                doctorNotes = ds.child("doctorNote").getValue(String.class);
                                pendingNotifications++;
                            } else {
                                doctorNotes = "لا توجد ملاحظات مُرسلة.";
                            }//end of else
                        }//end of if
                        else {
                            doctorNotes = "لا توجد ملاحظات مُرسلة.";
                            return;
                        }//end of else
                    }//end of main if
                }//end of for loop
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                doctorNotes = "لا توجد ملاحظات مُرسلة.";

            }
        });

        //check if current user diagnosis with BP, then get (classification Result, SBP, DBP) to check it in generating suitable diet.
        bpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        bpClassification = ds.child("classification").getValue(String.class);
                        SBP = ds.child("sbp").getValue(Double.class);
                        DBP = ds.child("dbp").getValue(Double.class);
                        checkValuesForeBP(bpClassification, SBP, DBP);
                        Log.d("", "bpClassification:     " + bpClassification + "," + SBP + "," + DBP);
                    } else {
                        Log.d("", "bpClassification:     null");
                    }//end of else
                }//end for loop
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //check if current user diagnosis with Diabetes, then get (classification Result, glucose) to check it in generating suitable diet.
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        dClassification = ds.child("classification").getValue(String.class);
                        glucose = ds.child("glucose").getValue(Double.class);
                        checkValuesForeDiabetes(dClassification, glucose);
                        Log.d("", "dClassification:     " + dClassification + "," + glucose);
                    } else {
                        Log.d("", "dClassification:     null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //check if current user diagnosis with Kidney, then get (classification Result, potassium) to check it in generating suitable diet.
        kRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("fileNumber").getValue().equals(userFileNumber)) {
                        kClassification = ds.child("classification").getValue(String.class);
                        pot = ds.child("pot").getValue(Double.class);
                        checkValuesForeKidney(kClassification, pot);
                        Log.d("", "kClassification:     " + kClassification);
                    } else {
                        Log.d("", "kClassification:     null");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDoctorNote();
            }
        });


    }//end of onCreate()

    public void checkValuesForeBP(String BPclassification, double SBP, double DBP) {
        if (BPclassification.equals("1")) {
            Log.d("", "yes  1");
            if (SBP <= 90.0 && DBP < 60.0) {
                //viewFlipperBP_low.setVisibility(View.VISIBLE);
                resultFood.setText(R.string.food_low_BP);
                String lifestyle = getString(R.string.food_low_BP);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);

            } else if (130.0 < SBP && SBP > 90.0 && 80 < DBP && DBP > 60) {
                //viewFlipperBP_normal.setVisibility(View.VISIBLE);
                resultFood.setText(R.string.food_normal_BP);
                String lifestyle = getString(R.string.food_normal_BP);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);

            } else if (131.0 > SBP && SBP < 140.0 && 80 < DBP && DBP > 60) {
                //viewFlipperBP_upNormal.setVisibility(View.VISIBLE);
                resultFood.setText(R.string.food_upnormal_BP);
                String lifestyle = getString(R.string.food_upnormal_BP);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);

            } else if (SBP > 140.0 && DBP > 90) {
                viewFlipperBP_high.setVisibility(View.VISIBLE);
                resultFood.setText(R.string.food_high_BP);
                String lifestyle = getString(R.string.food_high_BP) + "\n" + getString(R.string.food_high_BP2)
                        + "\n" + getString(R.string.food_high_BP3);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prev.setVisibility(View.VISIBLE);
                        viewFlipperBP_high.showNext();
                    }
                });
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipperBP_high.showPrevious();
                    }
                });
            }

        } else {
            Log.d("", "not 1");
            resultFood.setText("أنت غير مصاب بمرض الضغط .");
        }

    }//end of method

    public void checkValuesForeDiabetes(String Dclassification, double glucose) {
        if (Dclassification.equals("1")) {
            Log.d("", "yes  1");
            if (glucose < 70.0) {
                viewFlipperD_low.setVisibility(View.VISIBLE);
                resultFood.setText(R.string.food_low_suger1);
                String lifestyle = getString(R.string.food_low_suger1) + "\n" + getString(R.string.food_low_suger2)
                        + "\n" + getString(R.string.food_low_suger3);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prev.setVisibility(View.VISIBLE);
                        viewFlipperD_low.showNext();
                    }
                });
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipperD_low.showPrevious();
                    }
                });

            } else if (glucose >= 70 && glucose < 125) {
                viewFlipperD_normal.setVisibility(View.VISIBLE);
                resultFood.setText(R.string.food_normal_suger);
                String lifestyle = getString(R.string.food_normal_suger) + "\n" + getString(R.string.food_normal_suger2)
                        + "\n" + getString(R.string.food_normal_suger3) + "\n" + getString(R.string.food_normal_suger4);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prev.setVisibility(View.VISIBLE);
                        viewFlipperD_normal.showNext();
                    }
                });
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipperD_normal.showPrevious();
                    }
                });

            } else if (glucose >= 125) {
                viewFlipperD_high.setVisibility(View.VISIBLE);
                String lifestyle = getString(R.string.food_high_suger) + "\n" + getString(R.string.food_high_suger2)
                        + "\n" + getString(R.string.food_high_suger3);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prev.setVisibility(View.VISIBLE);
                        viewFlipperD_high.showNext();
                    }
                });
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipperD_high.showPrevious();
                    }
                });
            }

        } else {
            Log.d("", "not 1");
            resultFood.setText("أنت غير مصاب بمرض السكر .");
        }

    }//end of method

    public void checkValuesForeKidney(String Kclassification, double pot) {//todo
        if (Kclassification.equals("1")) {
            Log.d("", "yes  1");
            if (pot > 3.5 && pot < 5.0) {
                //resultFood.setText("Safe Zone");
                resultFood.setText(R.string.food_pot_safe);
                String lifestyle = getString(R.string.food_pot_safe);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);
            } else if (pot > 5.1 && pot < 6.0) {
                //resultFood.setText("CAUTION ZONE ");
                resultFood.setText(R.string.food_pot_caution);
                String lifestyle = getString(R.string.food_pot_caution);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);
            } else if (pot > 6.1) {
                //resultFood.setText("DANGER ZONE");
                resultFood.setText(R.string.food_pot_danger);
                String lifestyle = getString(R.string.food_pot_danger);
                referenceUser.child(id).child("lifestyle").setValue(lifestyle);
            }
        } else {
            Log.d("", "not 1");
            resultFood.setText("أنت غير مصاب بمرض الكلى .");
        }

    }//end of method

    public void init() {
        dietFood = (ImageView) findViewById(R.id.lifestyle_img);
        resultFood = (TextView) findViewById(R.id.food_result);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        Notification = (ImageView) findViewById(R.id.notifi_image);

        viewFlipperD_low = (ViewFlipper) findViewById(R.id.viewFlipperD_low);
        viewFlipperD_normal = (ViewFlipper) findViewById(R.id.viewFlipperD_normal);
        viewFlipperD_high = (ViewFlipper) findViewById(R.id.viewFlipperD_high);

//        viewFlipperBP_low = (ViewFlipper) findViewById(R.id.viewFlipperBP_low);
//        viewFlipperBP_normal = (ViewFlipper) findViewById(R.id.viewFlipperBP_normal);
       // viewFlipperBP_upNormal = (ViewFlipper) findViewById(R.id.viewFlipperBP_upNormal);
        viewFlipperBP_high = (ViewFlipper) findViewById(R.id.viewFlipperBP_high);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        menuItem = menu.findItem(R.id.nav_doctor_note_notification);

        //check if any pending notification
        if (pendingNotifications == 0) {
            // if no pending notification remove badge (NO notes sent from doctor)
            menuItem.setActionView(null);
        } else {
            // if notification than set the badge icon layout
            menuItem.setActionView(R.layout.lifestyle);
            // get the view from the nav item
            View view = menuItem.getActionView();
            // get the text view of the action view for the nav item
            badgeCounter = view.findViewById(R.id.badge_counter);
            // set the pending notifications value
            badgeCounter.setText(String.valueOf(pendingNotifications));
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void goToDoctorNote() {
        Intent intent = new Intent(Food.this, DoctorNotes.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("doctor_note", doctorNotes);
        editor.commit();
        startActivity(intent);
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.lifestyle_toolbar);
        setSupportActionBar(toolbar);

        //setting back button
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //setting back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//setting the tool bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);//making it showing

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//going automatically to the previous page
            }
        });
    }//end of toolbar()
}//end of class
