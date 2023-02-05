package com.example.reactiveprogramminginsprong5.conversion_problem;

import java.util.concurrent.CompletionStage;

public interface AsyncDatabaseClient {
    <T> CompletionStage<T> store(CompletionStage<T> stage);

}
