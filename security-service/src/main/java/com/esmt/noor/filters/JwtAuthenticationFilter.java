package com.esmt.noor.filters;

import com.esmt.noor.entities.AppUser;
import com.esmt.noor.securities.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    AuthenticationManager manager;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.manager=authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("attemptAuthentication");
        String username=request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username+" "+password);
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username,password);
        return manager.authenticate(user);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication");
        AppUser user = (AppUser) authResult.getPrincipal();
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

        String refreshToken = Jwts.builder()
                //claims registred
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+ SecurityConstants.EXPIRATION_TIME_REFRESH_TOKEN)) //15mn
                .setIssuer(request.getRequestURI().toString())
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
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setHeader("message","login failed");
        response.setStatus(response.getStatus());
    }
}
