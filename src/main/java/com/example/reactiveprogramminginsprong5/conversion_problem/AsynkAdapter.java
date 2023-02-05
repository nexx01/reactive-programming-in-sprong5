package com.example.reactiveprogramminginsprong5.conversion_problem;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import javax.annotation.processing.Completion;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AsynkAdapter {

    public static <T> CompletionStage<T> toCompletion(ListenableFuture<T> future) {
        var completableFuture = new CompletableFuture<T>();

        future.addCallback(completableFuture::complete, completableFuture::completeExceptionally);
        return completableFuture;
    }

    public static <T> ListenableFuture<T> toListenable(CompletionStage<T> stage) {
        var future
                = new SettableListenableFuture<T>();

        stage.whenComplete((v, t) -> {
            if (t == null) {
                future.set(v);
            } else {
                future.setException(t);
            }
        });
        return future;
    }
}
