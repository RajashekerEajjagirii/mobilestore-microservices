package com.raj.order_service.Controller;

import com.raj.order_service.DTO.OrderRequest;
import com.raj.order_service.Model.Order;
import com.raj.order_service.Service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

//    @PostMapping
//    @CircuitBreaker(name="inventory",fallbackMethod ="fallbackMethod" )
//    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
//        try{
//            Order order= orderService.placeOrder(orderRequest);
//            return new ResponseEntity<>(order,HttpStatus.CREATED);
//        }catch(Exception e){
//            e.printStackTrace();
//            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }


    // Catch block Overriding RuntimeException so removed
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod ="fallbackMethod" )
    @TimeLimiter(name="inventory")
    @Retry(name="inventory")
    public CompletableFuture<String>  placeOrder(@RequestBody OrderRequest orderRequest) {

         return CompletableFuture.supplyAsync(()-> orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(RuntimeException runtimeException)
    {
        return CompletableFuture.supplyAsync(()->"Oops! something went wrong, Please order after some time! ");
    }

}
