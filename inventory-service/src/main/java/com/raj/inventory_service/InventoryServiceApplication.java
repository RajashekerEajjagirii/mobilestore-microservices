package com.raj.inventory_service;

import com.raj.inventory_service.Model.Inventory;
import com.raj.inventory_service.Repository.InventoryRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	//Adding Data to db manually
	@Bean
	public CommandLineRunner loadData(InventoryRepo inventoryRepo) {
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iPhone_15");
			inventory.setQuantity(100);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iPhone_15_red");
			inventory1.setQuantity(0);

			inventoryRepo.save(inventory);
			inventoryRepo.save(inventory1);
		};
	}

}
