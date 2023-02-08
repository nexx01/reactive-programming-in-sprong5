package com.example.reactiveprogramminginsprong5;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadLocalProblemShowcaseTest {

    @Test
    public void shouldFailDueToDifferentThread() {
        ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<>();
        threadLocal.set(new HashMap<>());

        Flux.range(0, 10)
                .doOnNext(k -> threadLocal.get().put(k, new Random(k).nextGaussian()))
                .publishOn(Schedulers.parallel())
                .map(k -> threadLocal.get().get(k))
                .blockLast();
        //(expected = NullPointerException.class)
    }
}