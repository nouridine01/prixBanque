package com.esmt.noor.web;


import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class BanqueRestController {


    private ControllerService service;

    public BanqueRestController(ControllerService service) {
        this.service = service;
    }

    @GetMapping(path = "/create")
    public void create(@ModelAttribute CompteRequest request,HttpServletResponse response){

        try {
            service.create(response,request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
    public void getSolde(@PathVariable(name = "id") Long id,HttpServletResponse response){

        try {
            service.getSolde(response,id);
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
    public void virement(@ModelAttribute VirementResquest virementResquest, HttpServletResponse response){
        try {
            service.virement(response,virementResquest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @PostMapping(path = "/factureVirement")
    public void virementFacture(@ModelAttribute HttpServletRequest request, HttpServletResponse response){
        try {
            service.virementFacture(response,request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }




}
