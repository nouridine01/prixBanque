package com.esmt.noor.web;

import com.esmt.noor.entities.Bill;
import com.esmt.noor.repositories.BillRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
    private BillRepository billRepository;
   // private SecurityRestClient costumerRestClient;
    //private CompteRestClient productRestClient;

    public BillRestController(BillRepository billRepository) {
        this.billRepository = billRepository;

    }

    @GetMapping(path = "/bills/full/{id}")
    public Bill getBill(@PathVariable(name = "id") Long id){
        Bill bill=billRepository.findById(id).get();
       /* Costumer c = costumerRestClient.getCostumerById(bill.getCostumerId());
        bill.setCostumer(c);
        bill.getProductItem().forEach(item->{
            item.setProduct(productRestClient.getProductById(item.getProductId()));
        });*/
        return bill;
    }
}
