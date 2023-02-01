import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class RxJavaExampleTest {

    @Test
    @SuppressWarnings("Deprecated")
    void simpleRxJavaWorkflow() {
        Observable<String> observable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("Hello, reactive world!");
                        subscriber.onCompleted();
                    }
                }
        );

    }

    @Test
    void simpleRxJavaWorkflowWithLambdas() {

        Observable.create(
                subscriber -> {
                    subscriber.onNext("Hello, reactive world!");
                    subscriber.onCompleted();
                }
        ).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("Done!")
        );
    }

    @Test
    void zipOperatorExamples() {
        Observable.zip(
                Observable.just("A", "B", "C"),
                Observable.just("1", "2", "3"),
                (x, y) -> x + y
        ).forEach(System.out::println);
    }

    @Test
    void timeBasedSequenceExample() throws InterruptedException {

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(e -> System.out.println("Received: " + e));
        Thread.sleep(5000);
    }

    @Test
    void manageSubscribtion() {
        var subscription = new AtomicReference<Subscription>();
        subscription.set(
                Observable.interval(100,TimeUnit.MILLISECONDS)
                        .subscribe(
                                e->{
                                    System.out.println("Received: "+e);
                                    if (e >= 3) {
                                        subscription.get().unsubscribe();
                                    }
                                }
                        )
        );

        do {
            //executing something usefull...
        } while (!subscription.get().isUnsubscribed());

    }

    @Test
    void manageSubscription2() throws InterruptedException {
        var externalSignal = new CountDownLatch(3);

        var subscription = Observable
                .interval(100, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);

        externalSignal.await();
        subscription.unsubscribe();
    }

    @Test
    void deferSynchronousRequest() throws InterruptedException {
        var query = "query";

        Observable.fromCallable(() -> doSlowSyncRequest(query))
                .subscribeOn(Schedulers.io())
                .subscribe(this::processResult);

        Thread.sleep(1000);
    }

    private String doSlowSyncRequest(String query) {
        return "result";
    }

    private void processResult(String result) {
        System.out.println(Thread.currentThread().getName() + ": " + result);
    }
}
