package com.example.reactiveprogramminginsprong5.news_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Wither;

import java.util.Collection;

@Data(staticConstructor = "of")
@Builder(builderClassName = "NewsLetterTemplate", builderMethodName = "template")
@AllArgsConstructor
@Wither
public class NewsLetter {

    @NonNull
    private String title;
    private String recipient;
    private @NonNull Collection<News> digest;
}