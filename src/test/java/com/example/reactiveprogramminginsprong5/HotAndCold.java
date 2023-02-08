package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

@Slf4j
public class HotAndCold {

    @Test
    void cold() {
        Flux<String> coldPublisher = Flux.defer(() -> {
            log.info("Generating new items");
            return Flux.just(UUID.randomUUID().toString());
        });
        log.info("No data was generated so far");
        coldPublisher.subscribe(e -> log.info("onNext: {}", e));
        coldPublisher.subscribe(e -> log.info("onNext: {}", e));
        log.info("Data was generated twice for two subscribers");
    }

    @Test
    void connectableFlux() {
        Flux<Integer> source = Flux.range(0, 3)
                .doOnSubscribe(s ->
                        log.info("new subscription for the cold publisher"));
        ConnectableFlux<Integer> conn = source.publish();
        conn.subscribe(e -> log.info("[Subscriber 1] onNext: {}", e));
        conn.subscribe(e -> log.info("[Subscriber 2] onNext: {}", e));
        log.info("all subscribers are ready, connecting");
        conn.connect();
    }

    @Test
    void connectableFlux2_cache() throws InterruptedException {
        Flux<Integer> source = Flux.range(0, 2) // (1)
                .doOnSubscribe(s ->
                        log.info("new subscription for the cold publisher"));
        Flux<Integer> cachedSource = source.cache(Duration.ofSeconds(1)); // (2)
        cachedSource.subscribe(e -> log.info("[S 1] onNext: {}", e)); // (3)
        cachedSource.subscribe(e -> log.info("[S 2] onNext: {}", e)); // (4)
        Thread.sleep(1200); // (5)
        cachedSource.subscribe(e -> log.info("[S 3] onNext: {}", e)); // (6)
    }

    @Test
    void share() throws InterruptedException {
        Flux<Integer> source = Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .doOnSubscribe(s ->
                        log.info("new subscription for the cold publisher"));
        Flux<Integer> cachedSource = source.share();
        cachedSource.subscribe(e -> log.info("[S 1] onNext: {}", e));
        Thread.sleep(400);
        cachedSource.subscribe(e -> log.info("[S 2] onNext: {}", e));
    }
}
