package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.mock.MockName;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class ReactorTest {

    @Test
    void repeat1() {
        Flux.range(1, 5)
                .repeat()
                .take(20)
                .subscribe(System.out::println);
    }

    @Test
    void repeat2_withOutOfMemoryException() {
        Flux.range(1, 5)
                .repeat()
//                .doOnNext(System.out::println)
                .collectList()
                .block();
    }

    @Test
    void range3() {
        Flux.range(2010, 9)
                .subscribe(x -> System.out.println(x));
    }

    @Test
    void range4() {
        Flux.range(2010, 9)
                .subscribe(x -> System.out.println(x));

    }

    @Test
    void simpleReactiveStream() {
        Flux.just("a", "b", "c")
                .subscribe(
                        data -> log.info("onNext: {}", data),
                        err -> {/*ignore*/},
                        () -> log.info("onComplete")
                );
    }

    @Test
    void ruleSubscription() {
        Flux.range(1, 100)
                .doOnNext(data-> log.info("doOnNext: {}",data))
                .subscribe(
                        data->log.info("onNext: {}",data),
                        err->{/*ignore*/},
                        ()->log.info("onComplete"),
                        subscription -> {
                            subscription.request(4);
                            subscription.request(1);
                            subscription.cancel();
                        }
                );
        //Этот код выполняет следующие операции.
        //1. Сначала с помощью оператора range генерируется 100 значений.
        //2. Выполняется подписка на поток, так же как это делалось в предыдущем
        //примере.
        //3. Однако теперь мы управляем подпиской. Сначала запрашивается четыре
        //элемента (3.1), а потом подписка сразу же отменяется (3.2), то есть другие
        //элементы вообще не должны генерироваться.
    }

    @Test
    void useDisposable() throws InterruptedException {
        var disposable = Flux.interval(Duration.ofMillis(50))
                .subscribe(
                        data -> log.info("onNext: {}", data)
                );
        Thread.sleep(200);
        disposable.dispose();

    }
}
