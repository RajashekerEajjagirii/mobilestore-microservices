package com.raj.order_service.Consumer;

import com.raj.order_service.Event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaMessageListener {

    //Defining Kafka Listener
    @KafkaListener(topics = "orderPlaced", groupId ="confirmNotification" )
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        //Send out on email Notification
        System.out.println("coming kafka lIsterner");
        log.info("Recieved Order Notication for Order Number :  " + orderPlacedEvent.getOrderId());
    }
}
