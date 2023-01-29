package com.example.reactiveprogramminginsprong5.callback;

import common.Input;
import common.Output;

import java.util.function.Consumer;

public class AsyncShoppingCardService implements ShoppingCardService{
    @Override
    public void calculate(Input value, Consumer<Output> c) {
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            c.accept(new Output());
        }).start();
    }
}
