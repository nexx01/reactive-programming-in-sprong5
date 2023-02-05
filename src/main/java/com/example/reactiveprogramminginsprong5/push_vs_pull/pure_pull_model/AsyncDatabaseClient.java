package com.example.reactiveprogramminginsprong5.push_vs_pull.pure_pull_model;

import java.util.concurrent.CompletionStage;

public interface AsyncDatabaseClient {
     CompletionStage<Item> getNextAfterId(String id);

}
