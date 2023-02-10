package com.example.reactiveprogramminginsprong5.samples.functional_password_verification.clien;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaswordDto {
    private String raw;
    private String secured;

    @JsonCreator

    public PaswordDto(@JsonProperty("raw") String raw,@JsonProperty("secured") String secured) {
        this.raw = raw;
        this.secured = secured;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getSecured() {
        return secured;
    }

    public void setSecured(String secured) {
        this.secured = secured;
    }
}
