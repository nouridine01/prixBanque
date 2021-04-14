package com.esmt.noor.entities;


import com.esmt.noor.model.AppUser;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateCreation;
    private double solde=0;
    @OneToMany(mappedBy = "compteFrom",fetch = FetchType.LAZY)
    private Collection<Virement> virementsFrom;

    @OneToMany(mappedBy = "compteTo",fetch = FetchType.LAZY)
    private Collection<Virement> virementsTo;

    private Long appUserId;
    @Transient
    @OneToOne
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
}
