package com.example.reactiveprogramminginsprong5.news_service;

import com.example.reactiveprogramminginsprong5.news_service.dto.News;
import com.example.reactiveprogramminginsprong5.news_service.dto.NewsLetter;
import lombok.NonNull;

import java.util.Collection;
import java.util.Collections;

public class StubNewsLetter extends NewsLetter {

    public StubNewsLetter(int element) {
        super(String.valueOf(element), null, Collections.EMPTY_LIST);
    }
}
