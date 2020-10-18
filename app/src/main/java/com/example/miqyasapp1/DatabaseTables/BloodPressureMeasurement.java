package com.example.miqyasapp1.DatabaseTables;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BloodPressureMeasurement {
    private String id,fileNumber,sbp,dbp,measurementDate;

    public BloodPressureMeasurement() {
    }

    public BloodPressureMeasurement(String id, String fileNumber, String sbp, String dbp, String measurementDate) {
        this.id = id;
        this.fileNumber = fileNumber;
        this.sbp = sbp;
        this.dbp = dbp;
        this.measurementDate = measurementDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public void setSbp(String sbp) {
        this.sbp = sbp;
    }

    public void setDbp(String dbp) {
        this.dbp = dbp;
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

    public String getSbp() {
        return sbp;
    }

    public String getDbp() {
        return dbp;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }
}//end of class
