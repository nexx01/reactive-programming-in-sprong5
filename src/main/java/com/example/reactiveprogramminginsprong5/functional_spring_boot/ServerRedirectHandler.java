package com.example.reactiveprogramminginsprong5.functional_spring_boot;

import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.awt.image.DataBuffer;

public class ServerRedirectHandler implements HandlerFunction<ServerResponse> {

    final WebClient webClient = WebClient.create();

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return webClient
                .method(request.method())
                .uri(request.headers()
                        .header("Redirect-Traffic")
                        .get(0))
                .headers((h) -> h.addAll(request.headers().asHttpHeaders()))
                .body(BodyInserters.fromDataBuffers(
                        request.bodyToFlux(DataBuffer.class)
                ))
                .cookies(c -> request
                        .cookies()
                        .forEach((key, list) -> list.forEach(cookie -> c.add(key, cookie.getValue())))
                )
                .exchange()
                .flatMap(cr -> ServerResponse
                        .status(cr.statusCode())
                        .cookies(c -> c.addAll(cr.cookies()))
                        .headers(hh -> hh.addAll(cr.headers().asHttpHeaders()))
                        .body(BodyInserters.fromDataBuffers(
                                cr.bodyToFlux(DataBuffer.class)
                        ))
                );
    }
}