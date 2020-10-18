package com.example.miqyasapp1.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.DialogFactory;
import com.example.miqyasapp1.Patient.FirebaseDatabaseHelperPatient;
import com.example.miqyasapp1.OneButtonDialog;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.OnClick;

public class PatientDetailsActivity extends BaseActivity {
    Toolbar toolbar;
    private EditText fileNumberET, nameET, emailET, passwordET, phoneET;
    private Button buttonUpdate, buttonDelete;

    private String key;

    private final String USER_TYPE = "patient";
    private String fileNumber, name, email, password, phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        init();
        setToolbar();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient patient = new Patient();
                patient.setId(key);
                patient.setFileNumber(fileNumberET.getText().toString());
                patient.setName(nameET.getText().toString());
                patient.setEmail(emailET.getText().toString());
                patient.setPassword(passwordET.getText().toString());
                patient.setPhone(phoneET.getText().toString());
                patient.setUser_type(USER_TYPE);

                new FirebaseDatabaseHelperPatient().updatePatient(key, patient, new FirebaseDatabaseHelperPatient.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Patient> patients, List<String> keys) {

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
                new FirebaseDatabaseHelperPatient().deletePatient(key, new FirebaseDatabaseHelperPatient.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Patient> patients, List<String> keys) {

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
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(emailET.getText().toString(), passwordET.getText().toString());
                        assert user != null;
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            onBtnSuccessClicked();
                                                        } else {
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

    //Custom Dialog delete successfully!
    public void onBtnSuccessClicked() {
        OneButtonDialog oneButtonDialog =
                DialogFactory.makeSuccessDialog(R.string.success_title_delete,
                        R.string.success_message,
                        R.string.success_button_text,
                        new OneButtonDialog.ButtonDialogAction() {
                            @Override
                            public void onButtonClicked() {
                                startActivity(new Intent(PatientDetailsActivity.this, PatientListActivity.class));

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
                                startActivity(new Intent(PatientDetailsActivity.this, PatientListActivity.class));

                            }
                        });
        oneButtonDialog.show(getSupportFragmentManager(), OneButtonDialog.TAG);
    }

    public void init() {
        key = getIntent().getStringExtra("key");
        fileNumber = getIntent().getStringExtra("fileNumber");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone_number");

        fileNumberET = (EditText) findViewById(R.id.editText_userID);
        nameET = (EditText) findViewById(R.id.editText_name);
        emailET = (EditText) findViewById(R.id.editText_email);
        passwordET = (EditText) findViewById(R.id.editText_password);
        phoneET = (EditText) findViewById(R.id.editText_phone);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        fileNumberET.setText(fileNumber);
        nameET.setText(name);
        emailET.setText(email);
        passwordET.setText(password);
        phoneET.setText(phone);
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
