package com.example.miqyasapp1.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.miqyasapp1.DialogFactory;
import com.example.miqyasapp1.OneButtonDialog;
import com.example.miqyasapp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import javax.xml.transform.Result;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfileFragment extends Fragment {
    public EditText nameET, passET, emailET, phoneNumET;
    Button saveChanges;
    //firebase
    FirebaseUser user;
    DatabaseReference reference;
    public String id, email, password;
    AuthCredential credential;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameET = (EditText) view.findViewById(R.id.name_account);
        passET = (EditText) view.findViewById(R.id.pass_account);
        emailET = (EditText) view.findViewById(R.id.email_account);
        phoneNumET = (EditText) view.findViewById(R.id.phone_account);
        saveChanges = (Button) view.findViewById(R.id.saveChangesBtn);

        //firebase
        user = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String usertype = sharedPreferences.getString("user_type", "default");

        if (usertype.equals("patient")) {
            Log.d("TAG", "user_type" + usertype);
            reference = FirebaseDatabase.getInstance().getReference("patient");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //for loop to all database coloumns then check if any email match loged in user email
                        if (ds.child("email").getValue().equals(user.getEmail())) {
                            id = ds.getKey();
                            nameET.setText(ds.child("name").getValue(String.class));
                            passET.setText(ds.child("password").getValue(String.class));
                            password = ds.child("password").getValue(String.class);
                            emailET.setText(ds.child("email").getValue(String.class));
                            email = ds.child("email").getValue(String.class);
                            phoneNumET.setText(ds.child("phone").getValue(String.class));

                            credential = EmailAuthProvider
                                    .getCredential(user.getEmail(), password);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (usertype.equals("doctor")) {
            Log.d("TAG", "user_type" + usertype);
            reference = FirebaseDatabase.getInstance().getReference("doctor");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //for loop to all database coloumns then check if any email match loged in user email
                        if (Objects.equals(ds.child("email").getValue(), user.getEmail())) {
                            id = ds.getKey();
                            nameET.setText(ds.child("name").getValue(String.class));
                            passET.setText(ds.child("password").getValue(String.class));
                            password = ds.child("password").getValue(String.class);
                            emailET.setText(ds.child("email").getValue(String.class));
                            email = ds.child("email").getValue(String.class);
                            phoneNumET.setText(ds.child("phone").getValue(String.class));

                            credential = EmailAuthProvider
                                    .getCredential(email, password);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (usertype.equals("admin")) {
            Log.d("TAG", "user_type" + usertype);
            nameET.setText("Admin");
            passET.setText("123123");
            emailET.setText("alharbinada22@gmail.com");
            phoneNumET.setText("");

        }
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!usertype.equals("admin")) {
                    //update it in realtime database
                    reference.child(id).child("name").setValue(nameET.getText().toString());
                    reference.child(id).child("password").setValue(passET.getText().toString());
                    reference.child(id).child("email").setValue(emailET.getText().toString());
                    reference.child(id).child("phone").setValue(phoneNumET.getText().toString());
                    user.updateEmail(emailET.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                    }
                                }
                            });
                    user.updatePassword(passET.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                    }
                                }
                            });
                    onBtnSavedClicked();
                } else {
                    nameET.setText(nameET.getText().toString().trim());
                    passET.setText(passET.getText().toString().trim());
                    emailET.setText(emailET.getText().toString().trim());
                    phoneNumET.setText(phoneNumET.getText().toString().trim());
                    onBtnSavedClicked();
                }
            }
        });
        return view;
    }//end of onCreateView()

    //Custom Dialog saved successfully!
    public void onBtnSavedClicked() {
        OneButtonDialog oneButtonDialog =
                DialogFactory.makeSuccessDialog(R.string.success_title_save,
                        R.string.success_message,
                        R.string.success_button_text,
                        new OneButtonDialog.ButtonDialogAction() {
                            @Override
                            public void onButtonClicked() {

                            }
                        });
        oneButtonDialog.show(getFragmentManager(), OneButtonDialog.TAG);
    }

}//end of fragment