package com.esmt.noor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//pour activer feign
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
		System.out.println("hello world");
	}

	/*@Bean
	CommandLineRunner start(BillRepository billRepository, ProductItemRepository productItemRepository,
							SecurityRestClient costumerRestClient, CompteRestClient productRestClient){
		return args -> {
			Costumer costumer=costumerRestClient.getCostumerById(1L);
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
			System.out.println(costumer.getName());

		};
	}*/



}
