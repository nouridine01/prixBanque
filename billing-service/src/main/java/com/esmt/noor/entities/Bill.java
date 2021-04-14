package com.esmt.noor.entities;

import com.esmt.noor.model.AppUser;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateBilling;
    private double montant;
    private Long clientFromId;
    private Long clientToId;
    private Boolean statut = false;
    @Transient
    @OneToOne
    private AppUser clientFrom;
    @Transient
    @OneToOne
    private AppUser clientTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateBilling() {
        return dateBilling;
    }

    public void setDateBilling(Date dateBilling) {
        this.dateBilling = dateBilling;
    }

    public Long getClientFromId() {
        return clientFromId;
    }

    public void setClientFromId(Long clientFromId) {
        this.clientFromId = clientFromId;
    }

    public Long getClientToId() {
        return clientToId;
    }

    public void setClientToId(Long clientToId) {
        this.clientToId = clientToId;
    }

    public AppUser getClientFrom() {
        return clientFrom;
    }

    public void setClientFrom(AppUser clientFrom) {
        this.clientFrom = clientFrom;
    }

    public AppUser getClientTo() {
        return clientTo;
    }

    public void setClientTo(AppUser clientTo) {
        this.clientTo = clientTo;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }
}
