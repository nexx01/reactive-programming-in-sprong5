package com.example.reactiveprogramminginsprong5.news_service;

import com.example.reactiveprogramminginsprong5.news_service.dto.NewsLetter;
import org.reactivestreams.Subscriber;

public interface ResubscribableErrorLettter {

    void resubscribe(Subscriber<? super NewsLetter> subscriber);
}