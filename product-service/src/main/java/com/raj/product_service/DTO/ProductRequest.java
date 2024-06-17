package com.raj.product_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String brand;
    private String model;
    private String processor;
    private String storage;
    private String description;
    private double price;
    private String color;
}
