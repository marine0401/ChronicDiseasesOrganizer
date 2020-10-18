package com.example.miqyasapp1.DatabaseTables;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Kidney {

    public double sg, su, bp, al, bu, pot, wc;
    public int rbc, ba, cad, pe, appet;//0 or 1
    public String id, fileNumber, classification, measurementDate;


    public Kidney() {
        // Default constructor required for calls to DataSnapshot.getValue(Kidney.class)
    }

    public String getId() {
        return id;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public double getSg() {
        return sg;
    }

    public double getSu() {
        return su;
    }

    public int getRbc() {
        return rbc;
    }


    public int getAppet() {
        return appet;
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

    public void setSg(double sg) {
        this.sg = sg;
    }

    public void setSu(double su) {
        this.su = su;
    }

    public void setRbc(int rbc) {
        this.rbc = rbc;
    }

    public void setAppet(int appet) {
        this.appet = appet;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public double getBp() {
        return bp;
    }

    public void setBp(double bp) {
        this.bp = bp;
    }

    public void setAl(double al) {
        this.al = al;
    }

    public void setBu(double bu) {
        this.bu = bu;
    }

    public void setPot(double pot) {
        this.pot = pot;
    }

    public void setWc(double wc) {
        this.wc = wc;
    }

    public void setBa(int ba) {
        this.ba = ba;
    }

    public void setCad(int cad) {
        this.cad = cad;
    }

    public void setPe(int pe) {
        this.pe = pe;
    }

    public double getAl() {
        return al;
    }

    public double getBu() {
        return bu;
    }

    public double getPot() {
        return pot;
    }

    public double getWc() {
        return wc;
    }

    public int getBa() {
        return ba;
    }

    public int getCad() {
        return cad;
    }

    public int getPe() {
        return pe;
    }
}//end of class

