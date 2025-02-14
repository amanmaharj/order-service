package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    public void placeOrder(OrderRequest orderRequest){
       var isInStock = inventoryClient.isInventoryInStock(orderRequest.skuCode(), orderRequest.quantity());
       if(isInStock){
           Order order = Order.builder().orderNumber(UUID.randomUUID().toString())
                   .price(orderRequest.price())
                   .skuCode(orderRequest.skuCode())
                   .price(orderRequest.price())
                   .quantity(orderRequest.quantity()).build();

           orderRepository.save(order);
       } else {
           throw new RuntimeException("product with SkuCode " + orderRequest.skuCode() + " is not in stock.");
       }


    }


}
