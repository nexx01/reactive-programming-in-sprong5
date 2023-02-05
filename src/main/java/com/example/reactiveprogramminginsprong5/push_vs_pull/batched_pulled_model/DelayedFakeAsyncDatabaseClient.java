package com.example.reactiveprogramminginsprong5.push_vs_pull.batched_pulled_model;

import io.reactivex.Flowable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class DelayedFakeAsyncDatabaseClient implements AsuncDatabaseClient{

    @Override
    public CompletionStage<List<Item>> getNextBatchAfterId(String id, int count) {
        var future = new CompletableFuture<List<Item>>();

        Flowable.range(Integer.parseInt(id) + 1, count)
                .map(i -> new Item("" + 1))
                .collectInto(new ArrayList<Item>(), ArrayList::add)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribe();

        return future;

    }
}
