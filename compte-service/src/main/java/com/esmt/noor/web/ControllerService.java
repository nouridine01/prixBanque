package com.esmt.noor.web;

import com.esmt.noor.entities.Compte;
import com.esmt.noor.entities.Virement;
import com.esmt.noor.feign.SecurityRestClient;
import com.esmt.noor.model.AppUser;
import com.esmt.noor.repositories.CompteRepository;
import com.esmt.noor.repositories.VirementRepository;
import com.esmt.noor.securities.SecurityConstants;
import com.esmt.noor.services.BanqueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ControllerService {

    private CompteRepository compteRepository;
    private VirementRepository virementRepository;
    private SecurityRestClient securityRestClient;
    private BanqueService banqueService;

    public ControllerService(CompteRepository compteRepository, VirementRepository virementRepository, SecurityRestClient securityRestClient, BanqueService banqueService) {
        this.compteRepository = compteRepository;
        this.virementRepository = virementRepository;
        this.securityRestClient = securityRestClient;
        this.banqueService = banqueService;
    }

    @Transactional
    public void virement(HttpServletRequest httpServletRequest,HttpServletResponse response, VirementResquest request) throws IOException {
        try {
            AppUser userFrom = securityRestClient.getAppUserByEmail(getToken(httpServletRequest),getCurrentUserLogin());
            AppUser userTo = securityRestClient.getAppUserByEmail(getToken(httpServletRequest),request.getEmail());
            Virement virement= new Virement();
            banqueService.virement(userFrom.getCompteId(),userTo.getCompteId(),request.getMontant());
            virement.setCompteFrom(compteRepository.findById(userFrom.getCompteId()).get());
            virement.setCompteTo(compteRepository.findById(userTo.getCompteId()).get());
            virement.setDateVirement(request.getDate());
            virement.setMontant(request.getMontant());
            virement.setTypeVirement(request.getType());
            virement.setReponse(request.getResponse());
            virement.setQuestion(request.getQuestion());
            virementRepository.save(virement);
            Map<String,String> tokens = new HashMap<>();
            tokens.put("message","virement effectué avec succès");
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public boolean virementFacture(HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            double montant = Double.valueOf(request.getParameter("montant"));
            Long compteFromId = Long.valueOf(request.getParameter("compteFromId"));
            Long compteToId = Long.valueOf(request.getParameter("compteToId"));
            Virement virement= new Virement();
            banqueService.virement(compteFromId,compteToId,montant);
            virement.setCompteFrom(compteRepository.findById(compteFromId).get());
            virement.setCompteTo(compteRepository.findById(compteToId).get());
            virement.setDateVirement(new Date());
            virement.setMontant(montant);
            virement.setTypeVirement("Payement Facture");
            virement.setReponse("");
            virement.setQuestion("");
            virementRepository.save(virement);

            return true;

        }catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void getCompte(HttpServletResponse response, Long id) throws IOException {
        try {

            Compte compte =compteRepository.findById(id).get();
            Map<String,Object> tokens = new HashMap<>();
            tokens.put("response",compte);
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public void getSolde(HttpServletRequest httpServletRequest,HttpServletResponse response, Long id) throws IOException {
        try {
            System.out.println("token   :  " +getToken(httpServletRequest));
            Compte compte =compteRepository.findById(id).get();
            Map<String,Object> tokens = new HashMap<>();
            tokens.put("response",compte.getSolde());
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public void relever(HttpServletResponse response, Long id) throws IOException {
        try {
            Compte compte =banqueService.relever(id);
            Map<String,Object> tokens = new HashMap<>();
            tokens.put("response",compte);
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public Compte create(HttpServletResponse response, CompteRequest request) throws IOException {
        Compte compte =new Compte();
        try {
            compte.setSolde(request.getSolde());
            compte.setAppUserId(request.getIdUser());
            compte.setDateCreation(new Date());
            compte=compteRepository.save(compte);
            return compte;
        }catch (Exception e) {
            return null;
        }
    }

    /*getCompte(){
        Compte compte =compteRepository.findById(id).get();
        /*Costumer c = costumerRestClient.getCostumerById(compte.getCostumerId());
        compte.setCostumer(c);
        compte.getProductItem().forEach(item->{
            item.setProduct(productRestClient.getProductById(item.getProductId()));
        });
    }*/


    public String getCurrentUserLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((AppUser)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public String getToken(HttpServletRequest httpServletRequest){
        String jwtToken=httpServletRequest.getHeader(SecurityConstants.HEADER_STRING);
        return jwtToken;
    }

}
