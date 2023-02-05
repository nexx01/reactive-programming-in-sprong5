package com.example.reactiveprogramminginsprong5.push_vs_pull.pure_pull_model;

import io.reactivex.Flowable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class DelayedFakeAsynkDatabaseClient implements AsyncDatabaseClient {

    @Override
    public CompletionStage<Item> getNextAfterId(String id) {
        var future = new CompletableFuture<Item>();

        Flowable.just(new Item("" + (Integer.parseInt(id) + 1)))
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(future::complete);

        return future;
    }
}
