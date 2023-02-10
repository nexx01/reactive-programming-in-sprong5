package com.example.reactiveprogramminginsprong5.webSocket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;

import javax.lang.model.element.NestingKind;
import java.net.URI;
import java.time.Duration;

@SpringBootApplication
public class WebSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args -> {
            var reactorNettyWebSocketClient = new ReactorNettyWebSocketClient();

            reactorNettyWebSocketClient.execute(
                            URI.create("http://localhost:8080/ws/echo"),
                            session -> Flux
                                    .interval(Duration.ofMillis(100))
                                    .map(String::valueOf)
                                    .map(session::textMessage)
                                    .as(session::send)
                    )
                    .subscribe();
        });
    }
}
