package com.example.reactiveprogramminginsprong5.push_vs_pull.batched_pulled_model;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface AsuncDatabaseClient {
    CompletionStage<List<Item>> getNextBatchAfterId(String id, int count);

}
