package com.raj.product_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raj.product_service.DTO.ProductRequest;
import com.raj.product_service.Repository.ProductRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests
{

	@Container
	private static  MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.27");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepo productRepo;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
	}

	@Test
	void shouldAddProduct() throws Exception {
		ProductRequest productRequest= getProductRequest();
		String productRequestString= objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, productRepo.findAll().size());

	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.brand("iPhone")
				.model("15")
				.processor("16 Chip Intel")
				.storage("128B")
				.description("48+12MP Camera")
				.price(71499)
				.color("Blue")
				.build();
	}

}
