package com.esmt.noor.services;


import com.esmt.noor.email.EmailSender;
import com.esmt.noor.entities.AppUser;
import com.esmt.noor.registration.token.ConfirmationToken;
import com.esmt.noor.registration.token.ConfirmationTokenService;
import com.esmt.noor.repositories.AppUserRepository;
import com.esmt.noor.securities.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;


    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";



    @Override
    public String signUpUser(AppUser appUser) throws Exception{
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        String token = UUID.randomUUID().toString();

        if (userExists) {
            //            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.
            AppUser u = appUserRepository.findByEmail(appUser.getEmail()).get();

            if(appUser.getNom().equals(u.getNom()) && appUser.getPrenom().equals(u.getPrenom()) &&
                    passwordEncoder.encode(appUser.getPassword()).equals(u.getPassword()) && appUser.getEmail().equals(u.getEmail()) && (u.isEnabled()==false)){
                ConfirmationToken confirmationToken = new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        appUser
                );

                confirmationTokenService.saveConfirmationToken(
                        confirmationToken);

                return token;
            }
            else throw new Exception("email already taken");
        }

        String encodedPassword = passwordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);



        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

//        TODO: SEND EMAIL

        return token;
    }

    @Override
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }


    @Override
    public AppUser loadUserByEmail(String email)
         throws UsernameNotFoundException {
            return appUserRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    String.format(USER_NOT_FOUND_MSG, email)));
    }

    @Override
    public String getCurrentUserLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((AppUser)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    @Override
    public AppUser getUserById(Long id) {
        return appUserRepository.findById(id).get();
    }




}
