package com.example.reactiveprogramminginsprong5.completionStage;

import common.Input;
import common.Output;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

public interface ShoppingCardService {

    CompletionStage<Output> calculate(Input value);
}
