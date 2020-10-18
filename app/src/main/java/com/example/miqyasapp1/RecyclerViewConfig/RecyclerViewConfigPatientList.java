package com.example.miqyasapp1.RecyclerViewConfig;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miqyasapp1.Doctor.PatientOfDoctor;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.Patient;

import java.util.List;

//DONE
//The patient list in doctor page
public class RecyclerViewConfigPatientList {
    private Context context;
    private PatientListAdapter patientAdapter;

    public void setCofig(RecyclerView recyclerView, Context context, List<Patient> patients, List<String> keys) {
        this.context = context;
        patientAdapter = new PatientListAdapter(patients, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(patientAdapter);
    }

    class PatientItemView extends RecyclerView.ViewHolder {

        private TextView filenum, name, gender, age, bmi, classification, lifestyle;
        private String key;
        private final String USER_TYPE = "patient";


        public PatientItemView(ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.cardview_doctor, parent, false));

            //what you want to display in list?
            filenum = (TextView) itemView.findViewById(R.id.patient_filenumber);
            name = (TextView) itemView.findViewById(R.id.patient_name);
            age = (TextView) itemView.findViewById(R.id.ageTV);
            gender = (TextView) itemView.findViewById(R.id.genderTV);
            bmi = (TextView) itemView.findViewById(R.id.bmiTV);
            classification = (TextView) itemView.findViewById(R.id.classificationTV);
            lifestyle = (TextView) itemView.findViewById(R.id.lifestyleTV);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PatientOfDoctor.class);
                    intent.putExtra("key", key);
                    intent.putExtra("fileNumber", filenum.getText().toString());
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("age", age.getText().toString());
                    intent.putExtra("gender", gender.getText().toString());
                    intent.putExtra("bmi", bmi.getText().toString());
                    intent.putExtra("user_type", USER_TYPE);

                    context.startActivity(intent);
                }
            });
        }

        public void bind(Patient patient, String key) {
            filenum.setText(patient.getFileNumber());
            name.setText(patient.getName());
            age.setText(patient.getAge());
            gender.setText(patient.getGender());
            bmi.setText(String.valueOf(patient.getBmi()));
            this.key = key;
        }
    }

    class PatientListAdapter extends RecyclerView.Adapter<RecyclerViewConfigPatientList.PatientItemView> {

        private List<Patient> patients;
        private List<String> mkeys;

        public PatientListAdapter(List<Patient> patients, List<String> mkeys) {
            this.patients = patients;
            this.mkeys = mkeys;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewConfigPatientList.PatientItemView holder, int position) {
            holder.bind(patients.get(position), mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return patients.size();
        }

        @NonNull
        @Override
        public RecyclerViewConfigPatientList.PatientItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerViewConfigPatientList.PatientItemView(parent);
        }

    }


}//end of class

