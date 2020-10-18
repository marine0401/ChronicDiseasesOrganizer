package com.example.miqyasapp1.DatabaseTables;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DiabetesMeasurement {
    public String id,fileNumber,glucose,measurementDate;

    public DiabetesMeasurement() {
    }

    public DiabetesMeasurement(String id, String fileNumber, String glucose, String measurementDate) {
        this.id = id;
        this.fileNumber = fileNumber;
        this.glucose = glucose;
        this.measurementDate = measurementDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public void setGlucose(String glucose) {
        this.glucose = glucose;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public String getId() {
        return id;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public String getGlucose() {
        return glucose;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }
}//end of class
