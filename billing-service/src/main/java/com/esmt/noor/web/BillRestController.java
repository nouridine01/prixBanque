package com.esmt.noor.web;

import com.esmt.noor.entities.Bill;
import com.esmt.noor.repositories.BillRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class BillRestController {

    private ControllerService controllerService;

    public BillRestController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping(path = "/bills/full/{id}")
    public void getBill(@PathVariable(name = "id") Long id){

        try {

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @DeleteMapping(path = "/bills/supprimer/{id}")
    public void supprimer(@PathVariable(name = "id") Long id,HttpServletResponse response){

        try {
            controllerService.delete(response,id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping(path = "/getCreatedBills/{id}")
    public void getcreatedBills(@PathVariable(name = "id") Long id,HttpServletResponse response){

        try {
            controllerService.getCreatedBills(response, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping(path = "/getReceivedBills/{id}")
    public void getReceivedBills(@PathVariable(name = "id") Long id,HttpServletResponse response){

        try {
            controllerService.getReceivedBills(response, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping(path = "/payBill/{id}")
    public void PayBill(HttpServletRequest httpServletRequest, @PathVariable(name = "id") Long id, HttpServletResponse response){

        try {
            controllerService.payBill(httpServletRequest,response, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @PostMapping(path = "/create")
    public void create(HttpServletRequest httpServletRequest,@ModelAttribute BillRquest request, HttpServletResponse response){

        try {
            controllerService.create(httpServletRequest,response,request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
