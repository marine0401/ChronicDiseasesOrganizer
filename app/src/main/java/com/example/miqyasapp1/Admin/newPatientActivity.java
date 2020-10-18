package com.example.miqyasapp1.Admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.miqyasapp1.BaseActivity;
import com.example.miqyasapp1.DialogFactory;
import com.example.miqyasapp1.Patient.FirebaseDatabaseHelperPatient;
import com.example.miqyasapp1.OneButtonDialog;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.BloodPressure;
import com.example.miqyasapp1.DatabaseTables.Diabetes;
import com.example.miqyasapp1.DatabaseTables.Kidney;
import com.example.miqyasapp1.DatabaseTables.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class newPatientActivity extends BaseActivity {
    private ProgressDialog mProgress;
    Toolbar toolbar;
    public static EditText name, email, fileNumber, password, phoneNumber, age, obese, bmi, whr, sbp, dbp,
            glucose, bloodPressure, skinThickness, diabetesPedigreeFunction,
            sg, al, su, rbc, ba, bu, pot, wc, cad, appet, pe;

    private final String USER_TYPE = "patient";
    private Button addBt, doneBt;
    private LinearLayout doneLL, patientInputLL;
    FirebaseAuth mAuth;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("patient");
    public String id;
    public static String bpId;
    public static String DId;
    public static String KId;
    public String gender;
    RadioGroup genderGroup;
    RadioButton genderBT;
    //ref to each table in Database
    static DatabaseReference bpRef = FirebaseDatabase.getInstance().getReference("BloodPressure");
    static DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Diabetes");
    static DatabaseReference kRef = FirebaseDatabase.getInstance().getReference("Kidney");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_patient);
        init();
        setToolbar();

        //store generated id for both patient and diseases table
        id = ref.child("id").push().getKey();//patient id
        bpId = bpRef.child("id").push().getKey();
        DId = dRef.child("id").push().getKey();
        KId = kRef.child("id").push().getKey();

        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                checkPatientInput();

            }
        });
    }//end of onCreate()

    //making sure all the inputs are valid, and the required filed are not empty
    private void checkPatientInput() {
        //to patient table
        final String nameInput = name.getText().toString().trim();
        final String emailInput = email.getText().toString().trim();
        final String fileNumberInput = fileNumber.getText().toString().trim();
        final String passwordInput = password.getText().toString().trim();
        final String ageInput = age.getText().toString().trim();

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
        if (fileNumberInput.isEmpty()) {
            fileNumber.setError("مطلوب.");
            fileNumber.requestFocus();
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
        if (ageInput.isEmpty()) {
            age.setError("مطلوب.");
            age.requestFocus();
            return;
        }

        //check if exist fileNumber in db, to avoid two patients have same fileNumber
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("patient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (Objects.equals(ds.child("fileNumber").getValue(), (fileNumberInput))) {
                        fileNumber.setError("تم استخدام رقم الملف من قبل.");
                        mProgress.hide();
                        return;//stop
                    } else {
                        PatientLogin();

                    }
                }//end of for loop
            }//end of onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }//end of checkPatientInput()

    public void PatientLogin() {
        mProgress.show();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Patient patient = new Patient();
                            patient.setId(id);
                            patient.setFileNumber(fileNumber.getText().toString());
                            patient.setEmail(email.getText().toString());
                            patient.setName(name.getText().toString());
                            patient.setPassword(password.getText().toString());
                            patient.setPhone(phoneNumber.getText().toString());
                            patient.setAge(age.getText().toString());
                            patient.setGender(gender);//string

                            if (!(bmi.getText().toString().isEmpty())) {
                                patient.setBmi(bmi.getText().toString());
                            }

                            patient.setUser_type(USER_TYPE);
                            new FirebaseDatabaseHelperPatient().addPatient(patient, new FirebaseDatabaseHelperPatient.DataStatus() {
                                @Override
                                public void DataIsLoaded(List<Patient> patients, List<String> keys) {

                                }
                                @Override
                                public void DataIsInserted() {
                                    mProgress.hide();
                                    //patientInputLL.setVisibility(View.GONE);
                                    //addBt.setVisibility(View.GONE);
                                    //doneLL.setVisibility(View.VISIBLE);
                                    onBtnSuccessClicked();
                                    return;
                                }

                                @Override
                                public void DataIsUpdated() {

                                }

                                @Override
                                public void DataIsDeleted() {

                                }
                            });

                            //to blood-pressure table
                            final String obeseInput = obese.getText().toString().trim();
                            final String whrInput = whr.getText().toString().trim();
                            final String sbpInput = sbp.getText().toString().trim();
                            final String dbpInput = dbp.getText().toString().trim();

                            final boolean isBPInputEmpty = obeseInput.isEmpty() || whrInput.isEmpty() || sbpInput.isEmpty() || dbpInput.isEmpty();
                            if (gender.equals("Male")) {
                                if (!isBPInputEmpty) {
                                    try {
                                        postRequestManPredict();//will return classification
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(newPatientActivity.this, "blood pressure added to table", Toast.LENGTH_LONG).show();
                                }
                            }//end of if

                            if (!isBPInputEmpty) {
                                if (gender.equals("Female")) {
                                    try {
                                        postRequestFemalePredict();//will return classification
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(newPatientActivity.this, "blood pressure added to table", Toast.LENGTH_LONG).show();
                                } else if (gender.equals("Female")) {
                                    try {
                                        postRequestManPredict();//will return classification
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }//end of if

                            //to Diabetes table
                            final String glucoseInput = glucose.getText().toString().trim();
                            final String bloodPressureInput = bloodPressure.getText().toString().trim();
                            final String skinThicknessInput = skinThickness.getText().toString().trim();
                            final String diabetesPedigreeFunctionInput = diabetesPedigreeFunction.getText().toString().trim();

                            final boolean isDInputEmpty = glucoseInput.isEmpty() || bloodPressureInput.isEmpty() || skinThicknessInput.isEmpty() || diabetesPedigreeFunctionInput.isEmpty();
                            if (!isDInputEmpty) {
                                try {
                                    postRequestDiabetesPredict();//will return classification
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(newPatientActivity.this, "Diabetes added to table", Toast.LENGTH_LONG).show();
                            }

                            //to Kidney table
                            final String sgInput = sg.getText().toString().trim();
                            final String alInput = al.getText().toString().trim();
                            final String suInput = su.getText().toString().trim();
                            final String rbcInput = rbc.getText().toString().trim();
                            final String baInput = ba.getText().toString().trim();
                            final String buInput = bu.getText().toString().trim();
                            final String potInput = pot.getText().toString().trim();
                            final String wcInput = wc.getText().toString().trim();
                            final String cadInput = cad.getText().toString().trim();
                            final String appetInput = appet.getText().toString().trim();
                            final String peInput = pe.getText().toString().trim();
                            final boolean isKInputEmpty = sgInput.isEmpty() || alInput.isEmpty() || suInput.isEmpty() || rbcInput.isEmpty()
                                    || bloodPressureInput.isEmpty() || baInput.isEmpty() || buInput.isEmpty() || potInput.isEmpty()
                                    || wcInput.isEmpty() || cadInput.isEmpty() || appetInput.isEmpty() || peInput.isEmpty();
                            if (!isKInputEmpty) {
                                try {
                                    postRequestKidneyPredict();//will return classification
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(newPatientActivity.this, "Kidney added to table", Toast.LENGTH_LONG).show();
                            }
                        }//end of if successful
                    }
                });

    }//end of DoctorLogin()

    public void postRequestManPredict() throws IOException {
        Calendar calendar = Calendar.getInstance();
        String measurementDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        final BloodPressure bloodPressure = new BloodPressure();
        bloodPressure.setId(bpId);
        bloodPressure.setFileNumber(fileNumber.getText().toString());
        bloodPressure.setWhr(Double.parseDouble(whr.getText().toString()));
        bloodPressure.setSbp(Double.parseDouble(sbp.getText().toString()));
        bloodPressure.setDbp(Double.parseDouble(dbp.getText().toString()));
        bloodPressure.setObese(Integer.parseInt(obese.getText().toString()));
        bloodPressure.setMeasurementDate(measurementDate);

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.8.100:5000/manpredict";

        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("Age", Integer.parseInt(age.getText().toString()));
            postdata.put("Obese", Integer.parseInt(obese.getText().toString()));//Integer.parseInt(obese.getText().toString())
            postdata.put("bmi", Double.parseDouble(bmi.getText().toString()));//Double.parseDouble(bmi.getText().toString())
            postdata.put("whr", Double.parseDouble(whr.getText().toString()));//Double.parseDouble(whr.getText().toString())
            postdata.put("SBP", Double.parseDouble(sbp.getText().toString()));//Double.parseDouble(sbp.getText().toString())
            postdata.put("DBP", Double.parseDouble(dbp.getText().toString()));//Double.parseDouble(dbp.getText().toString())
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("FAILLLLES_Male", mMessage);
                //call.cancel();
            }

            String myResponse;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    myResponse = response.body().string();
                    final String classification = myResponse;
                    bloodPressure.setClassification(classification);
                    bpRef.push().setValue(bloodPressure);
                    Log.w("RESPONNNSE_Male", classification);

                    newPatientActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.w("RESPONNNSE_Male", myResponse);
                            //Toast.makeText(newPatientActivity.this, myResponse, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }//end of PostReq

    public void postRequestFemalePredict() throws IOException {
        Calendar calendar = Calendar.getInstance();
        String measurementDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        final BloodPressure bloodPressure = new BloodPressure();
        bloodPressure.setId(bpId);
        bloodPressure.setFileNumber(fileNumber.getText().toString());
        bloodPressure.setWhr(Double.parseDouble(whr.getText().toString()));
        bloodPressure.setSbp(Double.parseDouble(sbp.getText().toString()));
        bloodPressure.setDbp(Double.parseDouble(dbp.getText().toString()));
        bloodPressure.setObese(Integer.parseInt(obese.getText().toString()));
        bloodPressure.setMeasurementDate(measurementDate);

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.8.100:5000/femalepredict";
        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("Age", Integer.parseInt(age.getText().toString()));
            postdata.put("Obese", Integer.parseInt(obese.getText().toString()));//Integer.parseInt(obese.getText().toString())
            postdata.put("bmi", bmi.getText().toString());//Double.parseDouble(bmi.getText().toString())
            postdata.put("whr", Double.parseDouble(whr.getText().toString()));//Double.parseDouble(whr.getText().toString())
            postdata.put("SBP", Double.parseDouble(sbp.getText().toString()));//Double.parseDouble(sbp.getText().toString())
            postdata.put("DBP", Double.parseDouble(dbp.getText().toString()));//Double.parseDouble(dbp.getText().toString())
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("FAILLLLES_female", mMessage);
                //call.cancel();
            }

            String myResponse;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    myResponse = response.body().string();
                    final String classification = myResponse;
                    bloodPressure.setClassification(classification);
                    bpRef.push().setValue(bloodPressure);
                    Log.w("RESPONNNSE_Female", classification);

                    newPatientActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.w("RESPONNNSE_Female", myResponse);
                            //Toast.makeText(newPatientActivity.this, myResponse, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }//end of PostReq

    public void postRequestDiabetesPredict() throws IOException {
        Calendar calendar = Calendar.getInstance();
        String measurementDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        final Diabetes diabetes = new Diabetes();
        diabetes.setId(DId);//uniqe id
        diabetes.setFileNumber(fileNumber.getText().toString());//patient file
        diabetes.setGlucose(Double.parseDouble(glucose.getText().toString()));
        diabetes.setBloodPressure(Double.parseDouble(bloodPressure.getText().toString()));
        diabetes.setSkinThickness(Double.parseDouble(skinThickness.getText().toString()));
        diabetes.setDiabetesPedigreeFunction(Double.parseDouble(diabetesPedigreeFunction.getText().toString()));
        diabetes.setMeasurementDate(measurementDate);

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.8.100:5000/diabetespredict";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("Glucose", Double.parseDouble(glucose.getText().toString()));
            postdata.put("BloodPressure", Double.parseDouble(bloodPressure.getText().toString()));
            postdata.put("SkinThickness", Double.parseDouble(skinThickness.getText().toString()));
            postdata.put("BMI", Double.parseDouble(bmi.getText().toString()));
            postdata.put("DiabetesPedigreeFunction", Double.parseDouble(diabetesPedigreeFunction.getText().toString()));
            postdata.put("Age", Integer.parseInt(age.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("FAILLLLES_Diabetes", mMessage);
                //call.cancel();
            }

            String myResponse;
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    myResponse = response.body().string();
                    final String classification = myResponse;
                    diabetes.setClassification(classification);
                    dRef.push().setValue(diabetes);
                    Log.w("RESPONNNSE_Diabetes", classification);

                    newPatientActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.w("RESPONNNSE_Diabetes", myResponse);
                            //Toast.makeText(newPatientActivity.this, myResponse, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }//end of PostReq

    public void postRequestKidneyPredict() throws IOException {
        Calendar calendar = Calendar.getInstance();
        String measurementDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());


        final Kidney kidney = new Kidney();
        kidney.setId(KId);//uniqe id
        kidney.setFileNumber(fileNumber.getText().toString());//patient file

        kidney.setSg(Double.parseDouble(sg.getText().toString()));
        kidney.setAl(Double.parseDouble(al.getText().toString()));
        kidney.setSu(Double.parseDouble(su.getText().toString()));
        kidney.setRbc(Integer.parseInt(rbc.getText().toString()));
        kidney.setBp(Double.parseDouble(bloodPressure.getText().toString()));
        kidney.setBa(Integer.parseInt(ba.getText().toString()));
        kidney.setBu(Double.parseDouble(bu.getText().toString()));
        kidney.setPot(Double.parseDouble(pot.getText().toString()));
        kidney.setWc(Double.parseDouble(wc.getText().toString()));
        kidney.setCad(Integer.parseInt(cad.getText().toString()));
        kidney.setAppet(Integer.parseInt(appet.getText().toString()));
        kidney.setPe(Integer.parseInt(pe.getText().toString()));
        kidney.setMeasurementDate(measurementDate);

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.8.100:5000/kidneypredict";
        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("age", Integer.parseInt(age.getText().toString()));
            postdata.put("bp", Double.parseDouble(bloodPressure.getText().toString()));
            postdata.put("sg", Double.parseDouble(sg.getText().toString()));
            postdata.put("al", Double.parseDouble(al.getText().toString()));
            postdata.put("su", Double.parseDouble(su.getText().toString()));
            postdata.put("rbc", Integer.parseInt(rbc.getText().toString()));
            postdata.put("ba", Integer.parseInt(ba.getText().toString()));
            postdata.put("bu", Double.parseDouble(bu.getText().toString()));
            postdata.put("pot", Double.parseDouble(pot.getText().toString()));
            postdata.put("wc", Double.parseDouble(wc.getText().toString()));
            postdata.put("cad", Integer.parseInt(cad.getText().toString()));
            postdata.put("appet", Integer.parseInt(appet.getText().toString()));
            postdata.put("pe", Integer.parseInt(pe.getText().toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("FAILLLLES_Kidney", mMessage);
            }

            String myResponse;

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    myResponse = response.body().string();
                    final String classification = myResponse;
                    kidney.setClassification(classification);
                    kRef.push().setValue(kidney);
                    Log.w("RESPONNNSE_Kidney", classification);

                    newPatientActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.w("RESPONNNSE_Kidney", myResponse);
                        }
                    });
                }
            }
        });
    }//end of PostReq

    public void init() {
        mAuth = FirebaseAuth.getInstance();
        name = (EditText) findViewById(R.id.edit_text_name);
        email = (EditText) findViewById(R.id.edit_text_email);
        fileNumber = (EditText) findViewById(R.id.edit_text_filenumber);
        password = (EditText) findViewById(R.id.edit_text_password);
        phoneNumber = (EditText) findViewById(R.id.edit_text_phone);
        age = (EditText) findViewById(R.id.age);
        bmi = (EditText) findViewById(R.id.bmi);

        //blood pressure
        obese = (EditText) findViewById(R.id.obese);
        whr = (EditText) findViewById(R.id.whr);
        sbp = (EditText) findViewById(R.id.sbp);
        dbp = (EditText) findViewById(R.id.dbp);

        //Diabetes
        glucose = (EditText) findViewById(R.id.glucose);
        bloodPressure = (EditText) findViewById(R.id.bloodPressure);
        skinThickness = (EditText) findViewById(R.id.skinThickness);
        diabetesPedigreeFunction = (EditText) findViewById(R.id.diabetesPedigreeFunction);

        //Kidney
        sg = (EditText) findViewById(R.id.sg);
        al = (EditText) findViewById(R.id.al);
        su = (EditText) findViewById(R.id.su);
        rbc = (EditText) findViewById(R.id.rbc);
        ba = (EditText) findViewById(R.id.ba);
        bu = (EditText) findViewById(R.id.bu);
        pot = (EditText) findViewById(R.id.pot);
        wc = (EditText) findViewById(R.id.wc);
        cad = (EditText) findViewById(R.id.cad);
        appet = (EditText) findViewById(R.id.appet);
        pe = (EditText) findViewById(R.id.pe);
        genderGroup = (RadioGroup) findViewById(R.id.genderRadio);
        genderBT = (RadioButton) findViewById(genderGroup.getCheckedRadioButtonId());

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.male:
                        gender = "Male";
                        break;
                    case R.id.female:
                        gender = "Female";
                        break;
                }
            }
        });

        addBt = (Button) findViewById(R.id.button_add_patient);
        doneLL = (LinearLayout) findViewById(R.id.pdoneLL);
        doneBt = (Button) findViewById(R.id.pdoneBT);
        patientInputLL = (LinearLayout) findViewById(R.id.patientInputLL);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("جاري إضافة ملف مريض جديد...");
        mProgress.setMessage("فضلًا انتظر...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.hide();

    }//end of initializeValue

    //custom Dialog send successfully
    public void onBtnSuccessClicked() {
        OneButtonDialog oneButtonDialog =
                DialogFactory.makeSuccessDialog(R.string.success_title,
                        R.string.success_message,
                        R.string.success_button_text,
                        new OneButtonDialog.ButtonDialogAction() {
                            @Override
                            public void onButtonClicked() {
                                name.setText("");
                                email.setText("");
                                fileNumber.setText("");
                                password.setText("");
                                phoneNumber.setText("");
                                return;
                            }
                        });
        oneButtonDialog.show(getSupportFragmentManager(), OneButtonDialog.TAG);
    }

    public void setToolbar() {
        toolbar = findViewById(R.id.admin_patientAdd_toolbar);
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
