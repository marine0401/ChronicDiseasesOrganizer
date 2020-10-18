package com.example.miqyasapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miqyasapp1.DatabaseTables.Doctor;
import com.example.miqyasapp1.DatabaseTables.Patient;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    List<Patient> patient;
    List<Doctor> doctors;


    public MyAdapter(Context context, List<Patient> patient, List<Doctor> doctors) {
        this.context = context;
        this.patient = patient;
        this.doctors = doctors;

    }
//    public MyAdapter(Context context, List<Doctor> doctors) {
//        this.context = context;
//        this.doctors = doctors;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.patientFileNumber.setText(patient.get(position).getFileNumber());
        holder.patientEmail.setText(patient.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        if (!patient.equals(null))
            return patient.size();
        else
            return doctors.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView patientFileNumber, patientEmail;

        public MyViewHolder(View itemView) {
            super(itemView);
            patientFileNumber = (TextView) itemView.findViewById(R.id.patient_filenumber);
            patientEmail = (TextView) itemView.findViewById(R.id.patient_email);
        }
    }
}//end of class