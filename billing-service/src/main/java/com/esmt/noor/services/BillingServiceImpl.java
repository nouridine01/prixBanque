package com.esmt.noor.services;

import com.esmt.noor.entities.Bill;
import com.esmt.noor.feign.CompteRestClient;
import com.esmt.noor.feign.SecurityRestClient;
import com.esmt.noor.model.AppUser;
import com.esmt.noor.repositories.BillRepository;
import com.esmt.noor.securities.SecurityConstants;

import java.util.List;

public class BillingServiceImpl implements BillingServices {

    private BillRepository billRepository;
    private SecurityRestClient securityRestClient;
    private CompteRestClient compteRestClient;


    public BillingServiceImpl(BillRepository billRepository, SecurityRestClient securityRestClient, CompteRestClient compteRestClient) {
        this.billRepository = billRepository;
        this.securityRestClient = securityRestClient;
        this.compteRestClient = compteRestClient;
    }



    @Override
    public void createBill(Bill bill) {
        billRepository.save(bill);
    }

    @Override
    public List<Bill> getCreatedBills(Long id) {
        return billRepository.findByClientFromId(id);
    }

    @Override
    public List<Bill> getReceivedBills(Long id) {
        return billRepository.findByClientToId(id);
    }

    @Override
    public void payBill(Long id) throws Exception {
        Bill bill = billRepository.findById(id).get();
        AppUser from = securityRestClient.getAppUserById(getToken(),bill.getClientFromId());
        AppUser to = securityRestClient.getAppUserById(getToken(),bill.getClientToId());
        if(bill !=null){
            compteRestClient.payer(from.getCompteId(),to.getCompteId(),bill.getMontant());
            bill.setStatut(true);
            billRepository.save(bill);
        }else{
            throw new Exception("facture non existant");
        }
    }

    @Override
    public String getToken(){
        String token = SecurityConstants.TOKEN_PREFIX+"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtbm9tYW1hbmVAZXR1LnVxYWMuY2EiLCJleHAiOjE2MTg4MTI1ODgsImlzcyI6Ii9sb2dpbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJDTElFTlQifV19.KtIANCP4WtUp926FhWN_T5N_GTbsMwIUTSJ3iYGKvozDKijxhPLta9ZjCwrk4IdTHY0DHxoe0qyrVRDEsrGMhw";
        return token;
    }
}
