package com.esmt.noor;

import com.esmt.noor.email.EmailService;
import com.esmt.noor.entities.AppRole;
import com.esmt.noor.entities.AppUser;
import com.esmt.noor.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
@EnableFeignClients
public class AuthentificationApplication {
	@Autowired
	EmailService emailService;
	public static void main(String[] args) {
		SpringApplication.run(AuthentificationApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return  new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner start(AccountService accountService){
		return args->{

			//accountService.signUpUser(new AppUser("nouridine","oumarou","nouridine27041998@gmail.com","passer",AppRole.ADMIN));


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

}
