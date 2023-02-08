package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
public class CombineREactiveStream {

    @Test
    void concat() {
        Flux.concat(
                        Flux.range(1, 3),
                        Flux.range(4, 2),
                        Flux.range(6, 5)
                )
                .subscribe(e -> log.info("onNext: {}", e));
    }

    @Test
    void buffer() {
        Flux.range(1, 13)
                .buffer(4)
                .subscribe(e -> log.info("onNext: {}", e));
    }

    @Test
    void window() {
        var windowFlux = Flux.range(101, 20)
                .windowUntil(this::isPrime, true);

        windowFlux.subscribe(
                window -> window.collectList()
                        .subscribe(e -> log.info("window: {}", e))
        );
    }

    @Test
    void groupBy() {
        Flux.range(1, 7)
                .groupBy(e -> e % 2 == 0 ? "Even" : "Odd")
                .subscribe(
                        groupFlux -> groupFlux
                                .scan(
                                        new LinkedList<>(),
                                        (list, el) -> {
                                            list.add(el);
                                            if (list.size() > 2) {
                                                list.remove(0);
                                            }
                                            return list;
                                        })
                                .filter(arr -> !arr.isEmpty())
                                .subscribe(data ->
                                        log.info("{}: {}", groupFlux.key(), data)));
    }

    @Test
    void flatMap() {
        Flux.just("user-1", "user-2", "user-3")
                .flatMap(u -> requestBooks(u)
                        .map(b -> u + "/" + b))
                .subscribe(r -> log.info("onNext: {}", r));
    }

    @Test
    void flatMapDelayError() {
        Flux.just(1, 2, 3, 4)
                .flatMapDelayError(e1 -> Flux.just(5, 6, 7, 9)
                        .map(t -> {
                            if (t % 2 == 0) {
                                throw new RuntimeException("Illegal random value");
                            }
                            return t;
                        }),1,1)
                .subscribe(d -> log.info("onNext: {}", d), er -> log.error("onError: {}", er));
    }

    @Test
    void flatMapDelayError2() {
        Flux.just(1, 2, 3, 4)
                .flatMapDelayError(e1 -> Flux.just(5, 6, 7, 9)
                        .map(t -> {
                            if (t % 2 == 0) {
                                throw new RuntimeException("Illegal random value");
                            }
                            return t;
                        }),1,2)
                .subscribe(d -> log.info("onNext: {}", d), er -> log.error("onError: {}", er));
    }

    @Test
    void flatMapDelayError3() {
        Flux.just(1, 2, 3, 4)
                .flatMapDelayError(e1 -> Flux.just(5, 6, 7, 9)
                        .map(t -> {
                            if (t % 2 == 0) {
                                throw new RuntimeException("Illegal random value");
                            }
                            return t;
                        }),3,1)
                .subscribe(d -> log.info("onNext: {}", d), er -> log.error("onError: {}", er));
    }

    @Test
    void flatMapDelayError4() {
        Flux.just(1, 2, 3, 4)
                .flatMapDelayError(e1 -> Flux.just(5, 6, 7, 9)
                        .map(t -> {
                            if (t % 2 == 0) {
                                throw new RuntimeException("Illegal  value");
                            }
                            return t;
                        }),3,1)
                .switchIfEmpty(Mono.just(0))
                .subscribe(d -> log.info("onNext: {}", d), er -> log.error("onError: {}", er));
    }

    @Test
    void flatMap1() {
        Flux.just(1, 2, 3, 4)
                .flatMap(e1 -> Flux.just(5, 6, 7, 9)
                        .map(t -> {
                            if (t % 2 == 0) {
                                throw new RuntimeException("Illegal  value");
                            }
                            return t;
                        }),3,1)
                .switchIfEmpty(Mono.just(0))
                .subscribe(d -> log.info("onNext: {}", d), er -> log.error("onError: {}", er));
    }

    @Test
    void doOnEach() {
        Flux.just(1,2,3)
                .concatWith(Flux.error(new RuntimeException("Conn error")))
                .doOnEach(s->log.info("signal: {}",s))
                .subscribe();
    }

    @Test
    void materialize() {
        Flux.range(1, 3)
                .doOnNext(e -> log.info("data: {}", e))
                .materialize()
                .doOnNext(e -> log.info("signal: {}", e))
                .dematerialize()
                .collectList()
                .subscribe(r -> log.info("result: {}", r));
    }

    public Flux<String> requestBooks(String user) {
        return Flux.range(1, ThreadLocalRandom.current().nextInt(3) + 1) // (1)
                .map(i -> "book-" + i) // (2)
                .delayElements(Duration.ofMillis(3)); // (3)
    }

    public boolean isPrime(int number) {
        return number > 2
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }
}
