package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
public class ProgrammalyCreateDateStream {


    //Фабричный метод push позволяет программно создать экземпляр Flux на основе
    //однопоточного производителя. Этот подход можно использовать для адаптации
    //асинхронного, однопоточного, многозначного API, не заботясь об управлении
    //обратным давлением или отмене подписки. Оба аспекта управляются посред-
    //ством сигналов, если подписчик не справляется с нагрузкой.

    //Вызывается фабричный метод push, адаптирующий некий существующий
    //API под реактивную парадигму. Для простоты использован Java Stream API,
    //с помощью которого генерируется 1000 целочисленных элементов (1.1)
    //и передается объекту emitter типа FluxSink (1.2). Внутри метода мы никак
    //не заботимся ни об обратном давлении, ни об отмене подписки, потому что
    //эти функции реализует сам метод push.
    //2. Задержка после каждого элемента для имитации обратного давления.
    //3. Подписка на события onNext.
    @Test
    void push() {
        Flux.push(emmiter -> IntStream
                        .range(2000, 3000)
                        .forEach(emmiter::next))
                .delayElements(Duration.ofMillis(1))
                .subscribe(e -> log.info("onNext: {}", e));
    }

    //метод create, который дейст-
    //вует подобно методу push, но дополнительно позволяет пересылать события из
    //разных потоков выполнения, сериализуя экземпляр FluxSink. Оба метода допус-
    //кают переопределение стратегии реакции на переполнение и освобождение ре-
    //сурсов путем регистрации дополнительных обработчиков, как показано в следу-
    //ющем коде.
    @Test
    void create() {
        Flux.create(emitter -> {
                    emitter.onDispose(() -> log.info("Disposed"));
                    // отправить собфтик объекту emitter
                })
                .subscribe(e -> log.info("onNext: {}", e));
    }

    //Пояснения к коду.
    //1. С помощью фабричного метода generate можно создать свою реактивную
    //последовательность. В качестве начального состояния используется Tuples.
    //of(0L, 1L) (1.1). На этапе генерирования посылаем сигнал onNext со ссыл-
    //кой на второе значение из пары, описывающей состояние (1.2), и вычис-
    //ляем новую пару, опираясь на следующее значение в последовательности
    //Фиббоначчи (1.3).
    //2. С помощью оператора delayElements вводится некоторая задержка между
    //сигналами onNext.
    //3. Ради простоты извлекаем только первые семь элементов.
    //4. Наконец, чтобы запустить генерирование последовательности, подписы-
    //ваемся на события.
//
//    Как видите, каждое новое значение синхронно передается подписчику, и только
//    после этого генерируется новое. Данный подход можно использовать для созда-
//    ния сложных реактивных последовательностей, требующих сохранения проме-
//    жуточного состояния между созданием элементов.
    @Test
    void generate() {
        Flux.generate(
                        () -> Tuples.of(0L, 1L),
                        (state, sink) -> {
                            log.info("generated value: {}", state.getT1());
                            sink.next(state.getT2());
                            var newValue = state.getT1() + state.getT2();
                            return Tuples.of(state.getT2(), newValue);
                        }
                )
                .delayElements(Duration.ofMillis(1))
                .take(7)
                .subscribe(e -> log.info("onNext: {}", e));
    }

    //using -  реализует семантику try-with-resources
    //Пояснения к коду.
    //1. Класс Connection управляет некоторыми внутренними ресурсами и уве-
    //домляет об этом, реализуя интерфейс AutoClosable.
    //2. Метод getData имитирует операцию ввода/вывода и может вызывать ис-
    //ключения (2.1) или возвращать коллекцию Iterable с полезными данными
    //(2.2).
    //3. Метод close может освобождать внутренние ресурсы и должен вызываться
    //всегда, даже если в getData произойдет ошибка.
    //4. Статический фабричный метод newConnection всегда возвращает новый эк-
    //земпляр класса Connection.
    @Test
    void usingImperative() {
//        Использует Java-инструкцию try для создания нового соединения и автома-
//                тически закрывает его при выходе из текущего блока кода.
//        2. Получает и обрабатывает данные.
//        3. В случае исключения регистрирует сообщение об ошибке в журнале
        try (Connection conn = Connection.newConnection()) { // (1)
            conn.getData().forEach( // (2)
                    data -> log.info("Received data: {}", data)
            );
        } catch (Exception e) { // (3)
            log.info("Error: {}", e.getMessage());
        }
    }

    //Пояснения к коду.
    //1. Фабричный метод using позволяет связать жизненный цикл экземпляра
    //Connection с жизненным циклом обертывающего потока данных. Метод
    //using должен знать, как создать ресурс. В данном случае это код создания
    //нового соединения (1.1). Также метод должен знать, как преобразовать
    //только что созданный ресурс в реактивный поток. В данном случае это вы-
    //зов метода fromIterable (1.2). Наконец, он должен знать, как закрыть ре-
    //сурс. В данном случае по завершении оработки должен быть вызван метод
    //close экземпляра соединения.
    //2. Конечно, чтобы начать фактическую обработку, нужно создать подписку,
    //обрабатывающую сигналы onNext, onError и onComplete.
    @Test
    void using() {
        Flux<String> ioRequestResults = Flux.using( // (1)
                Connection::newConnection, // (1.1)
                connection -> Flux.fromIterable(connection.getData()), // (1.2)
                Connection::close // (1.3)
        );
        ioRequestResults.subscribe( // (2)
                data -> log.info("Received data: {}", data), //
                e -> log.info("Error: {}", e.getMessage()), //
                () -> log.info("Stream finished"));
    }
}
@Slf4j
class Connection implements AutoCloseable {
    private final Random rnd = new Random();

    public Iterable<String> getData() { // (2)
        if (rnd.nextInt(10) < 3) { // (2.1)
            throw new RuntimeException("Communication error");
        }
        return Arrays.asList("Some", "data"); // (2.2)
    }

    public void close() { // (3)
        log.info("IO Connection closed");
    }

    public static Connection newConnection() { // (4)
        log.info("IO Connection created");
        return new Connection();
    }
}