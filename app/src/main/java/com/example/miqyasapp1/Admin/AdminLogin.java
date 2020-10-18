package com.example.miqyasapp1.Admin;

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
import com.example.miqyasapp1.ui.home.AdminHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminLogin extends BaseActivity {
    private ProgressDialog mProgress;
    private Toolbar toolbar;
    private EditText adminEmail, adminPassword;
    private TextView forgetPassword;
    private Button adminLogingBT;

    //firbase
    FirebaseUser user;
    FirebaseAuth mAuth;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        init();
        setToolbar();

        adminLogingBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                String adminEmailInput = adminEmail.getText().toString();//getting patient's input and save it to check the string of patient name
                String adminPasswordInput = adminPassword.getText().toString();//getting patient's password as well

                if (adminEmailInput.equals("") || adminPasswordInput.equals("")) {
                    adminEmail.setError("لم يتم إدخال رقم الملف أو كلمة المرور!");
                } else if (!EMailValidation(adminEmailInput)) {
                    adminEmail.setError("فضلًا ادخِل عنوان بريد صالح." +
                            "\n" + "مثال:" +
                            "\n" +
                            "Example@example.com");
                }// end of isInputEditTextFilled
                else {
                    login();
                }//end of inner if
            }
        });


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                startActivity(new Intent(AdminLogin.this, ForgetPassword.class));
            }
        });
    }//end of onCreate()


    private void login() {
        if (!validateForm()) {
            return;
        }
        mProgress.show();
        if (adminEmail.getText().toString().equals("admin@gmail.com")
                && adminPassword.getText().toString().equals("123")) {//alharbiNada as one admin only
            goToAdminHome();

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
            adminEmail.setText("");
            adminPassword.setText("");
        }//end of else

        mProgress.hide();

    }//end of login()


    private boolean validateForm() {
        boolean valid = true;
        String emailInput = adminEmail.getText().toString();
        //check if empty fields
        if (TextUtils.isEmpty(emailInput)) {
            adminEmail.setError("فضلًا ادخل البريد الالكتروني.");
            valid = false;
        } else {
            adminEmail.setError(null);
        }
        String password = adminPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            adminPassword.setError("فضلًا ادخل الرقم السري.");
            valid = false;
        } else {
            adminPassword.setError(null);
        }
        if (!EMailValidation(emailInput)) {
            adminEmail.setError("فضلًا ادخِل عنوان بريد صالح." +
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

    //
//    //todo firebase will login automaticlly if not logged out
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if patient is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        goToAdminHome();
//        //updateUI(currentUser);
//    }
    public void init() {
        //linking
        adminEmail = (EditText) findViewById(R.id.admin_login_emailET);
        adminPassword = (EditText) findViewById(R.id.admin_login_passowrdET);
        forgetPassword = (TextView) findViewById(R.id.admin_forget_password);
        adminLogingBT = (Button) findViewById(R.id.admin_loginBT);

        //for dialog msg
        builder = new AlertDialog.Builder(this);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("جاري تسجيل الدخول...");
        mProgress.setMessage("فضلًا انتظر...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }


    public void setToolbar() {
        toolbar = findViewById(R.id.admin_login_toolbar);
        toolbar.setTitle("تسجيل الدخول");
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


    public void goToAdminHome() {
        Intent intent = new Intent(this, MainActivity.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_type", "admin");
        editor.commit();
        startActivity(intent);
    }

}//end of class
