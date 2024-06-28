package com.raj.order_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

//    //Defining Kafka Listener
//    @KafkaListener(topics = "notificationTopic-1", groupId ="notificationId-1" )
//    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
//        //Send out on email Notification
//        System.out.println("coming kafka lIsterner");
//        log.info("Recieved Order Notication for Order Number :  " + orderPlacedEvent.getOrderId());
//    }
}
