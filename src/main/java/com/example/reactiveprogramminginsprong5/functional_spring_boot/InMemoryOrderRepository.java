package com.example.reactiveprogramminginsprong5.functional_spring_boot;

import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepository {

    final Map<String, Order> ordersMap;

    public InMemoryOrderRepository() {
        ordersMap = new ConcurrentHashMap<>();
    }

    @Override
    public Mono<Order> findById(String id) {
        return Mono.justOrEmpty(ordersMap.get(id));
    }

    @Override
    public Mono<Order> save(Order order) {
        ordersMap.put(order.getId(), order);

        return Mono.just(order);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }
}