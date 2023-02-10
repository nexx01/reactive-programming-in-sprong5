package com.example.reactiveprogramminginsprong5.webClent;

import org.springframework.security.core.userdetails.User;
import org.springframework.web.reactive.function.client.WebClient;

public class TestWebClient {

    public static void main(String[] args) throws InterruptedException {
        WebClient.create("http://localhost:8080/api")
                .get()
                .uri("/users/{id}", 10)
                .retrieve()
                .bodyToMono(User.class)
                .map(User::getUsername);

        Thread.sleep(1000);
    }
}
