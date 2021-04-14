package com.esmt.noor.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Virement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateVirement;
    private String typeVirement;
    private double montant;
    private String question;
    private String reponse;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Compte compteFrom;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    private Compte compteTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateVirement() {
        return dateVirement;
    }

    public void setDateVirement(Date dateVirement) {
        this.dateVirement = dateVirement;
    }

    public String getTypeVirement() {
        return typeVirement;
    }

    public void setTypeVirement(String typeVirement) {
        this.typeVirement = typeVirement;
    }

    public Compte getCompteFrom() {
        return compteFrom;
    }

    public void setCompteFrom(Compte compteFrom) {
        this.compteFrom = compteFrom;
    }

    public Compte getCompteTo() {
        return compteTo;
    }

    public void setCompteTo(Compte compteTo) {
        this.compteTo = compteTo;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
}
