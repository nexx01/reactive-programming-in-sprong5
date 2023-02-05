package com.example.reactiveprogramminginsprong5.push_vs_pull.push_pull_model;

import org.reactivestreams.Publisher;

public interface AsyncDatabaseClient {
    Publisher<Item> getStreamOfItems();
}
