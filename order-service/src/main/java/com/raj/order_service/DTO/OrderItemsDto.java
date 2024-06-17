package com.raj.order_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsDto {

    private Long id;
    private String skuCode;
    private double price;
    private int quantity;

}
