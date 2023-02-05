package com.example.reactiveprogramminginsprong5.push_vs_pull.batched_pulled_model;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Puller {

    final AsuncDatabaseClient dbClient = new DelayedFakeAsyncDatabaseClient();

    public CompletionStage<Queue<Item>> list(int count) {

        var storage = new ArrayBlockingQueue<Item>(count);
        var result = new CompletableFuture<Queue<Item>>();

        pull("1", storage, result, count);
        return result;
    }

    void pull(String elementId,
              Queue<Item> queue,
              CompletableFuture resultFuture,
              int count) {

        dbClient.getNextBatchAfterId(elementId, count)
                .thenAccept(items -> {
                    for (Item item : items) {
                        if (isValid(item)) {
                            queue.offer(item);

                            if (queue.size() == count) {
                                resultFuture.complete(queue);
                                return;
                            }
                        }
                    }

                    pull(items.get(items.size() - 1)
                            .getId(), queue, resultFuture, count);
                });
    }

    boolean isValid(Item item) {
        return Integer.parseInt(item.getId()) % 2 == 0;
    }
}
