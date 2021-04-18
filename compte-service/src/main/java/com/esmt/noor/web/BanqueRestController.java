package com.esmt.noor.web;


import com.esmt.noor.entities.Compte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BanqueRestController {

    private ControllerService service;

    public BanqueRestController(ControllerService service) {
        this.service = service;
    }

    @PostMapping(path = "/create")
    public Compte create(@RequestBody CompteRequest request, HttpServletResponse response){

        try {
            return service.create(response,request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(path = "/compte/{id}")
    public void getCompte(@PathVariable(name = "id") Long id,HttpServletResponse response){

        try {
            service.getCompte(response,id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @GetMapping(path = "/solde/{id}")
    public void getSolde(HttpServletRequest httpServletRequest,@PathVariable(name = "id") Long id,HttpServletResponse response){

        try {
            service.getSolde(httpServletRequest,response,id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping(path = "/relever/{id}")
    public void relever(@PathVariable(name = "id") Long id,HttpServletResponse response){

        try {
            service.relever(response,id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping(path = "/virement")
    public void virement(HttpServletRequest httpServletRequest,@ModelAttribute VirementResquest virementResquest, HttpServletResponse response){
        try {
            service.virement(httpServletRequest,response,virementResquest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @GetMapping(path = "/factureVirement")
    public boolean virementFacture(HttpServletRequest request, HttpServletResponse response){
        try {
            return service.virementFacture(response,request);
        } catch (Exception e) {
            return false;
        }

    }




}
