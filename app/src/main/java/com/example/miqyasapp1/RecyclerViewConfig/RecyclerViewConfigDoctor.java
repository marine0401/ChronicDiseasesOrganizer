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

import com.example.miqyasapp1.Admin.DoctorDetailsActivity;
import com.example.miqyasapp1.R;
import com.example.miqyasapp1.DatabaseTables.Doctor;

import java.util.List;

//DONE
//The doctor list in admin page

public class RecyclerViewConfigDoctor {

    private Context context;
    private DoctorAdapter doctorAdapter;

    public void setCofig(RecyclerView recyclerView, Context context, List<Doctor> doctors, List<String> keys) {
        this.context = context;
        doctorAdapter = new DoctorAdapter(doctors, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(doctorAdapter);
    }

    class DoctorItemView extends RecyclerView.ViewHolder {
        private TextView employeenum;
        private TextView email;
        private TextView pass;
        private TextView name;
        private TextView phonenum;
        private String key;
        private final String USER_TYPE = "doctor";
        public DoctorItemView(ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.cardview, parent, false));

            //what you want to display in list?
            employeenum = (TextView) itemView.findViewById(R.id.patient_filenumber);
            email = (TextView) itemView.findViewById(R.id.patient_email);
            pass = (TextView) itemView.findViewById(R.id.pass);
            name = (TextView) itemView.findViewById(R.id.name);
            phonenum = (TextView) itemView.findViewById(R.id.phone);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DoctorDetailsActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("employee_number", employeenum.getText().toString());
                    intent.putExtra("email", email.getText().toString());
                    intent.putExtra("password", pass.getText().toString());
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("phone_number", phonenum.getText().toString());
                    intent.putExtra("user_type", USER_TYPE);
                    context.startActivity(intent);

                }
            });
        }
        public void bind(Doctor doctor, String key) {
            employeenum.setText(doctor.getEmployee_number());
            email.setText(doctor.getEmail());
            pass.setText(doctor.getPassword());
            name.setText(doctor.getName());
            phonenum.setText(doctor.getPhone_number());
            this.key = key;
        }
    }

    class DoctorAdapter extends RecyclerView.Adapter<DoctorItemView> {

        private List<Doctor> doctors;
        private List<String> mkeys;

        public DoctorAdapter(List<Doctor> doctors, List<String> mkeys) {
            this.doctors = doctors;
            this.mkeys = mkeys;
        }


        @Override
        public void onBindViewHolder(@NonNull DoctorItemView holder, int position) {
            holder.bind(doctors.get(position), mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return doctors.size();
        }

        @NonNull
        @Override
        public DoctorItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DoctorItemView(parent);
        }

    }


}//end of class


