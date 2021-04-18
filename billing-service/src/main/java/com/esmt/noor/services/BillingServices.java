package com.esmt.noor.services;

import com.esmt.noor.entities.Bill;

import java.util.List;

public interface BillingServices {
    public void createBill(Bill bill);
    public List<Bill> getCreatedBills(Long id);
    public List<Bill> getReceivedBills(Long id);
    public boolean payBill(String token,Long compteFromId,Long compteToId,double montant) throws Exception;

}
