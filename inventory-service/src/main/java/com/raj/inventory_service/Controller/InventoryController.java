package com.raj.inventory_service.Controller;

import com.raj.inventory_service.DTO.InventoryResponse;
import com.raj.inventory_service.Model.Inventory;
import com.raj.inventory_service.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

//    @GetMapping("/{sku-code}")
//    public boolean isInStock(@PathVariable String skuCode) {
//
//       return inventoryService.isInStock(skuCode);
//
//    }

    //Developing for list of skuCodes
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam List<String> skuCode) {
        try{
            List<InventoryResponse> responses=inventoryService.isInStock(skuCode);
            return  new ResponseEntity<>(responses,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
