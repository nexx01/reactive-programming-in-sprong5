package com.example.reactiveprogramminginsprong5.imperative;

import common.Input;
import common.Output;

public class BlockingShoppingCardService implements ShoppingCardService
{
    @Override
    public Output calculate(Input value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Output();
    }
}
