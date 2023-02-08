package com.example.reactiveprogramminginsprong5;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ReactiveContextTest {
    @Test
    public void showcaseContext() {
        printCurrentContext("top")
                .subscriberContext(Context.of("top", "context"))
                .flatMap(__ -> printCurrentContext("middle"))
                .subscriberContext(Context.of("middle", "context"))
                .flatMap(__ -> printCurrentContext("bottom"))
                .subscriberContext(Context.of("bottom", "context"))
                .flatMap(__ -> printCurrentContext("initial"))
                .block();
    }


    //1. Данная строка демонстрирует обращение к реализации Context в Reactor.
    //Как видите, доступ к экземпляру Context в текущем потоке данных осу-
    //ществляется вызовом статического оператора subscriberContext. Так же
    //как в предыдущем примере, получив экземпляр Context ( 1.1), мы можем
    //обратиться к хранимому экземпляру Map (1.2) и сохранить сгенерированное
    //значение. Наконец, возвращаем начальный параметр flatMap.
    //2. Снова обращаемся к экземпляру Context, уже после переключения потока
    //выполнения. Даже притом что этот фрагмент идентичен соответствующе-
    //му фрагменту в предыдущем примере, где мы использовали ThreadLocal,
    //в строке (2.1) мы благополучно извлекаем хранимый ассоциативный мас-
    //сив и получаем сгенерированное случайное значение (2.2).
    //3. В заключение добавляем новый ассоциативный массив "randoms" во входя-
    //щий поток в составе нового экземпляра Context.
    @RepeatedTest(100)
    void name() throws InterruptedException {
        Flux.range(0, 10) //
                .flatMap(k -> //
                        Mono.subscriberContext() // (1)
                                .doOnNext(context -> { // (1.1)
                                    Map<Object, Object> map = context.get("randoms"); // (1.2)
                                    System.out.println("1-> "+map);
                                    map.put(k, new Random(k).nextGaussian()); //
                                }) //
                                .thenReturn(k) // (1.3)
                ) //
                .publishOn(Schedulers.parallel()) //
                .flatMap(k -> //
                        Mono.subscriberContext() // (2)
                                .map(context -> { //
                                    Map<Object, Object> map = context.get("randoms"); // (2.1)
                                    System.out.println("2-> "+map);
                                    return map.get(k); // (2.2)
                                }) //
                ) //
                .subscriberContext(context -> // (3)
                        context.put("randoms", new HashMap()) //
                ) //
                .blockLast();

        Thread.sleep(1000);

    }

    void print(String id, Context context) {
        System.out.println(id + " {");
        System.out.print("  ");
        System.out.println(context);
        System.out.println("}");
        System.out.println();
    }

    Mono<Context> printCurrentContext(String id) {
        return Mono
                .subscriberContext()
                .doOnNext(context -> print(id, context));
    }
}
