package com.example.miqyasapp1.ui.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.miqyasapp1.R;
import com.example.miqyasapp1.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingFragment extends Fragment {
    private ProgressDialog mProgress;
    TextView share;
    Switch simpleSwitch;
    LinearLayout logout, deleteAccount;
    AlertDialog.Builder builder;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        init(view);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String usertype = sharedPreferences.getString("user_type", "default");
        if (usertype.equals("admin")) {
            deleteAccount.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            simpleSwitch.setVisibility(View.GONE);

        }

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("هل أنت متأكد تريد حذف الحساب نهائيًا\n وحذف جميع البيانات المسجلة؟").setTitle("حذف الحساب")
                        .setIcon(R.drawable.delete_warning)
                        .setCancelable(false)
                        .setPositiveButton("متأكد", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mProgress.show();
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mProgress.hide();
                                        }
                                    }
                                });
                                startActivity(new Intent(getContext(), SplashActivity.class));
                            }
                        }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("هل أنت متأكد تريد تسجيل الخروج؟").setTitle("تسجيل الخروج")
                        .setIcon(R.drawable.logout)
                        .setCancelable(false)
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getContext(), SplashActivity.class));
                            }
                        }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }

            //todo notification and add share or about us....
        });


        return view;
    }//end of onCreateView()

    public void init(View view) {
        builder = new AlertDialog.Builder(getContext());
        user = FirebaseAuth.getInstance().getCurrentUser();

        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("جاري حذف الحساب نهائيًا...");
        mProgress.setMessage("فضلًا انتظر...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.hide();

        //share=(TextView)view.findViewById(R.id.share);
        deleteAccount = (LinearLayout) view.findViewById(R.id.deleteAccountLL);
        logout = (LinearLayout) view.findViewById(R.id.signoutLL);
        share=(TextView)view.findViewById(R.id.share);
        simpleSwitch=(Switch)view.findViewById(R.id.simpleSwitch);

    }
}//end of fragment