package com.example.reactiveprogramminginsprong5;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.function.Consumer;

public class HooksReactor {

    @Test
    void debg() throws InterruptedException {
//        ReactorDebugAgent.init();
        Hooks.onOperatorDebug();

        Flux.just(1,2,3,4,5,6,7,8,9,10)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->{

                    throw new RuntimeException();
                })
                .subscribe();

        Thread.sleep(2000);
    }

    @Test
    void debg2() throws InterruptedException {
//        Hooks.onOperatorDebug();

        Flux.just(1,2,3,4,5,6,7,8,9,10)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->{
                    throw new RuntimeException();
                })
                .subscribe();

        Thread.sleep(2000);
    }

    @Test
    void debg3() throws InterruptedException {
        Hooks.onEachOperator(f-> {
            System.out.println("--------"+f.getClass());
            return f;
        });

        Flux.just(1,2,3,4,5,6,7,8,9,10)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->{
                    throw new RuntimeException();
                })
                .subscribe();

        Thread.sleep(2000);
    }

    @Test
    void debg4() throws InterruptedException {
        Hooks.onErrorDropped(f-> {
            System.out.println("--------"+f.getClass());
        });

        Flux.just(1,2,3,4,5,6,7,8,9,10)
                .map(f->f*2)
                .map(f->f*2)
                .map(f->{
                    throw new RuntimeException();
                })
                .map(f->f)
                .subscribe();

        Thread.sleep(2000);
    }

    @Test
    void debg5() throws InterruptedException {
        Hooks.onLastOperator("setup MDC", o -> {
            Consumer<Signal<Object>> mdcConsumer = sig -> MDC.put("request-id", sig.getContext().getOrDefault("request-id", "UNDEFINED").toString());
            if (o instanceof Mono) {
                return ((Mono) o).doOnEach(mdcConsumer);
            } else {
                return Flux.from(o)
                        .doOnEach(mdcConsumer);
            }
        });

        Flux.just(1,2,3,4,5,6,7,8,9,10)
                .map(f->f*2)
                .map(f->f*2)
//                .map(f->{
//                    throw new RuntimeException();
//                })
                .map(f->f)
                .subscribe();

        Thread.sleep(2000);
    }

    @Test
    public void onLastAssemblyLoop() {
        Hooks.onLastOperator("setup MDC", o -> {
            Consumer<Signal<Object>> mdcConsumer = sig -> System.out.println("hello");
            if (o instanceof Mono) {
                return ((Mono) o).doOnEach(mdcConsumer);
            } else {
                return Flux.from(o)
                        .doOnEach(mdcConsumer);
            }
        });

        Flux.range(1, 10).subscribe();

        Flux.just(100)
                .flatMap(i -> Mono.just(i))
                .subscribe();
    }
}
