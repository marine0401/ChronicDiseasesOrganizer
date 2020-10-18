package com.example.miqyasapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends BaseActivity {
    private ProgressDialog mProgress;

    private EditText forgetPassEmailET;
    private Button confirmBT;
    Toolbar toolbar;

    //firebase
    FirebaseAuth mAuth;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        init();
        setToolbar();

        confirmBT.setOnClickListener(new View.OnClickListener() {
            String emailInput = forgetPassEmailET.getText().toString().trim();

            @Override
            public void onClick(View v) {

                //mProgress.show();
                mAuth.sendPasswordResetEmail(emailInput)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    builder.setMessage("تم إرسال رابط إعادة كلمة المرور لعنوان بريدك الإلكتروني!").setTitle("إعادة تعيين كلمة المرور")
                                            .setCancelable(false)
                                            .setNegativeButton("موافق", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    startActivity(new Intent(ForgetPassword.this, UsersPage.class));

                                                }
                                            });
                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    //mProgress.hide();
                                    String message = task.getException().getMessage();
                                    Toast.makeText(ForgetPassword.this, "Error Occured " + message,
                                            Toast.LENGTH_SHORT).show();
                                }//end of else
                            }//end of onComplete
                        });
            }//end of onClick()

        });
    }//end of onCreate()

    public void init() {
        //for dialog msg
        builder = new AlertDialog.Builder(this);

        //linking
        forgetPassEmailET = (EditText) findViewById(R.id.textInputEditTextEmail);
        confirmBT = (Button) findViewById(R.id.confirmBT);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("جاري تسجيل الدخول...");
        mProgress.setMessage("فضلًا انتظر...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        //firebase
        mAuth = FirebaseAuth.getInstance();

    }

    public void setToolbar() {
        toolbar = findViewById(R.id.forget_password_toolbar);
        toolbar.setTitle("إستعادة كلمة المرور");
        setSupportActionBar(toolbar);

        //setting back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//setting the tool bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);//making it showing

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//going automatically to the previous page
            }
        });

    }

}//end of class
