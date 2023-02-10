package com.example.reactiveprogramminginsprong5.functional_spring_boot;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

public class OrderHandler {

    final OrderRepository orderRepository;

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request
                .bodyToMono(Order.class)
                .flatMap(orderRepository::save)
                .flatMap(o ->
                        ServerResponse.created(URI.create("/orders/" + o.getId()))
                                .build()
                );
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        return orderRepository
                .findById(request.pathVariable("id"))
                .flatMap(order ->
                        ServerResponse
                                .ok()
                                .syncBody(order)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> list(ServerRequest request) {
        return null;
    }
}