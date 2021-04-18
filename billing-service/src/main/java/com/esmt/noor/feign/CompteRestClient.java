package com.esmt.noor.feign;

import com.esmt.noor.securities.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.QueryParam;

@FeignClient(name = "COMPTE-SERVICE")
public interface CompteRestClient {
   /* @GetMapping(path = "/products")
    //@QueryParam(value = "page") int page,@QueryParam(value = "size") int size
    public PagedModel<Product> pageProducts();

    @GetMapping(path = "/products/{id}")
    public Product getProductById(@PathVariable(name = "id") Long id);*/

    @GetMapping(path = "/factureVirement")
    public boolean payer(@RequestHeader(name = SecurityConstants.HEADER_STRING) String token, @RequestParam("compteFromId") Long fromId, @RequestParam("compteToId") Long toId, @RequestParam("montant") double montant);
}
