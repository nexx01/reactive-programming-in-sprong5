package com.example.reactiveprogramminginsprong5.samples.functional_password_verification.clien;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class DefaultPasswordVerificetionServiceImpl implements PasswordVerificetionService {

    final WebClient webClient;

    public DefaultPasswordVerificetionServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhist:8080")
                .build();
    }

    @Override
    public Mono<Void> check(String raw, String encoded) {
        return webClient
                .post()
                .uri("/password")
                .body(BodyInserters.fromPublisher(
                        Mono.just(new PaswordDto(raw,encoded)),
                        PaswordDto.class
                ))
                .exchange()
                .flatMap(response->{
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    } else if (response.statusCode() == HttpStatus.EXPECTATION_FAILED) {
                        return Mono.error(new BadCredentialsException("Invalid credential"));
                    }
                    return Mono.error(new IllegalStateException());
                })
                ;
    }
}
