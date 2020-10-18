package com.example.miqyasapp1.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

//firebase
import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.ForgetPassword;
import com.example.miqyasapp1.MainActivity;
import com.example.miqyasapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientLogin extends BaseActivity {
    private ProgressDialog mProgress;

    Toolbar toolbar;
    private EditText patientUsername, patientPassword;
    private TextView forgetPassword;
    private Button loginBT;

    //firbase
    FirebaseUser user;
    FirebaseAuth mAuth;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login);
        init();
        setToolbar();

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                login(v);//calling
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                startActivity(new Intent(PatientLogin.this, ForgetPassword.class));
            }
        });
    }//end of onCreate()

    private void login(View view) {
        if (!validateForm()) {
            return;
        }
        mProgress.show();
        // showProgressBar();
        mAuth.signInWithEmailAndPassword(patientUsername.getText().toString(), patientPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToPatientHome();
                        } else {// If sign in fails, display a message to the patient.
                            //Setting message manually and performing action on button click
                            builder.setMessage("عنوان البريد الإلكتروني أو كلمة المرور خاطئة .").setTitle("فشل تسجيل الدخول")
                                    .setIcon(R.drawable.error)
                                    .setCancelable(false)
                                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            alert.show();

                            //clean fields

                            patientUsername.setText("");
                            patientPassword.setText("");
                        }
                        mProgress.hide();
                    }
                });
    }//end of login()


    private boolean validateForm() {
        boolean valid = true;
        String emailInput = patientUsername.getText().toString();
        //check if empty fields
        if (TextUtils.isEmpty(emailInput)) {
            patientUsername.setError("مطلوب.");
            valid = false;
        } else {
            patientUsername.setError(null);
        }
        String password = patientPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            patientPassword.setError("مطلوب.");
            valid = false;
        } else {
            patientPassword.setError(null);
        }
        if (!EMailValidation(emailInput)) {
            patientUsername.setError("فضلًا ادخِل عنوان بريد صالح." +
                    "\n" + "مثال:" +
                    "\n" +
                    "Example@example.com");
        }// end of isInputEditTextFilled)
        return valid;
    }//end of validateForm()


    public static boolean EMailValidation(String emailString) {
        if (null == emailString || emailString.length() == 0) {
            return false;
        }//end if

        Pattern emailPattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(emailString);
        return emailMatcher.matches();
    }//end of EMailValidation()


    public void init() {
        //linking
        patientUsername = (EditText) findViewById(R.id.patient_login_fileET);
        patientPassword = (EditText) findViewById(R.id.patient_login_passowrdET);
        forgetPassword = (TextView) findViewById(R.id.patient_forget_password);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("جاري تسجيل الدخول...");
        mProgress.setMessage("فضلًا انتظر...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        //for dialog msg
        builder = new AlertDialog.Builder(this);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //setting Login button
        loginBT = (Button) findViewById(R.id.patient_loginBT);
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.patient_login_toolbar);
        toolbar.setTitle("تسجيل الدخول");
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

    }//end of setToolbar()

    public void goToPatientHome() {
        Intent intent = new Intent(this, MainActivity.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_type", "patient");
        editor.commit();
        startActivity(intent);
    }//end of goToPatientHome()


}//end of class
