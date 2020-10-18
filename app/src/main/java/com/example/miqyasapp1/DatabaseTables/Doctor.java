package com.example.miqyasapp1.DatabaseTables;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class Doctor {
    public String name,email,employee_number,password,phone_number,user_type,id;
    public Patient patient;


    public Doctor() {
        // Default constructor required for calls to DataSnapshot.getValue(Patient.class)
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Doctor(String name, String employee_number) {
        this.name = name;
        this.employee_number = employee_number;
    }

    public Doctor(String email, String employee_number, String password, String phone_number) {
        this.email = email;
        this.employee_number = employee_number;
        this.password = password;
        this.phone_number = phone_number;
    }
    public Doctor(String name, String email, String employee_number, String password, String phone_number, String user_type, String id) {
        this.name = name;
        this.email = email;
        this.employee_number = employee_number;
        this.password = password;
        this.phone_number = phone_number;
        this.user_type = user_type;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmployee_number(String employee_number) {
        this.employee_number = employee_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmployee_number() {
        return employee_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}//end of class

