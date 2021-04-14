package com.esmt.noor.services;

import com.esmt.noor.entities.Compte;
import com.esmt.noor.feign.SecurityRestClient;
import com.esmt.noor.repositories.CompteRepository;
import com.esmt.noor.repositories.VirementRepository;
import com.esmt.noor.securities.SecurityConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BanqueServiceImpl implements  BanqueService {

    private CompteRepository compteRepository;
    private VirementRepository virementRepository;
    private SecurityRestClient securityRestClient;

    public BanqueServiceImpl(CompteRepository compteRepository, VirementRepository virementRepository, SecurityRestClient securityRestClient) {
        this.compteRepository = compteRepository;
        this.virementRepository = virementRepository;
        this.securityRestClient = securityRestClient;
    }


    @Override
    public void virement(Long fromId, Long toId, double montant) throws Exception {
        retrait(fromId,montant);
        versement(toId,montant);
    }

    @Override
    public void retrait(Long id, double montant) throws Exception {
        Compte c = compteRepository.findById(id).get();
        if(c!=null){
            c.setSolde(c.getSolde() - montant);
            compteRepository.save(c);
        }else{
            throw new Exception("compte doesn't exist");
        }
    }

    @Override
    public void versement(Long id, double montant) throws Exception {
        Compte c = compteRepository.findById(id).get();
        if(c!=null){
            c.setSolde(c.getSolde() + montant);
            compteRepository.save(c);
        }else{
            throw new Exception("compte doesn't exist");
        }
    }

    @Override
    public double getSolde(Long id) throws Exception {
        Compte c = compteRepository.findById(id).get();
        if(c==null){
            throw new Exception("compte doesn't exist");
        }
        return c.getSolde();
    }

    @Override
    public Compte getCompte(Long id) {
        return compteRepository.findById(id).get();
    }

    @Override
    public Compte relever(Long id) throws Exception {
        Compte c = compteRepository.findById(id).get();
        if(c==null){
            throw new Exception("compte doesn't exist");
        }
        c.getVirementsFrom();
        c.getVirementsTo();
        return c;
    }
    @Override
    public String getToken(){
        String token = SecurityConstants.TOKEN_PREFIX+"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtbm9tYW1hbmVAZXR1LnVxYWMuY2EiLCJleHAiOjE2MTg4MTI1ODgsImlzcyI6Ii9sb2dpbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJDTElFTlQifV19.KtIANCP4WtUp926FhWN_T5N_GTbsMwIUTSJ3iYGKvozDKijxhPLta9ZjCwrk4IdTHY0DHxoe0qyrVRDEsrGMhw";
        return token;
    }
}
