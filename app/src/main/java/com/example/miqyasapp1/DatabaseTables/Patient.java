package com.example.miqyasapp1.DatabaseTables;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Patient {
    public String name, email, fileNumber, password, phone, age, user_type, id, gender, height, weight;
    public String bmi;
    public String lifestyle;
    public String doctorNote;

    public Patient() {
        // Default constructor required for calls to DataSnapshot.getValue(Patient.class)
    }

    public Patient(String name, String fileNumber) {
        this.name = name;
        this.fileNumber = fileNumber;
    }

    public Patient(String name, String email, String fileNumber, String password, String phone, String user_type, String id) {
        this.name = name;
        this.email = email;
        this.fileNumber = fileNumber;
        this.password = password;
        this.phone = phone;
        this.user_type = user_type;
        this.id = id;
    }

    public Patient(String name, String email, String fileNumber, String password, String phone, String age, String user_type, String id, String gender, String height, String weight, String bmi, String lifestyle, String doctorNote) {
        this.name = name;
        this.email = email;
        this.fileNumber = fileNumber;
        this.password = password;
        this.phone = phone;
        this.age = age;
        this.user_type = user_type;
        this.id = id;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.lifestyle = lifestyle;
        this.doctorNote = doctorNote;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBmi() {
        return bmi;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public void setLifestyle(String lifestyle) {
        this.lifestyle = lifestyle;
    }

    public String getLifestyle() {
        return lifestyle;
    }

    public void setDoctorNote(String doctorNote) {
        this.doctorNote = doctorNote;
    }

    public String getDoctorNote() {
        return doctorNote;
    }
}//end of class