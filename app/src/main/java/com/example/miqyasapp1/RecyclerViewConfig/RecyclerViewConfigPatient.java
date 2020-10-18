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

import com.example.miqyasapp1.Admin.PatientDetailsActivity;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.Patient;

import java.util.List;

//DONE
//The patient list in admin page
public class RecyclerViewConfigPatient {

    private Context context;
    private PatientAdapter patientAdapter;



    public void setCofig(RecyclerView recyclerView, Context context, List<Patient> patients, List<String> keys) {
        this.context = context;
        patientAdapter = new PatientAdapter(patients, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(patientAdapter);
    }

    class PatientItemView extends RecyclerView.ViewHolder {
        private TextView filenum;
        private TextView email;
        private TextView pass;
        private TextView name;
        private TextView phonenum;

        private String key;
        private final String USER_TYPE = "patient";

        public PatientItemView(ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.cardview, parent, false));

            //what you want to display in list?
            filenum = (TextView) itemView.findViewById(R.id.patient_filenumber);
            email = (TextView) itemView.findViewById(R.id.patient_email);
            pass = (TextView) itemView.findViewById(R.id.pass);
            name = (TextView) itemView.findViewById(R.id.name);
            phonenum = (TextView) itemView.findViewById(R.id.phone);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PatientDetailsActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("fileNumber", filenum.getText().toString());
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("password", pass.getText().toString());
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("phone_number", phonenum.getText().toString());
                    intent.putExtra("user_type", USER_TYPE);
                    context.startActivity(intent);

                }
            });
        }
        public void bind(Patient patient, String key) {
            filenum.setText(patient.getFileNumber());
            email.setText(patient.getEmail());
            pass.setText(patient.getPassword());
            name.setText(patient.getName());
            phonenum.setText(patient.getPhone());
            this.key = key;
        }

    }
    class PatientAdapter extends RecyclerView.Adapter<PatientItemView> {

        private List<Patient> patients;
        private List<String> mkeys;

        public PatientAdapter(List<Patient> patients, List<String> mkeys) {
            this.patients = patients;
            this.mkeys = mkeys;
        }


        @Override
        public void onBindViewHolder(@NonNull PatientItemView holder, int position) {
            holder.bind(patients.get(position), mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return patients.size();
        }

        @NonNull
        @Override
        public PatientItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PatientItemView(parent);
        }

    }


}//end of class


