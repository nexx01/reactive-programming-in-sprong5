package com.example.reactiveprogramminginsprong5.push_vs_pull.pure_pull_model;

import com.example.reactiveprogramminginsprong5.push_vs_pull.pure_pull_model.AsyncDatabaseClient;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Puller {
    final AsyncDatabaseClient dbClient = new DelayedFakeAsynkDatabaseClient();

    public CompletionStage<Queue<Item>> list(int count) {
        var storage = new ArrayBlockingQueue<Item>(count);
        var result = new CompletableFuture<Queue<Item>>();

        pull("1", storage, result, count);

        return result;
    }

    private void pull(String elementId, Queue<Item> queue, CompletableFuture resultFuture, int count) {
        dbClient.getNextAfterId(elementId)
                .thenAccept(item->{
                    if(isValid(item)){
                        queue.offer(item);

                        if (queue.size() == count) {
                            resultFuture.complete(queue);
                            return;
                        }

                    }
                    pull(item.getId(), queue, resultFuture, count);
                });
    }

    private boolean isValid(Item item) {
        return Integer.parseInt(item.getId()) % 2 == 0;
    }
}
