package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
public class schedulers {


    @Test
    void publishOn() {

        var subscribe = Flux.interval(Duration.ofMillis(1000))
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(e -> e % 2 == 0)
                .publishOn(Schedulers.elastic())
                .subscribe(e -> log.info("onNext: {}", e));

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void publishOn2() {
        var subscribe = Flux.interval(Duration.ofMillis(10))
                .doOnEach(e->{
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onEach: {}", e);
                })
                .filter(e -> e % 2 == 0)
                .publishOn(Schedulers.elastic(),20)
                .subscribe(e -> log.info("onNext: {}", e));

        while (!subscribe.isDisposed()) {}
    }


    @Test
    void publishOn3() {
        var subscribe = Flux.interval(Duration.ofMillis(10))
                .doOnEach(e->{

                    log.info("onEach: {}", e);
                })
                .filter(e -> e % 2 == 0)
               .publishOn(Schedulers.elastic(),20)
                .subscribe(e -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onNext: {}", e);
                });

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void publishOn4() {
        var subscribe = Flux.interval(Duration.ofMillis(10))
                .doOnEach(e->{

                    log.info("onEach: {}", e);
                })
                .filter(e -> e % 2 == 0)
                .publishOn(Schedulers.elastic(),false,20)
                .subscribe(e -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onNext: {}", e);
                });

        while (!subscribe.isDisposed()) {}
    }


    @Test
    void publishOn6() {
        var subscribe = Flux.interval(Duration.ofMillis(10))
                .cache()
                .doOnEach(e-> log.info("onEach: {}", e))
                .filter(e -> e % 2 == 0)
                .publishOn(Schedulers.elastic(),false,20)
                .subscribe(e -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onNext: {}", e);
                });
        while (!subscribe.isDisposed()) {}
    }

    @Test
    void publishOn7() throws InterruptedException {
        var flux = Flux.interval(Duration.ofMillis(10))
//                .doOnEach(e -> log.info("onEach(before cache): {}", e))
                .cache(2000)
//                .cache()
//                .doOnEach(e -> log.info("onEach(after cache): {}", e))
                .filter(e -> e % 2 == 0)
                .publishOn(Schedulers.elastic(), false, 20);

        var subscribe = flux.subscribe(e -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
//            log.info("onNext second: {}", e);
        });


        Thread.sleep(3000);
        flux .subscribe(e -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onNext first: {}", e);
                });


        while (!subscribe.isDisposed()) {}
    }

    @Test
    void publishOn5() {
//        var subscribe = Flux.defer(()->Flux.range(1,1000))
        var subscribe = Flux.range(1,1000)
                .doOnEach(e->{

                    log.info("onEach: {}", e);
                })
                .filter(e -> e % 2 == 0)
                .publishOn(Schedulers.elastic(),false,20)
                .subscribe(e -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onNext: {}", e);
                });

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void subscribeOn() {

        var flux = Flux.interval(Duration.ofMillis(10)) //subscribeOn not work with
                //interval because interval run in other thread
//                .cache(2)
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(f->f%2==0)
//                .subscribeOn(Schedulers.elastic())
                ;

        var subscribe = flux.subscribe(d ->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            log.info("onNext: {}", d);
        });

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void subscribeOn2() {
        var flux = Flux.range(1,10000)
                .subscribeOn(Schedulers.elastic())
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(f->f%2==0)
                ;
        var subscribe = flux.subscribe(d -> log.info("onNext: {}", d));

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void subscribeOn3() {
        var flux = Flux.range(1,10000)
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(f->f%2==0)
                .subscribeOn(Schedulers.elastic())
                ;
        var subscribe = flux.subscribe(d -> log.info("onNext: {}", d));

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void subscribeOn4() {
        var flux = Flux.range(1,10000)
                .subscribeOn(Schedulers.parallel())
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(f->f%2==0)
                .subscribeOn(Schedulers.elastic())
                ;
        var subscribe = flux.subscribe(d -> log.info("onNext: {}", d));

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void subscribeOn_publishOn() {
        var flux = Flux.range(1,10000)
                .publishOn(Schedulers.parallel())
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(f->f%2==0)
                .subscribeOn(Schedulers.elastic())
                ;
        var subscribe = flux.subscribe(d -> log.info("onNext: {}", d));

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void subscribeOn_publishOn2() {
        var flux = Flux.range(1,10000)
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(f->f%2==0)
                .subscribeOn(Schedulers.elastic())
                .publishOn(Schedulers.parallel())
                ;
        var subscribe = flux.subscribe(d -> log.info("onNext: {}", d));

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void subscribeOn_publishOn4() {
        var flux = Flux.range(1,10000)
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(f->f%2==0)
                .subscribeOn(Schedulers.elastic())
                .doOnEach(e->log.info("onEach (between): {}",e))
                .publishOn(Schedulers.parallel())
                ;
        var subscribe = flux.subscribe(d -> log.info("onNext: {}", d));

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void subscribeOn_publishOn5() {
        var flux = Flux.range(1,10000)
                .doOnEach(e->log.info("onEach: {}",e))
                .filter(f->f%2==0)
                .subscribeOn(Schedulers.elastic())
                .doOnEach(e->log.info("onEach (between): {}",e))
                .publishOn(Schedulers.parallel())
                ;
        var subscribe = flux.subscribe(d -> log.info("onNext: {}", d));

        while (!subscribe.isDisposed()) {}
    }

    @Test
    void parallel() {
        var flux = Flux.range(0, 1000)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(d -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onNext: {}", d);
                    return d;
                });

        var subscribe = flux.subscribe(d ->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            log.info("onNext: {}", d);
        });

        while (!subscribe.isDisposed()) {}

    }

    @Test
    void parallel2() {
        var flux = Flux.range(0, 1000)
                .parallel()
//                .runOn(Schedulers.parallel())
                .map(d -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onNext: {}", d);
                    return d;
                });

        var subscribe = flux.subscribe(d ->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            log.info("onNext: {}", d);
        });

        while (!subscribe.isDisposed()) {}

    }

    @Test
    void singleScheduler() {
        var flux = Flux.range(0, 1000)
                .publishOn(Schedulers.single())
                .map(d -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    log.info("onNext: {}", d);
                    return d;
                });

        var subscribe = flux.subscribe(d ->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            log.info("onNext: {}", d);
        });

        while (!subscribe.isDisposed()) {}

    }
}
