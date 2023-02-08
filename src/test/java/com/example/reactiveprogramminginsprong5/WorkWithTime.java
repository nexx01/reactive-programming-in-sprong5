package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class WorkWithTime {

    @Test
    void elapsed() throws InterruptedException {
        Flux.range(0, 5)
                .delayElements(Duration.ofMillis(100))
                .elapsed()
                .subscribe(e -> log.info("Elapsed {} ms: {}", e.getT1(), e.getT2()));

        Thread.sleep(2000);
    }

    @Test
    void name() {
    }
}
