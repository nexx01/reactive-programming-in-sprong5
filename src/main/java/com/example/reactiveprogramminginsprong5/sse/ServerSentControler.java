package com.example.reactiveprogramminginsprong5.sse;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

import java.util.Map;

public class ServerSentControler {
    private Map<String, StockService> stringStockServiceMap;


    @GetMapping("/sse/stocks")
    public Flux<ServerSentEvent<?>> streamStocks() {
        return Flux
                .fromIterable(stringStockServiceMap.values())
                .flatMap(s->s.stream)
                .<ServerSentEvent<?>>map(item ->
                        ServerSentEvent
                                .builder(item)
                                .event("StockItem")
                                .id(item.getId())
                                .build()
                )
                .startWith(
                        ServerSentEvent
                                .builder()
                                .event("Stocks")
                                .data(stringStockServiceMap.keySet())
                                .build()
                );
    }
}