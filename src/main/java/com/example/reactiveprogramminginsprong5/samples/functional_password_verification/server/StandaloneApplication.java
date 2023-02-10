package com.example.reactiveprogramminginsprong5.samples.functional_password_verification.server;

import com.example.reactiveprogramminginsprong5.samples.functional_password_verification.clien.PaswordDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import javax.swing.plaf.synth.SynthDesktopIconUI;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class StandaloneApplication {

    public static final Logger log = LoggerFactory.getLogger(StandaloneApplication.class);

    public static void main(String[] args) {
        var start = System.currentTimeMillis();
        var httpHandler = RouterFunctions.toHttpHandler(
                routes(new BCryptPasswordEncoder())
        );

        var reactorHttpHandler = new ReactorHttpHandlerAdapter(httpHandler);

        var server = HttpServer.create()
                .host("localhost")
                .port(8080)
                .handle(reactorHttpHandler)
                .bindNow();

        log.debug("Startes in " + (System.currentTimeMillis() - start) + " ms");

        server.onDispose()
                .block();
    }

    private static RouterFunction<?> routes(PasswordEncoder passwordEncoder) {
        return
                route(POST("/password"),
                        request -> request
                                .bodyToMono(PaswordDto.class)
                                .map(p -> passwordEncoder.matches(p.getRaw(), p.getSecured()))
                                .flatMap(isMatched -> isMatched ?
                                        ServerResponse.ok().build()
                                        : ServerResponse.status(HttpStatus.EXPECTATION_FAILED).build())
                )


                ;
    }
}
