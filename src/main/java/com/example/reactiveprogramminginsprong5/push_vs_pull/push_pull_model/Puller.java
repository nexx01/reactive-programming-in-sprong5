package com.example.reactiveprogramminginsprong5.push_vs_pull.push_pull_model;

import org.reactivestreams.Publisher;
import rx.Observable;

public class Puller {
    final AsyncDatabaseClient dbClient = new DelayedFakeAsyncDatabaseClient();

    public Publisher<Item> list(int count) {
        var source = dbClient.getStreamOfItems();
        var takeFilter = new TakeFilterOperator<Item>(source, count, this::isValid);

        return takeFilter;
    }

    boolean isValid(Item item) {
        return Integer.parseInt(item.getId()) % 2 == 0;
    }
}
