package com.example.reactiveprogramminginsprong5.news_service;

import com.example.reactiveprogramminginsprong5.news_service.dto.News;

import java.util.Date;
import java.util.Random;

public interface NewsHarness {
    Random random = new Random();

    static News generate() {
        return News.builder()
                .author(String.valueOf(random.nextGaussian()))
                .category("tech")
                .publishedOn(new Date())
                .content(String.valueOf(random.nextGaussian()))
                .title(String.valueOf(random.nextGaussian()))
                .build();
    }

}
