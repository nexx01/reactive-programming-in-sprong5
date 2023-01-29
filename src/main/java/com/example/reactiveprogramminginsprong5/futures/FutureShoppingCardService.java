package com.example.reactiveprogramminginsprong5.futures;

import common.Input;
import common.Output;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureShoppingCardService implements ShoppingCardService
{
    @Override
    public Future<Output> calculate(Input value) {
        FutureTask<Output> future = new FutureTask<>(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return new Output();
        });

        new Thread(future).start();

        return future;
    }
}
