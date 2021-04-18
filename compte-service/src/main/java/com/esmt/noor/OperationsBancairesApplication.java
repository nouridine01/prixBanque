package com.esmt.noor;

import com.esmt.noor.feign.SecurityRestClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class OperationsBancairesApplication {


	public static void main(String[] args) {
		SpringApplication.run(OperationsBancairesApplication.class, args);

	}

	@Bean
	CommandLineRunner start(SecurityRestClient securityRestClient){
		return args -> {
			//String token = SecurityConstants.TOKEN_PREFIX+"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtbm9tYW1hbmVAZXR1LnVxYWMuY2EiLCJleHAiOjE2MTg4MTI1ODgsImlzcyI6Ii9sb2dpbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJDTElFTlQifV19.KtIANCP4WtUp926FhWN_T5N_GTbsMwIUTSJ3iYGKvozDKijxhPLta9ZjCwrk4IdTHY0DHxoe0qyrVRDEsrGMhw";
			//System.out.println(securityRestClient == null);
			//AppUser u = securityRestClient.getAppUserByEmail( token,"mnomamane@etu.uqac.ca");
			//System.out.println(u.getNom()+" "+u.getPrenom()+" : "+u.getEmail());
		};
	}



}
