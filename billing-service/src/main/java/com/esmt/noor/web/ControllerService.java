package com.esmt.noor.web;

import com.esmt.noor.entities.Bill;
import com.esmt.noor.feign.CompteRestClient;
import com.esmt.noor.feign.SecurityRestClient;
import com.esmt.noor.model.AppUser;
import com.esmt.noor.model.Compte;
import com.esmt.noor.repositories.BillRepository;
import com.esmt.noor.securities.SecurityConstants;
import com.esmt.noor.services.BillingServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ControllerService {

    private BillRepository billRepository;
    private SecurityRestClient securityRestClient;
    private CompteRestClient compteRestClient;
    private BillingServices billingServices;

    public ControllerService(BillRepository billRepository, SecurityRestClient securityRestClient, CompteRestClient compteRestClient, BillingServices billingServices) {
        this.billRepository = billRepository;
        this.securityRestClient = securityRestClient;
        this.compteRestClient = compteRestClient;
        this.billingServices = billingServices;
    }



    @Transactional
    public void getCreatedBills(HttpServletResponse response, Long id) throws IOException {
        try {
            List<Bill> liste = billRepository.findByClientFromId(id);
            Map<String,Object> tokens = new HashMap<>();
            tokens.put("response",liste);
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public void getReceivedBills(HttpServletResponse response, Long id) throws IOException {
        try {
            List<Bill> liste = billRepository.findByClientToId(id);
            Map<String,Object> tokens = new HashMap<>();
            tokens.put("response",liste);
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }


    @Transactional
    public void delete(HttpServletResponse response, Long id) throws IOException {
        try {
            Bill bill = billRepository.findById(id).get();
            if(bill.getStatut()) throw new Exception("impossible de supprimer la facture car elle est déjà payé");
            billRepository.delete(bill);
            Map<String,Object> tokens = new HashMap<>();
            tokens.put("response","facture supprimer avec succès");
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public void create(HttpServletRequest httpServletRequest,HttpServletResponse response, BillRquest request) throws IOException {
        try {
            System.out.println("bill create 3");
            AppUser user = securityRestClient.getAppUserByEmail(getToken(httpServletRequest),getCurrentUserLogin());
            AppUser userTo = securityRestClient.getAppUserByEmail(getToken(httpServletRequest),request.getEmail());
            System.out.println("bill create : "+userTo.getEmail());
            Bill bill = new Bill();
            bill.setDateBilling(request.getDate());
            bill.setClientFromId(user.getId());
            bill.setClientToId(userTo.getId());
            bill.setMontant(request.getMontant());
            bill=billRepository.save(bill);
            Map<String,Object> tokens = new HashMap<>();
            tokens.put("response",bill);
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public void payBill(HttpServletRequest httpServletRequest,HttpServletResponse response, Long id) throws IOException {
        try {
            Bill bill = billRepository.findById(id).get();
            AppUser from = securityRestClient.getAppUserById(getToken(httpServletRequest),bill.getClientFromId());
            AppUser to = securityRestClient.getAppUserById(getToken(httpServletRequest),bill.getClientToId());
            boolean retour = billingServices.payBill(getToken(httpServletRequest),from.getCompteId(),to.getCompteId(),bill.getMontant());
            Map<String,Object> tokens = new HashMap<>();
            if(retour){
                bill.setStatut(true);
                billRepository.save(bill);
                tokens.put("response","facture payer avec succès");
            }else tokens.put("response","erreur lors du payement de la facture");

            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
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
