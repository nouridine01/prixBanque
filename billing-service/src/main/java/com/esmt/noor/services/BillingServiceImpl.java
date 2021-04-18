package com.esmt.noor.services;

import com.esmt.noor.entities.Bill;
import com.esmt.noor.feign.CompteRestClient;
import com.esmt.noor.feign.SecurityRestClient;
import com.esmt.noor.model.AppUser;
import com.esmt.noor.repositories.BillRepository;
import com.esmt.noor.securities.SecurityConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
@Transactional
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
    public boolean payBill(String token,Long compteFromId,Long compteToId,double montant) throws Exception {
            boolean retour = compteRestClient.payer(token,compteFromId,compteToId,montant);
            return retour;
    }


}
