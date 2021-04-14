package com.esmt.noor.feign;



import com.esmt.noor.entities.AppUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/*
* feign permet de crée un client rest en utilisant des annotations de la mm maniere que spring data crée un client de DB
* */

@FeignClient(name = "SECURITY-SERVICE")
public interface CompteRestClient {
    @GetMapping(path = "/profil/{id}")
    public AppUser getAppUserById(@PathVariable(name = "id") Long id);

    /*@GetMapping(path = "/products")
    //@QueryParam(value = "page") int page,@QueryParam(value = "size") int size
    public PagedModel<Product> pageProducts();*/

    @GetMapping(path = "/create")
    public  void create(@RequestParam(name = "solde") double solde);
}
