package com.esmt.noor.web;

import com.esmt.noor.entities.Bill;
import com.esmt.noor.feign.CompteRestClient;
import com.esmt.noor.feign.SecurityRestClient;
import com.esmt.noor.model.AppUser;
import com.esmt.noor.model.Compte;
import com.esmt.noor.repositories.BillRepository;
import com.esmt.noor.services.BillingServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void create(HttpServletResponse response, BillRquest request) throws IOException {
        try {
            AppUser user = securityRestClient.getAppUserByEmail(getCurrentUserLogin());
            AppUser userTo = securityRestClient.getAppUserByEmail(request.getEmail());
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
    public void payBill(HttpServletResponse response, Long id) throws IOException {
        try {
            billingServices.payBill(id);
            Map<String,Object> tokens = new HashMap<>();
            tokens.put("response","facture payer avec succès");
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

}
