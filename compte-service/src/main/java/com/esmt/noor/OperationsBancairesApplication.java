package com.esmt.noor;

import com.esmt.noor.entities.Compte;
import com.esmt.noor.entities.Virement;
import com.esmt.noor.feign.SecurityRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@EnableFeignClients
@EnableWebMvc
public class OperationsBancairesApplication {

	@Autowired
	RepositoryRestConfiguration repositoryRestConfiguration;
	public static void main(String[] args) {
		SpringApplication.run(OperationsBancairesApplication.class, args);

	}

	@Bean
	CommandLineRunner start(SecurityRestClient securityRestClient){
		return args -> {
			repositoryRestConfiguration.exposeIdsFor(Compte.class, Virement.class);
			//String token = SecurityConstants.TOKEN_PREFIX+"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtbm9tYW1hbmVAZXR1LnVxYWMuY2EiLCJleHAiOjE2MTg4MTI1ODgsImlzcyI6Ii9sb2dpbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJDTElFTlQifV19.KtIANCP4WtUp926FhWN_T5N_GTbsMwIUTSJ3iYGKvozDKijxhPLta9ZjCwrk4IdTHY0DHxoe0qyrVRDEsrGMhw";
			//System.out.println(securityRestClient == null);
			//AppUser u = securityRestClient.getAppUserByEmail( token,"mnomamane@etu.uqac.ca");
			//System.out.println(u.getNom()+" "+u.getPrenom()+" : "+u.getEmail());
		};
	}

	
	//@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}


}
