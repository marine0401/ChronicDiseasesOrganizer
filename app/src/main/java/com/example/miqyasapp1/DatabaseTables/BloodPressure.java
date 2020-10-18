package com.example.miqyasapp1.DatabaseTables;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BloodPressure {
    public String id,fileNumber,classification,measurementDate;
    public double whr,sbp,dbp;
    public int obese;

    public BloodPressure() {
        // Default constructor required for calls to DataSnapshot.getValue(BloodPressure.class)

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public void setWhr(double whr) {
        this.whr = whr;
    }

    public void setSbp(double sbp) {
        this.sbp = sbp;
    }

    public void setDbp(double dbp) {
        this.dbp = dbp;
    }

    public String getId() {
        return id;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public double getWhr() {
        return whr;
    }

    public double getSbp() {
        return sbp;
    }

    public double getDbp() {
        return dbp;
    }

    public void setObese(int obese) {
        this.obese = obese;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getObese() {
        return obese;
    }

    public String getClassification() {
        return classification;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }
}//end of class
