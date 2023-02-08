package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.IntStream;

@Slf4j
public class TransformReactiveStreamWithOperators {

    @Test
    void index1() {

        Flux.just("a", "b", "c", "d")
                .index()
                .subscribe(d -> System.out.println(d));
    }

    @Test
    void index2() {
        Flux.range(2018, 5)
                .timestamp()
                .subscribe(d -> System.out.println(d));
    }


    //1. С помощью оператора range генерируются некоторые данные (числа от 2018
    //до 2022). Этот оператор возвращает последовательность типа Flux<Integer>.
    //2. С помощью оператора timestamp к значениям присоединяется текущее вре-
    //мя. Теперь последовательность имеет тип Flux<Tuple2<Long, Integer>>.
    //3. К последовательности применяется оператор index. Теперь последователь-
    //ность имеет тип Flux<Tuple2<Long, Tuple2<Long, Integer>>>.
    //4. Оформляется подписка на последовательность, и производится регистрация
    //ее элементов в журнале. Вызов e.getT1() возвращает индекс (4.1), а вызов
    //e.getT2().getT1() – метку времени, значение которой выводится в удобо-
    //читаемом формате с помощью класса Instant ( 4.2), а вызов e.getT2().
    //getT2() возвращает фактическое значение (4.3)
    @Test
    void index3() {
        Flux.range(2018, 5)
                .timestamp()
                .index()
                .subscribe(
                        e -> log.info("index: {},ts: {}, value: {}",
                                e.getT1(),
                                Instant.ofEpochMilli(e.getT2().getT1()),
                                e.getT2().getT2())
                );
    }

    @Test
    void ignoreElements1() {
        Flux.just(1, 2, 3, 45, 6)
                .ignoreElements()
                .subscribe(d -> System.out.println(d));
    }

    @Test
    public void shouldCreateDefer() {
        Mono<User> userMono = requestUserData(null);
        StepVerifier.create(userMono)
                .expectNextCount(0)
                .expectErrorMessage("Invalid user id")
                .verify();
    }

    @Test
    public void startStopStreamProcessing() throws Exception {
        Mono<?> startCommand = Mono.delay(Duration.ofSeconds(1));
        Mono<?> stopCommand = Mono.delay(Duration.ofSeconds(3));
        Flux<Long> streamOfData = Flux.interval(Duration.ofMillis(100));

        streamOfData
                .skipUntilOther(startCommand)
                .takeUntilOther(stopCommand)
                .subscribe(System.out::println);

        Thread.sleep(4000);
    }

    @Test
    void sortedList() {
        Flux.just(1, 6, 2, 8, 3, 1, 5, 1)
                .collectSortedList(Comparator.reverseOrder())
                .subscribe(System.out::println);
    }

    @Test
    void sortedMap() {
        Flux.just(1, 6, 2, 8, 3, 1, 5, 1)
                .collectMap(el -> el, el -> el, TreeMap::new)
                .subscribe(System.out::println);
    }

    @Test
    void sortedMap2() {
        Flux.just(1, 1, 1, 6, 2, 8, 3, 1, 5, 1)
                .collectMap(el -> el, el -> el, TreeMap::new)
                .subscribe(System.out::println);
    }

    @Test
    void sortedMultiMap() {
        Flux.just(1, 6, 2, 8, 3, 1, 5, 1)
                .collectMultimap(el -> el, el -> el, TreeMap::new)
                .subscribe(System.out::println);
    }

    @Test
    void collectWithCollectors() {
        Flux.just(1,1,1,1, 6, 2, 8, 3, 1, 5, 1)
                .collect(Collector.of(ArrayList::new, ArrayList::add, (list1, list2) -> {
                    list2.addAll(list1);
                    return list1;
                }))
                .subscribe(System.out::println);
    }

    @Test
    void collectWithCollectors_WithRepeat() {
        Flux.just(1,1,1,1, 6, 2, 8, 3, 1, 5, 1)
                .repeat(2)
                .collect(Collector.of(ArrayList::new, ArrayList::add, (list1, list2) -> {
                    list2.addAll(list1);
                    return list1;
                }))
                .subscribe(System.out::println);
    }

    @Test
    void distinctUntilChanged() {
        Flux.just(1,1,1,1, 6, 2, 8, 3, 1, 5, 1)
                .distinctUntilChanged()
                .subscribe(System.out::println);
    }

    @Test
    void distinctUntilChanged2() {
        Flux.just(1,1,1,1, 6, 2, 8, 3, 1, 5, 1)
                .distinctUntilChanged(integer -> {
                    var random = ThreadLocalRandom.current().nextInt();
                    return integer + random;
                })
                .subscribe(System.out::println);
    }

    @Test
    void distinctUntilChanged3() {
        Flux.just(1,1,1,1, 6, 2, 8, 3, 1, 5, 1)
                .distinctUntilChanged(Function.identity(),(el1, el2)->el1+5==el2)
                .subscribe(System.out::println);
    }


    @Test
    void any() {
        Flux.just(3, 5, 7, 9, 11, 15, 16, 17)
                .any(e -> e % 2 == 0)
                .subscribe(hasEvens -> log.info("Has evens: {}", hasEvens));
    }

    @Test
    void reduce() {
        Flux.range(1, 5)
                .reduce(0, (acc, elem) -> acc + elem)
                .subscribe(result -> log.info("Result: {}", result));
    }

    @Test
    void scan() {
        Flux.range(1, 5)
                .scan(0, (acc, elem) -> acc + elem)
                .subscribe(result -> log.info("Result: {}", result));
    }

    @Test
    void scan2() {
        var bucketSize = 5;
        Flux.range(1, 500)
                .index()
                .scan(
                        new int[bucketSize],
                        (acc, el) -> {
                            acc[(int) (el.getT1() % bucketSize)] = el.getT2();
                            return acc;
                        })
                .skip(bucketSize)
                .map(array -> Arrays.stream(array).sum() * 1.0 / bucketSize)
                .subscribe(al -> log.info("Running average: {}", al));
    }

//Лямбда-выражение в вызове метода subscribe получит только 4 и 5, даже притом
//что 1, 2 и 3 были сгенерированы и обработаны потоком
    @Test
    void thenMany() {
        Flux.just(1, 2, 3)
                .thenMany(Flux.just(4, 5))
                .log()
                .subscribe(e -> log.info("onNext: {}", e));
    }

    public Mono<User> requestUserData(String userId) {
        return Mono.defer(() ->
                isValid(userId)
                        ? Mono.fromCallable(() -> requestUser(userId))
                        : Mono.error(new IllegalArgumentException("Invalid user id")));
    }





    public Mono<User> requestUserData2(String userId) {
        return isValid(userId)
                ? Mono.fromCallable(() -> requestUser(userId))
                : Mono.error(new IllegalArgumentException("Invalid user id"));
    }

    private boolean isValid(String userId) {
        return userId != null;
    }

    private void doLongAction() {
        log.info("Long action");
    }

    private User requestUser(String id) {
        return new User();
    }

    private String httpRequest() {
        log.info("Making HTTP request");
        throw new RuntimeException("IO error");
    }

    public boolean isPrime(int number) {
        return number > 2
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

    static class Connection implements AutoCloseable {
        private final Random rnd = new Random();

        static Connection newConnection() {
            log.info("IO Connection created");
            return new Connection();
        }

        public Iterable<String> getData() {
            if (rnd.nextInt(10) < 3) {
                throw new RuntimeException("Communication error");
            }
            return Arrays.asList("Some", "data");
        }

        @Override
        public void close() {
            log.info("IO Connection closed");
        }
    }

    public static class MySubscriber<T> extends BaseSubscriber<T> {

        public void hookOnSubscribe(Subscription subscription) {
            System.out.println("Subscribed");
            request(1);
        }

        public void hookOnNext(T value) {
            System.out.println(value);
            request(1);
        }
    }

    static class User {
        public String id, name;
    }
}
