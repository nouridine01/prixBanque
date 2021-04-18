package com.esmt.noor.feign;


import com.esmt.noor.model.AppUser;
import com.esmt.noor.securities.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/*
* feign permet de crée un client rest en utilisant des annotations de la mm maniere que spring data crée un client de DB
* */

@FeignClient(name = "SECURITY-SERVICE")
public interface SecurityRestClient {

    @GetMapping(path = "/getUserById/{id}")
    public AppUser getAppUserById(@RequestHeader(name = SecurityConstants.HEADER_STRING) String token,@PathVariable(name = "id") Long id);
    @GetMapping(path = "/getUser")
    public AppUser getAppUserByEmail(@RequestHeader(name = SecurityConstants.HEADER_STRING) String token, @RequestParam("email") String email);

}
