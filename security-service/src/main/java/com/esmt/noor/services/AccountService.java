package com.esmt.noor.services;

import com.esmt.noor.entities.AppRole;
import com.esmt.noor.entities.AppUser;
import com.esmt.noor.securities.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AccountService {
    int enableAppUser(String email);
    String signUpUser(AppUser appUser) throws Exception;
    AppUser loadUserByEmail(String email);
    String getCurrentUserLogin();
    AppUser getUserById(Long id);

}
