package com.example.reactiveprogramminginsprong5.push_vs_pull.push_model;

import rx.Observable;

public interface AsyncDatabaseClient {
    Observable<Item> getStreamOfItems();
}
