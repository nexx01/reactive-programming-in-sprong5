package com.example.reactiveprogramminginsprong5.callback;

import common.Input;
import common.Output;

import java.util.function.Consumer;

public class SyncShoppingCardService implements ShoppingCardService{
    @Override
    public void calculate(Input value, Consumer<Output> c) {
        //Non blocking operation, better to imediately provide answer
        c.accept(new Output());
    }
}
