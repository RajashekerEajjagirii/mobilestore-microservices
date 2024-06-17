package com.raj.inventory_service.Service;

import com.raj.inventory_service.DTO.InventoryResponse;
import com.raj.inventory_service.Repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    @Autowired
    InventoryRepo inventoryRepo;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) throws InterruptedException {
        log.info("Wait started");
//        Thread.sleep(9000);
        log.info("Wait Ended");

        return inventoryRepo.findBySkuCodeIn(skuCode)
                .stream().map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();

    }
}
