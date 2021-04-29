package com.esmt.noor;

import com.esmt.noor.entities.Bill;
import com.esmt.noor.feign.CompteRestClient;
import com.esmt.noor.feign.SecurityRestClient;
import com.esmt.noor.repositories.BillRepository;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//pour activer feign
@EnableFeignClients
@EnableWebMvc
public class BillingServiceApplication {

	@Autowired
	RepositoryRestConfiguration repositoryRestConfiguration;
	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
		System.out.println("hello world");
	}

	@Bean
	CommandLineRunner start(BillRepository billRepository,
							SecurityRestClient costumerRestClient, CompteRestClient productRestClient){

		return args -> {
			repositoryRestConfiguration.exposeIdsFor(Bill.class);
			/*Costumer costumer=costumerRestClient.getCostumerById(1L);
			Bill bill=billRepository.save(new Bill(null,new Date(),null,null,costumer.getId()));
			PagedModel<Product> products=productRestClient.pageProducts();
			products.forEach(p->{
				ProductItem productItem=new ProductItem();
				productItem.setPrice(p.getPrice());
				productItem.setQuantity(1+new Random().nextInt(10));
				productItem.setBill(bill);
				productItem.setProductId(p.getId());
				productItemRepository.save(productItem);
			});
			System.out.println("---------------------------------------------------------");
			System.out.println(costumer.getId());
			System.out.println(costumer.getEmail());
			System.out.println(costumer.getName());*/

		};
	}

	//@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}

}
