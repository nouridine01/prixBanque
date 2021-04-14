package com.esmt.noor.gatway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	/*
	* tuer un port
	* netstat -ano | findstr :votre_numero_de_port
	* taskkill /PID identifiant_du_processus /F
	* */

	//configuration java de spring cloud
	//@Bean
	RouteLocator routeLocator(RouteLocatorBuilder builder){
	    /**config static en precisant l'emplacement des microservices
		return builder.routes()
				.route(r-> r.path("/costumers/**").uri("http://localhost:8081/"))
				.route(r-> r.path("/products/**").uri("http://localhost:8082/"))
				.build();*/


        //conf static en utilisant les  info du service d'enregistrement localhost:8761 pour recuperer le nom des service à partir de l'annuere du service d'enregistre
        return builder.routes()
                .route(r-> r.path("/costumers/**").uri("lb://COSTUMER-SERVICE"))
                .route(r-> r.path("/products/**").uri("lb://INVENTORY-SERVICE"))
                .build();


	}

    //conf dynamic en disant à gatewaye je ne connais pas les routes ni les microservices mais les noms des MS est dans l'url utilise le service d'enregistrement pour decouvrir les MS
    //l'url est du la sorte uri-gatewaye/nom-service/path : http://localhost:8888/COSTUMER-SERVICE/costumers et http://localhost:8888/INVENTORY-SERVICE/products et http://localhost:8888/BILLING-SERVICE/bills/full/1
	@Bean
    DiscoveryClientRouteDefinitionLocator discoveryClientRouteDefinitionLocator(ReactiveDiscoveryClient reactiveDiscoveryClient, DiscoveryLocatorProperties discoveryLocatorProperties){
	    return new DiscoveryClientRouteDefinitionLocator(reactiveDiscoveryClient,discoveryLocatorProperties);
    }

}
