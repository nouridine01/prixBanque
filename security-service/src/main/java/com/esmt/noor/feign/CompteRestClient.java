package com.esmt.noor.feign;



import com.esmt.noor.entities.AppUser;
import com.esmt.noor.model.Compte;
import com.esmt.noor.registration.CompteRequest;
import com.esmt.noor.securities.SecurityConstants;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
* feign permet de crée un client rest en utilisant des annotations de la mm maniere que spring data crée un client de DB
* */

@FeignClient(name = "COMPTE-SERVICE")
public interface CompteRestClient {
    @PostMapping(path = "/create")
    @Headers("Content-Type: application/json")
    public Compte create(@RequestBody CompteRequest compteRequest);
}
