package com.example.reactiveprogramminginsprong5.completionStage;

import common.Input;
import common.Output;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CompletionStageShoppingCardService implements ShoppingCardService
{
    @Override
    public CompletionStage<Output> calculate(Input value) {
     return    CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new Output();
        });

    }
}
