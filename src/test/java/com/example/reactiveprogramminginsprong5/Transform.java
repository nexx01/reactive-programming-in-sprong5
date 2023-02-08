package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.util.function.Tuple2;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@Slf4j
public class Transform {

    @Test
    void compose() {
        Hooks.onOperatorDebug();

        Function<Flux<String>, Flux<String>> logUserInfo = (stream) -> { // (1)
            if (ThreadLocalRandom.current().nextBoolean()) {

                return stream
                        .doOnNext(e -> log.info("[path A] User: {}", e));
            } else {
                return stream
                        .doOnNext(e -> log.info("[path B] User: {}", e));
            }
        };
        Flux<String> publisher = Flux.just("1", "2") // (2)
                .compose(logUserInfo); // (3)
        publisher.subscribe(); // (4)
        publisher.subscribe();

    }

    @Test
    void transform() {
        Function<Flux<String>, Flux<String>> logUserInfo = // (1)
                stream -> stream //
                        .index() // (1.1)
                        .doOnNext(tp -> // (1.2)
                                log.info("[{}] User: {}", tp.getT1(), tp.getT2())) //
                        .map(Tuple2::getT2); // (1.3)
        Flux.range(1000, 3) // (2)
                .map(i -> "user-" + i) //
                .transform(logUserInfo) // (3)
                .subscribe(e -> log.info("onNext: {}", e));

    }


}
