package com.example.miqyasapp1.DatabaseTables;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Diabetes {
    public String id,fileNumber,classification,measurementDate;
    public double glucose,bloodPressure,skinThickness,diabetesPedigreeFunction;

    public Diabetes() {
        // Default constructor required for calls to DataSnapshot.getValue(Diabetes.class)

    }

    public double getGlucose() {
        return glucose;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public double getSkinThickness() {
        return skinThickness;
    }

    public double getDiabetesPedigreeFunction() {
        return diabetesPedigreeFunction;
    }


    public void setGlucose(double glucose) {
        this.glucose = glucose;
    }

    public void setBloodPressure(double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public void setSkinThickness(double skinThickness) {
        this.skinThickness = skinThickness;
    }

    public void setDiabetesPedigreeFunction(double diabetesPedigreeFunction) {
        this.diabetesPedigreeFunction = diabetesPedigreeFunction;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public String getClassification() {
        return classification;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getId() {
        return id;
    }

    public String getFileNumber() {
        return fileNumber;
    }
}//end of class
