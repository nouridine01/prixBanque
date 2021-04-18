package com.esmt.noor.registration;

import com.esmt.noor.email.EmailSender;
import com.esmt.noor.entities.AppRole;
import com.esmt.noor.entities.AppUser;
import com.esmt.noor.entities.Counter;
import com.esmt.noor.feign.CompteRestClient;
import com.esmt.noor.model.Compte;
import com.esmt.noor.registration.token.ConfirmationToken;
import com.esmt.noor.registration.token.ConfirmationTokenService;
import com.esmt.noor.repositories.AppUserRepository;
import com.esmt.noor.securities.SecurityConstants;
import com.esmt.noor.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class RegistrationService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private  EmailValidator emailValidator;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private CompteRestClient compteRestClient;
    @Autowired
    AppUserRepository appUserRepository;

    @Transactional
    public void register(HttpServletResponse response,RegistrationRequest request) throws IOException {
        try{
            boolean isValidEmail = emailValidator.
                    test(request.getEmail());

            if (!isValidEmail) {
                throw new Exception("email not valid");
            }

            String token = null;

            token = accountService.signUpUser(
                    new AppUser(
                            request.getNom(),
                            request.getPrenom(),
                            request.getEmail(),
                            request.getTelephone(),
                            request.getPassword(),
                            AppRole.CLIENT
                        )
                );

            String link = emailSender.buildLink(token);
            emailSender.send(
                    request.getEmail(),
                    emailSender.buildEmail(request.getNom(), link));

            Map<String,String> tokens = new HashMap<>();
            tokens.put("message","veuillez confirmer votre eamil");
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public void confirmToken(HttpServletRequest httpServletRequest, HttpServletResponse response,String token) throws IOException {
        try {
            ConfirmationToken confirmationToken = confirmationTokenService
                    .getToken(token)
                    .orElseThrow(() ->
                            new IllegalStateException("token not found"));

            if (confirmationToken.getConfirmedAt() != null) {
                throw new Exception("email already confirmed");
            }

            LocalDateTime expiredAt = confirmationToken.getExpiresAt();

            if (expiredAt.isBefore(LocalDateTime.now())) {
                throw new Exception("token expired");
            }

            confirmationTokenService.setConfirmedAt(token);

            AppUser user = confirmationToken.getAppUser();
            double solde = 0;
            if(Counter.getCounter()<10000) solde=1000;
            CompteRequest compteRequest = new CompteRequest();
            compteRequest.setIdUser(user.getId());
            compteRequest.setSolde(solde);
            Compte c = compteRestClient.create(compteRequest);
            if(c != null){
                user.setCompteId(c.getId());
                appUserRepository.save(user);
                accountService.enableAppUser(
                        confirmationToken.getAppUser().getEmail());
            }

            //redirection vers la page de connexion
            Map<String,String> tokens = new HashMap<>();
            tokens.put("message","confirmed");
            response.setContentType("Application/json");
            //on est pas obligé de send le prefix
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);

        }catch (Exception e) {
            response.setHeader("error_message",e.getMessage());
            response.sendError(response.SC_FORBIDDEN);
        }
    }

    @Transactional
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String refreshToken = request.getHeader(SecurityConstants.HEADER_STRING);

        if(refreshToken != null && refreshToken.startsWith(SecurityConstants.TOKEN_PREFIX)){
            System.out.println("refresh rest");
            try {
                String token = refreshToken.substring(7);
                System.out.println(token);
                //il faut parser le jwt pour recuper les claims(subject et roles)
                Claims claims = Jwts.parser()
                        .setSigningKey(SecurityConstants.SECRET)
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();


                //now est le bon moment de verifier si le token n'est pas dans la blackList avec le username

                AppUser user =accountService.loadUserByEmail(username);

                String jwtToken = Jwts.builder()
                        //claims registred
                        .setSubject(user.getUsername())
                        .setExpiration(new Date(System.currentTimeMillis()+ SecurityConstants.EXPIRATION_TIME_ACCESS_TOKEN))
                        .setIssuer(request.getRequestURI().toString())
                        //claims
                        .claim("roles",user.getAuthorities())
                        //type d'encodage
                        .signWith(SignatureAlgorithm.HS512,SecurityConstants.SECRET)
                        .compact();

                Map<String,String> tokens = new HashMap<>();
                tokens.put("jwtToken",jwtToken);
                tokens.put("refreshToken",refreshToken);
                response.setContentType("Application/json");
                //on est pas obligé de send le prefix
                response.addHeader(SecurityConstants.HEADER_STRING,SecurityConstants.TOKEN_PREFIX+jwtToken);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            }catch (Exception e) {
                response.setHeader("error_message",e.getMessage());
                response.sendError(response.SC_FORBIDDEN);
            }
        }else{
            response.setHeader("error_message","refresh token is required");
            response.sendError(response.SC_BAD_REQUEST);
        }
    }

    public String getToken(HttpServletRequest httpServletRequest){
        String jwtToken=httpServletRequest.getHeader(SecurityConstants.HEADER_STRING);
        return jwtToken;
    }


}
