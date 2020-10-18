package com.example.miqyasapp1.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.DialogFactory;
import com.example.miqyasapp1.Doctor.FirebaseDatabaseHelperDoctor;
import com.example.miqyasapp1.OneButtonDialog;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DoctorDetailsActivity extends BaseActivity {
    Toolbar toolbar;
    private EditText employeeNumberET, nameET, emailET, passwordET, phoneET;
    private Button buttonUpdate, buttonDelete;
    private final String USER_TYPE = "doctor";
    private String key, emplyeeNumber, name, email, password, phone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        init();
        setToolbar();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Doctor doctor = new Doctor();
                doctor.setId(key);
                doctor.setEmployee_number(employeeNumberET.getText().toString());
                doctor.setName(nameET.getText().toString());
                doctor.setEmail(emailET.getText().toString());
                doctor.setPassword(passwordET.getText().toString());
                doctor.setPhone_number(phoneET.getText().toString());
                doctor.setUser_type(USER_TYPE);
                new FirebaseDatabaseHelperDoctor().updateDoctor(key, doctor, new FirebaseDatabaseHelperDoctor.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Doctor> doctors, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {
                        onBtnSuccessClickedUpdate();
                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDatabaseHelperDoctor().deleteDoctor(key, new FirebaseDatabaseHelperDoctor.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Doctor> doctors, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // email and password credentials but there are multiple possible providers,
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(emailET.getText().toString(), passwordET.getText().toString());
                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
//                                                            LinearLayout linearLaayout = findViewById(R.id.linearLayout);
//                                                            Snackbar snackbar = Snackbar
//                                                                    .make(linearLaayout, "تم الحذف بنجاح", Snackbar.LENGTH_LONG);
//                                                            snackbar.show();
                                                            onBtnSuccessClicked();
                                                        }
                                                    }
                                                });

                                    }
                                });
                    }
                });
            }
        });

    }//end of onCreate

    public void init() {
        key = getIntent().getStringExtra("key");
        emplyeeNumber = getIntent().getStringExtra("employee_number");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone_number");

        employeeNumberET = (EditText) findViewById(R.id.editText_userID);
        nameET = (EditText) findViewById(R.id.editText_name);
        emailET = (EditText) findViewById(R.id.editText_email);
        passwordET = (EditText) findViewById(R.id.editText_password);
        phoneET = (EditText) findViewById(R.id.editText_phone);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        employeeNumberET.setText(emplyeeNumber);
        nameET.setText(name);
        emailET.setText(email);
        passwordET.setText(password);
        phoneET.setText(phone);
    }

    //Custom Dialog delete successfully!
    public void onBtnSuccessClicked() {
        OneButtonDialog oneButtonDialog =
                DialogFactory.makeSuccessDialog(R.string.success_title_delete,
                        R.string.success_message,
                        R.string.success_button_text,
                        new OneButtonDialog.ButtonDialogAction() {
                            @Override
                            public void onButtonClicked() {
                                startActivity(new Intent(DoctorDetailsActivity.this, DoctorListActivity.class));

                            }
                        });
        oneButtonDialog.show(getSupportFragmentManager(), OneButtonDialog.TAG);
    }
    //Custome Dialog updated successfully
    public void onBtnSuccessClickedUpdate() {
        OneButtonDialog oneButtonDialog =
                DialogFactory.makeSuccessDialog(R.string.success_title_update,
                        R.string.success_message,
                        R.string.success_button_text,
                        new OneButtonDialog.ButtonDialogAction() {
                            @Override
                            public void onButtonClicked() {
                                startActivity(new Intent(DoctorDetailsActivity.this, DoctorListActivity.class));

                            }
                        });
        oneButtonDialog.show(getSupportFragmentManager(), OneButtonDialog.TAG);
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.update_dialog_toolbar);
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
