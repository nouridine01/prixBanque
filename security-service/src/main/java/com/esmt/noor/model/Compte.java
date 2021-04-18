package com.esmt.noor.model;


import com.esmt.noor.entities.AppUser;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;


public class Compte {
    private Long id;
    private Date dateCreation;
    private double solde=0;
    private Collection<Virement> virementsFrom;
    private Collection<Virement> virementsTo;
    private Long appUserId;
    private AppUser appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Collection<Virement> getVirementsFrom() {
        return virementsFrom;
    }

    public void setVirementsFrom(Collection<Virement> virementsFrom) {
        this.virementsFrom = virementsFrom;
    }

    public Collection<Virement> getVirementsTo() {
        return virementsTo;
    }

    public void setVirementsTo(Collection<Virement> virementsTo) {
        this.virementsTo = virementsTo;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "id=" + id +
                ", dateCreation=" + dateCreation +
                ", solde=" + solde +
                ", virementsFrom=" + virementsFrom +
                ", virementsTo=" + virementsTo +
                ", appUserId=" + appUserId +
                ", appUser=" + appUser +
                '}';
    }
}
