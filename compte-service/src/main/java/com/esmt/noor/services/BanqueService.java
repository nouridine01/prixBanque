package com.esmt.noor.services;

import com.esmt.noor.entities.Compte;

public interface BanqueService {
    public void virement(Long fromId,Long  toId,double montant) throws Exception;
    public void retrait(Long id,double montant) throws Exception;
    public void versement(Long id,double montant) throws Exception;
    public double getSolde(Long id) throws Exception;
    public Compte getCompte(Long id);
    public Compte relever(Long id) throws Exception;
    public String getToken();
}
