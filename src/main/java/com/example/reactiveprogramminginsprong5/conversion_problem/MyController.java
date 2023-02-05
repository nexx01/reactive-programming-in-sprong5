package com.example.reactiveprogramminginsprong5.conversion_problem;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
public class MyController {
    private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

    {
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new MappingJackson2CborHttpMessageConverter());
    }

    @RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ListenableFuture<?> requestData() {
        var asyncRestTemplate = new AsyncRestTemplate();
        var fakeAsynkDatabaseClient = new FakeAsynkDatabaseClient();

        var completionStage = AsynkAdapter.toCompletion(asyncRestTemplate.execute(
                "http://localhost:8080/hello",
                HttpMethod.GET,
                null,
                new HttpMessageConverterExtractor<>(String.class, messageConverters)
        ));

        return AsynkAdapter.toListenable(fakeAsynkDatabaseClient.store(completionStage));
    }
}
