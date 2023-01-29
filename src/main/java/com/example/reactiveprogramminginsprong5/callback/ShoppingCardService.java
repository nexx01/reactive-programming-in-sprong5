package com.example.reactiveprogramminginsprong5.callback;

import common.Input;
import common.Output;

import java.util.function.Consumer;

public interface ShoppingCardService {

    void calculate(Input value, Consumer<Output> c);
}
