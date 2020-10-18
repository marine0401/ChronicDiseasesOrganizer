package com.example.miqyasapp1.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.Doctor.FirebaseDatabaseHelperDoctor;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class newDoctorActivity extends BaseActivity {
    private ProgressDialog mProgress;
    Toolbar toolbar;
    private EditText name, email, employeeNumber, password, phoneNumber;
    private final String USER_TYPE = "doctor";
    private Button addDoctorBt, doneBt;
    private LinearLayout doneLL, doctorInputLL;
    FirebaseAuth mAuth;


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("doctor");
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_doctor);
        init();
        setToolbar();

        id = ref.child("id").push().getKey();
        addDoctorBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                checkDoctorInput();

            }
        });

    }//end of onCreate()

    //making sure all the inputs are valid, and the required filed are not empty
    private void checkDoctorInput() {
        final String nameInput = name.getText().toString().trim();
        final String emailInput = email.getText().toString().trim();
        final String employeeNumberInput = employeeNumber.getText().toString().trim();
        final String passwordInput = password.getText().toString().trim();

        if (nameInput.isEmpty()) {
            name.setError("مطلوب.");
            name.requestFocus();
            return;
        }

        if (emailInput.isEmpty()) {
            email.setError("مطلوب.");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("فضلًا ادخِل عنوان بريد صالح." +
                    "\n" + "مثال:" +
                    "\n" +
                    "Example@example.com");
            email.requestFocus();
            return;
        }
        if (employeeNumberInput.isEmpty()) {
            employeeNumber.setError("مطلوب.");
            employeeNumber.requestFocus();
            return;
        }


        if (passwordInput.isEmpty()) {
            password.setError("مطلوب.");
            password.requestFocus();
            return;
        }

        if (passwordInput.length() < 6) {
            password.setError("يجب أن تكون كلمة المرور المُدخلة ٦ خانات فأكثر.");
            password.requestFocus();
            return;
        }
        //check if exist employeeNumber in db, to avoid two doctors have same employeeNumber
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("doctor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (Objects.equals(ds.child("employee_number").getValue(), (employeeNumberInput))) {
                        employeeNumber.setError("تم استخدام الرقم الوظيفي من قبل.");
                        mProgress.hide();
                        return;//stop
                    } else {//
                        DoctorLogin();
                    }
                }//end of for loop
            }//end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//end of checkDoctorInput()

    public void DoctorLogin() {
        mProgress.show();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Doctor doctor = new Doctor();
                            doctor.setId(id);
                            doctor.setEmployee_number(employeeNumber.getText().toString());
                            doctor.setEmail(email.getText().toString());
                            doctor.setName(name.getText().toString());
                            doctor.setPassword(password.getText().toString());
                            doctor.setPhone_number(phoneNumber.getText().toString());
                            doctor.setUser_type(USER_TYPE);
                            new FirebaseDatabaseHelperDoctor().addDoctor(doctor, new FirebaseDatabaseHelperDoctor.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<Doctor> doctors, List<String> keys) {

                                }

                                @Override
                                public void DataIsInserted() {
                                    mProgress.hide();
                                    doctorInputLL.setVisibility(View.GONE);
                                    addDoctorBt.setVisibility(View.GONE);
                                    doneLL.setVisibility(View.VISIBLE);
                                    doneBt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            doneLL.setVisibility(View.GONE);
                                            doctorInputLL.setVisibility(View.VISIBLE);
                                            addDoctorBt.setVisibility(View.VISIBLE);
                                            //clear fields to add another doctor
                                            name.setText("");
                                            email.setText("");
                                            employeeNumber.setText("");
                                            password.setText("");
                                            phoneNumber.setText("");
                                            return;
                                        }
                                    });
                                    return;
                                }

                                @Override
                                public void DataIsUpdated() {
                                }

                                @Override
                                public void DataIsDeleted() {
                                }
                            });

                        }//end of if
                    }
                });
    }//end of DoctorLogin()

    public void init() {
        mAuth = FirebaseAuth.getInstance();

        name = (EditText) findViewById(R.id.edittext_name_doctor);
        email = (EditText) findViewById(R.id.edittext_email_doctor);
        employeeNumber = (EditText) findViewById(R.id.edittext_employeenumber);
        password = (EditText) findViewById(R.id.edittext_password_doctor);
        phoneNumber = (EditText) findViewById(R.id.edittext_phone_doctor);
        addDoctorBt = (Button) findViewById(R.id.button_add_doctor);

        doneLL = (LinearLayout) findViewById(R.id.doneLL);
        doneBt = (Button) findViewById(R.id.doneBT);
        doctorInputLL = (LinearLayout) findViewById(R.id.doctorInputLL);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("جاري إضافة ملف دكتور جديد...");
        mProgress.setMessage("فضلًا انتظر...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.hide();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.admin_doctorAdd_toolbar);
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
