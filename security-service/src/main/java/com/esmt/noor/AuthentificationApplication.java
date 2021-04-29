package com.esmt.noor;

import com.esmt.noor.email.EmailService;
import com.esmt.noor.entities.AppUser;
import com.esmt.noor.feign.CompteRestClient;
import com.esmt.noor.registration.CompteRequest;
import com.esmt.noor.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
@EnableFeignClients
@EnableWebMvc
public class AuthentificationApplication {
	@Autowired
	EmailService emailService;
	@Autowired
	RepositoryRestConfiguration repositoryRestConfiguration;
	public static void main(String[] args) {
		SpringApplication.run(AuthentificationApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return  new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner start(AccountService accountService, CompteRestClient compteRestClient){
		return args->{
			repositoryRestConfiguration.exposeIdsFor(AppUser.class);

			//accountService.signUpUser(new AppUser("nouridine","oumarou","nouridine27041998@gmail.com","passer",AppRole.ADMIN));
            CompteRequest compteRequest = new CompteRequest();
            compteRequest.setIdUser(Long.valueOf(7));
            compteRequest.setSolde(1000);
            //Compte c = compteRestClient.create(compteRequest);
            //System.out.println(c);

			String l = emailService.buildLink("token");
			String m = emailService.buildEmail("noor",l);
			//emailService.send("nouridine27041998@gmail.com",m);
			/*accountService.addRoleToUser("user1","USER");
			accountService.addRoleToUser("admin","USER");
			accountService.addRoleToUser("admin","ADMIN");
			accountService.addRoleToUser("user2","USER");
			accountService.addRoleToUser("user2","COSTUMER_MANAGER");
			accountService.addRoleToUser("user3","USER");
			accountService.addRoleToUser("user3","PRODUCT_MANAGER");
			accountService.addRoleToUser("user4","USER");
			accountService.addRoleToUser("user4","BILLS_MANAGER");*/

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
