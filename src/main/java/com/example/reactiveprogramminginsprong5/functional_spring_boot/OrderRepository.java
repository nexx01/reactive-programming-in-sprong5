package com.example.reactiveprogramminginsprong5.functional_spring_boot;


import reactor.core.publisher.Mono;

public interface OrderRepository {

    Mono<Order> findById(String id);

    Mono<Order> save(Order order);

    Mono<Void> deleteById(String id);
}
