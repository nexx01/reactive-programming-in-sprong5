package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
//784

@Slf4j
public class TransactionWithReactiveStream {

    @Test
    void usingWhen() throws InterruptedException {
        Flux.usingWhen(
                Transaction.beginTransaction(), // (1)
                transaction -> transaction.insertRows(Flux.just("A", "B", "C")), // (2)
                Transaction::commit, // (3)
                Transaction::rollback // (4)
        ).subscribe(
                d -> log.info("onNext: {}", d),
                e -> log.info("onError: {}", e.getMessage()),
                () -> log.info("onComplete")
        );

        Thread.sleep(2000);
    }


    @Test
    void name() throws InterruptedException {
        Flux.just("user-1") // (1)
                .flatMap(user -> // (2)
                        recommendedBooks(user) // (2.1)
                                .retryBackoff(5, Duration.ofMillis(100)) // (2.2)
                                .timeout(Duration.ofSeconds(3)) // (2.3)
                                .onErrorResume(e -> Flux.just("The Martian"))) // (2.4)
                .subscribe( // (3)
                        b -> log.info("onNext: {}", b),
                        e -> log.warn("onError: {}", e.getMessage()),
                        () -> log.info("onComplete")
                );       Thread.sleep(2000);

    }

    //1. Откладывает вычисления, пока не появится подписчик
    //2. Весьма вероятно, что наша служба вернет ошибку, поэтому мы сдвигаем все
    //сигналы во времени, применив оператор delaySequence.
    //3. Если клиенту повезет, он получит свои рекомендации с некоторой задерж-
    //кой.
    //4. Ответ службы записывается в журнал
     public Flux<String> recommendedBooks(String userId) {
       return   Flux.defer(() -> { // (1)
             if (ThreadLocalRandom.current().nextInt(10) < 7) {
                 return Flux.<String>error(new RuntimeException("Err")) // (2)
                         .delaySequence(Duration.ofMillis(100));
             } else {
                 return Flux.just("Blue Mars", "The Expanse") // (3)
                         .delayElements(Duration.ofMillis(50));
             }
         }).doOnSubscribe(s -> log.info("Request for {}", "userId")); // (4)

     }
}

@Slf4j
 class Transaction {
    private static final Random random = new Random();
    private final int id;
    public Transaction(int id) {
        this.id = id;
        log.info("[T: {}] created", id);
    }
    public static Mono<Transaction> beginTransaction() { // (1)
        return Mono.defer(() ->
                Mono.just(new Transaction(random.nextInt(1000))));
    }
    public Flux<String> insertRows(Publisher<String> rows) { // (2)
        return Flux.from(rows)
                .delayElements(Duration.ofMillis(100))
                .flatMap(r -> {
                    if (random.nextInt(10) < 2) {
                        return Mono.error(new RuntimeException("Error: " + r));
                    } else {
                        return Mono.just(r);
                    }
                });
    }
    public Mono<Void> commit() { // (3)
        return Mono.defer(() -> {
            log.info("[T: {}] commit", id);
            if (random.nextBoolean()) {
                return Mono.empty();
            } else {
                return Mono.error(new RuntimeException("Conflict"));
            }
        });
    }
    public Mono<Void> rollback() { // (4))
        return Mono.defer(() -> {
            log.info("[T: {}] rollback", id);
            if (random.nextBoolean()) {
                return Mono.empty();
            } else {
                return Mono.error(new RuntimeException("Conn error"));
            }
        });
    }
}