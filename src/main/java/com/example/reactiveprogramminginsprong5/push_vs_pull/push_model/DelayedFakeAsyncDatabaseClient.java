package com.example.reactiveprogramminginsprong5.push_vs_pull.push_model;


import rx.Observable;

import java.util.concurrent.TimeUnit;

public class DelayedFakeAsyncDatabaseClient implements AsyncDatabaseClient {
    @Override
    public Observable<Item> getStreamOfItems() {
        return Observable.range(1, Integer.MAX_VALUE)
                .map(i -> new Item("" + 1))
                .delay(50, TimeUnit.MILLISECONDS)
                .delaySubscription(100, TimeUnit.MILLISECONDS);
    }
}
