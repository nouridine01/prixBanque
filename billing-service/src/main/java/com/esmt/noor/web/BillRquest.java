package com.esmt.noor.web;

import java.sql.Date;

public class BillRquest {
    private String email;
    private double montant;
    private Date date;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
