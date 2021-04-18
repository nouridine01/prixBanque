package com.esmt.noor.filters;

import com.esmt.noor.securities.SecurityConstants;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JwtAuthorisationFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

            String jwtToken=httpServletRequest.getHeader(SecurityConstants.HEADER_STRING);

            if(jwtToken != null && jwtToken.startsWith(SecurityConstants.TOKEN_PREFIX)){

                try {
                    String token = jwtToken.substring(7);
                    //System.out.println(token);
                    //il faut parser le jwt pour recuper les claims(subject et roles)
                    Claims claims = Jwts.parser()
                            .setSigningKey(SecurityConstants.SECRET)
                            .parseClaimsJws(token)
                            .getBody();

                    String username = claims.getSubject();
                    //System.out.println(claims.toString());
                    //recuperer les roles qui sont un tableau de clé valeur
                /*
                * {
                      "sub": "noor@gmail.com",
                      "exp": 1586886839,
                      "roles": [
                        {
                          "authority": "ADMIN"
                        }
                      ]
                    }
               */
                    ArrayList<Map<String, String>> roles=(ArrayList<Map<String, String>>) claims.get("roles");

                    Collection<GrantedAuthority> authorities=new ArrayList<>();
                    roles.forEach(r->{
                        //System.out.println(r.get("authority"));
                        authorities.add(new SimpleGrantedAuthority(r.get("authority")));
                    });

                    UsernamePasswordAuthenticationToken authenticatedUser= new UsernamePasswordAuthenticationToken(username, null,authorities);

                    //on charge le user dans le contexte de securité de spring pour savoir par example pour telle route telle role est requise etc
                    //donc on dit à spring security que le user est authentifié donc on ne passe plus par le filtre JWTAuthenticationFilter
                    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
                    System.out.println("bill create 1");

                    filterChain.doFilter(httpServletRequest,httpServletResponse);

                }catch (Exception e) {
                    httpServletResponse.setHeader("error_message",e.getMessage());
                    httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            }else{
                //httpServletResponse.setHeader("error_message","veuillez vous authentifier");
                //httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
                filterChain.doFilter(httpServletRequest,httpServletResponse);
            }


    }
}
