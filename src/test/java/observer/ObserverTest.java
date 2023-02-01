package observer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ObserverTest {

    @Test
    void observerHandleEventsFromSubjectWithAssertion() {


    //given
    Subject<String> subject = new ConcreteSubject();
    Observer<String> observerA = Mockito.spy(new ConcreteObserverA());
    Observer<String> observerB = Mockito.spy(new ConcreteObserverB());

    //when
        subject.notifyObservers("No listeners");

        subject.registerObserver(observerA);
        subject.notifyObservers("Message for A");

        subject.registerObserver(observerB);
        subject.notifyObservers("Message for B & A");


        subject.unregisterObserver(observerA);
        subject.notifyObservers("Message for B");

        subject.unregisterObserver(observerB);
        subject.notifyObservers("No listeners");

//        then
        verify(observerA).observe("Message for A");
        verify(observerA).observe("Message for B & A");
        verifyNoMoreInteractions(observerA);

        verify(observerB).observe("Message for B & A");
        verify(observerB).observe("Message for B");
        verifyNoMoreInteractions(observerB);
    }

    @Test
    void subjectLeveragesLamdas() {
        var concreteSubject = new ConcreteSubject();

        concreteSubject.registerObserver(e -> System.out.println("A: " + e));
        concreteSubject.registerObserver(e -> System.out.println("B: " + e));
        concreteSubject.notifyObservers("This message will receive A & B");
    }

    @Test
    void parralelSubject() {

        var parallelSubject = new ParallelSubject();

        Supplier<String> thread = () -> Thread.currentThread().getName();
        Function<String, Observer<String>> generateObserve = (String name) ->
                (e -> System.out.println(thread.get() + " | " + name + ": " + e));

        parallelSubject.registerObserver(generateObserve.apply("A"));
        parallelSubject.registerObserver(generateObserve.apply("B"));

        IntStream.range(0, 10)
                .forEach(i ->
                        parallelSubject.notifyObservers("Temperature " + i));
    }
}