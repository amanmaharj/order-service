package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){
       var isInStock = inventoryClient.isInventoryInStock(orderRequest.skuCode(), orderRequest.quantity());
       if(isInStock){
           Order order = Order.builder().orderNumber(UUID.randomUUID().toString())
                   .price(orderRequest.price())
                   .skuCode(orderRequest.skuCode())
                   .quantity(orderRequest.quantity()).build();

           orderRepository.save(order);
           //send the message to Kafka topic which we have to inclue the email address and message.

       } else {
           throw new RuntimeException("product with SkuCode " + orderRequest.skuCode() + " is not in stock.");
       }


    }


}
