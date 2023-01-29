package com.example.reactiveprogramminginsprong5.futures;

import common.Input;
import common.Output;

import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface ShoppingCardService {

    Future<Output> calculate(Input value);
}
