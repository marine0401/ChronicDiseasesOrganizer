package com.example.miqyasapp1.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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

import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.ForgetPassword;
import com.example.miqyasapp1.MainActivity;
import com.example.miqyasapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoctorLogin extends BaseActivity {
    private ProgressDialog mProgress;
    AlertDialog.Builder builder;
    Toolbar toolbar;
    private EditText doctorEmail, doctorPassword;
    private TextView forgetPassword;
    private Button doctorLogingBT;

    //firbase
    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_login);
        init();
        setToolbar();

        doctorLogingBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                login();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                startActivity(new Intent(DoctorLogin.this, ForgetPassword.class));
            }
        });
    }//end of onCreate()


    private void login() {
        if (!validateForm()) {
            return;
        }//end of if
        mProgress.show();
        mAuth.signInWithEmailAndPassword(doctorEmail.getText().toString(), doctorPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            reference = FirebaseDatabase.getInstance().getReference().child("doctor");
                            reference.orderByChild("email").equalTo(doctorEmail.getText().toString()).
                                    addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                goToDoctorHome();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }//end of if
                        else {//Setting message manually and performing action on button click
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
                            doctorEmail.setText("");
                            doctorPassword.setText("");

                        }//end of big else
                        mProgress.hide();
                    }
                });
    }//end of login()

    private boolean validateForm() {
        boolean valid = true;
        String emailInput = doctorEmail.getText().toString();
        //check if empty fields
        if (TextUtils.isEmpty(emailInput)) {
            doctorEmail.setError("فضلًا ادخل البريد الالكتروني.");
            valid = false;
        } else {
            doctorEmail.setError(null);
        }
        String password = doctorPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            doctorPassword.setError("فضلًا ادخل الرقم السري.");
            valid = false;
        } else {
            doctorPassword.setError(null);
        }
        //giving user hint if the  email invaild
        if (!EMailValidation(emailInput)) {
            doctorEmail.setError("فضلًا ادخِل عنوان بريد صالح." +
                    "\n" + "مثال:" +
                    "\n" +
                    "Example@example.com");
        }// end of isInputEditTextFilled)
        return valid;
    }//end of validateForm()

//check email pattern
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

    public void goToDoctorHome() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("user_type", doctor);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_type", "doctor");
        editor.commit();
        startActivity(intent);
    }//end of goToPatientHome()

    public void init() {
        //linking
        doctorEmail = (EditText) findViewById(R.id.doctor_login_emailET);
        doctorPassword = (EditText) findViewById(R.id.doctor_login_passowrdET);
        forgetPassword = (TextView) findViewById(R.id.doctor_forget_password);
        doctorLogingBT = (Button) findViewById(R.id.doctor_loginBT);

        //for dialog msg
        builder = new AlertDialog.Builder(this);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("جاري تسجيل الدخول...");
        mProgress.setMessage("فضلًا انتظر...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.doctor_login_toolbar);
        toolbar.setTitle("تسجيل الدخول");
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
