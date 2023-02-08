package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class CustomSibscriber {

    //Однако здесь описан неправильный подход к определению подписки. Он рушит
    //линейность кода и способствует появлению ошибок. Но самое сложное – мы долж-
    //ны организовать управление обратным давлением и обеспечить соответствие
    //подписчика всем требованиям TCK. Кроме того, в предыдущем примере мы на-
    //рушили несколько требований TCK, касающихся проверки и отмены подписки.
    //Чтобы избежать таких проблем, рекомендуется наследовать класс BaseSubscriber
    //из библиотеки Project Reactor.
    @Test
    void wrongCustomSubscriber1() {
        var subscriber = new Subscriber<String>() {

            volatile Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                subscription = s;
                log.info("initial request for 1 element");
                subscription.request(1);
            }

            @Override
            public void onNext(String s) {
                log.info("onNext: {}", s);
                log.info("requesting 1 more element");
                subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                log.warn("onError: {}", t.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };

        var stream = Flux.just("Hello", "world", "!");
        stream.subscribe(subscriber);
        //. Наш подписчик должен хранить ссылку на экземпляр подписки Subscrip-
        //tion, связывающий издателя Publisher и нашего подписчика Subscriber.
        //Поскольку подписка и обработка данных могут происходить в разных по-
        //токах выполнения, мы использовали ключевое слово volatile, чтобы обес-
        //печить правильность ссылки на экземпляр Subscription во всех потоках
        //выполнения.
        //2. После оформления подписки наш подписчик Subscriber информируется об-
        //ратным вызовом onSubscribe. В нем мы сохраняем подписку (2.1) и по-
        //сылаем запрос с начальным требованием (2.2). Без такого запроса TCK-
        //совместимый производитель не будет посылать данные и обработка эле-
        //ментов вооб ще никогда не начнется.
        //3. В обратном вызове onNext регистрируем полученные данные и запрашива-
        //ем следующий элемент. В данном случае используем простую модель PULL
        //(subscription.request(1)) управления обратным давлением.
        //4. Генерируем простой поток данных с помощью фабричного метода just.
        //5. Подписываем нашего подписчика на реактивный поток, который определен
        //в строке (4).
    }


    /*
    Кроме методов hookOnSubscribe(Subscription) и hookOnNext(T) можно пере-
определить такие методы, как hookOnError(Throwable), hookOnCancel(),
hookOnComplete(), и др. Класс BaseSubscriber предлагает методы для более точ-
ного управления требованиями – request(long) и requestUnbounded(). Кроме
того, с классом BaseSubscriber намного проще реализовать TCK-совместимого
подписчика. Такой подход выглядит более предпочтительным, когда сам подпис-
чик обладает ценными ресурсами со своим жизненным циклом. Например, под-
писчик может включать обработчик файлов или соединение WebSocket со сторон-
ней службой.
*/
    @Test
    void rightCustomSubscriber() {
        class MySubscriber<T> extends BaseSubscriber<T> {
            public void hookOnSubscriber(Subscription subscription) {
                log.info("initial request for 1 element");
                request(1);
            }

            public void hookOnNext(T value) {
                log.info("onNext: {}", value);
                log.info("" +
                        "requesting 1 more element");
                request(1);
            }
        }

        var subscriber = new MySubscriber<String>();
        var just = Flux.just("Hello", "world", "!");
        just.subscribe(subscriber);
    }
}
