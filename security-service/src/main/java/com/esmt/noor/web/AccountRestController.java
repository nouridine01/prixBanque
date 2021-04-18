package com.esmt.noor.web;


import com.esmt.noor.entities.AppUser;
import com.esmt.noor.registration.RegistrationRequest;
import com.esmt.noor.registration.RegistrationService;
import com.esmt.noor.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
public class AccountRestController {

    @Autowired
    AccountService accountService;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping(path = "/register")
    public void register(@ModelAttribute RegistrationRequest request, HttpServletResponse response) {

        try {
            registrationService.register(response, request);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping(path = "/confirm")
    public void confirm(HttpServletRequest httpServletRequest,@RequestParam("token") String token,HttpServletResponse response) {
        try {
            registrationService.confirmToken(httpServletRequest,response,token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*@GetMapping(path = "/users")
    public List<AppUser> listUsers(){
        return accountService.listUsers();
    }

    /*@PostMapping(path = "/users")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppUser addUser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }*/

    @GetMapping(path = "/getUserById/{id}")
    public AppUser getuserById(@PathVariable(name = "id") Long id){
        return accountService.getUserById(id);
    }


    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        try {
            registrationService.refreshToken(request,response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping(path = "/profile")
    public AppUser profile(){
        return accountService.loadUserByEmail(accountService.getCurrentUserLogin());
    }

    @GetMapping(path = "/getUser")
    public AppUser getUserByEmail(@RequestParam("email") String email){
        return accountService.loadUserByEmail(email);

    }
}

