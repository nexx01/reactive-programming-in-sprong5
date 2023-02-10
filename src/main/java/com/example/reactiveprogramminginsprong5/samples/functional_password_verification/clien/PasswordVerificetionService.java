package com.example.reactiveprogramminginsprong5.samples.functional_password_verification.clien;

import reactor.core.publisher.Mono;

public interface PasswordVerificetionService {
    Mono<Void> check(String raw, String encoded);
}
