package com.raj.order_service.Service;

import com.raj.order_service.DTO.InventoryResponse;
import com.raj.order_service.DTO.OrderItemsDto;
import com.raj.order_service.DTO.OrderRequest;
import com.raj.order_service.Event.OrderPlacedEvent;
import com.raj.order_service.Model.Order;
import com.raj.order_service.Model.OrderItems;
import com.raj.order_service.Repository.OrderRepo;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
//@AllArgsConstructor
@Slf4j
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

//    public OrderService(OrderRepo orderRepo, WebClient.Builder webClientBuilder, Tracer tracer, KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
//        this.orderRepo = orderRepo;
//        this.webClientBuilder = webClientBuilder;
//        this.tracer = tracer;
//        this.kafkaTemplate = kafkaTemplate;
//    }

    @Autowired
    private final WebClient.Builder webClientBuilder;

    @Getter
    private final Tracer tracer;

    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order=new Order();
        order.setOrderId(UUID.randomUUID().toString());
        log.info("comming order service");
        List<OrderItems> orderItems= orderRequest.getOrderItemsDtoList()
                .stream()
                .map(this::mapToDTO)
                .toList();
        order.setOrderItems(orderItems);

        //Collecting all skuCodes
        List<String> skuCodes= order.getOrderItems().stream()
                .map(OrderItems::getSkuCode)
                .toList();

        //Creating Custom Span for inventory-monitoring
        Span inventoryServiceLookup=this.tracer.nextSpan().name("inventoryServiceLookup");
        try(Tracer.SpanInScope spanInScope= tracer.withSpan(inventoryServiceLookup.start())){
            log.info("inside before inventoryServiceLookup");
            //Calling Inventory Service, to check whether product is in stock or not
            InventoryResponse[] inventoryResponsesArray=webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder ->uriBuilder.queryParam("skuCode", skuCodes).build() )
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
            log.info("after calling inventoryServiceLookup:{}", inventoryResponsesArray);
            //Checking Stock
            boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                    .allMatch(InventoryResponse::isInStock);
            log.info("all products in stock:{}", allProductsInStock);
            if(Boolean.TRUE.equals(allProductsInStock)){
                orderRepo.save(order);
                log.info("before calling kafka");
                //triggering event to notification-service to send mail
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderId()));
                log.info("Order Placed Successfully with id: {} ", order.getOrderId());
                return "Order Placed Successfully!";
            }else{
                throw new IllegalArgumentException("Product is not in Stock, please try again later.");
            }

        }finally {
            inventoryServiceLookup.end();
        }

        //Calling Inventory Service, to check whether product is in stock or not
//        InventoryResponse[] inventoryResponsesArray=webClientBuilder.build().get()
//                .uri("http://inventory-service/api/inventory",
//                        uriBuilder ->uriBuilder.queryParam("skuCode", skuCodes).build() )
//                .retrieve()
//                .bodyToMono(InventoryResponse[].class)
//                .block();
//
//        //Checking Stock
//        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
//                .allMatch(InventoryResponse::isInStock);
//
//        if(Boolean.TRUE.equals(allProductsInStock)){
//             orderRepo.save(order);
//             return "Order Placed Successfully!";
//        }else{
//            throw new IllegalArgumentException("Product is not in Stock, please try again later.");
//        }


    }

    private OrderItems mapToDTO(OrderItemsDto orderItemsDto) {

        OrderItems orderItems=new OrderItems();
        orderItems.setPrice(orderItemsDto.getPrice());
        orderItems.setQuantity(orderItemsDto.getQuantity());
        orderItems.setSkuCode(orderItemsDto.getSkuCode());
        return orderItems;

    }


//    public Tracer getTracer() {
//        return tracer;
//    }
}
